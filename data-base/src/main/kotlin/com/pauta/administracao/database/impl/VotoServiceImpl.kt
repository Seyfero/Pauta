package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.voto.toDomain
import com.pauta.administracao.database.converters.voto.toEntity
import com.pauta.administracao.database.repository.VotoRepository
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.voto.toDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import com.pauta.administracao.outputboundary.service.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VotoServiceImpl(

    private val votoRepository: VotoRepository

) : VotoService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(voto: VotoOutputDto) {
        try {
            logger.info("votoRepository.create, status=try")
            votoRepository.save(voto.toDomain().toEntity())
            logger.info("votoRepository.create, status=complete")
        } catch (ex: Exception) {
            logger.error("votoRepository.create, status=error message:${ex.message}")
            throw ex
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
                Mono.error(IllegalAccessException("Erro ao executar a operação"))
            }
    }

    override fun findAll(): Flux<VotoDomain> {
        try {
            logger.info("votoRepository.findAll, status=try")
            val res = votoRepository.findAll()
            logger.info("votoRepository.findAll, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findAll, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByVotoPauta(idVotoPauta: Long?): Flux<VotoDomain> {
        try {
            logger.info("votoRepository.findByVotoPauta, status=try")
            val res = votoRepository.findByVotoPauta(idVotoPauta)
            logger.info("votoRepository.findByVotoPauta, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findByVotoPauta, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByVotoPautaNome(idVotoPauta: Long?): Flux<VotoDomain> {
        try {
            logger.info("votoRepository.findByVotoPauta, status=try")
            val res = votoRepository.findByVotoUsuario(idVotoPauta)
            logger.info("votoRepository.findByVotoPauta, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findByVotoPauta, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByVotoPautaAndVotoUsuario(idVotoPauta: Long?, idVotoUsuario: Long?): Mono<VotoDomain> {
        try {
            logger.info("votoRepository.findByVotoPautaAndVotoUsuario, status=try")
            val res = votoRepository.findByVotoPautaAndVotoUsuario(idVotoPauta, idVotoUsuario)
            logger.info("votoRepository.findByVotoPautaAndVotoUsuario, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findByVotoPautaAndVotoUsuario, status=error message:${ex.message}")
            throw ex
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
                Mono.error(IllegalAccessException("Erro ao executar a operação"))
            }
    }
}
