package com.yellowsunn.notificationservice.mongodb

import com.yellowsunn.notificationservice.domain.NotificationDocument
import com.yellowsunn.notificationservice.repository.NotificationDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class NotificationDocumentMongoRepository(
    private val delegate: NotificationDocumentMongoRepositoryDelegate,
    private val mongoTemplate: MongoTemplate,
) : NotificationDocumentRepository {
    private companion object {
        private const val USER_ID = "userId"
        private const val READ_AT = "readAt"
    }

    override fun save(notificationDocument: NotificationDocument): NotificationDocument {
        return delegate.save(notificationDocument)
    }

    override fun findByUserId(userId: Long): NotificationDocument? {
        return delegate.findByUserId(userId)
    }

    override fun findUserNotifications(userId: Long, page: Int, size: Int): Page<NotificationDocument> {
        val pageable = PageRequest.of(page, size)
        return delegate.findByUserIdOrderByCreatedAtDesc(userId, pageable)
    }

    override fun existUnreadNotifications(userId: Long): Boolean {
        return delegate.existsByUserIdAndReadAtIsNull(userId)
    }

    override fun updateReadAtByUserId(userId: Long, readAt: ZonedDateTime): Long {
        val query = Query(Criteria.where(USER_ID).`is`(userId))
            .addCriteria(Criteria.where(READ_AT).isNull)

        val update = Update.update(READ_AT, readAt)

        val updateResult = mongoTemplate.updateMulti(query, update, NotificationDocument::class.java)
        return updateResult.modifiedCount
    }
}

interface NotificationDocumentMongoRepositoryDelegate : MongoRepository<NotificationDocument, String> {
    fun findByUserId(userId: Long): NotificationDocument?
    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<NotificationDocument>
    fun existsByUserIdAndReadAtIsNull(userId: Long): Boolean
}
