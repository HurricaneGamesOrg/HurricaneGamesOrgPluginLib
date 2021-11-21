package org.hurricanegames.pluginlib.configurations;

import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class ConfigurationEntryCodecDefaulting<T> extends ConfigurationEntryCodec<T> {

	protected final Supplier<T> defaultValue;

	public ConfigurationEntryCodecDefaulting(String path, TypeSerializer<T> serializer) {
		this(path, serializer, () -> null);
	}

	public ConfigurationEntryCodecDefaulting(String path, TypeSerializer<T> serializer, T defaultValue) {
		this(path, serializer, () -> defaultValue);
	}

	public ConfigurationEntryCodecDefaulting(String path, TypeSerializer<T> serializer, Supplier<T> defaultValue) {
		super(path, serializer);
		this.defaultValue = defaultValue;
	}

	@Override
	public T read(ConfigurationSection section) {
		return readOrDefault(section, defaultValue);
	}

	public T readOrDefault(ConfigurationSection section, T defaultValue) {
		T value = serializer.deserialize(section.get(path));
		return value != null ? value : defaultValue;
	}

	public T readOrDefault(ConfigurationSection section, Supplier<T> defaultValue) {
		T value = serializer.deserialize(section.get(path));
		return value != null ? value : defaultValue.get();
	}

}
