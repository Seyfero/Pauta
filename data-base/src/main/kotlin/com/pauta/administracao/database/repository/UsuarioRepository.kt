package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.UsuarioEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UsuarioRepository : ReactiveCrudRepository<UsuarioEntity, Long?> {

    fun deleteByUsuarioCpf(cpf: String): Mono<Boolean>

    fun findByUsuarioCpf(cpf: String): Mono<UsuarioEntity>
}
