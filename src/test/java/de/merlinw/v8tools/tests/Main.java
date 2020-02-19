package de.merlinw.v8tools.tests;

import com.eclipsesource.v8.V8;
import de.merlinw.v8tools.mappers.V8ObjectGsonMapper;
import de.merlinw.v8tools.context.V8SharedContext;

public class Main {

    public static void main(String[] args) {
        V8 engine = V8.createV8Runtime();

        V8SharedContext context = new V8SharedContext();
        context.bind(engine);
        engine.executeVoidScript("\"use strict\";");

        engine.executeVoidScript("env.set('Hey', {this: 'is', a: ['test!']});");
        System.out.println(engine.executeStringScript("env.get('Hey').this;"));

        engine.registerJavaMethod(((v8Object, v8Array) -> {
            System.out.println(V8ObjectGsonMapper.mapToGson(v8Array).toString());
            return V8ObjectGsonMapper.mapToV8(V8ObjectGsonMapper.mapToGson(v8Array), v8Object.getRuntime());
        }), "test");

        System.out.println(engine.executeStringScript("test('Test', 123, {this: 'is', 1: 'test', with: {a: 'nested', object: [], n: null}}, ['hi', {h: 'i'}])[0];"));

        V8 engine2 = V8.createV8Runtime();
        context.bind(engine2);

        System.out.println(V8ObjectGsonMapper.mapToGson(engine2.executeArrayScript("env.get('Hey').a;")).toString());

        engine2.executeVoidScript("env.set('test', 123.456);");

        System.out.println(engine2.executeDoubleScript("env.get('test');"));
    }

}
