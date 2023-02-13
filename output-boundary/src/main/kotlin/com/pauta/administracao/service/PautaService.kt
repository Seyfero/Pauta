package com.pauta.administracao.service

import com.pauta.administracao.dto.PautaOutputDto
import com.pauta.administracao.dto.UsuarioOutputDto

interface PautaService {

    fun create(pautaOutputDto: PautaOutputDto)

    fun update(pautaOutputDto: PautaOutputDto): UsuarioOutputDto

    fun deleteById(id: Long)

    fun deleteByName(nome: String)

    fun findById(id: Long): PautaOutputDto

    fun findByName(nome: String): PautaOutputDto

    fun findAll(): List<PautaOutputDto>

}