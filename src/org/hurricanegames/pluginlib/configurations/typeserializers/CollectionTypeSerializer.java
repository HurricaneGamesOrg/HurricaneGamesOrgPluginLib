package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.hurricanegames.pluginlib.utils.bukkit.MiscBukkitUtils;

public class CollectionTypeSerializer<C extends Collection<T>, T> implements TypeSerializer<C> {

	protected final Supplier<C> collectionSupplier;
	protected final TypeSerializer<T> elementSerializer;

	public CollectionTypeSerializer(Supplier<C> collectionSupplier, TypeSerializer<T> elementSerializer) {
		this.collectionSupplier = collectionSupplier;
		this.elementSerializer = elementSerializer;
	}

	@Override
	public C deserialize(Object object) {
		if (object instanceof Collection) {
			C collection = collectionSupplier.get();
			for (Object element : (Collection<?>) object) {
				if (element instanceof Map) {
					element = MiscBukkitUtils.createConfigurationSection((Map<?, ?>) element);
				}
				T t = elementSerializer.deserialize(element);
				if (t != null) {
					collection.add(t);
				}
			}
			return collection;
		}
		return null;
	}

	@Override
	public Object serialize(C type) {
		List<Object> list = new ArrayList<>();
		for (T element : type) {
			list.add(elementSerializer.serialize(element));
		}
		return list;
	}

}