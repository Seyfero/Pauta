package com.pauta.administracao.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("vt_usuario")
data class UsuarioEntity(
    @Id @Column("vt_usuario_id") val id: Long? = null,
    @Column("vt_usuario_nome") val usuarioNome: String,
    @Column("vt_usuario_cpf") val usuarioCpf: String,
)
