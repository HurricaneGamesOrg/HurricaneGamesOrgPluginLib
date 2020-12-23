package org.hurricanegames.pluginlib.configurations.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.hurricanegames.pluginlib.configurations.ConfigurationEntryCodec;
import org.hurricanegames.pluginlib.configurations.IConfiguration;
import org.hurricanegames.pluginlib.configurations.annotated.AnnotatedConfiguration.ConfigurationFieldDefinition.DefaultConfigurationField;
import org.hurricanegames.pluginlib.configurations.typeserializers.CollectionTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.ColorizedStringTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.IConfigurationTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.IdentityTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.IntegerTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.ListTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.LongTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.MapTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.MapTypeSerializer.MapKVTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.SetTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;
import org.hurricanegames.pluginlib.utils.ReflectionUtils;

public class AnnotatedConfiguration implements IConfiguration {

	@SuppressWarnings("rawtypes")
	protected final ConfigurationField[] fields;

	@SuppressWarnings("rawtypes")
	public AnnotatedConfiguration() {
		List<ConfigurationField> fieldsList = new ArrayList<>();
		Class<?> clazz = getClass();
		do {
			Arrays.stream(clazz.getDeclaredFields())
			.filter(field -> {
				return !Modifier.isStatic(field.getModifiers());
			})
			.map(f -> {
				f.setAccessible(true);
				return f;
			})
			.forEach(field -> {
				ConfigurationFieldDefinition definition = field.getAnnotation(ConfigurationFieldDefinition.class);

				if (definition == null) {
					return;
				}

				String fieldName =
					!definition.fieldName().isEmpty() ?
					definition.fieldName() :
					field.getName().toLowerCase().replace("_", ".");

				if (definition.fieldType() != DefaultConfigurationField.class) {
					try {
						boolean found = false;
						for (Constructor<?> construstor : definition.fieldType().getConstructors()) {
							Parameter[] parameters = construstor.getParameters();
							if ((parameters.length == 3) && parameters[1].getType().isAssignableFrom(Field.class) && parameters[2].getType().isAssignableFrom(String.class)) {
								fieldsList.add(ReflectionUtils.newInstance(ReflectionUtils.setAccessible(construstor), this, field, fieldName));
								found = true;
								break;
							}
						}
						if (!found) {
							throw new IllegalArgumentException("Can't find suitable constructor");
						}
					} catch (Exception e) {
						throw new RuntimeException("Unable to instantiate custom configuration field", e);
					}
				} else {
					Class<?> fieldType = field.getType();
					if (AnnotatedConfiguration.class.isAssignableFrom(fieldType)) {
						fieldsList.add(new BaseConfigurationField<>(this, field, fieldName));
					} else {
						fieldsList.add(new SimpleConfigurationField<>(this, field, fieldName));
					}
				}
			});
		} while ((clazz = clazz.getSuperclass()) != null);
		this.fields = fieldsList.toArray(new ConfigurationField[0]);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void load(ConfigurationSection section) {
		for (ConfigurationField field : fields) {
			try {
				field.load(section);
			} catch (Throwable t) {
				throw new RuntimeException("Unable to load field " + field.configurationField, t);
			}
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void save(ConfigurationSection section) {
		for (ConfigurationField field : fields) {
			try {
				field.save(section);
			} catch (Throwable t) {
				throw new RuntimeException("Unable to save field " + field.configurationField, t);
			}
		}
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ConfigurationFieldDefinition {

		String fieldName() default "";

		@SuppressWarnings("rawtypes")
		Class<? extends ConfigurationField> fieldType() default DefaultConfigurationField.class;

		/**
		 * A marker class for {@link ConfigurationFieldDefinition#fieldType()}<br>
		 * This marker class means that creating configuration field from definition should be done by the configuration itself
		 */
		@SuppressWarnings("rawtypes")
		public static final class DefaultConfigurationField extends ConfigurationField {

			@SuppressWarnings("unchecked")
			public DefaultConfigurationField(AnnotatedConfiguration configuration, Field field) {
				super(configuration, field);
				throw new UnsupportedOperationException("Marker class");
			}

			@Override
			protected void load(ConfigurationSection section) {
				throw new UnsupportedOperationException("Marker class");
			}

			@Override
			protected void save(ConfigurationSection section) {
				throw new UnsupportedOperationException("Marker class");
			}

		}

	}


	public abstract static class ConfigurationField<O> {

		protected final O configuration;
		protected final Field configurationField;

		public ConfigurationField(O configuration, Field field) {
			this.configurationField = field;
			this.configuration = configuration;
		}

		protected abstract void load(ConfigurationSection section);

		protected abstract void save(ConfigurationSection section);

	}

	public static class SimpleConfigurationField<O, T> extends ConfigurationField<O> {

		protected final ConfigurationEntryCodec<T> entryCodec;

		public SimpleConfigurationField(O configuration, Field field, String path) {
			this(configuration, field, path, new IdentityTypeSerializer<>(field.getType()));
		}

		public SimpleConfigurationField(O configuration, Field field, String path, TypeSerializer<T> typeSerializer) {
			this(configuration, field, new ConfigurationEntryCodec<>(path, typeSerializer));
		}

		public SimpleConfigurationField(O configuration, Field field, ConfigurationEntryCodec<T> entryCodec) {
			super(configuration, field);
			this.entryCodec = entryCodec;
		}

		@Override
		protected void load(ConfigurationSection section) {
			T t = entryCodec.read(section);
			if (t != null) {
				ReflectionUtils.setField(configurationField, configuration, t);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void save(ConfigurationSection section) {
			entryCodec.write(section, (T) ReflectionUtils.getField(configurationField, configuration));
		}

	}

	public static class IntegerConfigurationField<O> extends SimpleConfigurationField<O, Integer> {

		public IntegerConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, IntegerTypeSerializer.INSTANCE);
		}

	}

	public static class LongConfigurationField<O> extends SimpleConfigurationField<O, Long> {

		public LongConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, LongTypeSerializer.INSTANCE);
		}

	}

	public static class SimpleColorizedStringConfigurationField<O> extends SimpleConfigurationField<O, String> {

		public SimpleColorizedStringConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, ColorizedStringTypeSerializer.INSTANCE);
		}

	}

	public static class BaseConfigurationField<O, T extends AnnotatedConfiguration> extends SimpleConfigurationField<O, T> {

		@SuppressWarnings("unchecked")
		protected static <T extends AnnotatedConfiguration> IConfigurationTypeSerializer<T> createSerializer(Object object, Field field) {
			return new IConfigurationTypeSerializer<>(() -> {
				try {
					return (T) field.get(object);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			});
		}

		public BaseConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, createSerializer(configuration, field));
		}

	}

	public static class SimpleCollectionConfigurationField<O, T> extends SimpleConfigurationField<O, Collection<T>> {

		@SuppressWarnings("unchecked")
		protected static <T> TypeSerializer<T> createCollectionElementSerializer(Field field) {
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
				if (actualTypeArguments.length == 1) {
					Type actualTypeArgument = actualTypeArguments[0];
					if (actualTypeArgument instanceof Class) {
						return new IdentityTypeSerializer<>((Class<T>) actualTypeArgument);
					}
				}
			}
			System.err.println("Unable to get element type from collection generic type " + type.getClass().getName() + "(" + type + ")");
			return new IdentityTypeSerializer<>(Object.class);
		}

		@SuppressWarnings("unchecked")
		protected static <T> CollectionTypeSerializer<Collection<T>, T> createCollectionSerializer(Field field, TypeSerializer<T> elementSerializer) {
			Class<?> fieldType = field.getType();
			if (!fieldType.isInterface() && !Modifier.isAbstract(fieldType.getModifiers())) {
				return new CollectionTypeSerializer<>(() -> {
					try {
						return (Collection<T>) fieldType.getConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						throw new RuntimeException("Unable to create collection instance", e);
					}
				}, elementSerializer);
			} else if (Queue.class.isAssignableFrom(fieldType)) {
				return new CollectionTypeSerializer<>(ArrayDeque::new, elementSerializer);
			} else if (Set.class.isAssignableFrom(fieldType)) {
				return new CollectionTypeSerializer<>(HashSet::new, elementSerializer);
			} else {
				return new CollectionTypeSerializer<>(ArrayList::new, elementSerializer);
			}
		}

		public SimpleCollectionConfigurationField(O configuration, Field field, String path) {
			this(configuration, field, path, createCollectionElementSerializer(field));
		}

		public SimpleCollectionConfigurationField(O configuration, Field field, String path, TypeSerializer<T> elementSerializer) {
			this(configuration, field, path, createCollectionSerializer(field, elementSerializer));
		}

		@SuppressWarnings("unchecked")
		public SimpleCollectionConfigurationField(O configuration, Field field, String path, CollectionTypeSerializer<? extends Collection<T>, T> serializer) {
			super(configuration, field, path, (TypeSerializer<Collection<T>>) serializer);
		}

	}

	public static class SimpleListConfigurationField<O, T> extends SimpleCollectionConfigurationField<O, T> {

		public SimpleListConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path);
		}

		public SimpleListConfigurationField(O configuration, Field field, String path, TypeSerializer<T> elementSerializer) {
			super(configuration, field, path, new ListTypeSerializer<>(elementSerializer));
		}

	}

	public static class SimpleSetConfigurationField<O, T> extends SimpleCollectionConfigurationField<O, T> {

		public SimpleSetConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path);
		}

		public SimpleSetConfigurationField(O configuration, Field field, String path, TypeSerializer<T> elementSerializer) {
			super(configuration, field, path, new SetTypeSerializer<>(elementSerializer));
		}

	}

	public static class SimpleColorizedStringListConfigurationField<O> extends SimpleListConfigurationField<O, String> {

		public SimpleColorizedStringListConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, ColorizedStringTypeSerializer.INSTANCE);
		}

	}

	public static class SimpleMapConfigurationField<O, K, V> extends SimpleConfigurationField<O, Map<K, V>> {

		protected static <K, V> MapKVTypeSerializer<K, V> createMapKVSerializer(Field field) {
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
				if (actualTypeArguments.length == 2) {
					Type keyType = actualTypeArguments[0];
					Type valueType = actualTypeArguments[1];
					if ((keyType instanceof Class) && (valueType instanceof Class)) {
						return MapKVTypeSerializer.create(new IdentityTypeSerializer<>((Class<?>) keyType), new IdentityTypeSerializer<>((Class<?>) valueType));
					}
				}
			}
			System.err.println("Unable to get element type from map generic type " + type.getClass().getName() + "(" + type + ")");
			return MapKVTypeSerializer.create(new IdentityTypeSerializer<>(Object.class), new IdentityTypeSerializer<>(Object.class));
		}

		@SuppressWarnings("unchecked")
		protected static <K, V> MapTypeSerializer<Map<K, V>, K, V> createMapSerializer(Field field, MapKVTypeSerializer<K, V> entrySerializer) {
			Class<?> fieldType = field.getType();
			if (!fieldType.isInterface() && !Modifier.isAbstract(fieldType.getModifiers())) {
				return new MapTypeSerializer<>(() -> {
					try {
						return (Map<K, V>) fieldType.getConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						throw new RuntimeException("Unable to create collection instance", e);
					}
				}, entrySerializer);
			} else if (NavigableMap.class.isAssignableFrom(fieldType)) {
				return new MapTypeSerializer<>(TreeMap::new, entrySerializer);
			} else {
				return new MapTypeSerializer<>(LinkedHashMap::new, entrySerializer);
			}
		}

		public SimpleMapConfigurationField(O configuration, Field field, String path) {
			super(configuration, field, path, createMapSerializer(field, createMapKVSerializer(field)));
		}

		public SimpleMapConfigurationField(O configuration, Field field, String path, TypeSerializer<K> keySerializer, TypeSerializer<V> valueSerializer) {
			super(configuration, field, path, createMapSerializer(field, MapKVTypeSerializer.create(keySerializer, valueSerializer)));
		}

		public SimpleMapConfigurationField(O configuration, Field field, String path, MapKVTypeSerializer<K, V> entrySerializer) {
			super(configuration, field, path, createMapSerializer(field, entrySerializer));
		}

		public SimpleMapConfigurationField(O configuration, Field field, String path, MapTypeSerializer<Map<K,V>, K, V> mapSerializer) {
			super(configuration, field, path, mapSerializer);
		}

	}

}
