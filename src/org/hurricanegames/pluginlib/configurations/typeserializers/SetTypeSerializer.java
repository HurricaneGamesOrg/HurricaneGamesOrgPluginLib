package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

public class SetTypeSerializer<T> extends CollectionTypeSerializer<Set<T>, T> {

	public SetTypeSerializer(TypeSerializer<T> elementSerializer) {
		super(() -> Collections.newSetFromMap(new LinkedHashMap<>()), elementSerializer);
	}

}