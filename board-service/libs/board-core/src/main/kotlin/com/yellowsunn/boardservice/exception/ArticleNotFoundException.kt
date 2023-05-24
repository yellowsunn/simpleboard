package com.yellowsunn.boardservice.exception

class ArticleNotFoundException : IllegalArgumentException(
    "게시글을 찾을 수 없습니다.",
)
