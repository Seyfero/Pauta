package com.pauta.administracao.usecase.service.voto

import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoExternalDto
import com.pauta.administracao.outputboundary.converters.voto.toOutputDto
import com.pauta.administracao.outputboundary.service.gateway.ValidateExternalCallUserCpfService
import com.pauta.administracao.outputboundary.service.repository.PautaService
import com.pauta.administracao.outputboundary.service.repository.VotoService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class CreateVotoServiceImplTest {

    private val pautaService = mock(PautaService::class.java)

    private val votoService = mock(VotoService::class.java)

    private val validateExternalCallUserCpfService = mock(ValidateExternalCallUserCpfService::class.java)

    private val createVotoServiceImpl = CreateVotoServiceImpl(votoService, pautaService, validateExternalCallUserCpfService)

    @Test
    fun `should create vote if all pre requirements are ok`() {
        val order = populateOrder().copy(id = 1)
        val vote = populateVote()
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.just(order.toDomain()))

        `when`(votoService.findByVotoPautaAndVotoUsuarioCpf(order.id, vote.votoUsuarioDomainCpf)).thenReturn(Mono.empty())

        `when`(votoService.create(vote.toOutputDto())).thenReturn(Mono.just(true))

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectNext(true)
            .verifyComplete()

    }


    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, 0)
    }

    private fun populateVote(): VotoDomain {
        return VotoDomain(null, "sim", "12130546641", 1)
    }
    private fun populateInputVotoExternalDto(): InputVotoExternalDto {
        return InputVotoExternalDto("name", "sim", "12130546641")
    }
}