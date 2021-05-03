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
		load(this, storageFile);
	}

	@Override
	public void save() {
		save(this, storageFile);
	}

	public static void load(AnnotatedRootConfiguration configuration, File storageFile) {
		configuration.load(YamlConfiguration.loadConfiguration(storageFile));
	}

	public static void save(AnnotatedRootConfiguration configuration, File storageFile) {
		YamlConfiguration config = new YamlConfiguration();
		configuration.save(config);
		ConfigurationUtils.safeSave(config, storageFile);
	}

}
