package com.qtd.realestate1012.messageevent;

import org.json.JSONArray;

/**
 * Created by DELL on 8/31/2016.
 */
public class MessageDataLocationNearBy {
    public final String result;
    public final JSONArray data;
    public final String placeType;

    public MessageDataLocationNearBy(String result, JSONArray data, String placeType) {
        this.result = result;
        this.data = data;
        this.placeType = placeType;
    }

    public MessageDataLocationNearBy() {
        result = null;
        data = null;
        placeType = null;
    }
}
