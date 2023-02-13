package com.pauta.administracao.repository

import com.pauta.administracao.entity.VotoEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VotoRepository: ReactiveCrudRepository<VotoEntity, Long?> {
}