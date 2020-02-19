# J2V8Tools
A library for prodividing some more functionality to the V8 Bindings for Java ([J2V8](https://github.com/eclipsesource/J2V8)).

This is mainly written for [twasi-core](https://github.com/twasi/twasi-core)'s (experimental) JavaScript plugin loader.

## Concepts

This library provides
 - shared context objects between multiple V8 runtimes (primitives only)
 - strictly typed JavaMethod arguments
 - a mapper for mapping V8Objects to [Google's Gson](https://github.com/google/gson)\-objects and vice versa

## Getting started

Get a V8T Instance:
```Java
V8T firstRuntime = V8T.getInstance();
System.out.println(firstRuntime.executeStringScript("'Hello World'"));
```

Bind instances together using a shared context:
```Java
V8T secondRuntime = V8T.getInstance();
secondRuntime.setContext(firstRuntime.getContext());
```

Share data using the context we just bound to our second engine:
```Java
firstRuntime.executeVoidScript("env.set('test', 'This is just a string');");
System.out.println(secondRuntime.executeStringScript("env.get('test');"));
```

This works with all primitive types.

## Maven

```xml
Coming soon
```