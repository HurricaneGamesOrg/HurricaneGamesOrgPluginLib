package org.hurricanegames.pluginlib.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class ConfigurationEntryCodec<T> {

	protected final String path;
	protected final TypeSerializer<T> serializer;

	public ConfigurationEntryCodec(String path, TypeSerializer<T> codec) {
		this.path = path;
		this.serializer = codec;
	}

	public T read(ConfigurationSection section) {
		return serializer.deserialize(section.get(path));
	}

	public void write(ConfigurationSection section, T value) {
		section.set(path, serializer.serialize(value));
	}

}
