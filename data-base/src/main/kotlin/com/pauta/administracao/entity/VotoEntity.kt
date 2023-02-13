package com.pauta.administracao.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("vt_voto")
data class VotoEntity(
    @Id @Column("vt_voto_id") val id: Long? = null,
    @Column("vt_usuario_id") val usuario: UsuarioEntity,
    @Column("vt_pauta_id") val pauta: PautaEntity,
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