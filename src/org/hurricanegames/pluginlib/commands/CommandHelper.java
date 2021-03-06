package org.hurricanegames.pluginlib.commands;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.hurricanegames.pluginlib.playerinfo.PlayerInfo;
import org.hurricanegames.pluginlib.playerinfo.PlayerInfoProvider;

public class CommandHelper<P extends Plugin, M extends CommandsLocalization, PI extends PlayerInfo, PIP extends PlayerInfoProvider<PI>> {

	protected final P plugin;
	protected final M messagesProvider;
	protected final PIP playerInfoProvider;

	public CommandHelper(P plugin, M messages, PIP playerInfoProvider) {
		this.plugin = plugin;
		this.messagesProvider = messages;
		this.playerInfoProvider = playerInfoProvider;
	}

	public P getPlugin() {
		return plugin;
	}

	public M getMessages() {
		return messagesProvider;
	}

	public PIP getPlayersInfoProvider() {
		return playerInfoProvider;
	}

	public Server getServer() {
		return plugin.getServer();
	}

	public Player getSenderAsPlayer(CommandSender sender, String notAPlayerMessage) {
		if (!(sender instanceof Player)) {
			throw new CommandResponseException(notAPlayerMessage);
		}
		return (Player) sender;
	}

	public Player parseOnlinePlayer(String value) {
		Player player = null;
		try {
			player = getServer().getPlayer(UUID.fromString(value));
		} catch (Exception e) {
			player = getServer().getPlayerExact(value);
		}
		return validateNotNull(player, getMessages().getArgOnlinePlayerErrorNotOnlineMessage(value));
	}

	public PI parseOfflinePlayer(String value) {
		try {
			UUID uuid = UUID.fromString(value);
			PI player = getPlayersInfoProvider().getByUUID(UUID.fromString(value));
			return player != null ? player : getPlayersInfoProvider().createUnknown(uuid);
		} catch (IllegalArgumentException e) {
			return validateNotNull(getPlayersInfoProvider().getByName(value), getMessages().getArgOfflinePlayerErrorNeverPlayedMessage(value));
		}
	}

	/**
	 * Parses string as integer (via {@link Integer#parseInt(String)})
	 * Throws {@link CommandResponseException} on parse fail with message from {@link CommandsLocalization#getArgIntegerErrorNotIntegerMessage(String)}
	 * @param value value
	 * @return integer
	 */
	public int parseInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new CommandResponseException(getMessages().getArgIntegerErrorNotIntegerMessage(value));
		}
	}

	/**
	 * Parses string as double (via {@link Double#parseDouble(String)})
	 * Throws {@link CommandResponseException} on parse fail with message from {@link CommandsLocalization#getArgDoubleErrorNotDoubleMessage(String)}
	 * @param value value
	 * @return double
	 */
	public double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new CommandResponseException(getMessages().getArgDoubleErrorNotDoubleMessage(value));
		}
	}

	/**
	 * Parses string as boolean<br>
	 * Returns {@link Boolean#TRUE} if string equals {@link CommandsLocalization#getArgBooleanValueTrue()}<br>
	 * Returns {@link Boolean#FALSE} if string equals {@link CommandsLocalization#getArgBooleanValueFalse()}
	 * Throws {@link CommandResponseException} on parse fail with message from {@link CommandsLocalization#getArgBooleanErrorNotBooleanMessage(String)}
	 * @param value value
	 * @return boolean
	 */
	public Boolean parseBoolean(String value) {
		if (value.equalsIgnoreCase(getMessages().getArgBooleanValueTrue())) {
			return Boolean.TRUE;
		}
		if (value.equalsIgnoreCase(getMessages().getArgBooleanValueFalse())) {
			return Boolean.FALSE;
		}
		throw new CommandResponseException(getMessages().getArgBooleanErrorNotBooleanMessage(value));
	}

	/**
	 * Validates that provided value is not null<br>
	 * Throws {@link CommandResponseException} on validation fail with provided message
	 * @param t value
	 * @param nullMessageFormat message format
	 * @param nullMessageArguments message arguments
	 * @return value
	 */
	public <V> V validateNotNull(V t, String nullMessageFormat, Object... nullMessageArguments) {
		if (t == null) {
			throw new CommandResponseException(MessageFormat.format(nullMessageFormat, nullMessageArguments));
		}
		return t;
	}

	/**
	 * Validates that provided value is true
	 * @param t value
	 * @param notTrueMessageFormat message format
	 * @param notTrueMessageArguments message arguments
	 * @return value
	 */
	public boolean validateIsTrue(boolean t, String notTrueMessageFormat, Object... notTrueMessageArguments) {
		if (!t) {
			throw new CommandResponseException(MessageFormat.format(notTrueMessageFormat, notTrueMessageArguments));
		}
		return t;
	}

	/**
	 * Validates that permissible has provided permission<br>
	 * Throws {@link CommandResponseException} on validation fail with message from {@link CommandsLocalization#getValidateHasPermissionErrorNoPermissionMessage(String)}
	 * @param permissible permissible
	 * @param permission permission
	 * @return permissible
	 */
	public Permissible validateHasPermission(Permissible permissible, String permission) {
		validateHasPermission(permissible, permission, getMessages().getValidateHasPermissionErrorNoPermissionMessage(permission));
		return permissible;
	}

	/**
	 * Validates that permissible has provided permission<br>
	 * Throws {@link CommandResponseException} on validation fail with provided message
	 * @param permissible permissible
	 * @param permission permission
	 * @param missingPermissionMessage message format
	 * @param missingPermissionMessageArguments message arguments
	 * @return permissible
	 */
	public Permissible validateHasPermission(Permissible permissible, String permission, String missingPermissionMessage, Object... missingPermissionMessageArguments) {
		if (permission == null) {
			return permissible;
		}
		if (!permissible.hasPermission(permission)) {
			throw new CommandResponseException(missingPermissionMessage, missingPermissionMessageArguments);
		}
		return permissible;
	}

	/**
	 * Checks if permissible has provided permission<br>
	 * If permission is null returns true
	 * @param permissible permissible
	 * @param permission permission
	 * @return permissions status
	 */
	public boolean hasPermission(Permissible permissible, String permission) {
		if (permission == null) {
			return true;
		}
		return permissible.hasPermission(permission);
	}

	/**
	 * Creates predicate from validate methods (by catching the command response exception)
	 * @param <T> predicate type
	 * @param validate validate run
	 * @return predicate
	 */
	public <T> Predicate<T> createPredicate(Consumer<T> validate) {
		return t -> {
			try {
				validate.accept(t);
				return true;
			} catch (CommandResponseException e) {
				return false;
			}
		};
	}

}
