package com.yellowsunn.boardservice.query.service

import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import com.yellowsunn.boardservice.query.dto.CommentDocumentPageDto
import com.yellowsunn.boardservice.query.repository.CommentDocumentRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

@Service
class CommentQueryService(
    private val commentDocumentRepository: CommentDocumentRepository,
    private val userHttpClient: UserHttpClient,
) {
    private companion object {
        private const val DEFAULT_ELEM_SIZE = 10
        private const val MAX_ELEM_SIZE = 100
    }

    fun findComments(articleId: Long, page: Int, size: Int): CommentDocumentPageDto {
        val curPage = max(page, 1) - 1
        val curSize = if (size <= 0) {
            DEFAULT_ELEM_SIZE
        } else {
            min(size, MAX_ELEM_SIZE)
        }

        val commentDocumentPage: Page<CommentDocument> =
            commentDocumentRepository.findComments(articleId, curPage, curSize)
        val userIds = filterUserIds(commentDocumentPage)
        val users = userHttpClient.findUsersByIds(userIds)

        return CommentDocumentPageDto.from(commentDocumentPage, users)
    }

    fun findCommentPage(commentId: Long, size: Int): Long? {
        val commentDocument: CommentDocument = commentDocumentRepository.findByCommentId(commentId) ?: return null

        val offset = commentDocumentRepository.findOffsetByCommentId(
            articleId = commentDocument.articleId,
            baseCommentId = commentDocument.baseCommentId,
            commentId = commentDocument.commentId,
        )

        return offset / size + 1L
    }

    private fun filterUserIds(commetDocumentPage: Page<CommentDocument>): List<Long> {
        return commetDocumentPage.content.map { it.userId }
    }
}
