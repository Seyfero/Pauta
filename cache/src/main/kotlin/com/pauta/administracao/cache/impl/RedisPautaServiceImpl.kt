package com.pauta.administracao.cache.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pauta.administracao.cache.RedisServiceImpl
import com.pauta.administracao.domain.PautaDomain
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RedisPautaServiceImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>
): RedisServiceImpl<PautaDomain>(reactiveRedisOperations) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun put(key: String, value: PautaDomain): Mono<Boolean> {
        val serializedValue = serialize(value)
        val hashKey = "pauta:id:${value.id}:pauta"
        val nameIndexKey = "pauta:nome:${value.pautaNome}:pauta"
        return reactiveRedisOperations.opsForValue().set(hashKey, serializedValue)
            .flatMap { success ->
                if (success) {
                    reactiveRedisOperations.opsForSet().add(nameIndexKey, hashKey)
                    Mono.just(true)
                } else {
                    Mono.just(false)
                }
            }
            .onErrorResume { Mono.just(false) }
    }

    override fun get(key: String): Mono<PautaDomain?> {
        return reactiveRedisOperations.opsForSet().members(key)
            .flatMap { redisKey ->
                reactiveRedisOperations.opsForValue().get(redisKey)
            }
            .map {
                deserialize(it)
            }
            .singleOrEmpty()
    }

    override fun remove(key: String): Mono<Boolean> {
        return reactiveRedisOperations.opsForSet()
            .members(key)
            .map { redisKey -> redisKey.toString() }
            .collectList()
            .flatMapMany { redisKeys -> Flux.fromIterable(redisKeys) }
            .flatMap { redisKey ->
                reactiveRedisOperations.opsForValue().delete(redisKey)
            }
            .then(Mono.just(true))
            .onErrorResume { Mono.just(false) }
    }

    override fun serialize(value: PautaDomain?): String {
        return try {
            val objectMapper = jacksonObjectMapper()
            objectMapper.writeValueAsString(value)
        } catch (e: Exception) {
            logger.error("Error to serializable, message=${e.message}")
            throw RuntimeException("Failed to serialize value!")
        }
    }

    override fun deserialize(value: Any?): PautaDomain? {
        if (value == null) {
            return null
        }
        return try {
            val objectMapper = jacksonObjectMapper()
            val json = value as String
            objectMapper.readValue(json, PautaDomain::class.java)
        } catch (e: Exception) {
            logger.error("Error to deserialize, message=${e.message}")
            throw RuntimeException("Failed to deserialize value!")
        }
    }

}