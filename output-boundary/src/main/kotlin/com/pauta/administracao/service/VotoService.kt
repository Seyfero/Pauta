package com.pauta.administracao.service

import com.pauta.administracao.domain.Pauta
import com.pauta.administracao.domain.Usuario
import com.pauta.administracao.domain.Voto
import com.pauta.administracao.dto.PautaOutputDto
import com.pauta.administracao.dto.UsuarioOutputDto
import com.pauta.administracao.dto.VotoOutputDto
import reactor.core.publisher.Flux

interface VotoService {

    fun create(voto: VotoOutputDto)

    fun deleteByPautaNomeAndUsuarioCpf(pauta: Pauta, usuario: Usuario)

    fun findByPautaNome(pauta: PautaOutputDto): Flux<Voto>

    fun findByUsuarioCpf(usuario: UsuarioOutputDto): Flux<Voto>

    fun findAll(): Flux<Voto>

}