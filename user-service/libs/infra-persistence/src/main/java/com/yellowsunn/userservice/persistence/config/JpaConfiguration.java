package com.yellowsunn.userservice.persistence.config;

import com.yellowsunn.userservice.persistence.PersistenceModule;
import com.yellowsunn.userservice.domain.DomainModule;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = DomainModule.class)
@EnableJpaRepositories(basePackageClasses = PersistenceModule.class)
public class JpaConfiguration {
}
