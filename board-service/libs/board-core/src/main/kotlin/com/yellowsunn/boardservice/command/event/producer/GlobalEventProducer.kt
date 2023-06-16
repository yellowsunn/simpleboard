package com.yellowsunn.boardservice.command.event.producer

interface GlobalEventProducer {
    fun send(topic: String, data: Any)
}
