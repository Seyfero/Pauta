package com.pauta.administracao.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {

    @Value("\${spring.redis.host}")
    lateinit var host: String

    @Value("\${spring.redis.port}")
    lateinit var port: String

    @Value("\${spring.redis.password}")
    lateinit var password: String

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration(host, port.toInt())
        redisStandaloneConfiguration.password = RedisPassword.of(password)
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun reactiveRedisOperations(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Any> {
        val serializer = StringRedisSerializer()
        val hashValueSerializer = GenericJackson2JsonRedisSerializer()

        val context = RedisSerializationContext.newSerializationContext<String, Any>(serializer)
            .hashKey(serializer)
            .hashValue(hashValueSerializer)
            .build()

        return ReactiveRedisTemplate(connectionFactory, context)
    }
}
