package com.hyunbenny.test.testFixtures;

import com.hyunbenny.test.article.domain.Board;

public class BoardFixtures {
    public static Board board() {
        return new Board(5L, "board");
    }
}
