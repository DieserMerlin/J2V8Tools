package de.merlinw.v8tools.mappers;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class V8ObjectMapper {

    public static <T> T map(V8Object ob, Class<T> clazz) {
        return new Gson().fromJson(V8ObjectGsonMapper.mapToGson(ob), clazz);
    }

    public static <T> List<T> map(V8Array arr, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        V8ObjectGsonMapper.mapToGson(arr).forEach(e -> list.add(new Gson().fromJson(e, clazz)));
        return list;
    }

    public static V8Object map(Object o, V8 engine) {
        return V8ObjectGsonMapper.mapToV8(new Gson().toJsonTree(o).getAsJsonObject(), engine);
    }

    public static V8Array map(List<?> objects, V8 engine) {
        JsonArray array = new JsonArray();
        objects.forEach(o -> array.add(new Gson().toJsonTree(o)));
        return V8ObjectGsonMapper.mapToV8(array, engine);
    }

}
