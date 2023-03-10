package com.pauta.administracao.usecase.service.helper

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ValidCpfHelper {
    companion object {
        fun checkCpf(et: String): Mono<Boolean> {
            val regex = Regex("[a-zA-Z]")
            if (regex.containsMatchIn(et)) {
                return Mono.error(IllegalArgumentException("Cpf contains letters!"))
            }

            val str = et.replace("-", "").replace("/", "").replace(".", "").padStart(11, '0')
            var calc: Int
            var num = 10
            var sum = 0

            for (x in 0..8) {
                calc = str[x].toString().toInt() * num
                sum += calc
                --num
            }

            var rest = sum % 11
            var test = 11 - rest

            if (test > 9) test = 0

            if (test != str[9].toString().toInt()) {
                return Mono.just(false)
            }

            num = 11
            sum = 0

            for (x in 0..9) {
                calc = str[x].toString().toInt() * num
                sum += calc
                --num
            }

            rest = sum % 11
            test = 11 - rest

            if (test > 9) test = 0

            if (test != str[10].toString().toInt()) {
                return Mono.just(false)
            }

            return Mono.just(true)
                .onErrorResume {
                    Mono.error(IllegalArgumentException("Cpf is not valid!"))
                }
        }
    }
}
