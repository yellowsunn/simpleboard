package com.yellowsunn.notificationservice.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.ZonedDateTime

abstract class BaseDocumentEntity {
    @Id
    @Field("_id", targetType = FieldType.OBJECT_ID)
    lateinit var id: String
        private set

    @CreatedDate
    var createdAt: ZonedDateTime = ZonedDateTime.now()
        private set

    @LastModifiedDate
    var modifiedAt: ZonedDateTime = ZonedDateTime.now()
        private set
}
