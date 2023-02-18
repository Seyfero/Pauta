package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.pauta.toDomain
import com.pauta.administracao.database.converters.pauta.toEntity
import com.pauta.administracao.database.repository.PautaRepository
import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.converters.pauta.toDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.outputboundary.service.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PautaServiceImpl(

    private val pautaRepository: PautaRepository

) : PautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(pauta: PautaOutputDto): Mono<Boolean> {
        logger.info("pautaRepository.save, status=try")
        return pautaRepository.save(pauta.toDomain().toEntity())
            .flatMap { Mono.just(true) }
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
    }

    override fun findById(id: Long): Mono<PautaDomain> {
        logger.info("pautaRepository.findById, status=try")
        return pautaRepository.findById(id)
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("pautaRepository.findById, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.findById, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to search!"))
            }
    }

    override fun findByName(nome: String): Mono<PautaDomain> {
        logger.info("pautaRepository.findByName, status=try")
        return pautaRepository.findByPautaNome(nome)
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
    }

    override fun findAll(): Flux<PautaDomain> {
        logger.info("pautaRepository.findAll, status=try")
        return pautaRepository.findAll()
            .map {
                it.toDomain()
            }
            .doOnTerminate {
                logger.info("pautaRepository.findAll, status=complete")
            }
            .onErrorResume {
                logger.error("pautaRepository.findAll, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to search!"))
            }
    }
}
