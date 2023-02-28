package com.pauta.administracacao.impl

import com.pauta.administracao.cache.service.RedisService
import com.pauta.administracao.database.converters.pauta.toDomain
import com.pauta.administracao.database.converters.pauta.toEntity
import com.pauta.administracao.database.entity.PautaEntity
import com.pauta.administracao.database.impl.PautaServiceImpl
import com.pauta.administracao.database.repository.PautaRepository
import com.pauta.administracao.domain.PautaDomain
import com.pauta.administracao.outputboundary.converters.pauta.toOutputDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PautaServiceImplTest {

    @Mock
    lateinit var redisService: RedisService<PautaDomain>

    @Mock
    lateinit var orderRepository: PautaRepository

    @InjectMocks
    lateinit var pautaService: PautaServiceImpl

    @Test
    fun `return ok when save order with success`() {
        val order = PautaDomain(null, "johndoe@example.com", LocalDateTime.now(), 30)
        val savedOrder = populateOrder().copy(id = 1)

        `when`(orderRepository.save(order.toEntity())).thenReturn(Mono.just(savedOrder.toEntity().copy(id = 1)))

        `when`(redisService.put(savedOrder)).thenReturn(Mono.just(true))

        pautaService.create(order.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertNotNull(it.id)
                assertEquals(savedOrder.pautaNome, it.pautaNome)
            }
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when save order with error`() {
        val order = PautaDomain(null, "johndoe@example.com", LocalDateTime.now(), 30)

        `when`(orderRepository.save(order.toEntity())).thenReturn(Mono.error(Exception("Error")))

        pautaService.create(order.toOutputDto())
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to create order!"
            }
            .verify()
    }

    @Test
    fun `continue operation when redis return error`() {
        val order = populateOrder()
        val orderSaved = populateOrder().copy(id = 1)

        `when`(orderRepository.save(order.toEntity())).thenReturn(Mono.just(orderSaved.toEntity().copy(id = 1)))

        `when`(redisService.put(orderSaved)).thenReturn(Mono.error(Exception("Error")))

        pautaService.create(order.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertThrows<UnsupportedOperationException> {
                    throw UnsupportedOperationException("Error to create order!")
                }
            }
            .verifyComplete()

        pautaService.create(order.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertNotNull(it.id)
                assertEquals(orderSaved.pautaNome, it.pautaNome)
            }
            .verifyComplete()

    }

    @Test
    fun `return ok when update order with success`() {
        val order = populateOrder().copy(id = 1)
        val preSaveorder = order.copy(pautaNome = "namePre")
        val posSaveOrder = order.copy(pautaNome = "namePos")

        `when`(orderRepository.save(preSaveorder.toEntity().copy(id = 1))).thenReturn(Mono.just(posSaveOrder.toEntity().copy(id = 1)))

        `when`(redisService.put(posSaveOrder)).thenReturn(Mono.just(true))

        pautaService.update(preSaveorder.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertNotNull(it.id)
                assertEquals(posSaveOrder.pautaNome, it.pautaNome)
            }
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when update order with error`() {
        val order = populateOrder().copy(id = 1)

        `when`(orderRepository.save(order.toEntity().copy(id = 1))).thenReturn(Mono.error(Exception("Error")))

        pautaService.update(order.toOutputDto())
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to update order!"
            }
            .verify()
    }

    @Test
    fun `continue operation when redis return error on update`() {
        val order = populateOrder().copy(id = 1)
        val preSaveorder = order.copy(pautaNome = "namePre")
        val posSaveOrder = order.copy(pautaNome = "namePos")

        `when`(orderRepository.save(preSaveorder.toEntity().copy(id = 1))).thenReturn(Mono.just(posSaveOrder.toEntity().copy(id = 1)))

        `when`(redisService.put(posSaveOrder)).thenReturn(Mono.error(Exception("Error")))

        pautaService.update(preSaveorder.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertThrows<UnsupportedOperationException> {
                    throw UnsupportedOperationException("Error to update order!")
                }
            }
            .verifyComplete()

        pautaService.update(preSaveorder.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertNotNull(it.id)
                assertEquals(posSaveOrder.pautaNome, it.pautaNome)
            }
            .verifyComplete()

    }

    private fun populateOrder(): PautaDomain {
        return PautaDomain(null, "name", LocalDateTime.now(), 60, 0)
    }
}
