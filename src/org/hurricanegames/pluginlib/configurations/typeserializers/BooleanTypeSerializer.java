package org.hurricanegames.pluginlib.configurations.typeserializers;

public class BooleanTypeSerializer implements TypeSerializer<Boolean> {

	@Override
	public Boolean deserialize(Object object) {
		if (object instanceof Boolean) {
			return (Boolean) object;
		} else {
			return null;
		}
	}

	@Override
	public Object serialize(Boolean type) {
		return type;
	}

}
