package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.hurricanegames.pluginlib.configurations.IConfiguration;

public class IConfigurationTypeSerializer<T extends IConfiguration> implements TypeSerializer<T> {

	protected final Supplier<T> configurationSupplier;

	public IConfigurationTypeSerializer(Supplier<T> configurationSupplier) {
		this.configurationSupplier = configurationSupplier;
	}

	@Override
	public T deserialize(Object object) {
		if (object instanceof ConfigurationSection) {
			T configuration = configurationSupplier.get();
			configuration.load((ConfigurationSection) object);
			return configuration;
		}
		return null;
	}

	@Override
	public Object serialize(T type) {
		ConfigurationSection section = new MemoryConfiguration();
		type.save(section);
		return section;
	}

}