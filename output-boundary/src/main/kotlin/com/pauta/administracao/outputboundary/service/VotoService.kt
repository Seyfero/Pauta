package com.pauta.administracao.outputboundary.service

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import reactor.core.publisher.Flux

interface VotoService {

    fun create(voto: VotoOutputDto)

    fun findAll(): Flux<VotoDomain>
}
