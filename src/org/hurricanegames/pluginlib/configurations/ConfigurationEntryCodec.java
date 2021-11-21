package org.hurricanegames.pluginlib.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class ConfigurationEntryCodec<T> {

	protected final String path;
	protected final TypeSerializer<T> serializer;

	public ConfigurationEntryCodec(String path, TypeSerializer<T> serializer) {
		this.path = path;
		this.serializer = serializer;
	}

	public T read(ConfigurationSection section) {
		return serializer.deserialize(section.get(path));
	}

	public void write(ConfigurationSection section, T value) {
		if (value != null) {
			section.set(path, serializer.serialize(value));
		} else {
			section.set(path, null);
		}
	}

}
