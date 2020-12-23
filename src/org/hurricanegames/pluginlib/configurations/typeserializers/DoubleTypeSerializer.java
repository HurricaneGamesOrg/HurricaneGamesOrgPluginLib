package org.hurricanegames.pluginlib.configurations.typeserializers;

public class DoubleTypeSerializer implements TypeSerializer<Double> {

	public static final DoubleTypeSerializer INSTANCE = new DoubleTypeSerializer();

	@Override
	public Double deserialize(Object object) {
		if (object instanceof Double) {
			return (Double) object;
		} else if (object instanceof Number) {
			return Double.valueOf(((Number) object).doubleValue());
		} else {
			return null;
		}
	}

	@Override
	public Object serialize(Double type) {
		return type;
	}

}