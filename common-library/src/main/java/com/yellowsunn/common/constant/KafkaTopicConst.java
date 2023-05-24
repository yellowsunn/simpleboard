package com.yellowsunn.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTopicConst {
    public static final String ARTICLE_TOPIC = "queuing.board.article";
    public static final String ARTICLE_LIKE_TOPIC = "queuing.board.article-like";
    public static final String ARTICLE_UNDO_LIKE_TOPIC = "queuing.board.article-undo-like";
}
