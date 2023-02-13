package com.pauta.administracao.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("vt_pauta")
data class PautaEntity(
    @Id @Column("vt_pauta_id") val id: Long? = null,
    @Column("vt_pauta_nome") val pautaNome: String,
    @Column("vt_pauta_criacao") val pautaDataCriacao: LocalDate,
    @Column("vt_pauta_duracao") val pautaDuracao: Long,
    @Column("vt_pauta_votos_total") val pautaVotosTotal: Long?,
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