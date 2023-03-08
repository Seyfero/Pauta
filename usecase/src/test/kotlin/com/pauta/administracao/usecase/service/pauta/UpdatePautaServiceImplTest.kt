package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class UpdatePautaServiceImplTest {

    private val pautaService = mock(PautaService::class.java)

    private val updatePautaServiceImpl = UpdatePautaServiceImpl(pautaService)

    @Test
    fun `should validate order update, after validation`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao, id = 1)
        val orderDomainAfterUpdate= order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao, id = 1, pautaNome = "otherName")

        `when`(pautaService.update(orderDomain.toOutputDto())).thenReturn(Mono.just(orderDomainAfterUpdate))

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.just(orderDomain))

        updatePautaServiceImpl.execute(orderDomain.toInputDto())
            .`as`(StepVerifier::create)
            .expectNext(orderDomainAfterUpdate.toInputDto())
            .verifyComplete()

    }


    @Test
    fun `should return error if not exists order`() {
        val order = populateOrder()

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.empty())

        updatePautaServiceImpl.execute(order)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "Error to update order!"
            }
            .verify()
    }

    @Test
    fun `should return error on operation`() {
        val order = populateOrder()

        `when`(pautaService.findByName(order.pautaNome)).thenReturn(Mono.error(Exception("Error")))

        updatePautaServiceImpl.execute(order)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "Error to update order!"
            }
            .verify()
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, 0)
    }
}