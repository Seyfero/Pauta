package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.voto.toDomain
import com.pauta.administracao.database.converters.voto.toEntity
import com.pauta.administracao.database.repository.VotoRepository
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.voto.toDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VotoServiceImpl(

    private val votoRepository: VotoRepository

) : VotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(voto: VotoOutputDto): Mono<Boolean> {
        logger.info("votoRepository.create, status=try")
        return votoRepository.save(voto.toDomain().toEntity())
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("votoRepository.create, status=complete")
            }
            .onErrorResume {
                logger.error("votoRepository.create, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to create vote!"))
            }
    }

    override fun delete(id: Long): Mono<Boolean> {
        logger.info("votoRepository.delete, status=try")
        return votoRepository.deleteById(id)
            .flatMap {
                logger.info("votoRepository.delete, status=complete")
                Mono.just(true)
            }
            .onErrorResume {
                logger.error("votoRepository.delete, status=error message:${it.message}")
                Mono.error(IllegalAccessException("Error to delete vote!"))
            }
    }

    override fun findAll(): Flux<VotoDomain> {
        logger.info("votoRepository.findAll, status=try")
        return votoRepository.findAll()
            .map {
                it.toDomain()
            }
            .doOnTerminate {
                logger.info("votoRepository.findAll, status=complete")
            }
            .onErrorResume {
                logger.error("votoRepository.findAll, status=error message:${it.message}")
                Mono.error(IllegalAccessException("Error to find all votes!"))
            }
    }

    override fun findByVotoPauta(idVotoPauta: Long?): Flux<VotoDomain> {
        logger.info("votoRepository.findByVotoPauta, status=try")
        return votoRepository.findByVotoPauta(idVotoPauta)
            .map {
                it.toDomain()
            }
            .doOnTerminate {
                logger.info("votoRepository.findByVotoPauta, status=complete")
            }
            .onErrorResume {
                logger.error("votoRepository.findByVotoPauta, status=error message:${it.message}")
                Mono.error(IllegalAccessException("Error to find vote by order!"))
            }
    }

    override fun findByVotoPautaAndVotoUsuarioCpf(idVotoPauta: Long?, cpfUsuario: String): Mono<VotoDomain> {
        logger.info("votoRepository.findByVotoPautaAndVotoUsuario, status=try")
        return votoRepository.findByVotoPautaAndVotoUsuarioCpf(idVotoPauta, cpfUsuario)
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("votoRepository.findByVotoPautaAndVotoUsuario, status=complete")
            }
            .onErrorResume {
                logger.error("votoRepository.findByVotoPautaAndVotoUsuario, status=error message:${it.message}")
                Mono.error(IllegalAccessException("Error to find vote by order and user!"))
            }
    }

    override fun getCountVotosByPautaId(idVotoPauta: Long?, votoEscolha: String): Mono<Long> {
        logger.info("votoRepository.getCountVotosByPautaId, status=try")
        return votoRepository.getCountVotosByPautaId(idVotoPauta, votoEscolha)
            .doOnSuccess {
                logger.info("votoRepository.findByVotoPautaAndVotoUsuario, status=complete")
            }
            .onErrorResume {
                logger.error("votoRepository.findByVotoPautaAndVotoUsuario, status=error message:${it.message}")
                Mono.error(IllegalAccessException("Error to execute find by user and order!"))
            }
    }
}
