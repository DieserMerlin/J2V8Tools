package de.merlinw.v8tools;

import de.merlinw.v8tools.context.V8SharedContext;

public class V8TFactory {

    private V8SharedContext context = new V8SharedContext();

    public V8T newInstance() {
        V8T v8t = V8T.getInstance();
        v8t.setContext(context);
        return v8t;
    }

    public V8T newInstance(String globalAlias) {
        V8T v8t = V8T.getInstance(globalAlias);
        v8t.setContext(context);
        return v8t;
    }

}
