package com.pauta.administracao.service

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.dto.PautaOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface PautaService {

    fun create(pauta: PautaOutputDto)

    fun update(pauta: PautaOutputDto): Mono<Pauta>

    fun deleteById(id: Long)

    fun deleteByName(nome: String)

    fun findById(id: Long): Mono<Pauta>

    fun findByName(nome: String): Mono<Pauta>

    fun findAll(): Flux<Pauta>

}