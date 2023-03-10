package com.pauta.administracao.outputboundary.service.repository

import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PautaService {

    fun create(pauta: PautaOutputDto): Mono<PautaDomain>

    fun update(pauta: PautaOutputDto): Mono<PautaDomain>

    fun deleteByName(nome: String): Mono<Boolean>

    fun findById(id: Long): Mono<PautaDomain>

    fun findByName(nome: String): Mono<PautaDomain>

    fun findByPautaProcessada(value: Boolean): Flux<PautaDomain>

    fun findAll(): Flux<PautaDomain>

    fun removeAllDataOnRedis(): Flux<Boolean>
}
