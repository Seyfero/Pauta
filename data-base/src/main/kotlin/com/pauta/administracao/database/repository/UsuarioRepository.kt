package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.UsuarioEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UsuarioRepository: ReactiveCrudRepository<UsuarioEntity, Long?> {

    fun deleteByCpf(cpf: String)

    fun findByNome(nome: String): Mono<UsuarioEntity>

    fun findByCpf(cpf: String): Mono<UsuarioEntity>

}