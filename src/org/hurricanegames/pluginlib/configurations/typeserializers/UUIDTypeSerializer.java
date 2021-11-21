package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.UUID;

public class UUIDTypeSerializer extends AsStringTypeSerializer<UUID> {

	public static final UUIDTypeSerializer INSTANCE = new UUIDTypeSerializer();

	protected UUIDTypeSerializer() {
		super(UUID::fromString, UUID::toString);
	}

}
