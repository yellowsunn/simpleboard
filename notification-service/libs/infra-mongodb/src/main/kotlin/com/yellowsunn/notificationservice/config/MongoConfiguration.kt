package com.yellowsunn.notificationservice.config

import java.time.ZoneId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.time.ZonedDateTime
import java.util.Date
import java.util.Optional

@Configuration
@EnableMongoRepositories(basePackageClasses = [com.yellowsunn.notificationservice.mongodb.MongoModule::class])
@EnableMongoAuditing(dateTimeProviderRef = "customDateTimeProvider")
class MongoConfiguration {
    @Bean
    fun customConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(DateConverter(), ZonedDateTimeDateConverter()))
    }

    @Bean
    fun customDateTimeProvider(): DateTimeProvider {
        return DateTimeProvider {
            Optional.of(ZonedDateTime.now())
        }
    }

    private class DateConverter : Converter<Date, ZonedDateTime> {
        override fun convert(source: Date): ZonedDateTime {
            return ZonedDateTime.ofInstant(source.toInstant(), ZoneId.of("Asia/Seoul"))
        }
    }

    private class ZonedDateTimeDateConverter : Converter<ZonedDateTime, Date> {
        override fun convert(source: ZonedDateTime): Date {
            return Date.from(source.toInstant())
        }
    }
}
