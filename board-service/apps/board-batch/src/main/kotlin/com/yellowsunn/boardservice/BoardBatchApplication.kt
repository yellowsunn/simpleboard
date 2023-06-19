package com.yellowsunn.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [MongoAutoConfiguration::class],
)
class BoardBatchApplication

fun main(args: Array<String>) {
    runApplication<BoardBatchApplication>(*args)
}
