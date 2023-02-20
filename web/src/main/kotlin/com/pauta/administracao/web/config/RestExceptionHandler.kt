package com.pauta.administracao.web.config

import com.pauta.administracao.domain.exception.ExpiredPautaException
import com.pauta.administracao.domain.exception.IllegalPautaException
import com.pauta.administracao.domain.exception.IllegalUsuarioException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.webjars.NotFoundException
import java.time.LocalDateTime

@ControllerAdvice
class RestExceptionHandler {

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
            message = "This request returned no results",
            description = "The resources not found!",
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
            description = "Please review the informed parameters and try again!",
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
            description = "Please review the pauta's information and try again!",
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
            description = "This cpf voted before on this pauta!",
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
