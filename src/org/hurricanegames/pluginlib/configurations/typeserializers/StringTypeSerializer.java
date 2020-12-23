package org.hurricanegames.pluginlib.configurations.typeserializers;

public class StringTypeSerializer implements TypeSerializer<String> {

	public static final StringTypeSerializer INSTANCE = new StringTypeSerializer();

	protected StringTypeSerializer() {
	}

	@Override
	public String deserialize(Object object) {
		return object != null ? object.toString() : null;
	}

	@Override
	public Object serialize(String type) {
		return type;
	}

}
