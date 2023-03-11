package com.pauta.administracao.usecase.service.pauta

import com.pauta.administracao.inputservice.converters.pauta.toDomain
import com.pauta.administracao.inputservice.converters.pauta.toInputTotal
import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.outputboundary.service.repository.PautaService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.LocalDateTime

class ListarTodasPautaServiceImplTest {

    private val pautaService = mock(PautaService::class.java)

    private val listTodasPautaServiceImpl = ListTodasPautasServiceImpl(pautaService)

    @Test
    fun `should get all orders if exist`() {
        val order = populateOrder()
        val orderDomain = order.toDomain().copy(pautaDataCriacao = order.pautaDataCriacao, id = 1)

        `when`(pautaService.findAll()).thenReturn(Flux.just(orderDomain.copy(id = 1)))

        listTodasPautaServiceImpl.execute()
            .`as`(StepVerifier::create)
            .expectNext(orderDomain.toInputTotal())
            .verifyComplete()
    }

    @Test
    fun `should return empty if not exists order`() {

        `when`(pautaService.findAll()).thenReturn(Flux.empty())

        listTodasPautaServiceImpl.execute()
            .`as`(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    fun `should return error on findAll`() {

        `when`(pautaService.findAll()).thenReturn(Flux.error(Exception("Error")))

        listTodasPautaServiceImpl.execute()
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalStateException && it.message == "server.error.Error on convert all orders!"
            }
            .verify()
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, false)
    }
}
