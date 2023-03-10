package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class DeletePautaServiceImplTest {

    private val pautaService = mock(PautaService::class.java)

    private val deletePautaServiceImpl = DeletePautaServiceImpl(pautaService)

    @Test
    fun `should validate order deleted, after validation`() {
        val order = populateOrder().copy(id = 1)
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.deleteByName(orderDomain.pautaNome)).thenReturn(Mono.just(true))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.just(order.toDomain()))

        deletePautaServiceImpl.execute(order.pautaNome)
            .`as`(StepVerifier::create)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun `should return error if not exists order wirth name`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.deleteByName(orderDomain.pautaNome)).thenReturn(Mono.just(true))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.empty())

        deletePautaServiceImpl.execute(order.pautaNome)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is NoSuchElementException && it.message == "Order not found!"
            }
            .verify()
    }

    @Test
    fun `should return error on create`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao)

        `when`(pautaService.deleteByName(orderDomain.pautaNome)).thenReturn(Mono.error(Exception("Error")))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.just(orderDomain))

        deletePautaServiceImpl.execute(order.pautaNome)
            .`as`(StepVerifier::create)
            .expectNext(false)
            .verifyComplete()
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, false)
    }
}
