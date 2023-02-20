package com.pauta.administracao.configuration

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.serializer.Serializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import reactor.core.publisher.Mono

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

    private val logger = LoggerFactory.getLogger(this::class.java)

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
    fun adminClient(): AdminClient {
        val props = HashMap<String, Any?>()
        props["bootstrap.servers"] = bootstrapServers
        return AdminClient.create(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun createTopicIfNotExists(adminClient: AdminClient,
                               @Value("\${kafka.topic.name}") topicName: String,
                               @Value("\${kafka.topic.partition}") numPartitions: Int,
                               @Value("\${kafka.topic.replicationFactor}") replicationFactor: Short,
    ): NewTopic {
        val existingTopics = adminClient.listTopics().names().get()
        return if (!existingTopics.contains(topicName)) {
            val newTopic = NewTopic(topicName, numPartitions, replicationFactor)
            adminClient.createTopics(listOf(newTopic)).all().get()
            logger.info("Topic created with success!")
            newTopic
        } else {
            logger.info("Topic exist with this name!")
            NewTopic(topicName, numPartitions, replicationFactor)
        }
    }
}
