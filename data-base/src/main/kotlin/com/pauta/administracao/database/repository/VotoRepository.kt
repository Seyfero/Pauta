package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.VotoEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface VotoRepository : ReactiveCrudRepository<VotoEntity, Long?> {

    fun findByVotoPauta(idVotoPauta: Long): Flux<VotoEntity>

    fun findByVotoPautaAndVotoUsuario(idVotoPauta: Long, idVotoUsuario: Long): Mono<VotoEntity>

}
