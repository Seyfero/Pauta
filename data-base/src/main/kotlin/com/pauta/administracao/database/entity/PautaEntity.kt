package com.pauta.administracao.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("vt_pauta")
data class PautaEntity(
    @Id @Column("vt_pauta_id") val id: Long? = null,
    @Column("vt_pauta_nome") val pautaNome: String,
    @Column("vt_pauta_criacao") val pautaDataCriacao: LocalDateTime,
    @Column("vt_pauta_duracao") val pautaDuracao: Long,
    @Column("vt_pauta_votos_total") val pautaVotosTotal: Long?,
)
