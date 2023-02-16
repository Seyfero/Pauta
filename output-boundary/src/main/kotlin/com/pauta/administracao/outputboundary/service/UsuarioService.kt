package com.pauta.administracao.outputboundary.service

import com.pauta.administracao.domain.UsuarioDomain
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UsuarioService {

    fun create(usuario: UsuarioOutputDto)

    fun update(usuario: UsuarioOutputDto): Mono<UsuarioDomain>

    fun deleteById(id: Long)

    fun deleteByCpf(cpf: String)

    fun findById(id: Long): Mono<UsuarioDomain>

    fun findByName(nome: String): Mono<UsuarioDomain>

    fun findByCpf(cpf: String): Mono<UsuarioDomain>

    fun findAll(): Flux<UsuarioDomain>
}
