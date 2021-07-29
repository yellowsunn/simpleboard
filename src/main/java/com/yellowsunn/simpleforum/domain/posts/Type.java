package com.yellowsunn.simpleforum.domain.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {

    NOTICE("공지사항"), GENERAL("일반게시글");

    private String value;
}
