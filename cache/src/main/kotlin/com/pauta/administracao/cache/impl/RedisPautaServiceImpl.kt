package com.pauta.administracao.cache.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pauta.administracao.cache.RedisServiceImpl
import com.pauta.administracao.domain.PautaDomain
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Primary
@Qualifier(value = "redisService")
class RedisPautaServiceImpl(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>,
) : RedisServiceImpl<PautaDomain>() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun put(value: PautaDomain): Mono<Boolean> {
        val serializedValue = serialize(value)
        val hashKey = "pauta:id:${value.id}:pauta"
        val nameIndexKey = "pauta:nome:${value.pautaNome}:pauta"
        return reactiveRedisOperations.opsForValue().set(hashKey, serializedValue)
            .flatMap { success ->
                if (success) {
                    logger.info("Value of order created with success in redis!")
                    reactiveRedisOperations.opsForSet().add(nameIndexKey, hashKey)
                    logger.info("Compose keys created with success!")
                    Mono.just(true)
                } else {
                    logger.error("Not success to create data in redis!")
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

    override fun getAll(key: String): Flux<PautaDomain?> {
        return reactiveRedisOperations.opsForSet().members(key)
            .flatMap { redisKey ->
                reactiveRedisOperations.opsForValue().get(redisKey)
            }
            .map {
                deserialize(it)
            }
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
            .onErrorResume {
                logger.error("Not success to delete data in redis!")
                Mono.just(false)
            }
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