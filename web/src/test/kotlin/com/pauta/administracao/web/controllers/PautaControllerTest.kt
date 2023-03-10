package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.services.pauta.CreatePautaService
import com.pauta.administracao.inputservice.services.pauta.DeletePautaService
import com.pauta.administracao.inputservice.services.pauta.ListTodasPautasService
import com.pauta.administracao.inputservice.services.pauta.UpdatePautaService
import com.pauta.administracao.web.config.RestExceptionHandler
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest(PautaController::class)
@ContextConfiguration(classes = [PautaController::class])
@Import(RestExceptionHandler::class)
class PautaControllerTest {

    @MockBean
    private lateinit var createPautaService: CreatePautaService

    @MockBean
    private lateinit var listTodasPautasService: ListTodasPautasService

    @MockBean
    private lateinit var updatePautaService: UpdatePautaService

    @MockBean
    private lateinit var deletePautaService: DeletePautaService

    private val URL = "/v1/pauta"

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `should create an order and return 200 when data is correct`() {
        val pauta = populateOrder()
        val request = webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(pauta), InputPautaDto::class.java)
            .exchange()

        request.expectStatus().isOk
        verify(createPautaService).execute(pauta)
    }

    @Test
    fun `should return error an order from create when data is incorrect by name`() {
        val pauta = populateOrder()
        doThrow(IllegalArgumentException::class.java).`when`(createPautaService).execute(pauta)

        webTestClient.post()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(pauta)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.message").isEqualTo("This request was no accepted!")
    }

    @Test
    fun `should update an order and return 200 when data is correct`() {
        val pauta = populateOrder()
        val request = webTestClient.put()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(pauta), InputPautaDto::class.java)
            .exchange()

        request.expectStatus().isOk
        verify(updatePautaService).execute(pauta)
    }

    @Test
    fun `should return error an order from update when data is incorrect by name`() {
        val pauta = populateOrder()
        doThrow(IllegalArgumentException::class.java).`when`(updatePautaService).execute(pauta)

        webTestClient.put()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(pauta)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.message").isEqualTo("This request was no accepted!")
    }

    @Test
    fun `should return not found an order from update when data not exists`() {
        val pauta = populateOrder()
        doThrow(NoSuchElementException::class.java).`when`(updatePautaService).execute(pauta)

        webTestClient.put()
            .uri(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(pauta)
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.message").isEqualTo("This request returned no results!")
    }

    private fun populateOrder(): InputPautaDto {
        return InputPautaDto(null, "name", LocalDateTime.now(), 60, false)
    }
}
