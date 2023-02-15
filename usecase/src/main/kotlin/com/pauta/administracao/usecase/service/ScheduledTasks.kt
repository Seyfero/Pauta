package com.pauta.administracao.usecase.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.util.concurrent.Executors

@Component
class ScheduledTasks(
    val votingSessionService: VotingSessionService
    ) {
    private val scheduledExecutorService = Executors.newScheduledThreadPool(1)

    @Scheduled(fixedDelay = 1000)
    fun scheduleTasks() {
        val currentTime = LocalTime.now()
        val votingSessions = votingSessionService.getVotingSessions()

        for (votingSession in votingSessions) {
            val endTime = votingSession.endTime.toLocalTime()
            if (currentTime >= endTime) {
                // Lógica para encerrar a sessão de votação
            }
        }
    }
}