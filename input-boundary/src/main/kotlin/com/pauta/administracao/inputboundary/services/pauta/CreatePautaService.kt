package com.pauta.administracao.inputboundary.services.pauta

import com.pauta.administracao.inputboundary.dto.pauta.InputPautaDto

interface CreatePautaService {

    fun execute(inputPautaDto: InputPautaDto)

}