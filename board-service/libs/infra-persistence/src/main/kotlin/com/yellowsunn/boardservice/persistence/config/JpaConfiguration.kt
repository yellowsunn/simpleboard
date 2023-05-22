package com.yellowsunn.boardservice.persistence.config

import com.yellowsunn.boardservice.domain.DomainModule
import com.yellowsunn.boardservice.persistence.PersistenceModule
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackageClasses = [DomainModule::class])
@EnableJpaRepositories(basePackageClasses = [PersistenceModule::class])
class JpaConfiguration
