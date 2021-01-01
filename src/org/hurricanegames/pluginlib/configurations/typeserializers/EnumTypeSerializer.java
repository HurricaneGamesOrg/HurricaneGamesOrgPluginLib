package org.hurricanegames.pluginlib.configurations.typeserializers;

public class EnumTypeSerializer<T extends Enum<T>> implements TypeSerializer<T> {

	protected final Class<T> clazz;

	public EnumTypeSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T deserialize(Object object) {
		if (object != null) {
			return Enum.valueOf(clazz, object.toString());
		}
		return null;
	}

	@Override
	public Object serialize(T type) {
		return type.name();
	}

}
