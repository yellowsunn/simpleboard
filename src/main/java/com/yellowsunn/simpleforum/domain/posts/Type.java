package com.yellowsunn.simpleforum.domain.posts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {

    Notice("공지사항"), General("일반게시글");

    private String value;
}
