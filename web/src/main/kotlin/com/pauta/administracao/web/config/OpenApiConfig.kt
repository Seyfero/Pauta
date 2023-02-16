package com.pauta.administracao.web.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Collections

@Configuration
@OpenAPIDefinition
class OpenApiConfig(
    @Value("\${springdoc.api.title}") private val title: String,
    @Value("\${springdoc.api.version}") private val version: String,
    @Value("\${springdoc.app.proxy}") private val proxy: String,
) {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title(title)
                    .version(version)
            )
            .also {
                openApi ->
                openApi.servers = Collections.singletonList(Server().url(proxy))
            }
    }
}
