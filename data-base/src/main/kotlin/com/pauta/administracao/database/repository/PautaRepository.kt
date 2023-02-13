package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.PautaEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface PautaRepository: ReactiveCrudRepository<PautaEntity, Long?> {

    fun findByNome(nome: String): Mono<PautaEntity>

    fun deleteByNome(nome: String)

}