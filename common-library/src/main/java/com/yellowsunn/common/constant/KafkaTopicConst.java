package com.yellowsunn.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTopicConst {
    public static final String ARTICLE_CREATE_TOPIC = "queuing.board.article-create";
    public static final String ARTICLE_UPDATE_TOPIC = "queuing.board.article-update";
    public static final String ARTICLE_LIKE_TOPIC = "queuing.board.article-like";
    public static final String ARTICLE_UNDO_LIKE_TOPIC = "queuing.board.article-undo-like";
}
