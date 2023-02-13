package com.pauta.administracao.repository

import com.pauta.administracao.entity.UsuarioEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: ReactiveCrudRepository<UsuarioEntity, Long?> {

    fun findByNome(nome: String): UsuarioEntity

    fun findByCpf(cpf: String): UsuarioEntity

}