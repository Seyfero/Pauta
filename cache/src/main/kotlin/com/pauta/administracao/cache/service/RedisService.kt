package com.pauta.administracao.cache.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface RedisService<T> {

    fun put(value: T): Mono<Boolean>

    fun get(key: String): Mono<T?>

    fun getAll(): Flux<T?>

    fun removeAllDataOnRedis(): Flux<Boolean>

    fun remove(key: String): Mono<Boolean>
}
