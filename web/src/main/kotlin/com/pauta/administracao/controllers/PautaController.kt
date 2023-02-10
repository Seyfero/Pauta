package com.pauta.administracao.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Tag(
    name = "Serviço para operações sobre Pautas",
    description = "EndPoints create, update, delete to execute operations"
)
@RestController
@RequestMapping(value = ["/v1"], produces = [MediaType.APPLICATION_JSON_VALUE])
class PautaController {

    @PostMapping(value = ["/pauta"])
    fun createPauta(@RequestBody value: String): Mono<String> {
        return Mono.just("Hello-World: $value")
    }


}