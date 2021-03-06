package org.hurricanegames.pluginlib.configurations;

import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class ConfigurationEntryCodec<T> {

	protected final String path;
	protected final TypeSerializer<T> serializer;

	protected final Supplier<T> defaultValue;

	public ConfigurationEntryCodec(String path, TypeSerializer<T> serializer) {
		this(path, serializer, () -> null);
	}

	public ConfigurationEntryCodec(String path, TypeSerializer<T> serializer, T defaultValue) {
		this.path = path;
		this.serializer = serializer;
		this.defaultValue = () -> defaultValue;
	}

	public ConfigurationEntryCodec(String path, TypeSerializer<T> serializer, Supplier<T> defaultValue) {
		this.path = path;
		this.serializer = serializer;
		this.defaultValue = defaultValue;
	}

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

	public void write(ConfigurationSection section, T value) {
		if (value != null) {
			section.set(path, serializer.serialize(value));
		} else {
			section.set(path, null);
		}
	}

}
