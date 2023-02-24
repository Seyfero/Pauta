package com.pauta.administracao.web.controllers

import com.pauta.administracao.inputservice.dto.usuario.ExternalVerificationCpfUser
import com.pauta.administracao.inputservice.services.usuario.ValidUserCpfToVoteService
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Tag(
    name = "Serviço para operações sobre Usuários",
    description = "EndPoints create, update, delete to execute operations"
)
@RestController
@RequestMapping(value = ["/v1/usuario"], produces = [MediaType.APPLICATION_JSON_VALUE])
class UsuarioController(
    private val validUserCpfToVoteService: ValidUserCpfToVoteService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(value = ["valid-cpf/{cpf}"])
    @ResponseBody
    fun getValidatePossibleVote(
        @PathVariable(name = "cpf", required = true) cpf: String
    ): Mono<ExternalVerificationCpfUser> {
        return validUserCpfToVoteService.execute(cpf)
    }
}
