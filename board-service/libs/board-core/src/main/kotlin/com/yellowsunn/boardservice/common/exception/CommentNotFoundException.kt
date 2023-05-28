package com.yellowsunn.boardservice.common.exception

class CommentNotFoundException : IllegalArgumentException(
    "댓글을 찾을 수 없습니다.",
)
