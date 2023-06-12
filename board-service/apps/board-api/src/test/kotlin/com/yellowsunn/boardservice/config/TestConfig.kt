package com.yellowsunn.boardservice.config

import com.yellowsunn.boardservice.command.facade.ArticleFacade
import com.yellowsunn.boardservice.command.facade.CommentFacade
import com.yellowsunn.boardservice.query.service.ArticleQueryService
import com.yellowsunn.boardservice.query.service.CommentQueryService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun articleFacade() = mockk<ArticleFacade>()

    @Bean
    fun articleQueryService() = mockk<ArticleQueryService>()

    @Bean
    fun commentFacade() = mockk<CommentFacade>()

    @Bean
    fun commentQueryService() = mockk<CommentQueryService>()
}
