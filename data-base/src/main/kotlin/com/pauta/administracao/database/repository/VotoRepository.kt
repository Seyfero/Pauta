package com.pauta.administracao.database.repository

import com.pauta.administracao.database.entity.VotoEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VotoRepository : ReactiveCrudRepository<VotoEntity, Long?>
