package com.yellowsunn.boardservice.command.message.producer

import com.yellowsunn.boardservice.command.domain.event.EventSendFailure
import java.util.concurrent.CompletableFuture

interface GlobalMessageProducer {
    fun send(topic: String, data: Any): CompletableFuture<EventSendFailure?>
}
