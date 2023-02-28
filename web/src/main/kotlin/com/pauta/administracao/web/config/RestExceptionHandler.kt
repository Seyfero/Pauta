package com.pauta.administracao.web.config

import com.pauta.administracao.domain.exception.ExpiredPautaException
import com.pauta.administracao.domain.exception.IllegalPautaException
import com.pauta.administracao.domain.exception.IllegalUsuarioException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.webjars.NotFoundException
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(
        value = [
            NotFoundException::class,
            NoSuchElementException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun notFoundException(ex: RuntimeException): ApiError {
        return ApiError(
            code = "E${HttpStatus.NOT_FOUND.value()}",
            message = "This request returned no results!",
            description = ex.message,
            timesTemp = LocalDateTime.now()
        )
    }

    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            IllegalStateException::class,
            IllegalAccessException::class,
            UnsupportedOperationException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun illegalArgumentsException(ex: RuntimeException): ApiError {
        return ApiError(
            code = "E${HttpStatus.BAD_REQUEST.value()}",
            message = "This request was no accepted!",
            description = ex.message,
            timesTemp = LocalDateTime.now()
        )
    }

    @ExceptionHandler(
        value = [
            IllegalPautaException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun illegalPautaException(ex: RuntimeException): ApiError {
        return ApiError(
            code = "E${HttpStatus.BAD_REQUEST.value()}",
            message = "This pauta was no accepted!",
            description = ex.message,
            timesTemp = LocalDateTime.now()
        )
    }

    @ExceptionHandler(
        value = [
            IllegalUsuarioException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun illegalUsuarioException(ex: RuntimeException): ApiError {
        return ApiError(
            code = "E${HttpStatus.BAD_REQUEST.value()}",
            message = "This user was no accepted!",
            description = "This cpf wasn't validate to vote! Please, verify and try again.",
            timesTemp = LocalDateTime.now()
        )
    }

    @ExceptionHandler(
        value = [
            ExpiredPautaException::class
        ]
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun expiredPautaException(ex: RuntimeException): ApiError {
        return ApiError(
            code = "E${HttpStatus.BAD_REQUEST.value()}",
            message = "This pauta is not avaliable!",
            description = "This pauta is expired!",
            timesTemp = LocalDateTime.now()
        )
    }
}
