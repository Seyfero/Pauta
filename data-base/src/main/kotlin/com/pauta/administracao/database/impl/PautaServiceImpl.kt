package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.toDomain
import com.pauta.administracao.database.converters.toEntity
import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.database.repository.PautaRepository
import com.pauta.administracao.outputboundary.service.PautaService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PautaServiceImpl(

    private val pautaRepository: PautaRepository

): PautaService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(pauta: PautaOutputDto) {
        try {
            logger.info("pautaRepository.save, status=try")
            pautaRepository.save(pauta.toDomain().toEntity())
            logger.info("pautaRepository.save, status=complete")
        } catch (ex: Exception) {
            logger.error("pautaRepository.save, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun update(pauta: PautaOutputDto): Mono<Pauta> {
        try {
            logger.info("pautaRepository.update, status=try")
            pautaRepository.save(pauta.toDomain().toEntity())
            logger.info("pautaRepository.update, status=complete")
            return Mono.just(pauta.toDomain())
        } catch (ex: Exception) {
            logger.error("pautaRepository.update, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun deleteById(id: Long) {
        try {
            logger.info("pautaRepository.deleteById, status=try")
            pautaRepository.deleteById(id)
            logger.info("pautaRepository.deleteById, status=complete")
        } catch (ex: Exception) {
            logger.error("pautaRepository.deleteById, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun deleteByName(nome: String) {
        try {
            logger.info("pautaRepository.deleteByName, status=try")
            pautaRepository.deleteByPautaNome(nome)
            logger.info("pautaRepository.deleteByName, status=complete")
        } catch (ex: Exception) {
            logger.error("pautaRepository.deleteByName, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findById(id: Long): Mono<Pauta> {
        try {
            logger.info("pautaRepository.findById, status=try")
            val res = pautaRepository.findById(id)
            logger.info("pautaRepository.findById, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("pautaRepository.findById, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByName(nome: String): Mono<Pauta> {
        try {
            logger.info("pautaRepository.findByName, status=try")
            val res = pautaRepository.findByPautaNome(nome)
            logger.info("pautaRepository.findByName, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("pautaRepository.findByName, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findAll(): Flux<Pauta> {
        try {
            logger.info("pautaRepository.findAll, status=try")
            val res = pautaRepository.findAll()
            logger.info("pautaRepository.findAll, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("pautaRepository.findAll, status=error message:${ex.message}")
            throw ex
        }
    }
}