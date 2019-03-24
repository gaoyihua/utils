package com.gary.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * describe:
 *
 * @author gary
 * @date 2019/01/18
 */
public class GsonUtil {
    private Map<String, String> paraMap;
    private Gson gson;

    public GsonUtil() {
        paraMap = new HashMap<>();
        gson = new GsonBuilder().create();
    }

    public GsonUtil addArg(String name, Object value) {
        paraMap.put(name, gson.toJson(value));
        return this;
    }

    public String toJson() {
        return gson.toJson(paraMap);
    }
}
