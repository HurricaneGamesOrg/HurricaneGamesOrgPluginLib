package org.hurricanegames.pluginlib.configurations.typeserializers;

import org.hurricanegames.pluginlib.utils.bukkit.MiscBukkitUtils;

public class ColorizedStringTypeSerializer implements TypeSerializer<String> {

	public static final ColorizedStringTypeSerializer INSTANCE = new ColorizedStringTypeSerializer();

	@Override
	public String deserialize(Object object) {
		return object != null ? MiscBukkitUtils.colorize((String) object) : null;
	}

	@Override
	public Object serialize(String type) {
		return type;
	}

}