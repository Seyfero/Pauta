package com.pauta.administracacao.impl

import com.pauta.administracao.database.converters.voto.toEntity
import com.pauta.administracao.database.impl.VotoServiceImpl
import com.pauta.administracao.database.repository.VotoRepository
import com.pauta.administracao.domain.VotoDomain
import com.pauta.administracao.outputboundary.converters.voto.toOutputDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class VotoServiceImplTest {

    @Mock
    lateinit var votoRepository: VotoRepository

    @InjectMocks
    lateinit var votoServiceImpl: VotoServiceImpl

    @Test
    fun `return ok when save vote with success`() {
        val vote = populateVote()
        val saveVote = vote.copy(id = 1)

        `when`(votoRepository.save(vote.toEntity())).thenReturn(Mono.just(saveVote.toEntity().copy(id = 1)))

        votoServiceImpl.create(vote.toOutputDto())
            .`as`(StepVerifier::create)
            .assertNext {
                assertEquals(it, true)
            }
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when save vote with error`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.save(vote.toEntity())).thenReturn(Mono.error(Exception("Error")))

        votoServiceImpl.create(vote.toOutputDto())
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to create vote!"
            }
            .verify()
    }

    @Test
    fun `return ok when delete vote with success`() {
        val voteDelete = populateVote().copy(id = 1)

        `when`(votoRepository.deleteById(voteDelete.id!!)).thenReturn(Mono.empty())

        votoServiceImpl.delete(voteDelete.id!!)
            .`as`(StepVerifier::create)
            .expectNext()
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when delete vote with error`() {
        val voteDelete = populateVote().copy(id = 1)

        `when`(votoRepository.deleteById(voteDelete.id!!)).thenReturn(Mono.error(Exception("Error")))

        votoServiceImpl.delete(voteDelete.id!!)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to delete vote!"
            }
            .verify()
    }

    @Test
    fun `return ok when find by Id vote with success`() {
        val vote = populateVote().copy(id = 1)
        val vote2 = populateVote().copy(id = 2)

        `when`(votoRepository.findByVotoPauta(vote.votoPautaDomainId!!))
            .thenReturn(Flux.just(vote.toEntity().copy(id = 1), vote.toEntity().copy(id = 2)))

        votoServiceImpl.findByVotoPauta(vote.votoPautaDomainId!!)
            .`as`(StepVerifier::create)
            .expectNext(vote)
            .expectNext(vote2)
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when find by Id vote with error`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.findByVotoPauta(vote.id!!)).thenReturn(Flux.error(Exception("Error")))

        votoServiceImpl.findByVotoPauta(vote.id!!)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to find vote by id order!"
            }
            .verify()
    }

    @Test
    fun `return ok when find all vote with success on database`() {
        val vote = populateVote().copy(id = 1)
        val vote2 = populateVote().copy(id = 2)

        `when`(votoRepository.findAll()).thenReturn(Flux.just(vote.toEntity().copy(id = 1), vote2.toEntity().copy(id = 2)))

        votoServiceImpl.findAll()
            .`as`(StepVerifier::create)
            .expectNext(vote)
            .expectNext(vote2)
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when find all vote with error`() {

        `when`(votoRepository.findAll()).thenReturn(Flux.error(Exception("Error")))

        votoServiceImpl.findAll()
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to find all votes!"
            }
            .verify()
    }

    @Test
    fun `return ok when find by cpf vote with success`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.findByVotoPautaAndVotoUsuarioCpf(vote.votoPautaDomainId!!, vote.votoUsuarioDomainCpf))
            .thenReturn(Mono.just(vote.toEntity().copy(id = 1)))

        votoServiceImpl.findByVotoPautaAndVotoUsuarioCpf(vote.votoPautaDomainId!!, vote.votoUsuarioDomainCpf)
            .`as`(StepVerifier::create)
            .expectNext(vote)
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when find by cpf vote with error`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.findByVotoPautaAndVotoUsuarioCpf(vote.id!!, vote.votoUsuarioDomainCpf)).thenReturn(Mono.error(Exception("Error")))

        votoServiceImpl.findByVotoPautaAndVotoUsuarioCpf(vote.id!!, vote.votoUsuarioDomainCpf)
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to find vote by id order and cpf user!"
            }
            .verify()
    }

    @Test
    fun `return count of votes`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.getCountVotosByPautaId(vote.votoPautaDomainId!!, "sim"))
            .thenReturn(Mono.just(1))

        votoServiceImpl.getCountVotosByPautaId(vote.votoPautaDomainId!!, "sim")
            .`as`(StepVerifier::create)
            .expectNext(1)
            .verifyComplete()
    }

    @Test
    fun `return unsupported operation when count votes`() {
        val vote = populateVote().copy(id = 1)

        `when`(votoRepository.getCountVotosByPautaId(vote.id!!, "sim")).thenReturn(Mono.error(Exception("Error")))

        votoServiceImpl.getCountVotosByPautaId(vote.id!!, "sim")
            .`as`(StepVerifier::create)
            .expectErrorMatches {
                it is UnsupportedOperationException && it.message == "Error to execute find by id order and value vote!"
            }
            .verify()
    }

    private fun populateVote(): VotoDomain {
        return VotoDomain(null, "sim", "1", 1)
    }
}
