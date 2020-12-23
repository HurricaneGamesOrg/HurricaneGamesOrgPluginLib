package org.hurricanegames.pluginlib.configurations;

public interface IRootConfiguration {

	public void load();

	public void save();

	public default void reload() {
		load();
		save();
	}

}
