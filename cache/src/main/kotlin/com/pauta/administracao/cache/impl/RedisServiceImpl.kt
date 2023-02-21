package com.pauta.administracao.cache.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pauta.administracao.cache.service.RedisService
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RedisServiceImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>
): RedisService<Any> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // TODO Essa classe vai virar abstract e o método put e get vao ser implementado nas filhas
    override fun put(key: String, value: Any): Mono<Boolean> {
        val serializedValue = serialize(value)
        return reactiveRedisOperations.opsForValue().set(key, serializedValue)
            .map { true }
            .onErrorResume { Mono.just(false) }
    }

    override fun get(key: String): Mono<Any> {
        return reactiveRedisOperations.opsForValue().get(key)
            .map { deserialize(it) }
    }

    override fun remove(key: String): Mono<Boolean> {
        TODO("Not yet implemented")
    }

    private fun serialize(value: Any): String {
        return try {
            val objectMapper = jacksonObjectMapper()
            objectMapper.writeValueAsString(value)
        } catch (e: Exception) {
            logger.error("Error to serializable, message=${e.message}")
            throw RuntimeException("Failed to serialize value!")
        }
    }

    private fun deserialize(value: Any?): Any? {
        if (value == null) {
            return null
        }
        return try {
            val objectMapper = jacksonObjectMapper()
            val json = value as String
            objectMapper.readValue(json, Any::class.java)
        } catch (e: Exception) {
            logger.error("Error to deserialize, message=${e.message}")
            throw RuntimeException("Failed to deserialize value!")
        }
    }
}