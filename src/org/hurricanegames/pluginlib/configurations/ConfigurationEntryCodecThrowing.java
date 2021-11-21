package org.hurricanegames.pluginlib.configurations;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class ConfigurationEntryCodecThrowing<T> extends ConfigurationEntryCodec<T> {

	public ConfigurationEntryCodecThrowing(String path, TypeSerializer<T> serializer) {
		super(path, serializer);
	}

	@Override
	public T read(ConfigurationSection section) {
		T value = serializer.deserialize(ConfigurationUtils.getValueOrThrow(section, path));
		if (value == null) {
			throw new IllegalStateException("Configuration " + section.getCurrentPath() + " has invalid value at path " + path);
		}
		return value;
	}

}
