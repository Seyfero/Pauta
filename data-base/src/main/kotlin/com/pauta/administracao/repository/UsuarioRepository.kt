package com.pauta.administracao.repository

import com.pauta.administracao.entity.UsuarioEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UsuarioRepository: ReactiveCrudRepository<UsuarioEntity, Long?> {
}