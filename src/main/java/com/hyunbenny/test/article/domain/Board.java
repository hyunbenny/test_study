package com.hyunbenny.test.article.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Board {

    private Long id;
    private String name;

}
