package org.hurricanegames.pluginlib.utils.paper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.hurricanegames.pluginlib.commands.BukkitCommandExecutor;

public class PaperCommandMapHelper {

	protected final Plugin owner;

	public PaperCommandMapHelper(Plugin owner) {
		this.owner = owner;
	}

	public void registerCommand(String name, BukkitCommandExecutor executor) {
		registerCommand(name, null, executor);
	}

	public void registerCommand(String name, String permission, BukkitCommandExecutor executor) {
		registerCommand(name, permission, owner.getDescription().getName() + " command", Collections.emptyList(), executor);
	}

	public void registerCommand(String name, String description, List<String> aliases, BukkitCommandExecutor executor) {
		registerCommand(name, null, description, aliases, executor);
	}

	public void registerCommand(String name, String permission, String description, List<String> aliases, BukkitCommandExecutor executor) {
		Bukkit.getCommandMap().register(owner.getName().toLowerCase(Locale.ENGLISH), new BukkitPluginCommand(owner, name, permission, description, aliases, executor));
	}

	protected static class BukkitPluginCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {

		protected final Plugin owner;
		protected final BukkitCommandExecutor executor;

		public BukkitPluginCommand(Plugin owner, String name, String permission, String description, List<String> aliases, BukkitCommandExecutor executor) {
			super(name, description, "/" + name, new ArrayList<>(aliases));
			this.setPermission(permission);
			this.executor = executor;
			this.owner = owner;
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
