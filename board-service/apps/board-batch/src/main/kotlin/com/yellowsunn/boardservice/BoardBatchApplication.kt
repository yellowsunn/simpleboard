package com.yellowsunn.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoardBatchApplication

fun main(args: Array<String>) {
    runApplication<BoardBatchApplication>(*args)
}
