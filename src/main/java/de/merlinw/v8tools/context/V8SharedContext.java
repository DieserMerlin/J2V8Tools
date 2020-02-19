package de.merlinw.v8tools.context;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import de.merlinw.v8tools.mappers.V8ObjectGsonMapper;

import java.util.HashMap;
import java.util.Map;

public class V8SharedContext {

    private Map<String, JsonElement> store = new HashMap<>();

    public void bind(V8 engine) {
        V8Object context = new V8Object(engine);

        context.registerJavaMethod((v8object, v8array) -> {
            JsonArray arr = V8ObjectGsonMapper.mapToGson(v8array);
            String key = arr.get(0).getAsString();
            JsonElement val = arr.get(1);
            store.put(key, val);
        }, "set");

        context.registerJavaMethod((v8object, v8array) -> {
            JsonArray arr = V8ObjectGsonMapper.mapToGson(v8array);
            String key = arr.get(0).getAsString();
            JsonElement val = store.get(key);

            if (val == null || val.isJsonNull()) return null;

            if (val.isJsonPrimitive()) {
                JsonPrimitive primitive = val.getAsJsonPrimitive();

                if (primitive.isBoolean())
                    return primitive.getAsBoolean();

                if (primitive.isString())
                    return primitive.getAsString();

                if (primitive.isNumber()) {
                    Number num = primitive.getAsNumber();
                    if (num instanceof Integer)
                        return primitive.getAsInt();
                    else return num;
                }
            }

            if (val.isJsonObject())
                return V8ObjectGsonMapper.mapToV8(val.getAsJsonObject(), v8object.getRuntime());

            if (val.isJsonArray())
                return V8ObjectGsonMapper.mapToV8(val.getAsJsonArray(), v8object.getRuntime());

            return null;
        }, "get");

        context.registerJavaMethod((v8object, v8array) -> {
            JsonArray arr = V8ObjectGsonMapper.mapToGson(v8array);
            store.remove(arr.get(0).getAsString());
        }, "del");

        engine.add("env", context);
    }

}
