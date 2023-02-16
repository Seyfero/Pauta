package com.pauta.administracao.outputboundary.service

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PautaService {

    fun create(pauta: PautaOutputDto)

    fun update(pauta: PautaOutputDto): Mono<PautaDomain>

    fun deleteById(id: Long)

    fun deleteByName(nome: String)

    fun findById(id: Long): Mono<PautaDomain>

    fun findByName(nome: String): Mono<PautaDomain>

    fun findAll(): Flux<PautaDomain>
}
