package com.pauta.administracao.repository

import com.pauta.administracao.entity.PautaEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PautaRepository: ReactiveCrudRepository<PautaEntity, Long?> {
}