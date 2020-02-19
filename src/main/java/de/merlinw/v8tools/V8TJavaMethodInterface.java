package de.merlinw.v8tools;

import java.util.List;

public interface V8TJavaMethodInterface<R> {

    R execute(List<Class<?>> paramTypes, V8TObjectHolder... params);

}
