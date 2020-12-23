package org.hurricanegames.pluginlib.configurations.typeserializers;

public class IntegerTypeSerializer implements TypeSerializer<Integer> {

	public static final IntegerTypeSerializer INSTANCE = new IntegerTypeSerializer();

	@Override
	public Integer deserialize(Object object) {
		if (object instanceof Integer) {
			return (Integer) object;
		} else if (object instanceof Number) {
			return Integer.valueOf(((Number) object).intValue());
		} else {
			return null;
		}
	}

	@Override
	public Object serialize(Integer type) {
		return type;
	}

}