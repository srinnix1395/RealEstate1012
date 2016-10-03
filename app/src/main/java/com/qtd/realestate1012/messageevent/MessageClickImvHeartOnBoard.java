package com.qtd.realestate1012.messageevent;

import com.qtd.realestate1012.model.BoardHasHeart;

/**
 * Created by DELL on 8/30/2016.
 */
public class MessageClickImvHeartOnBoard {

    public final BoardHasHeart board;
    public final String action;

    public MessageClickImvHeartOnBoard(BoardHasHeart board, String action) {
        this.board = board;
        this.action = action;
    }
}
