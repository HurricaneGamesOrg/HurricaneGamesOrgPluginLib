package org.hurricanegames.pluginlib.configurations.typeserializers;

public interface TypeSerializer<T> {

	public T deserialize(Object object);

	public Object serialize(T type);

}