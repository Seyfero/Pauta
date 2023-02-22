package com.pauta.administracao.database.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostgreSql(
    @Value("\${spring.r2dbc.host}")
    private val host: String,

    @Value("\${spring.r2dbc.port}")
    private val port: String,

    @Value("\${spring.r2dbc.data-base}")
    private val dataBase: String,

    @Value("\${spring.r2dbc.schema}")
    private val schema: String,

    @Value("\${spring.r2dbc.username}")
    private val username: String,

    @Value("\${spring.r2dbc.password}")
    private val password: String
) {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .schema(schema)
                .host(host)
                .port(port.toInt())
                .database(dataBase)
                .username(username)
                .password(password)
                .build()
        )
    }
}
