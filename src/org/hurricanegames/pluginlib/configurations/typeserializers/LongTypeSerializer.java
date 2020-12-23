package org.hurricanegames.pluginlib.configurations.typeserializers;

public class LongTypeSerializer implements TypeSerializer<Long> {

	public static final LongTypeSerializer INSTANCE = new LongTypeSerializer();

	@Override
	public Long deserialize(Object object) {
		if (object instanceof Long) {
			return (Long) object;
		} else if (object instanceof Number) {
			return Long.valueOf(((Number) object).longValue());
		} else {
			return null;
		}
	}

	@Override
	public Object serialize(Long type) {
		return type;
	}

}