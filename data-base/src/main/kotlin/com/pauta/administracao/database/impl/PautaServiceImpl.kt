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
                addOnRedis(pautaEntity.toDomain())
                    .flatMap { Mono.just(it) }
            }
            .doOnSuccess {
                logger.info("pautaRepository.save, status=complete")
            }
            .onErrorResume { error ->
                logger.error("pautaRepository.save, status=error message:${error.message}")
                Mono.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to create order!" else it
                        }
                    )
                )
            }
    }

    override fun update(pauta: PautaOutputDto): Mono<PautaDomain> {
        logger.info("pautaRepository.update, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity().copy(id = pauta.id))
            .flatMap { pautaEntity ->
                addOnRedis(pautaEntity.toDomain())
                    .flatMap { Mono.just(it) }
            }
            .doOnSuccess {
                logger.info("pautaRepository.update, status=complete")
            }
            .onErrorResume {
                error ->
                logger.error("pautaRepository.update, status=error message:${error.message}")
                Mono.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to update order!" else it
                        }
                    )
                )
            }
    }

    override fun deleteByName(nome: String): Mono<Boolean> {
        logger.info("pautaRepository.deleteByName, status=try")
        return pautaRepository.deleteByPautaNome(nome)
            .flatMap {
                redisService.remove("pauta:nome:$nome:pauta")
                    .doOnSuccess { logger.info("Order removed with success!") }
                    .onErrorResume {
                        logger.error("Order not removed!")
                        Mono.just(true)
                    }
            }
            .doOnSuccess {
                logger.info("pautaRepository.deleteByName, status=complete")
            }
            .onErrorResume { error ->
                logger.error("pautaRepository.deleteByName, status=error message:${error.message}")
                Mono.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to delete order by name!" else it
                        }
                    )
                )
            }
    }

    override fun findById(id: Long): Mono<PautaDomain> {
        logger.info("pautaRepository.findById, status=try")
        return pautaRepository.findById(id)
            .map { dbValue ->
                dbValue.toDomain()
            }
            .doOnSuccess {
                logger.info("pautaRepository.findById, status=complete by db")
            }
            .onErrorResume { error ->

                logger.error("pautaRepository.findById, status=error message:${error.message}")
                Mono.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to find order by ID!" else it
                        }
                    )
                )
            }
    }

    override fun findByName(nome: String): Mono<PautaDomain> {
        logger.info("pautaRepository.findByName, status=try")
        return redisService.get("pauta:nome:$nome:pauta")
            .flatMap { redisValue ->
                redisValue?.let {
                    logger.info("pautaRepository.findById, status=complete by redis")
                    return@flatMap Mono.just(it)
                } ?: Mono.empty()
            }
            .onErrorResume {
                Mono.empty()
            }
            .switchIfEmpty {
                pautaRepository.findByPautaNome(nome)
                    .flatMap { dbValue ->
                        addOnRedis(dbValue.toDomain())
                            .flatMap { Mono.just(it) }
                    }
                    .doOnSuccess {
                        logger.info("pautaRepository.findByName, status=complete")
                    }
                    .onErrorResume { error ->

                        logger.error("pautaRepository.findByName, status=error message:${error.message}")
                        Mono.error(
                            UnsupportedOperationException(
                                error.message?.let {
                                    if (!it.contains("server.error"))
                                        "server.error.Error to search order by name!" else it
                                }
                            )
                        )
                    }
            }
    }

    override fun findByPautaProcessada(value: Boolean): Flux<PautaDomain> {
        return redisService.getAll()
            .filter {
                it?.pautaProcessada == value
            }
            .collectList()
            .flatMapMany {
                if (it.isNotEmpty()) {
                    Flux.fromIterable((it))
                        .flatMap { pautaDoamin ->
                            Flux.just(pautaDoamin)
                        }
                } else {
                    pautaRepository.findByPautaProcessada(value)
                        .flatMap { pautaDomain ->
                            addOnRedis(pautaDomain.toDomain())
                            Flux.just(pautaDomain.toDomain())
                        }
                }
            }
            .onErrorResume {
                pautaRepository.findByPautaProcessada(value)
                    .flatMap { pautaDomain ->
                        Flux.just(pautaDomain.toDomain())
                    }
                    .switchIfEmpty { Flux.empty<PautaDomain>() }
                    .onErrorResume { error ->
                        logger.error("pautaRepository.findByPautaProcessada, status=error message:${error.message}")
                        Flux.error(
                            UnsupportedOperationException(
                                error.message?.let {
                                    if (!it.contains("server.error"))
                                        "server.error.Error to find orders actives on data base!" else it
                                }
                            )
                        )
                    }
            }
            .flatMap {
                return@flatMap Flux.just(it)
            }
    }

    override fun findAll(): Flux<PautaDomain> {
        return redisService.getAll()
            .collectList()
            .flatMapMany {
                if (it.isNotEmpty()) {
                    Flux.fromIterable((it))
                        .flatMap { pautaDoamin ->
                            Flux.just(pautaDoamin)
                        }
                } else {
                    getAllByDatabase()
                }
            }
            .onErrorResume {
                return@onErrorResume getAllByDatabase()
                    .switchIfEmpty { Flux.empty<PautaDomain>() }
            }
            .flatMap {
                return@flatMap Flux.just(it)
            }
    }

    override fun removeAllDataOnRedis(): Flux<Boolean> {
        logger.info("pautaRepository.removeAllDataOnRedis, status=try")
        return redisService.removeAllDataOnRedis().map { true }
            .doOnTerminate { logger.info("Orders removed with success!") }
            .onErrorResume { error ->

                logger.error("pautaRepository.removeAllDataOnRedis, status=error message:${error.message}")
                Flux.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to remove all keys from redis!" else it
                        }
                    )
                )
            }
    }

    private fun addOnRedis(pautaDomain: PautaDomain): Mono<PautaDomain> {
        return redisService.put(pautaDomain).thenReturn(pautaDomain)
            .doOnSuccess { logger.info("Order founded with success!") }
            .onErrorResume {
                logger.error("Order not created!")
                Mono.just(pautaDomain)
            }
    }

    private fun getAllByDatabase(): Flux<PautaDomain> {
        return pautaRepository.findAll()
            .flatMap { pautaDb ->
                addOnRedis(pautaDb.toDomain())
            }
            .doOnComplete {
                logger.info("pautaRepository.findAll, status=complete")
            }
            .onErrorResume { error ->
                logger.error("pautaRepository.findAll, status=error message:${error.message}")
                Flux.error(
                    UnsupportedOperationException(
                        error.message?.let {
                            if (!it.contains("server.error"))
                                "server.error.Error to search all orders!" else it
                        }
                    )
                )
            }
            .switchIfEmpty { Flux.empty<PautaDomain>() }
    }
}
