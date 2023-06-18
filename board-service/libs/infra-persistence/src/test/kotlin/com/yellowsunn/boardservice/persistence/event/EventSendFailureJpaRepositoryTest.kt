package com.yellowsunn.boardservice.persistence.event

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import com.yellowsunn.boardservice.persistence.PersistenceIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

class EventSendFailureJpaRepositoryTest : PersistenceIntegrationTest() {
    @Autowired
    lateinit var em: TestEntityManager

    lateinit var eventSendFailureJpaRepository: EventSendFailureJpaRepository

    @BeforeEach
    fun setUp() {
        eventSendFailureJpaRepository = EventSendFailureJpaRepository(em.entityManager)
    }

    @Test
    fun save() {
        val eventSendFailure = EventSendFailure(
            topic = "kafka-topic",
            data = """
            {
              "test": "hello"
            }
            """.trimIndent(),
        )

        val savedEventSendFailure = eventSendFailureJpaRepository.save(eventSendFailure)

        assertThat(savedEventSendFailure.id).isNotNull
    }
}
