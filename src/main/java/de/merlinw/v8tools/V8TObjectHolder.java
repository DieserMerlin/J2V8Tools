package de.merlinw.v8tools;

public class V8TObjectHolder {

    private Object object;
    private Class<?> typeClass;

    public V8TObjectHolder(Object object, Class<?> typeClass) {
        if (!object.getClass().equals(typeClass) && !object.getClass().isAssignableFrom(typeClass))
            throw new IllegalArgumentException();
        this.object = object;
        this.typeClass = typeClass;
    }

    public <T> T getAs(Class<T> clazz) {
        try {
            return (T) object;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Wrong class provided.");
        }
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }
}
