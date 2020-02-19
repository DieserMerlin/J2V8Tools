package de.merlinw.v8tools;

import com.eclipsesource.v8.V8;
import com.sun.istack.internal.NotNull;
import de.merlinw.v8tools.context.V8SharedContext;
import de.merlinw.v8tools.mappers.V8ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class V8T extends V8 {

    private V8SharedContext context;

    public static V8T getInstance() {
        return new V8T(new V8SharedContext());
    }

    public static V8T getInstance(String globalAlias) {
        return new V8T(globalAlias, new V8SharedContext());
    }

    public V8T(V8SharedContext context) {
        super();
        this.setContext(context).init();
    }

    public V8T(String globalAlias, V8SharedContext context) {
        super(globalAlias);
        this.setContext(context).init();
    }

    private void init() {
        executeVoidScript("\"use strict\";\n");
    }

    public V8T setContext(@NotNull V8SharedContext context) {
        this.context = context;
        context.bind(this);
        return this;
    }

    public V8SharedContext getContext() {
        return context;
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return V8ObjectMapper.map(getObject(key), clazz);
    }

    public void add(String key, Object o) {
        if (o instanceof List) add(key, V8ObjectMapper.map((List<?>) o, this));
        else add(key, V8ObjectMapper.map(o, this));
    }

    public final <R> void registerJavaMethod(V8TJavaMethodInterface<R> callback, String name, Class<?>... params) {
        registerJavaMethod(((v8Object, v8Array) -> {
            List<V8TObjectHolder> holders = new ArrayList<>();
            int i = 0;
            for (Class<?> param : params) {
                if (v8Array.length() >= i)
                    holders.add(new V8TObjectHolder(param, params[i++]));
                else holders.add(null);
            }
            return callback.execute(Arrays.asList(params), holders.toArray(new V8TObjectHolder[]{}));
        }), name);
    }

    public final void registerJavaMethod(V8TJavaVoidMethodInterface callback, String name, Class<?>... params) {
        registerJavaMethod((V8TJavaMethodInterface) callback, name, params);
    }
}
