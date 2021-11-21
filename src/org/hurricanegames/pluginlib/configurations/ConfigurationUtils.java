package org.hurricanegames.pluginlib.configurations;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationUtils {

	/**
	 * Returns configuration value or throws an exception if not set
	 * @param <T> configuration value type
	 * @param config configuration
	 * @param path path
	 * @return configuration value
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueOrThrow(ConfigurationSection config, String path) {
		if (!config.isSet(path)) {
			throw new IllegalStateException("Configuration section " + config.getCurrentPath() + " is missing path " + path);
		}
		return (T) config.get(path);
	}

	/**
	 * Safely saves the config to file<br>
	 * Saves config to temp file and them atomically replaces target
	 * @param config config to save
	 * @param file target file
	 * @throws UncheckedIOException if saving or atomic move fails
	 */
	public static void safeSave(FileConfiguration config, File file) {
		try {
			File tmpfile = new File(file.getParentFile(), file.getName() + ".tmp");
			config.save(tmpfile);
			Files.move(tmpfile.toPath(), file.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
