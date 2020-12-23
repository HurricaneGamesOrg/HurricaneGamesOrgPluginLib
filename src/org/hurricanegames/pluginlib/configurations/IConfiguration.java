package org.hurricanegames.pluginlib.configurations;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfiguration {

	public void load(ConfigurationSection section);

	public void save(ConfigurationSection section);

}
