package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.usuario.toEntity
import com.pauta.administracao.database.converters.voto.toDomain
import com.pauta.administracao.database.converters.voto.toEntity
import com.pauta.administracao.database.repository.VotoRepository
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.pauta.toDomain
import com.pauta.administracao.outputboundary.converters.voto.toDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import com.pauta.administracao.outputboundary.service.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

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
}
