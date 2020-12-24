package org.hurricanegames.pluginlib.commands;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.stream.Collectors;

import org.hurricanegames.pluginlib.utils.types.Tuple;

public class CommandResponseException extends RuntimeException {

	public static final String SEPARATOR = "\n";

	private static final long serialVersionUID = 1L;

	public static CommandResponseException ofList(Collection<String> messages) {
		return new CommandResponseException(String.join(SEPARATOR, messages));
	}

	public static CommandResponseException ofFormattedList(Collection<Tuple<String, Object[]>> messagesFormatAndArgs) {
		return new CommandResponseException(
			messagesFormatAndArgs.stream()
			.map(entry -> MessageFormat.format(entry.getObject1(), entry.getObject2()))
			.collect(Collectors.joining(SEPARATOR))
		);
	}

	public CommandResponseException(String message) {
		super(message);
	}

	public CommandResponseException(String format, Object... args) {
		this(MessageFormat.format(format, args));
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}