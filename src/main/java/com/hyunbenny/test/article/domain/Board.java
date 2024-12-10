package com.hyunbenny.test.article.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Board {

    private Long id;
    private String name;

    @Builder
    public Board(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
