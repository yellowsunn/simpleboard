package com.yellowsunn.notificationservice.mongodb

import com.yellowsunn.notificationservice.config.MongoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataMongoTest
@TestPropertySource(properties = ["de.flapdoodle.mongodb.embedded.version=6.0.5"])
@ContextConfiguration(classes = [MongoConfiguration::class])
abstract class MongoIntegrationTest
