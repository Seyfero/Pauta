package com.pauta.administracao.outputboundary.service.repository

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VotoService {

    fun create(voto: VotoOutputDto): Mono<Boolean>

    fun delete(id: Long): Mono<Boolean>

    fun findAll(): Flux<VotoDomain>

    fun findByVotoPauta(idVotoPauta: Long?): Flux<VotoDomain>

    fun findByVotoPautaAndVotoUsuarioCpf(idVotoPauta: Long?, cpfUsuario: String): Mono<VotoDomain>

    fun getCountVotosByPautaId(idVotoPauta: Long?, votoEscolha: String): Mono<Long>
}
