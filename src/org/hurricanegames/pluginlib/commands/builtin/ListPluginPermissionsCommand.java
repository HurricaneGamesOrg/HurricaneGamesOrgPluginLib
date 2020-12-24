package org.hurricanegames.pluginlib.commands.builtin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.hurricanegames.pluginlib.commands.CommandBasic;
import org.hurricanegames.pluginlib.commands.CommandHelper;
import org.hurricanegames.pluginlib.commands.CommandResponseException;
import org.hurricanegames.pluginlib.utils.types.Tuple;

//TODO: localization
public class ListPluginPermissionsCommand<H extends CommandHelper<?, ?, ?, ?>> extends CommandBasic<H> {

	public ListPluginPermissionsCommand(H helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand() {
		Plugin plugin = helper.getPlugin();
		List<Tuple<String, Object[]>> messages = new ArrayList<>();
		for (Permission permission : plugin.getDescription().getPermissions()) {
			messages.add(new Tuple<>(
				"{0} - {1} (Default - {2}, children - {3})",
				new Object[] {
					permission.getName(),
					permission.getDescription(),
					permission.getDefault().toString(),
					permission.getChildren().keySet().stream().collect(Collectors.joining(", "))
				}
			));
		}
		try {
			Bukkit.getCommandMap().getKnownCommands().values().stream()
			.filter(command -> {
				if (command instanceof PluginIdentifiableCommand) {
					return plugin.equals(((PluginIdentifiableCommand) command).getPlugin());
				}
				return false;
			})
			.filter(command -> command.getPermission() != null)
			.forEach(command -> messages.add(new Tuple<>(
				"{0} - Access to command {1} (Description - {2}, aliases - {3})",
				new Object[] {
					command.getPermission(),
					command.getName(),
					command.getDescription(),
					command.getAliases()
				}
			)));
		} catch (Throwable t) {
		}
		throw CommandResponseException.ofFormattedList(messages);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "lists plugin permissions";
	}

}
