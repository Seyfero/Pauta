package com.pauta.administracao.repository

import com.pauta.administracao.entity.VotoEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface VotoRepository: ReactiveCrudRepository<VotoEntity, Long?> {
    fun deleteByPautaNomeAndUsuarioCpf(pautaNome: String, usuarioCpf: String)

    fun findByPautaName(pautaNome: String): Flux<VotoEntity>

    fun findByUsuarioCpf(usuarioCpf: String): Flux<VotoEntity>
}