package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.function.Function;

public class AsStringTypeSerializer<T> implements TypeSerializer<T> {

	protected final Function<String, T> fromString;
	protected final Function<T, String> toString;

	public AsStringTypeSerializer(Function<String, T> fromString, Function<T, String> toString) {
		this.fromString = fromString;
		this.toString = toString;
	}


	@Override
	public T deserialize(Object object) {
		if (object != null) {
			return fromString.apply(object.toString());
		}
		return null;
	}

	@Override
	public Object serialize(T type) {
		return toString.apply(type);
	}

}
