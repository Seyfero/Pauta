package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.toDomain
import com.pauta.administracao.database.converters.toEntity
import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.domain.Voto
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import com.pauta.administracao.database.repository.VotoRepository
import com.pauta.administracao.outputboundary.service.VotoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class VotoServiceImpl(

    private val votoRepository: VotoRepository

): VotoService {

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

    override fun deleteByPautaNomeAndUsuarioCpf(pauta: Pauta, usuario: Usuario) {
        try {
            logger.info("votoRepository.deleteByPautaNomeAndUsuarioCpf, status=try")
            votoRepository.deleteByPautaNomeAndUsuarioCpf(pauta.pautaNome, usuario.usuarioCpf)
            logger.info("votoRepository.deleteByPautaNomeAndUsuarioCpf, status=complete")
        } catch (ex: Exception) {
            logger.error("votoRepository.deleteByPautaNomeAndUsuarioCpf, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByPautaNome(pauta: PautaOutputDto): Flux<Voto> {
        try {
            logger.info("votoRepository.findByPautaNome, status=try")
            val res = votoRepository.findByPautaName(pauta.pautaNome)
            logger.info("votoRepository.findByPautaNome, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findByPautaNome, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByUsuarioCpf(usuario: UsuarioOutputDto): Flux<Voto> {
        try {
            logger.info("votoRepository.findByUsuarioCpf, status=try")
            val res = votoRepository.findByUsuarioCpf(usuario.usuarioCpf)
            logger.info("votoRepository.findByUsuarioCpf, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("votoRepository.findByUsuarioCpf, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findAll(): Flux<Voto> {
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