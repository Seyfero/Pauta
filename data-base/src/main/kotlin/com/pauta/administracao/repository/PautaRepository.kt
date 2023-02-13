package com.pauta.administracao.repository

import com.pauta.administracao.entity.PautaEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PautaRepository: ReactiveCrudRepository<PautaEntity, Long?> {

    fun deleteByNome(nome: String)

}