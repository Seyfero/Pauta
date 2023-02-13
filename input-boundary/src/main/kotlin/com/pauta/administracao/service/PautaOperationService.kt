package com.pauta.administracao.service

import com.pauta.administracao.dto.PautaDto

interface PautaOperationService {

    fun createPauta()

    fun deletePauta()

    fun updatePauta(): PautaDto

    fun findPautaByName(): PautaDto

    fun findPautaById(): PautaDto

    fun findAll(): List<PautaDto>

}