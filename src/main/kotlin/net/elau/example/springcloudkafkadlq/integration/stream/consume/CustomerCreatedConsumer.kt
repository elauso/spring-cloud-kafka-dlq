package net.elau.example.springcloudkafkadlq.integration.stream.consume

import net.elau.example.springcloudkafkadlq.integration.stream.event.CustomerCreatedEvent
import net.elau.example.springcloudkafkadlq.util.isOdd
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class CustomerCreatedConsumer {
    companion object {
        private val log = LoggerFactory.getLogger(CustomerCreatedConsumer::class.java)
    }

    @Bean
    fun customerCreatedConsume(): Consumer<CustomerCreatedEvent> =
        Consumer<CustomerCreatedEvent> { event ->
            log.debug("Event received: {}", event)

            if (isOdd(event.id))
                throw RuntimeException("Failed to process customer-created-consumer")

            log.debug("Event process with success")
        }

    @KafkaListener(id = "customer-created-dlq", topics = ["queueing.example.customer.created.dlq"])
    fun error(message: Message<Any>) {
        log.debug("customer-created-dlq consumed: {}", message)
    }
}