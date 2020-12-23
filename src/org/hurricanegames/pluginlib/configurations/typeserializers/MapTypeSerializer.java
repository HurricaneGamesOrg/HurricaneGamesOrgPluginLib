package org.hurricanegames.pluginlib.configurations.typeserializers;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

public class MapTypeSerializer<C extends Map<K, V>, K, V> implements TypeSerializer<C> {

	protected final Supplier<C> mapSupplier;
	protected final MapTypeSerializer.MapKVTypeSerializer<K, V> entrySerializer;

	public MapTypeSerializer(Supplier<C> mapSupplier, TypeSerializer<K> keySerializer, TypeSerializer<V> valueSerializer) {
		this(mapSupplier, MapKVTypeSerializer.create(keySerializer, valueSerializer));
	}

	public MapTypeSerializer(Supplier<C> mapSupplier, MapTypeSerializer.MapKVTypeSerializer<K, V> entrySerializer) {
		this.mapSupplier = mapSupplier;
		this.entrySerializer = entrySerializer;
	}

	public abstract static class MapKVTypeSerializer<K, V> implements TypeSerializer<Map.Entry<K, V>> {

		public static <K, V> MapTypeSerializer.MapKVTypeSerializer<K, V> create(TypeSerializer<K> keySerializer, TypeSerializer<V> valueSerializer) {
			return new MapTypeSerializer.MapKVTypeSerializer<K,V>() {
				@Override
				protected Map.Entry<K, V> deserializeKV(String key, Object value) {
					return new AbstractMap.SimpleEntry<>(keySerializer.deserialize(key), valueSerializer.deserialize(value));
				}
				@Override
				protected Map.Entry<String, Object> serializeKV(K key, V value) {
					return new AbstractMap.SimpleEntry<>(keySerializer.serialize(key).toString(), valueSerializer.serialize(value));
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map.Entry<K, V> deserialize(Object object) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) object;
			return deserializeKV(entry.getKey(), entry.getValue());
		}

		@Override
		public Object serialize(Map.Entry<K, V> type) {
			return serializeKV(type.getKey(), type.getValue());
		}

		protected abstract Map.Entry<K, V> deserializeKV(String key, Object value);

		protected abstract Map.Entry<String, Object> serializeKV(K key, V value);

	}

	@Override
	public C deserialize(Object object) {
		if (object instanceof ConfigurationSection) {
			ConfigurationSection section = (ConfigurationSection) object;
			C map = mapSupplier.get();
			for (String keyString : section.getKeys(false)) {
				Map.Entry<K, V> entry = entrySerializer.deserialize(new AbstractMap.SimpleEntry<>(keyString, section.get(keyString)));
				if (entry != null) {
					map.put(entry.getKey(), entry.getValue());
				}
			}
			return map;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object serialize(C type) {
		ConfigurationSection section = new MemoryConfiguration();
		for (Map.Entry<K, V> entry : type.entrySet()) {
			Map.Entry<String, Object> serialized = (Map.Entry<String, Object>) entrySerializer.serialize(entry);
			section.set(serialized.getKey(), serialized.getValue());
		}
		return section;
	}

}