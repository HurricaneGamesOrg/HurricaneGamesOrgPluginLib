package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.ArrayList;
import java.util.List;

public class ListTypeSerializer<T> extends CollectionTypeSerializer<List<T>, T> {

	public ListTypeSerializer(TypeSerializer<T> elementSerializer) {
		super(ArrayList::new, elementSerializer);
	}

}