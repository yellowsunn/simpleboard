package com.yellowsunn.notificationservice.mongodb

import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
class NotificationDocumentMongoRepository(
    private val delegate: NotificationDocumentMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : NotificationDocumentRepository {
    override fun save(notificationDocument: NotificationDocument): NotificationDocument {
        return delegate.save(notificationDocument)
    }

    override fun findByUserId(userId: Long): NotificationDocument? {
        return delegate.findByUserId(userId)
    }
}

interface NotificationDocumentMongoRepositoryDelegate : MongoRepository<NotificationDocument, String> {
    fun findByUserId(userId: Long): NotificationDocument?
}
