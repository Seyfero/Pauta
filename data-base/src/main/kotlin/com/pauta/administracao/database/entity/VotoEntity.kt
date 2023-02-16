package com.pauta.administracao.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("vt_voto")
data class VotoEntity(
    @Id @Column("vt_voto_id") val id: Long? = null,
    @Column("vt_voto_escolha") val votoEscolha: String,
    @Column("vt_usuario_id") val votoUsuario: UsuarioEntity,
    @Column("vt_pauta_id") val votoPauta: PautaEntity,
)
