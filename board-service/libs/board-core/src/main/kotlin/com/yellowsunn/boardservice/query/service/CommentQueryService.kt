package com.yellowsunn.boardservice.query.service

import com.yellowsunn.boardservice.common.http.client.user.UserHttpClient
import com.yellowsunn.boardservice.query.domain.comment.CommentDocument
import com.yellowsunn.boardservice.query.dto.CommentDocumentPageDto
import com.yellowsunn.boardservice.query.repository.CommentDocumentRepository
import com.yellowsunn.common.utils.PageUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class CommentQueryService(
    private val commentDocumentRepository: CommentDocumentRepository,
    private val userHttpClient: UserHttpClient,
) {
    private companion object {
        private const val MAX_ELEM_SIZE = 100
    }

    fun findComments(articleId: Long, page: Int, size: Int): CommentDocumentPageDto {
        val curPage = PageUtils.currentPage(page - 1)
        val curSize = PageUtils.currentSize(size, MAX_ELEM_SIZE)

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
        return commetDocumentPage.content.map { it.userId }.distinct()
    }
}
