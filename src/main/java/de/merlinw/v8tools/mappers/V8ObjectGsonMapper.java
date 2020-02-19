package de.merlinw.v8tools.mappers;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.google.gson.*;

import java.util.Map.Entry;

public class V8ObjectGsonMapper {

    public static JsonObject mapToGson(V8Object object) {
        JsonObject ob = new JsonObject();

        for (String key : object.getKeys()) {
            Object current = object.get(key);

            // Check null
            if (current == null)
                ob.add(key, JsonNull.INSTANCE);

            // Primitives
            if (current instanceof String)
                ob.add(key, new JsonPrimitive((String) current));
            else if (current instanceof Number)
                ob.add(key, new JsonPrimitive((Number) current));
            else if (current instanceof Character)
                ob.add(key, new JsonPrimitive((Character) current));
            else if (current instanceof Boolean)
                ob.add(key, new JsonPrimitive((Boolean) current));

                // Arrays (before Objects since Arrays are Objects too)
            else if (current instanceof V8Array)
                ob.add(key, mapToGson((V8Array) current));

                // Last but not least objects
            else if (current instanceof V8Object)
                ob.add(key, mapToGson((V8Object) current));
        }

        return ob;
    }

    public static JsonArray mapToGson(V8Array array) {
        JsonArray arr = new JsonArray();

        for (int i = 0; i < array.length(); i++) {
            Object current = array.get(i);

            // Check null
            if (current == null)
                arr.add(JsonNull.INSTANCE);

                // Primitives
            else if (current instanceof String)
                arr.add(new JsonPrimitive((String) current));
            else if (current instanceof Number)
                arr.add(new JsonPrimitive((Number) current));
            else if (current instanceof Character)
                arr.add(new JsonPrimitive((Character) current));
            else if (current instanceof Boolean)
                arr.add(new JsonPrimitive((Boolean) current));

                // Arrays (before Objects since Arrays are Objects too)
            else if (current instanceof V8Array)
                arr.add(mapToGson((V8Array) current));

                // Last but not least objects
            else if (current instanceof V8Object)
                arr.add(mapToGson((V8Object) current));
        }

        return arr;
    }

    public static V8Object mapToV8(JsonObject object, V8 engine) {
        V8Object ob = new V8Object(engine);

        for (Entry<String, JsonElement> e : object.entrySet()) {
            JsonElement current = e.getValue();
            String key = e.getKey();

            // Check null
            if (current.isJsonNull())
                ob.addNull(key);

                // Objects
            else if (current.isJsonObject())
                ob.add(key, mapToV8(current.getAsJsonObject(), engine));

                // Arrays
            else if (current.isJsonArray())
                ob.add(key, mapToV8(current.getAsJsonArray(), engine));

                // Check for primitives
            else if (current.isJsonPrimitive()) {
                JsonPrimitive primitive = (JsonPrimitive) current;

                // Check for string
                if (primitive.isString())
                    ob.add(key, primitive.getAsString());

                // Check for boolean
                if (primitive.isBoolean())
                    ob.add(key, primitive.getAsBoolean());

                // Check for number
                if (primitive.isNumber()) {

                    // Check if it's an integer
                    if (primitive.getAsNumber() instanceof Integer)
                        ob.add(key, primitive.getAsInt());
                        // Must be a floating value
                    else primitive.getAsDouble();
                }
            }
        }

        return ob;
    }

    public static V8Array mapToV8(JsonArray array, V8 engine) {
        V8Array arr = new V8Array(engine);

        for (int i = 0; i < array.size(); i++) {
            JsonElement current = array.get(i);

            // Check null
            if (current.isJsonNull())
                arr.pushNull();

                // Objects
            else if (current.isJsonObject())
                arr.push(mapToV8(current.getAsJsonObject(), engine));

                // Arrays
            else if (current.isJsonArray())
                arr.push(mapToV8(current.getAsJsonArray(), engine));

                // Check for primitives
            else if (current.isJsonPrimitive()) {
                JsonPrimitive primitive = (JsonPrimitive) current;

                // Check for string
                if (primitive.isString())
                    arr.push(primitive.getAsString());

                // Check for boolean
                if (primitive.isBoolean())
                    arr.push(primitive.getAsBoolean());

                // Check for number
                if (primitive.isNumber()) {

                    // Check if it's an integer
                    if (primitive.getAsNumber() instanceof Integer)
                        arr.push(primitive.getAsInt());
                        // Must be a floating value
                    else primitive.getAsDouble();
                }
            }
        }

        return arr;
    }
}

