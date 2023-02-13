package com.pauta.administracao.service

import com.pauta.administracao.dto.UsuarioOutputDto

interface UsuarioService {

    fun create(usuarioOutputDto: UsuarioOutputDto)

    fun update(usuarioOutputDto: UsuarioOutputDto): UsuarioOutputDto

    fun deleteByid(id: Long)

    fun deleteByCpf(cpf: String)

    fun findById(id: Long): UsuarioOutputDto

    fun findByName(nome: String): UsuarioOutputDto

    fun findByCpf(cpf: String): UsuarioOutputDto

    fun findAll(): List<UsuarioOutputDto>

}