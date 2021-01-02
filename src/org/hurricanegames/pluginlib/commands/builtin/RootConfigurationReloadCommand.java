package org.hurricanegames.pluginlib.commands.builtin;

import java.util.function.Supplier;

import org.hurricanegames.pluginlib.commands.CommandBasic;
import org.hurricanegames.pluginlib.commands.CommandHelper;
import org.hurricanegames.pluginlib.commands.CommandResponseException;
import org.hurricanegames.pluginlib.configurations.IRootConfiguration;

public class RootConfigurationReloadCommand<H extends CommandHelper<?, ?, ?, ?>> extends CommandBasic<H> {

	protected final IRootConfiguration configuration;
	protected final Supplier<String> configurationName;

	public RootConfigurationReloadCommand(H helper, IRootConfiguration configuration, Supplier<String> configurationName) {
		super(helper);
		this.configuration = configuration;
		this.configurationName = configurationName;
	}

	public RootConfigurationReloadCommand(H helper, String permission, IRootConfiguration configuration, Supplier<String> configurationName) {
		super(helper, permission);
		this.configuration = configuration;
		this.configurationName = configurationName;
	}

	@CommandHandler
	private void handleCommand() {
		try {
			configuration.reload();
			throw new CommandResponseException(helper.getMessages().getCommandConfigurationReloadSuccessMessage(configurationName.get()));
		} catch (CommandResponseException e) {
			throw e;
		} catch (Exception e) {
			handleReloadFailException(e);
			throw new CommandResponseException(helper.getMessages().getCommandConfigurationReloadFailMessage(configurationName.get(), e.getMessage()));
		}
	}

	@Override
	protected String getHelpExplainMessage() {
		return helper.getMessages().getCommandConfigurationReloadHelpMessage(configurationName.get());
	}

	protected void handleReloadFailException(Throwable t) {
		System.err.println("Error while reloading configuration " + configurationName);
		t.printStackTrace(System.err);
	}

}
