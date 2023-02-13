package com.pauta.administracao.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("vt_usuario")
data class UsuarioEntity(
    @Id @Column("vt_usuario_id") val id: Long? = null,
    @Column("vt_usuario_nome") val usuarioNome: String,
    @Column("vt_usuario_cpf") val usuarioCpf: String,
) {
//    @PersistenceCreator
//    constructor(
//        id: Long?,
//        pautaDuration: Long,
//        pautaVotosTotal: Long? = 0,
//        pautaCreation: LocalDate
//    ) : this(
//        id = Long.MAX_VALUE,
//        pautaName = "",
//        pautaDuration = 0,
//        pautaVotosTotal = 0,
//        pautaCreation = LocalDate.now()
//    )
}