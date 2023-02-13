package com.pauta.administracao.service

import com.pauta.administracao.dto.PautaOutputDto
import com.pauta.administracao.dto.UsuarioOutputDto
import com.pauta.administracao.dto.VotacaoOutputDto

interface VotoService {

    fun create(votacaoOutputDto: VotacaoOutputDto)

    fun deleteByPautaNomeAndUsuarioCpf(pautaOutputDto: PautaOutputDto, usuarioOutputDto: UsuarioOutputDto)

    fun findByPautaName(pautaOutputDto: PautaOutputDto): List<VotacaoOutputDto>

    fun findByUsuarioCpf(usuarioOutputDto: UsuarioOutputDto): List<VotacaoOutputDto>

    fun findAll(): List<VotacaoOutputDto>

}