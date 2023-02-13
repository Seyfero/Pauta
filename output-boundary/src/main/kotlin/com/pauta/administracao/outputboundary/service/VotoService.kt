package com.pauta.administracao.outputboundary.service

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.domain.Voto
import com.pauta.administracao.outputboundary.dto.PautaOutputDto
import com.pauta.administracao.outputboundary.dto.UsuarioOutputDto
import com.pauta.administracao.outputboundary.dto.VotoOutputDto
import reactor.core.publisher.Flux

interface VotoService {

    fun create(voto: VotoOutputDto)

    fun deleteByPautaNomeAndUsuarioCpf(pauta: Pauta, usuario: Usuario)

    fun findByPautaNome(pauta: PautaOutputDto): Flux<Voto>

    fun findByUsuarioCpf(usuario: UsuarioOutputDto): Flux<Voto>

    fun findAll(): Flux<Voto>

}