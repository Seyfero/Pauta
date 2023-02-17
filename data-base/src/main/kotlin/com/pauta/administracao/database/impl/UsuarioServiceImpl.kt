package com.pauta.administracao.database.impl

import com.pauta.administracao.database.converters.usuario.toDomain
import com.pauta.administracao.database.converters.usuario.toEntity
import com.pauta.administracao.database.repository.UsuarioRepository
import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.outputboundary.converters.pauta.toDomain
import com.pauta.administracao.outputboundary.converters.usuario.toDomain
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto
import com.pauta.administracao.outputboundary.service.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume

@Service
class UsuarioServiceImpl(

    private val usuarioRepository: UsuarioRepository

) : UsuarioService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(usuario: UsuarioOutputDto): Mono<Boolean> {
        logger.info("usuarioRepository.create, status=try")
        return usuarioRepository.save(usuario.toDomain().toEntity())
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("usuarioRepository.create, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.create, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to create user!"))
            }
    }

    override fun update(usuario: UsuarioOutputDto): Mono<UsuarioDomain> {
        logger.info("usuarioRepository.update, status=try")
        return usuarioRepository.save(usuario.toDomain().toEntity())
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("usuarioRepository.update, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.update, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to update user!"))
            }
    }

    override fun deleteById(id: Long): Mono<Boolean> {
        logger.info("usuarioRepository.deleteByid, status=try")
        return usuarioRepository.deleteById(id)
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("usuarioRepository.deleteByid, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.deleteByid, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to delete vote by Id!"))
            }
    }

    override fun deleteByCpf(cpf: String): Mono<Boolean> {
        logger.info("usuarioRepository.deleteByCpf, status=try")
        return usuarioRepository.deleteByUsuarioCpf(cpf)
            .flatMap {
                Mono.just(true)
            }
            .doOnSuccess {
                logger.info("usuarioRepository.deleteByCpf, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.deleteByCpf, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to delete user by Cpf !"))
            }
    }

    override fun findById(id: Long): Mono<UsuarioDomain> {
        logger.info("usuarioRepository.findById, status=try")
        return usuarioRepository.findById(id)
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("usuarioRepository.findById, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.findById, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to find user by Id!"))
            }
    }

    override fun findByCpf(cpf: String): Mono<UsuarioDomain> {
        logger.info("usuarioRepository.findByCpf, status=try")
        return usuarioRepository.findByUsuarioCpf(cpf)
            .map {
                it.toDomain()
            }
            .doOnSuccess {
                logger.info("usuarioRepository.findByCpf, status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.findByCpf, status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to find user by Cpf!"))
            }
    }

    override fun findAll(): Flux<UsuarioDomain> {
        logger.info("usuarioRepository.    override fun findAll(): List<Usuario> status=try")
        return usuarioRepository.findAll()
            .map {
                it.toDomain()
            }
            .doOnTerminate {
                logger.info("usuarioRepository.    override fun findAll(): List<Usuario> status=complete")
            }
            .onErrorResume {
                logger.error("usuarioRepository.s    override fun findAll(): List<Usuario> status=error message:${it.message}")
                Mono.error(UnsupportedOperationException("Error to find all users!"))
            }
    }
}
