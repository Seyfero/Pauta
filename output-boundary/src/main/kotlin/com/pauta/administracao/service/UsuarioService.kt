package com.pauta.administracao.service

import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.dto.UsuarioOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UsuarioService {

    fun create(usuario: UsuarioOutputDto)

    fun update(usuario: UsuarioOutputDto): Mono<Usuario>

    fun deleteByid(id: Long)

    fun deleteByCpf(cpf: String)

    fun findById(id: Long): Mono<Usuario>

    fun findByName(nome: String): Mono<Usuario>

    fun findByCpf(cpf: String): Mono<Usuario>

    fun findAll(): Flux<Usuario>

}