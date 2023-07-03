package net.elau.example.springcloudkafkadlq.integration.stream.consume

import net.elau.example.springcloudkafkadlq.integration.stream.event.CustomerUpdatedEvent
import net.elau.example.springcloudkafkadlq.util.isOdd
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders.ACKNOWLEDGMENT
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.time.Duration.ofMillis
import java.util.function.Consumer

@Component
class CustomerUpdatedConsumer {
    companion object {
        private val log = LoggerFactory.getLogger(CustomerUpdatedConsumer::class.java)
    }

    @Bean
    fun customerUpdatedConsume(): Consumer<Message<CustomerUpdatedEvent>> =
        Consumer<Message<CustomerUpdatedEvent>> { event ->
            val payload = event.payload
            val acknowledgment = event.headers.get(ACKNOWLEDGMENT, Acknowledgment::class.java)
            runCatching {
                log.debug("Event received: {}", event)

                if (isOdd(payload.id))
                    throw RuntimeException("Failed to process customer-updated-consumer")

                acknowledgment!!.acknowledge()
                log.debug("Event process with success")
            }.onFailure { error ->
                log.error("Event process failed: ${error.message}")
                acknowledgment!!.nack(ofMillis(500)) // it will force consumer to infinite loop on error...
            }
        }
}