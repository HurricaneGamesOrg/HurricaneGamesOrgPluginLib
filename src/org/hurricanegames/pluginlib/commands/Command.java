package org.hurricanegames.pluginlib.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface Command<H extends CommandHelper<?, ?, ?, ?>> {

	public H getHelper();

	public String getPermission();

	public void handleCommand(CommandContext context);

	public List<String> getAutoComplete(CommandContext context);

	public List<String> getHelpMessages(CommandSender sender, String commandLabel);

}
