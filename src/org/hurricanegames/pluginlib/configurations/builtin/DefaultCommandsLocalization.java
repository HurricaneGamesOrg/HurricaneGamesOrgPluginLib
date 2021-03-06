package org.hurricanegames.pluginlib.configurations.builtin;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.hurricanegames.pluginlib.commands.CommandsLocalization;
import org.hurricanegames.pluginlib.configurations.annotated.AnnotatedRootConfiguration;

/**
 * Implements a {@link AnnotatedRootConfiguration} that stores all needed messages for {@link CommandsLocalization}
 */
public abstract class DefaultCommandsLocalization extends AnnotatedRootConfiguration implements CommandsLocalization {

	public static final DefaultCommandsLocalization IMMUTABLE = new DefaultCommandsLocalization() {
		@Override
		public void load() {
		}
		@Override
		public void save() {
		}
	};

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_COLOR_LABEL = ChatColor.GOLD.toString();
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_COLOR_HELP_ARGS = ChatColor.YELLOW.toString();
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_COLOR_HELP_EXPLAIN = ChatColor.AQUA.toString();

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_NOTFOUND = ChatColor.RED + "Command {0} doesnt exist";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGINDEX_NEGATIVE = ChatColor.RED + "Argument index cant be negative";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGINDEX_OOB = ChatColor.RED + "Not enough arguments";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_VALIDATE_NOPERMISSION = ChatColor.RED + "You dont have permission {0}";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_SENDERPLAYER_ERROR = ChatColor.RED + "This command is for players only";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_ONLINEPLAYER_HELP = "{name or uuid of online player}";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_ONLINEPLAYER_ERROR = ChatColor.RED + "Player {0} is not online";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_OFFLINEPLAYER_HELP = "{name or uuid of player}";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_OFFLINEPLAYER_ERROR = ChatColor.RED + "Player {0} never played on a server";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_INTEGER_ERROR = ChatColor.RED + "String {0} is not a number";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_DOUBLE_ERROR = ChatColor.RED + "String {0} is not a double";

	@ConfigurationFieldDefinition
	protected String COMMAND_ARGS_BOOLEAN_TRUE = "true";
	@ConfigurationFieldDefinition
	protected String COMMAND_ARGS_BOOLEAN_FALSE = "false";
	@ConfigurationFieldDefinition
	protected String COMMAND_ARGS_BOOLEAN_HELP = "'{'{0}/{1}'}'";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_ARGS_BOOLEAN_ERROR = ChatColor.RED + "String {0} is not a boolean";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_CONFIGURATION_RELOAD_HELP = "reloads configuration {0}";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_CONFIGURATION_RELOAD_SUCCESS = ChatColor.GREEN + "Configuration {0} successfully reloaded";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	protected String COMMAND_CONFIGURATION_RELOAD_FAIL = ChatColor.GREEN + "Configuration {0} failed to reload due to error {1}, see console for more details";


	@Override
	public String getSubCommandNotFoundMessage(String commandName) {
		return MessageFormat.format(COMMAND_NOTFOUND, commandName);
	}

	@Override
	public String getHelpArgsColor() {
		return COMMAND_COLOR_HELP_ARGS;
	}

	@Override
	public String getHelpExplainColor() {
		return COMMAND_COLOR_HELP_EXPLAIN;
	}

	@Override
	public String getSubCommandLabelColor() {
		return COMMAND_COLOR_LABEL;
	}

	@Override
	public String getArgIndexErrorNegativeMessage() {
		return COMMAND_ARGINDEX_NEGATIVE;
	}

	@Override
	public String getArgIndexErrorOOBMessage(int argIndex) {
		return MessageFormat.format(COMMAND_ARGINDEX_OOB, argIndex);
	}

	@Override
	public String getValidateHasPermissionErrorNoPermissionMessage(String permission) {
		return MessageFormat.format(COMMAND_VALIDATE_NOPERMISSION, permission);
	}

	@Override
	public String getArgSenderPlayerErrorNotPlayerMessage() {
		return COMMAND_ARGS_SENDERPLAYER_ERROR;
	}

	@Override
	public String getArgOnlinePlayerHelp() {
		return COMMAND_ARGS_ONLINEPLAYER_HELP;
	}

	@Override
	public String getArgOnlinePlayerErrorNotOnlineMessage(String playerName) {
		return MessageFormat.format(COMMAND_ARGS_ONLINEPLAYER_ERROR, playerName);
	}

	@Override
	public String getArgOfflinePlayerHelp() {
		return COMMAND_ARGS_OFFLINEPLAYER_HELP;
	}

	@Override
	public String getArgOfflinePlayerErrorNeverPlayedMessage(String playerName) {
		return MessageFormat.format(COMMAND_ARGS_OFFLINEPLAYER_ERROR, playerName);
	}

	@Override
	public String getArgIntegerErrorNotIntegerMessage(String string) {
		return MessageFormat.format(COMMAND_ARGS_INTEGER_ERROR, string);
	}

	@Override
	public String getArgDoubleErrorNotDoubleMessage(String string) {
		return MessageFormat.format(COMMAND_ARGS_DOUBLE_ERROR, string);
	}

	@Override
	public String getArgBooleanHelp() {
		return MessageFormat.format(COMMAND_ARGS_BOOLEAN_HELP, COMMAND_ARGS_BOOLEAN_TRUE, COMMAND_ARGS_BOOLEAN_FALSE);
	}

	@Override
	public String getArgBooleanValueTrue() {
		return COMMAND_ARGS_BOOLEAN_TRUE;
	}

	@Override
	public String getArgBooleanValueFalse() {
		return COMMAND_ARGS_BOOLEAN_FALSE;
	}

	@Override
	public String getArgBooleanErrorNotBooleanMessage(String string) {
		return MessageFormat.format(COMMAND_ARGS_BOOLEAN_ERROR, string);
	}

	@Override
	public String getCommandConfigurationReloadHelpMessage(String configuration) {
		return MessageFormat.format(COMMAND_CONFIGURATION_RELOAD_HELP, configuration);
	}

	@Override
	public String getCommandConfigurationReloadSuccessMessage(String configuration) {
		return MessageFormat.format(COMMAND_CONFIGURATION_RELOAD_SUCCESS, configuration);
	}

	@Override
	public String getCommandConfigurationReloadFailMessage(String configuration, String error) {
		return MessageFormat.format(COMMAND_CONFIGURATION_RELOAD_FAIL, configuration, error);
	}

}
