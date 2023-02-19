package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.voto.InputVotoExternalDto
import com.pauta.administracao.inputservice.dto.voto.InputVotoInternalDto
import com.pauta.administracao.inputservice.services.voto.CreateVotoService
import com.pauta.administracao.inputservice.services.voto.DeleteVotoService
import com.pauta.administracao.inputservice.services.voto.ListVotoByEscolhaService
import com.pauta.administracao.inputservice.services.voto.ListVotoByPautaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Tag(
    name = "Serviço para operações sobre Votos",
    description = "EndPoints create, update, delete to execute operations"
)
@RestController
@RequestMapping(value = ["/v1/voto"], produces = [MediaType.APPLICATION_JSON_VALUE])
class VotoController(

    private val createVotoService: CreateVotoService,
    private val deleteVotoService: DeleteVotoService,
    private val listVotoByEscolhaService: ListVotoByEscolhaService,
    private val listVotoByPautaService: ListVotoByPautaService

) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(
        summary = "EndPoint de criação da voto",
        description = "EndPoint de criação da voto"
    )
    @PostMapping()
    fun createVoto(@RequestBody inputVotoExternalDto: InputVotoExternalDto): Mono<Boolean> {
        logger.info("Data took input=$inputVotoExternalDto")
        return createVotoService.execute(inputVotoExternalDto)
    }

    @Operation(
        summary = "EndPoint que contabiliza os votos",
        description = "EndPoint que contabiliza os votos"
    )
    @GetMapping
    @ResponseBody
    fun getVotosAtivas(@RequestParam id: Long, @RequestParam valorVoto: String): Mono<Long> {
        return listVotoByEscolhaService.execute(id, valorVoto)
    }

    @Operation(
        summary = "EndPoint de listagem das votos por pauta",
        description = "EndPoint de listagem das votos por pauta"
    )
    @GetMapping(value = ["/all"])
    @ResponseBody
    fun getTodasVotos(@RequestParam nomePauta: String): Flux<InputVotoInternalDto> {
        return listVotoByPautaService.execute(nomePauta)
    }

    @Operation(
        summary = "EndPoint de deleção da voto por Id",
        description = "EndPoint de deleção da voto por Id"
    )
    @DeleteMapping(value = ["/id"])
    fun deleteVotoById(@RequestParam id: Long): Mono<Boolean> {
        return deleteVotoService.execute(id)
    }
}
