package com.pauta.administracao.cache.service

import reactor.core.publisher.Mono

interface RedisService<T> {

    fun put(key: String, value: T): Mono<Boolean>

    fun get(key: String): Mono<T>

    fun remove(key: String): Mono<Boolean>

}