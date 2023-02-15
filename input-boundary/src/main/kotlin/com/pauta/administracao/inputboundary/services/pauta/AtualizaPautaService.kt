package com.pauta.administracao.inputboundary.services.pauta

import com.pauta.administracao.inputboundary.dto.pauta.InputPautaDto

interface AtualizaPautaService {

    fun execute(inputPautaDto: InputPautaDto)

}