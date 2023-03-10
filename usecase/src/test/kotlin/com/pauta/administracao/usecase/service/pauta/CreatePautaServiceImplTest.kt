package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class CreatePautaServiceImplTest {

    private val pautaService = mock(PautaService::class.java)

    private val createPautaServiceImpl = CreatePautaServiceImpl(pautaService)

    @Test
    fun `should validate order create, after validation`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.create(orderDomain.toOutputDto())).thenReturn(Mono.just(orderDomain.copy(id = 1)))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.empty())

        createPautaServiceImpl.execute(order)
            .`as`(StepVerifier::create)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun `should return error if exists order wirth same name`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.create(orderDomain.toOutputDto())).thenReturn(Mono.just(orderDomain.copy(id = 1)))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.just(orderDomain))

        createPautaServiceImpl.execute(order)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalArgumentException && it.message == "This order exists!"
            }
            .verify()
    }

    @Test
    fun `should return error on create`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.create(orderDomain.toOutputDto())).thenReturn(Mono.error(Exception("Error")))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.empty())

        createPautaServiceImpl.execute(order)
            .`as`(StepVerifier::create)
            .expectNext(false)
            .verifyComplete()
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, 0)
    }
}
