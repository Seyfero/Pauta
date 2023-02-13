package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.toDomain
import com.pauta.administracao.database.converters.toEntity
import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.dto.UsuarioOutputDto
import com.pauta.administracao.database.repository.UsuarioRepository
import com.pauta.administracao.service.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UsuarioServiceImpl(

    private val usuarioRepository: UsuarioRepository

): UsuarioService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(usuario: UsuarioOutputDto) {
        try {
            logger.info("usuarioRepository.create, status=try")
            usuarioRepository.save(usuario.toDomain().toEntity())
            logger.info("usuarioRepository.create, status=complete")
        } catch (ex: Exception) {
            logger.error("usuarioRepository.create, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun update(usuario: UsuarioOutputDto): Mono<Usuario> {
        try {
            logger.info("usuarioRepository.update, status=try")
            val res = usuarioRepository.save(usuario.toDomain().toEntity())
            logger.info("usuarioRepository.update, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("usuarioRepository.update, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun deleteByid(id: Long) {
        try {
            logger.info("usuarioRepository.deleteByid, status=try")
            usuarioRepository.deleteById(id)
            logger.info("usuarioRepository.deleteByid, status=complete")
        } catch (ex: Exception) {
            logger.error("usuarioRepository.deleteByid, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun deleteByCpf(cpf: String) {
        try {
            logger.info("usuarioRepository.deleteByCpf, status=try")
            usuarioRepository.deleteByCpf(cpf)
            logger.info("usuarioRepository.deleteByCpf, status=complete")
        } catch (ex: Exception) {
            logger.error("usuarioRepository.deleteByCpf, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findById(id: Long): Mono<Usuario> {
        try {
            logger.info("usuarioRepository.findById, status=try")
            val res = usuarioRepository.findById(id)
            logger.info("usuarioRepository.findById, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("usuarioRepository.findById, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByName(nome: String): Mono<Usuario> {
        try {
            logger.info("usuarioRepository.findByName, status=try")
            val res = usuarioRepository.findByNome(nome)
            logger.info("usuarioRepository.findByName, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("usuarioRepository.findByName, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findByCpf(cpf: String): Mono<Usuario> {
        try {
            logger.info("usuarioRepository.findByCpf, status=try")
            val res = usuarioRepository.findByCpf(cpf)
            logger.info("usuarioRepository.findByCpf, status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("usuarioRepository.findByCpf, status=error message:${ex.message}")
            throw ex
        }
    }

    override fun findAll(): Flux<Usuario> {
        try {
            logger.info("usuarioRepository.    override fun findAll(): List<Usuario> status=try")
            val res = usuarioRepository.findAll()
            logger.info("usuarioRepository.    override fun findAll(): List<Usuario> status=complete")
            return res.map { it.toDomain() }
        } catch (ex: Exception) {
            logger.error("usuarioRepository.s    override fun findAll(): List<Usuario> status=error message:${ex.message}")
            throw ex
        }
    }

}