package org.hurricanegames.pluginlib.configurations.typeserializers;

public class IdentityTypeSerializer<T> implements TypeSerializer<T> {

	protected final Class<?> clazz;

	public IdentityTypeSerializer(Class<?> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(Object object) {
		if (clazz.isInstance(object)) {
			return (T) object;
		}
		return null;
	}

	@Override
	public Object serialize(T type) {
		return type;
	}

}