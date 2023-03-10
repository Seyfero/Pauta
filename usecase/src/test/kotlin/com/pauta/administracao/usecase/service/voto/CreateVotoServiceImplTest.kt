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
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
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

    @Test
    fun `should return false when user no able to vote`() {
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("NOT_ABLE_TO_VOTE"))

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.Error to validate cpf on vote!"
            }
            .verify()
    }

    @Test
    fun `should return error if not find order`() {
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.empty())

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.This order doesn't exists in database!"
            }
            .verify()
    }

    @Test
    fun `should return error if user voted this order`() {
        val order = populateOrder().copy(id = 1)
        val vote = populateVote()
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.just(order.toDomain()))

        `when`(votoService.findByVotoPautaAndVotoUsuarioCpf(order.id, vote.votoUsuarioDomainCpf)).thenReturn(Mono.just(vote))

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.Error to validate conditions of vote!"
            }
            .verify()
    }

    @Test
    fun `should return error if order expired`() {
        val order = populateOrder().copy(id = 1, pautaDataCriacao = LocalDateTime.now().minusSeconds(120))
        val vote = populateVote()
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.just(order.toDomain()))

        `when`(votoService.findByVotoPautaAndVotoUsuarioCpf(order.id, vote.votoUsuarioDomainCpf)).thenReturn(Mono.empty())

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.Order expired!"
            }
            .verify()
    }

    @Test
    fun `should return error if value vote is invalid`() {
        val order = populateOrder().copy(id = 1)
        val vote = populateVote()
        val inputPautaDto = populateInputVotoExternalDto().copy(votoEscolha = "outro")

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.just(order.toDomain()))

        `when`(votoService.findByVotoPautaAndVotoUsuarioCpf(order.id, vote.votoUsuarioDomainCpf)).thenReturn(Mono.empty())

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.The value of vote is invalid!"
            }
            .verify()
    }

    @Test
    fun `should return error create return error`() {
        val order = populateOrder().copy(id = 1)
        val vote = populateVote().copy(id = 1)
        val inputPautaDto = populateInputVotoExternalDto()

        `when`(validateExternalCallUserCpfService.validateExternalCallUserCpf(anyString())).thenReturn(Mono.just("ABLE_TO_VOTE"))

        `when`(pautaService.findByName(anyString())).thenReturn(Mono.just(order.toDomain()))

        `when`(votoService.findByVotoPautaAndVotoUsuarioCpf(order.id, vote.votoUsuarioDomainCpf)).thenReturn(Mono.empty())

        `when`(votoService.create(vote.toOutputDto())).thenReturn(Mono.error(Exception()))

        createVotoServiceImpl.execute(inputPautaDto)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.Error to persist vote!"
            }
            .verify()
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(1, "name", LocalDateTime.now(), 60, false)
    }

    private fun populateVote(): VotoDomain {
        return VotoDomain(null, "sim", "12130546641", 1)
    }
    private fun populateInputVotoExternalDto(): InputVotoExternalDto {
        return InputVotoExternalDto("name", "sim", "12130546641")
    }
}
