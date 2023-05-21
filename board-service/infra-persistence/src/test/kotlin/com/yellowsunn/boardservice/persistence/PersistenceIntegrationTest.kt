package com.yellowsunn.boardservice.persistence

import com.yellowsunn.boardservice.persistence.config.JpaConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Repository
import org.springframework.test.context.ContextConfiguration

@DataJpaTest(includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [Repository::class])])
@ContextConfiguration(classes = [JpaConfiguration::class])
abstract class PersistenceIntegrationTest
