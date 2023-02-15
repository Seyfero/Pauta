package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputboundary.dto.pauta.InputPautaDto
import com.pauta.administracao.inputboundary.dto.pauta.InputPautasAtivasDto
import com.pauta.administracao.inputboundary.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputboundary.services.pauta.CreatePautaService
import com.pauta.administracao.inputboundary.services.pauta.ListPautasAtivasService
import com.pauta.administracao.inputboundary.services.pauta.ListTodasPautasService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(
    name = "Serviço para operações sobre Pautas",
    description = "EndPoints create, update, delete to execute operations"
)
@RestController
@RequestMapping(value = ["/v1/pauta"], produces = [MediaType.APPLICATION_JSON_VALUE])
class PautaController(
    val listPautasAtivas: ListPautasAtivasService,
    val createPautaService: CreatePautaService,
    val listTodasPautasService: ListTodasPautasService
) {

    @PostMapping()
    fun createPauta(@RequestBody inputPautaDto: InputPautaDto): Mono<Boolean> {
        try {
            createPautaService.execute(inputPautaDto)
            return Mono.just(true)
        } catch (ex: Exception) {
            throw ex
        }
    }

    @GetMapping
    fun getPautasAtivas(): Flux<InputPautasAtivasDto> {
        try {
            return listPautasAtivas.execute()
        } catch (ex: Exception) {
            throw ex
        }
    }

    @GetMapping(value = ["/all"])
    fun getTodasPautas(): Flux<InputTodasPautasDto> {
        try {
            return listTodasPautasService.execute()
        } catch (ex: Exception) {
            throw ex
        }
    }
}