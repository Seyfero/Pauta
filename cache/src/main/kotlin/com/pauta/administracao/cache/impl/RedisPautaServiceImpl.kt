package com.pauta.administracao.cache.impl

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
        val nameIndexKey = "pauta:nome:${value.pautaNome}:pauta"
        return reactiveRedisOperations.opsForValue().set(nameIndexKey, serializedValue)
            .thenReturn(true)
            .doOnSuccess { logger.info("Value of order created with success in redis!") }
            .onErrorResume {
                logger.error("Not success to create data in redis!")
                Mono.just(false)
            }
    }

    override fun get(key: String): Mono<PautaDomain?> {
        return reactiveRedisOperations.opsForValue().get(key)
            .map {
                deserialize(it)
            }
            .doOnSuccess { logger.info("Order searched on redis with success!") }
            .doOnError { logger.error("Order not searched on redis!") }
            .switchIfEmpty(Mono.empty())
    }

    override fun getAll(): Flux<PautaDomain?> {
        return reactiveRedisOperations.keys("*")
            .collectList()
            .flatMap (reactiveRedisOperations.opsForValue()::multiGet)
            .flatMapMany { Flux.fromIterable((it)) }
            .map { deserialize(it) }
            .onErrorResume {
                logger.error("Not success to take data in redis!")
                Mono.empty()
            }
    }

    override fun removeAll(): Flux<Boolean> {
        return reactiveRedisOperations.keys("*")
            .flatMap { redisKey ->
                reactiveRedisOperations.opsForValue().delete(redisKey).thenReturn(true)
            }
            .doOnTerminate { logger.info("Order removed on redis with success!") }
            .onErrorResume {
                logger.error("Not success to delete data in redis!")
                Flux.just(false)
            }
    }

    override fun remove(key: String): Mono<Boolean> {
        return reactiveRedisOperations.opsForValue().delete(key)
            .doOnSuccess { logger.info("Order removed on redis with success!") }
            .doOnError { logger.error("Order not removed on redis!") }
    }

    override fun serialize(value: PautaDomain?): String {
        return try {
            val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
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
            val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
            val json = value as String
            objectMapper.readValue(json, PautaDomain::class.java)
        } catch (e: Exception) {
            logger.error("Error to deserialize, message=${e.message}")
            throw RuntimeException("Failed to deserialize value!")
        }
    }
}
