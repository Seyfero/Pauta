package com.pauta.administracao.usecase.service

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.kafkaproducer.dto.KafkaDto
import com.pauta.administracao.kafkaproducer.service.KafkaProducerService
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Component
class ScheduledTasks(
    private val pautaService: PautaService,
    private val kafkaProducerService: KafkaProducerService,
    private val votoService: VotoService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        Flux.interval(Duration.ofSeconds(1))
            .delaySubscription(Duration.ofSeconds(15))
            .subscribe { scheduleTasks().subscribe() }
    }

    fun scheduleTasks(): Flux<Boolean> {
        return getAllPauta()
            .collectList()
            .flatMapMany { Flux.fromIterable(it) }
            .flatMap {
                if (orderExpiredDuration(it)) {
                    val p1 = sendAnounciant(it)
                        .doOnSuccess {
                            logger.info("Sent with success to kafka!")
                        }
                        .doOnError {
                            logger.error("Error te sent order!")
                        }

                    val p2 = updatePauta(it)
                        .doOnSuccess {
                            logger.info("Order removed with success!")
                        }
                        .doOnError {
                            logger.error("Error te remove order!")
                        }
                    Mono.zip(p1, p2).then().subscribe()
                }
                Flux.just(true)
            }
            .switchIfEmpty(Flux.just(true))
    }

    private fun getAllPauta(): Flux<PautaDomain> {
        return pautaService.findByPautaProcessada(false)
            .onErrorResume {
                logger.error("Error to toke list from redis or database! Message=${it.message}")
                Flux.error(IllegalAccessException("Error to toke order lists!"))
            }
    }

    private fun orderExpiredDuration(pautaDomain: PautaDomain): Boolean {
        if (pautaDomain.pautaDataCriacao.plusSeconds(pautaDomain.pautaDuracao).isBefore(LocalDateTime.now())) {
            return true
        }
        return false
    }

    private fun updatePauta(pautaDomain: PautaDomain): Mono<Boolean> {
        return pautaDomain.let {
            pautaService.update(it.toOutputDto().copy(pautaProcessada = true))
                .doOnSuccess {
                    logger.info("Order updated with success!")
                }
                .doOnError {
                    logger.error("Error to updated order in kafka process!")
                }
                .thenReturn(true)
        }
    }

    private fun sendAnounciant(pautaDomain: PautaDomain): Mono<Boolean> {
        return getDtoTosend(pautaDomain)
            .flatMap {
                logger.info("Prepare to sent message!")
                kafkaProducerService.sendMessage(it.toString())
                    .doOnSuccess {
                        logger.info("Message sent with success!")
                    }
                    .doOnError {
                        logger.error("Error to sent message!")
                    }
                    .thenReturn(true)
            }
            .onErrorResume {
                logger.error("Error to sent message to kafka message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to sent message for kafka!"))
            }
    }

    private fun getDtoTosend(pautaDomain: PautaDomain): Mono<KafkaDto> {
        return getTotalVoteCount(pautaDomain)
            .map {
                logger.info("Got count votes from pauta with success!")
                KafkaDto(
                    pautaDomain.pautaNome,
                    pautaDomain.pautaDataCriacao,
                    pautaDomain.pautaDataCriacao.plusSeconds(pautaDomain.pautaDuracao),
                    it.sim,
                    it.nao,
                    it.sim + it.nao,
                )
            }
            .doOnSuccess {
                logger.info("Object created to kafka!")
            }
            .onErrorResume {
                logger.error("Error to got votes message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to got votes!"))
            }
    }

    private fun getTotalVoteCount(pautaDomain: PautaDomain): Mono<VoteCount> {
        return votoService.findByVotoPauta(pautaDomain.id)
            .collectList()
            .map {
                expressionCountVote(it)
            }
            .doOnSuccess {
                logger.info("Votes counted with success!")
            }
            .onErrorResume {
                logger.error("Error to count votes message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to count votes!"))
            }
    }

    fun expressionCountVote(votes: List<VotoDomain>): VoteCount {
        return votes.fold(VoteCount()) { acc, voto ->
            if (voto.votoEscolha.lowercase() == "sim") {
                acc.sim++
            } else {
                acc.nao++
            }
            VoteCount(acc.sim, acc.nao)
        }
    }

    data class VoteCount(var sim: Long = 0, var nao: Long = 0)
}
