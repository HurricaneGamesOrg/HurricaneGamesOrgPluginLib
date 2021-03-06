package org.hurricanegames.pluginlib.utils.paper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.hurricanegames.pluginlib.commands.BukkitCommandExecutor;

public class PaperCommandMapHelper {

	public static void registerCommand(String name, BukkitCommandExecutor executor) {
		registerCommand(name, "Description N/A", Collections.emptyList(), executor);
	}

	public static void registerCommand(String name, String description, List<String> aliases, BukkitCommandExecutor executor) {
		Plugin plugin = executor.getCommand().getHelper().getPlugin();
		plugin.getServer().getCommandMap().register(plugin.getName().toLowerCase(Locale.ENGLISH), new PaperPluginCommand(name, description, aliases, executor));
	}

	protected static class PaperPluginCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {

		protected final Plugin owner;
		protected final BukkitCommandExecutor executor;

		public PaperPluginCommand(String name, String description, List<String> aliases, BukkitCommandExecutor executor) {
			super(name, description, "/" + name, new ArrayList<>(aliases));
			this.executor = executor;
			this.owner = executor.getCommand().getHelper().getPlugin();
		}

		@Override
		public Plugin getPlugin() {
			return owner;
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			return executor.onCommand(sender, this, commandLabel, args);
		}

		@Override
		public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args) {
			return executor.onTabComplete(sender, this, commandLabel, args);
		}

	}

}
