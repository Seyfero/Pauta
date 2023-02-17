package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.pauta.InputPautasAtivasDto
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputservice.services.pauta.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
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
    val listTodasPautasService: ListTodasPautasService,
    val updatePautaService: UpdatePautaService,
    val deletePautaService: DeletePautaService
) {

    @Operation(
        summary = "EndPoint de criação da pauta",
        description = "EndPoint de criação da pauta"
    )
    @PostMapping()
    fun createPauta(@RequestBody inputPautaDto: InputPautaDto): Mono<Boolean> {
            return createPautaService.execute(inputPautaDto)
    }

    @Operation(
        summary = "EndPoint de alteração da pauta",
        description = "EndPoint de alteração da pauta"
    )
    @PutMapping()
    fun updatePauta(@RequestBody inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        return updatePautaService.execute(inputPautaDto)
    }

    @Operation(
        summary = "EndPoint de listagem das pautas",
        description = "EndPoint de listagem das pautas"
    )
    @GetMapping
    fun getPautasAtivas(): Flux<InputPautasAtivasDto> {
        return listPautasAtivas.execute()
    }

    @Operation(
        summary = "EndPoint de listagem das pautas",
        description = "EndPoint de listagem das pautas"
    )
    @GetMapping(value = ["/all"])
    fun getTodasPautas(): Flux<InputTodasPautasDto> {
        return listTodasPautasService.execute()
    }

    @Operation(
        summary = "EndPoint de deleção da pauta por Id",
        description = "EndPoint de deleção da pauta por Id"
    )
    @DeleteMapping(value = ["/id"])
    fun deletePautaById(@RequestParam id: Long): Mono<Boolean> {
        return deletePautaService.execute(id)
    }

    @Operation(
        summary = "EndPoint de deleção da pauta por Nome",
        description = "EndPoint de deleção da pauta por Nome"
    )
    @DeleteMapping(value = ["/nome"])
    fun deletePautaByNome(@RequestParam nome: String): Mono<Boolean> {
        return deletePautaService.execute(nome)
    }
}
