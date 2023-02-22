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

@Service
class PautaServiceImpl(

    private val pautaRepository: PautaRepository,
    private val redisService: RedisService<PautaDomain>

) : PautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(pauta: PautaOutputDto): Mono<PautaDomain> {
        logger.info("pautaRepository.save, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity())
            .map { it.toDomain() }
            .doOnSuccess {
                logger.info("pautaRepository.save, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.save, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to create order!"))
            }
            .also {
                it.map {
                    redisService.put(pauta.toDomain()).subscribe()
                    logger.info("Order added on redis with success!")
                }
            }
            .doOnError {
                logger.error("Order not created on redis with success!")
            }
    }

    override fun update(pauta: PautaOutputDto): Mono<PautaDomain> {
        logger.info("pautaRepository.update, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity())
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("pautaRepository.update, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.update, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to update update!"))
            }
            .also {
                it.map {
                    redisService.put(pauta.toDomain())
                    logger.info("Order updated on redis with success!")
                }.subscribe()
            }
            .doOnError {
                logger.error("Order not updated on redis with success!")
            }
    }

    override fun deleteById(id: Long): Mono<Boolean> {
        logger.info("pautaRepository.deleteById, status=try")
        return pautaRepository.deleteById(id)
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("pautaRepository.deleteById, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.update, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to delete!"))
            }
            .also {
                redisService.remove("pauta:id:$id:pauta").subscribe()
                logger.info("Order removed on redis with success!")
            }
            .doOnError {
                logger.error("Order not removed on redis with success!")
            }
    }

    override fun deleteByName(nome: String): Mono<Boolean> {
        logger.info("pautaRepository.deleteByName, status=try")
        return pautaRepository.deleteByPautaNome(nome)
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("pautaRepository.deleteByName, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.deleteByName, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to delete!"))
            }
            .also {
                redisService.remove("pauta:nome:$nome:pauta").subscribe()
                logger.info("Order removed on redis with success!")
            }
            .doOnError {
                logger.error("Order not removed on redis with success!")
            }
    }

    override fun findById(id: Long): Mono<PautaDomain> {
        logger.info("pautaRepository.findById, status=try")
        return redisService.get("pauta:id:$id:pauta")
            .flatMap { redisValue ->
                if (redisValue != null) {
                    logger.info("pautaRepository.findById, status=complete by redis")
                    return@flatMap Mono.just(redisValue)
                }
                pautaRepository.findById(id)
                    .map { dbValue ->
                        dbValue.toDomain()
                    }
                    .doOnSuccess {
                        logger.info("pautaRepository.findById, status=complete by db")
                    }
                    .onErrorResume {
                        logger.error("pautaRepository.findById, status=error message:${it.message}")
                        Mono.error(UnsupportedOperationException("Error to search!"))
                    }
                    .also {
                        it.map { valueRedis ->
                            redisService.put(valueRedis).subscribe()
                            logger.info("pautaRepository.findById, added on redis")
                        }
                            .doOnError {
                                logger.error("Error to add on redis!")
                            }
                    }
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
                pautaRepository.findByPautaNome(nome)
                    .map {
                        it.toDomain()
                    }
                    .doOnSuccess {
                        logger.info("pautaRepository.findByName, status=complete")
                    }
                    .onErrorResume {
                        logger.error("pautaRepository.findByName, status=error message:${it.message}")
                        Mono.error(UnsupportedOperationException("Error to search!"))
                    }
                    .also {
                        it.map { valueRedis ->
                            redisService.put(valueRedis).subscribe()
                            logger.info("pautaRepository.findById, added on redis")
                        }
                            .doOnError {
                                logger.error("Error to add on redis!")
                            }
                    }
            }
    }

    override fun findAll(): Flux<PautaDomain> {
        logger.info("pautaRepository.findAll, status=try")
        return redisService.getAll("pauta:id:*")
            .flatMap {
                if (it != null) {
                    logger.info("pautaRepository.findById, status=complete by redis")
                    return@flatMap Mono.just(it)
                }
                pautaRepository.findAll()
                    .map { pautaDb ->
                        pautaDb.toDomain()
                    }
                    .doOnTerminate {
                        logger.info("pautaRepository.findAll, status=complete")
                    }
                    .onErrorResume { error ->
                        logger.error("pautaRepository.findAll, status=error message:${error.message}")
                        Mono.error(UnsupportedOperationException("Error to search!"))
                    }
                    .also { fluxoPauta ->
                        fluxoPauta.map { pauta ->
                            redisService.put(pauta).subscribe()
                            logger.info("pautaRepository.findAll, added on redis")
                        }
                            .doOnError {
                                logger.error("Error to add on redis!")
                            }
                    }
            }
    }
}
