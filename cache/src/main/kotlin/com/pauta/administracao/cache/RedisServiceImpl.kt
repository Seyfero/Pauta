package com.pauta.administracao.cache

import com.pauta.administracao.cache.service.RedisService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class RedisServiceImpl<T : Any>: RedisService<T> {

    abstract override fun put(value: T): Mono<Boolean>

    abstract override fun get(key: String): Mono<T?>

    abstract override fun getAll(key: String): Flux<T?>

    abstract override fun remove(key: String): Mono<Boolean>

    protected abstract fun serialize(value: T?): String

    protected abstract fun deserialize(value: Any?): Any?
}
