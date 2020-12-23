package org.hurricanegames.pluginlib.configurations.annotated;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.hurricanegames.pluginlib.configurations.ConfigurationUtils;

public abstract class AnnotatedRootYamlConfiguration extends AnnotatedRootConfiguration {

	protected final File storageFile;

	protected AnnotatedRootYamlConfiguration(File storageFile) {
		this.storageFile = storageFile;
	}

	@Override
	public void load() {
		load(YamlConfiguration.loadConfiguration(storageFile));
	}

	@Override
	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		save(config);
		ConfigurationUtils.safeSave(config, storageFile);
	}

}
