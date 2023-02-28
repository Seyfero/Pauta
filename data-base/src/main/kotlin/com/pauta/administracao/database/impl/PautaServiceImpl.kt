package com.pauta.administracao.database.impl

import com.pauta.administracao.cache.service.RedisService
import com.pauta.administracao.database.converters.pauta.toDomain
import com.pauta.administracao.database.converters.pauta.toEntity
import com.pauta.administracao.database.repository.PautaRepository
import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.converters.pauta.toDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class PautaServiceImpl(

    private val pautaRepository: PautaRepository,
    private val redisService: RedisService<PautaDomain>

) : PautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(pauta: PautaOutputDto): Mono<PautaDomain> {
        logger.info("pautaRepository.save, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity())
            .flatMap { pautaEntity ->
                redisService.put(pautaEntity.toDomain()).thenReturn(pautaEntity.toDomain())
                    .doOnSuccess { logger.info("Order created with success!") }
                    .onErrorResume {
                        logger.error("pautaRepository.save, status=error message:${it.message}")
                        Mono.just(pautaEntity.toDomain())
                    }
            }
            .doOnSuccess {
                logger.info("pautaRepository.save, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.save, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to create order!"))
            }
    }

    override fun update(pauta: PautaOutputDto): Mono<PautaDomain> {
        logger.info("pautaRepository.update, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity().copy(id = pauta.id))
            .flatMap { pautaEntity ->
                redisService.put(pautaEntity.toDomain()).thenReturn(pautaEntity.toDomain())
                    .doOnSuccess { logger.info("Order updated with success!") }
                    .onErrorResume {
                        logger.error("pautaRepository.save, status=error message:${it.message}")
                        Mono.just(pautaEntity.toDomain())
                    }
            }
            .doOnSuccess {
                logger.info("pautaRepository.update, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.update, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to update order!"))
            }
    }

    override fun deleteByName(nome: String): Mono<Boolean> {
        logger.info("pautaRepository.deleteByName, status=try")
        return pautaRepository.deleteByPautaNome(nome)
            .flatMap {
                redisService.remove("pauta:nome:$nome:pauta")
                    .doOnSuccess { logger.info("Order removed with success!") }
                    .doOnError { logger.error("Order not removed!") }
            }
            .doOnSuccess {
                logger.info("pautaRepository.deleteByName, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.deleteByName, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to delete order by name!"))
            }
    }

    override fun findById(id: Long): Mono<PautaDomain> {
        logger.info("pautaRepository.findById, status=try")
        return pautaRepository.findById(id)
            .flatMap { dbValue ->
                redisService.put(dbValue.toDomain()).thenReturn(dbValue.toDomain())
                    .doOnSuccess { logger.info("Order founded with success!") }
                    .doOnError { logger.error("Order not founded!") }
            }
            .doOnSuccess {
                logger.info("pautaRepository.findById, status=complete by db")
            }
            .onErrorResume {
                logger.error("pautaRepository.findById, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to find order by ID!"))
            }
    }

    override fun findByName(nome: String): Mono<PautaDomain> {
        logger.info("pautaRepository.findByName, status=try")
        return redisService.get("pauta:nome:$nome:pauta")
            .flatMap { redisValue ->
                if (redisValue != null) {
                    logger.info("pautaRepository.findById, status=complete by redis")
                    return@flatMap Mono.just(redisValue)
                }
                Mono.empty()
            }
            .switchIfEmpty {
                pautaRepository.findByPautaNome(nome)
                    .flatMap { dbValue ->
                        redisService.put(dbValue.toDomain()).thenReturn(dbValue.toDomain())
                            .doOnSuccess { logger.info("Order founded with success!") }
                            .doOnError { logger.error("Order not founded!") }
                    }
                    .doOnSuccess {
                        logger.info("pautaRepository.findByName, status=complete")
                    }
                    .onErrorResume {
                        logger.error("pautaRepository.findByName, status=error message:${it.message}")
                        Mono.error(UnsupportedOperationException("Error to search order by name!"))
                    }
            }
    }

    override fun findAll(): Flux<PautaDomain> {
        return redisService.getAll()
            .flatMap {
                if (it != null) {
                    Flux.just(it)
                } else {
                    Flux.empty()
                }
            }
            .switchIfEmpty(getAllByDatabase())
    }

    private fun getAllByDatabase(): Flux<PautaDomain> {
        return pautaRepository.findAll()
            .collectList()
            .flatMapMany {
                Flux.fromIterable(it)
            }
            .flatMap { pautaDb ->
                redisService.put(pautaDb.toDomain()).thenReturn(pautaDb.toDomain())
                    .doOnSuccess { logger.info("Order founded with success!") }
                    .doOnError { logger.error("Error to find orders !") }
            }
            .doOnComplete {
                logger.info("pautaRepository.findAll, status=complete")
            }
            .onErrorResume { error ->
                logger.error("pautaRepository.findAll, status=error message:${error.message}")
                Flux.error(UnsupportedOperationException("Error to search all orders!"))
            }
            .collectList()
            .flatMapMany { Flux.fromIterable(it) }
            .switchIfEmpty(Flux.empty())
    }

    override fun removeAll(): Flux<Boolean> {
        logger.info("pautaRepository.removeAll, status=try")
        return redisService.removeAll().map { true }
            .doOnTerminate { logger.info("Orders removed with success!") }
            .onErrorResume { error ->
                logger.error("pautaRepository.removeAll, status=error message:${error.message}")
                Flux.error(UnsupportedOperationException("Error to remove all keys from redis!"))
            }
    }
}
