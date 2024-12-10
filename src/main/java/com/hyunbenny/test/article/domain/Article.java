package com.hyunbenny.test.article.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Article {
    private Long id;
    private Board board;
    private String subject;
    private String content;
    private String username;
    private LocalDateTime createdDate;

    public void update(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
