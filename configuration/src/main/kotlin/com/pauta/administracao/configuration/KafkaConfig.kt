package com.pauta.administracao.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.serializer.Serializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig {
    @Value("\${kafka.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${kafka.producer.key-serializer}")
    private val keySerializer: Class<out Serializer<*>>? = null

    @Value("\${kafka.producer.value-serializer}")
    private val valueSerializer: Class<out Serializer<*>>? = null

    @Value("\${kafka.producer.acks}")
    private val acks: String? = null

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configs: MutableMap<String, Any> = HashMap()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers!!
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = keySerializer!!.name
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = valueSerializer!!.name
        configs[ProducerConfig.ACKS_CONFIG] = acks!!

        return DefaultKafkaProducerFactory(configs)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }
}
