package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.CreateUsuarioService
import com.pauta.administracao.inputservice.services.usuario.DeleteUsuarioService
import com.pauta.administracao.inputservice.services.usuario.ListUsuariosService
import com.pauta.administracao.inputservice.services.usuario.UpdateUsuarioService
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
    name = "Serviço para operações sobre Usuários",
    description = "EndPoints create, update, delete to execute operations"
)
@RestController
@RequestMapping(value = ["/v1/usuario"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UsuarioController(
    private val createUsuarioService: CreateUsuarioService,
    private val deleteUsuarioService: DeleteUsuarioService,
    private val listUsuariosService: ListUsuariosService,
    private val updateUsuarioService: UpdateUsuarioService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Operation(
        summary = "EndPoint de criação da usuario",
        description = "EndPoint de criação da usuario"
    )
    @PostMapping()
    fun createUsuario(@RequestBody inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        logger.info("Data took input=$inputUsuarioDto")
        return createUsuarioService.execute(inputUsuarioDto)
    }

    @Operation(
        summary = "EndPoint de alteração da usuario",
        description = "EndPoint de alteração da usuario"
    )
    @PutMapping()
    @ResponseBody
    fun updateUsuario(@RequestBody inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto> {
        logger.info("Data took input=$inputUsuarioDto")
        return updateUsuarioService.execute(inputUsuarioDto)
    }

    @Operation(
        summary = "EndPoint de listagem das usuarios",
        description = "EndPoint de listagem das usuarios"
    )
    @GetMapping(value = ["/all"])
    @ResponseBody
    fun getTodasUsuarios(): Flux<InputUsuarioDto> {
        return listUsuariosService.execute()
    }

    @Operation(
        summary = "EndPoint de deleção da usuario por Id",
        description = "EndPoint de deleção da usuario por Id"
    )
    @DeleteMapping(value = ["/id"])
    fun deleteUsuarioById(@RequestParam id: Long): Mono<Boolean> {
        return deleteUsuarioService.execute(id)
    }

    @Operation(
        summary = "EndPoint de deleção da usuario por Cpf",
        description = "EndPoint de deleção da usuario por Cpf"
    )
    @DeleteMapping(value = ["/cpf"])
    fun deleteUsuarioByNome(@RequestParam cpf: String): Mono<Boolean> {
        return deleteUsuarioService.execute(cpf)
    }
}
