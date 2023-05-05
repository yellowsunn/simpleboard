package com.yellowsunn.simpleboard.boardservice.domain.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {

    NOTICE("공지사항"), GENERAL("일반게시글");

    private String value;
}
