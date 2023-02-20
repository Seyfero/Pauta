package com.pauta.administracao

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
@ComponentScan("com.pauta.administracao")
class Boot {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Boot>(*args)
        }
    }
}
