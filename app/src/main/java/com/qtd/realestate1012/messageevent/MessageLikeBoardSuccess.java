package com.qtd.realestate1012.messageevent;

import org.json.JSONObject;

/**
 * Created by DELL on 8/31/2016.
 */
public class MessageLikeBoardSuccess {
    public final JSONObject message;

    public MessageLikeBoardSuccess(JSONObject message) {
        this.message = message;
    }
}
