package com.pauta.administracao.outputboundary.service

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VotoService {

    fun create(voto: VotoOutputDto)

    fun findAll(): Flux<VotoDomain>

    fun findByVotoPauta(idVotoPauta: Long): Flux<VotoDomain>

    fun findByVotoPautaAndVotoUsuario(idVotoPauta: Long?, idVotoUsuario: Long?): Mono<VotoDomain>
}
