package com.yellowsunn.boardservice.command.repository

import com.yellowsunn.boardservice.command.domain.comment.Comment

interface CommentRepository {
    fun save(entity: Comment): Comment
    fun findById(id: Long, includeDeleted: Boolean = false): Comment?
    fun countByArticleId(articleId: Long): Long
}
