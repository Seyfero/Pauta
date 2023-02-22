package com.pauta.administracao.usecase.service

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.kafkaproducer.dto.KafkaDto
import com.pauta.administracao.kafkaproducer.service.KafkaProducerService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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
        scheduleTasks()
    }

    @Scheduled(fixedDelay = 1000)
    fun scheduleTasks() {
        getAllPauta()
            .flatMap {
                if (orderExpiredDuration(it)) {
                    sendAnounciant(it).subscribe()
                    removePauta(it).subscribe()
                }
                Mono.just(true)
            }
            .switchIfEmpty {
                logger.info("Doesn't data to process!")
            }
    }

    private fun getAllPauta(): Flux<PautaDomain> {
        return pautaService.findAll()
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

    private fun removePauta(pautaDomain: PautaDomain): Mono<Boolean> {
        return pautaDomain.id?.let {
            pautaService.deleteById(it)
        } ?: Mono.error<Boolean?>(UnsupportedOperationException("Error to delete pauta on database!"))
            .doOnError {
                logger.error("Error to delete pauta on database message=${it.message}")
            }
    }

    private fun sendAnounciant(pautaDomain: PautaDomain): Mono<Boolean> {
        return getDtoTosend(pautaDomain)
            .flatMap {
                logger.info("Prepare to sent message!")
                kafkaProducerService.sendMessage(it.toString())
            }
            .doOnSuccess {
                logger.info("Message sent with success!")
            }
            .onErrorResume {
                logger.error("Error to sent message to kafka message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to sent message for kafka!"))
            }
    }

    private fun getDtoTosend(pautaDomain: PautaDomain): Mono<KafkaDto> {
        return getTotalVoteCount(pautaDomain)
            .flatMap {
                logger.info("Got count votes from pauta with success!")
                Mono.just(
                    KafkaDto(
                        pautaDomain.pautaNome,
                        pautaDomain.pautaDataCriacao,
                        pautaDomain.pautaDataCriacao.plusSeconds(pautaDomain.pautaDuracao),
                        it.sim,
                        it.nao,
                        it.sim + it.nao,
                    )
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
        return expressionCountVote(votoService.findByVotoPauta(pautaDomain.id))
            .doOnSuccess {
                logger.info("Votes counted with success!")
            }
            .onErrorResume {
                logger.error("Error to count votes message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to count votes!"))
            }
    }

    fun expressionCountVote(votes: Flux<VotoDomain>): Mono<VoteCount> {
        return votes.reduce(VoteCount()) { acc, vote ->
            if (vote.votoEscolha.lowercase() == "sim") {
                acc.sim++
            } else {
                acc.nao++
            }
            acc
        }
            .doOnSuccess {
                logger.info("Votes counted with success!")
            }
            .onErrorResume {
                logger.error("Error to get votes sim or nao message=${it.message}")
                Mono.error(UnsupportedOperationException("Error to get votes sim or nao!"))
            }
    }

    data class VoteCount(var sim: Long = 0, var nao: Long = 0)
}
