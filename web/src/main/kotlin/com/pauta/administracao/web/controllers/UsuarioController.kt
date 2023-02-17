package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.usuario.InputUsuarioDto
import com.pauta.administracao.inputservice.services.usuario.CreateUsuarioService
import com.pauta.administracao.inputservice.services.usuario.DeleteUsuarioService
import com.pauta.administracao.inputservice.services.usuario.ListUsuariosService
import com.pauta.administracao.inputservice.services.usuario.UpdateUsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
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

    @Operation(
        summary = "EndPoint de criação da usuario",
        description = "EndPoint de criação da usuario"
    )
    @PostMapping()
    fun createUsuario(@RequestBody inputUsuarioDto: InputUsuarioDto): Mono<Boolean> {
        return createUsuarioService.execute(inputUsuarioDto)
    }

    @Operation(
        summary = "EndPoint de alteração da usuario",
        description = "EndPoint de alteração da usuario"
    )
    @PutMapping()
    @ResponseBody
    fun updateUsuario(@RequestBody inputUsuarioDto: InputUsuarioDto): Mono<InputUsuarioDto> {
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