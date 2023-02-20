package com.pauta.administracao.gatewayimpl

import com.pauta.administracao.outputboundary.service.gateway.ValidateExternalCallUserCpfService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Service
class ValidateExternalCallUserCpfServiceImpl(

    @Value("\${gateway.url}") private val url: String,

) : ValidateExternalCallUserCpfService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun validateExternalCallUserCpf(cpf: String): Mono<String> {
        val client = WebClient.create()

        val uri = UriComponentsBuilder.fromUriString("$url/{cpf}")
            .buildAndExpand(cpf)
            .toUri()

        logger.info("Url created!")

        return client.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String::class.java)
            .doOnSuccess {
                logger.info("User founded with succsess!")
            }
            .onErrorResume {
                logger.info("Error to verify user's cpf in external call!")
                Mono.error(IllegalStateException("Error to verify validate user's cpf!", it))
            }
    }
}
