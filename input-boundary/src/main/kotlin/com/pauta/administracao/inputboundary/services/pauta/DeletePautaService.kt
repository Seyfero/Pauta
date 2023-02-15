package com.pauta.administracao.inputboundary.services.pauta

interface DeletePautaService {

    fun execute(inputId: Long)

    fun execute(inputNome: String)

}