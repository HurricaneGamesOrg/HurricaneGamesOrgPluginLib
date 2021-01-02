package org.hurricanegames.pluginlib.commands;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRouter<H extends CommandHelper<?, ?, ?, ?>> implements Command<H> {

	protected final H helper;

	public CommandRouter(H helper) {
		this.helper = helper;
	}

	private String permission;
	private final Map<String, Command<H>> commands = new LinkedHashMap<>();

	protected void setPermission(String permission) {
		this.permission = permission;
	}

	protected void addCommand(String name, Command<H> command) {
		this.commands.put(name, command);
	}

	protected void removeCommand(String name) {
		this.commands.remove(name);
	}

	@Override
	public H getHelper() {
		return helper;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public void handleCommand(CommandContext context) {
		CommandSender sender = context.getSender();
		if (context.hasArg(0)) {
			String commandName = context.getArg(0);
			Command<H> command = commands.get(commandName);
			if (command == null) {
				throw new CommandResponseException(helper.getMessages().getSubCommandNotFoundMessage(commandName));
			}
			helper.validateHasPermission(sender, command.getPermission());
			command.handleCommand(context.getSubContext(1));
		} else {
			String commandName = context.getCommand();
			if (sender instanceof Player) {
				commandName = "/" + commandName;
			}
			getHelpMessages(sender, commandName).forEach(sender::sendMessage);
		}
	}

	@Override
	public List<String> getAutoComplete(CommandContext context) {
		CommandSender sender = context.getSender();
		if (!context.hasArg(0)) {
			return
				commands.entrySet().stream()
				.filter(entry -> getHelper().hasPermission(sender, entry.getValue().getPermission()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		}
		String commandName = context.getArg(0);
		if (!context.hasArg(1)) {
			return
				commands.entrySet().stream()
				.filter(entry -> getHelper().hasPermission(sender, entry.getValue().getPermission()))
				.map(Map.Entry::getKey)
				.filter(c -> c.startsWith(commandName))
				.collect(Collectors.toList());
		}
		Command<H> command = commands.get(commandName);
		if (command == null) {
			return Collections.emptyList();
		}
		getHelper().validateHasPermission(sender, command.getPermission());
		return command.getAutoComplete(context.getSubContext(1));
	}

	@Override
	public List<String> getHelpMessages(CommandSender sender, String commandLabel) {
		String color = helper.getMessages().getSubCommandLabelColor();
		return
			commands.entrySet().stream()
			.filter(entry -> helper.hasPermission(sender, entry.getValue().getPermission()))
			.map(entry -> entry.getValue().getHelpMessages(sender, entry.getKey()))
			.flatMap(List::stream)
			.map(s -> color + commandLabel + " " + s)
			.collect(Collectors.toList());
	}

}
