package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.pauta.InputPautaDto
import com.pauta.administracao.inputservice.dto.pauta.InputTodasPautasDto
import com.pauta.administracao.inputservice.services.pauta.CreatePautaService
import com.pauta.administracao.inputservice.services.pauta.DeletePautaService
import com.pauta.administracao.inputservice.services.pauta.ListTodasPautasService
import com.pauta.administracao.inputservice.services.pauta.UpdatePautaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
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
    val createPautaService: CreatePautaService,
    val listTodasPautasService: ListTodasPautasService,
    val updatePautaService: UpdatePautaService,
    val deletePautaService: DeletePautaService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(
        summary = "EndPoint de criação da pauta",
        description = "EndPoint de criação da pauta"
    )
    @PostMapping()
    fun createPauta(@RequestBody inputPautaDto: InputPautaDto): Mono<Boolean> {
        logger.info("Data took input=$inputPautaDto")
        return createPautaService.execute(inputPautaDto)
    }

    @Operation(
        summary = "EndPoint de alteração da pauta",
        description = "EndPoint de alteração da pauta"
    )
    @PutMapping()
    @ResponseBody
    fun updatePauta(@RequestBody inputPautaDto: InputPautaDto): Mono<InputPautaDto> {
        logger.info("Data took input=$inputPautaDto")
        return updatePautaService.execute(inputPautaDto)
    }

    @Operation(
        summary = "EndPoint de listagem das pautas",
        description = "EndPoint de listagem das pautas"
    )
    @GetMapping(value = ["/all"])
    @ResponseBody
    fun getTodasPautas(): Flux<InputTodasPautasDto> {
        return listTodasPautasService.execute()
    }

    @Operation(
        summary = "EndPoint de deleção das pautas no redis",
        description = "EndPoint de deleção das pautas no redis"
    )
    @DeleteMapping(value = ["/redis/all"])
    fun deleteAllPauta(): Flux<Boolean> {
        return deletePautaService.execute()
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
