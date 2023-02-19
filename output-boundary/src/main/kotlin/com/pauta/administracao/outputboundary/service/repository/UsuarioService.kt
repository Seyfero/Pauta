package com.pauta.administracao.outputboundary.service.repository

import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UsuarioService {

    fun create(usuario: UsuarioOutputDto): Mono<Boolean>

    fun update(usuario: UsuarioOutputDto): Mono<UsuarioDomain>

    fun deleteById(id: Long): Mono<Boolean>

    fun deleteByCpf(cpf: String): Mono<Boolean>

    fun findById(id: Long): Mono<UsuarioDomain>

    fun findByCpf(cpf: String): Mono<UsuarioDomain>

    fun findAll(): Flux<UsuarioDomain>
}
