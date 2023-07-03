package net.elau.example.springcloudkafkadlq.integration.stream.event

data class CustomerUpdatedEvent(
    val id: Long,
    val name: String,
    val document: String,
    val email: String
)
