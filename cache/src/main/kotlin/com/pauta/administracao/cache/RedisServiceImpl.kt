package com.pauta.administracao.cache

import com.pauta.administracao.cache.service.RedisService
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import reactor.core.publisher.Mono


abstract class RedisServiceImpl<T : Any>(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>
): RedisService<T> {

    abstract override fun put(key: String, value: T): Mono<Boolean>

    abstract override fun get(key: String): Mono<T?>

    abstract override fun remove(key: String): Mono<Boolean>

    protected abstract fun serialize(value: T?): String

    protected abstract fun deserialize(value: Any?): Any?

}