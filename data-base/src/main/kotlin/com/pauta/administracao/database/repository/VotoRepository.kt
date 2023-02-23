package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.VotoEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface VotoRepository : ReactiveCrudRepository<VotoEntity, Long?> {

    fun findByVotoPauta(idVotoPauta: Long?): Flux<VotoEntity>

    @Query("SELECT COUNT(v) FROM VotoEntity v WHERE v.votoPauta = :idVotoPauta AND votoEscolha = :votoEscolha")
    fun getCountVotosByPautaId(idVotoPauta: Long?, votoEscolha: String): Mono<Long>

    fun findByVotoPautaAndVotoUsuarioCpf(idVotoPauta: Long?, cpfUsuario: String): Mono<VotoEntity>
}
