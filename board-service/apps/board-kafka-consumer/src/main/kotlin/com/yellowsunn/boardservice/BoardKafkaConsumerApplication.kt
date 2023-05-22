package com.yellowsunn.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoardKafkaConsumerApplication

fun main(args: Array<String>) {
    runApplication<BoardKafkaConsumerApplication>(*args)
}
