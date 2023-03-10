package com.pauta.administracao.usecase.service.helper

import com.pauta.administracao.usecase.service.helper.ValidCpfHelper.Companion.checkCpf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class ValidCpfHelperTest {

    @Test
    fun `should return ok if cpf is valid`() {
        val res = checkCpf("55517183360")

        res.`as`(StepVerifier::create)
            .assertNext {
                Assertions.assertEquals(it, true)
            }
            .verifyComplete()
    }

    @Test
    fun `should return false if cpf is invalid`() {
        val res = checkCpf("55517183362")

        res.`as`(StepVerifier::create)
            .assertNext {
                Assertions.assertEquals(it, false)
            }
            .verifyComplete()
    }

    @Test
    fun `should return error on validate`() {
        val res = checkCpf("palavra")

        res.`as`(StepVerifier::create)
            .expectErrorMatches {
                it is IllegalArgumentException && it.message == "Cpf contains letters!"
            }
            .verify()
    }
}
