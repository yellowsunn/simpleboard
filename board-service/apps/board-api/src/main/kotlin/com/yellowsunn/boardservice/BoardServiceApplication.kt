package com.yellowsunn.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class BoardServiceApplication

fun main(args: Array<String>) {
    runApplication<BoardServiceApplication>(*args)
}
