package org.hurricanegames.pluginlib.utils.bukkit.types.world;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.util.Vector;
import org.hurricanegames.pluginlib.configurations.ConfigurationEntryCodec;
import org.hurricanegames.pluginlib.configurations.typeserializers.IntegerTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class Coord2D {

	protected final int x;
	protected final int z;

	public Coord2D(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(getClass())) {
			return false;
		}
		Coord2D other = (Coord2D) obj;
		return (x == other.x) && (z == other.z);
	}

	public List<Coord2D> getTo(Coord2D other) {
		if ((other.getX() < getX()) || (other.getZ() < getZ())) {
			throw new IllegalArgumentException("Other coordinate should be bigger");
		}
		List<Coord2D> list = new ArrayList<>();
		for (int lX = getX(); lX <= other.getX(); lX++) {
			for (int lZ = getZ(); lZ <= other.getZ(); lZ++) {
				list.add(new Coord2D(lX, lZ));
			}
		}
		return list;
	}

	public Iterable<Coord2D> iterateTo(Coord2D other) {
		if ((other.getX() < getX()) || (other.getZ() < getZ())) {
			throw new IllegalArgumentException("Other coordinates should be bigger");
		}
		return () -> new Iterator<Coord2D>() {
			protected Coord2D next = new Coord2D(getX(), getZ());
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public Coord2D next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				Coord2D ret = next;
				int x = next.getX();
				int z = next.getZ();
				if (z < other.getZ()) {
					next = new Coord2D(x, z + 1);
				} else if (x < other.getX()) {
					next = new Coord2D(x + 1, getZ());
				} else {
					next = null;
				}
				return ret;
			}
		};
	}

	public boolean isInAABB(Coord2D otherMin, Coord2D otherMax) {
		return
			(x >= otherMin.getX()) && (x <= otherMax.getX()) &&
			(z >= otherMin.getZ()) && (z <= otherMax.getZ());
	}

	@Override
	public Coord2D clone() {
		return new Coord2D(getX(), getZ());
	}

	@Override
	public int hashCode() {
		return (x * 31) ^ z;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}(x: {1}, z: {2})", getClass().getSimpleName(), getX(), getZ());
	}

	public static Coord2D ofChunk(Chunk chunk) {
		return new Coord2D(chunk.getX(), chunk.getZ());
	}

	public static Coord2D createChunkCoord(Block block) {
		return new Coord2D(block.getX() >> 4, block.getZ() >> 4);
	}

	public static Coord2D createChunkCoord(Vector worldLocation) {
		return new Coord2D(worldLocation.getBlockX() >> 4, worldLocation.getBlockZ() >> 4);
	}

	public static class Coord2DSerializer implements TypeSerializer<Coord2D> {

		public static final Coord2DSerializer INSTANCE = new Coord2DSerializer();

		protected Coord2DSerializer() {
		}

		protected static final ConfigurationEntryCodec<Integer> codec_x = new ConfigurationEntryCodec<>("x", IntegerTypeSerializer.INSTANCE);
		protected static final ConfigurationEntryCodec<Integer> codec_z = new ConfigurationEntryCodec<>("z", IntegerTypeSerializer.INSTANCE);

		@Override
		public Coord2D deserialize(Object object) {
			if (object instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) object;
				Integer x = codec_x.read(section);
				Integer z = codec_z.read(section);
				if ((x != null) && (z != null)) {
					return new Coord2D(x, z);
				}
			}
			return null;
		}

		@Override
		public Object serialize(Coord2D type) {
			ConfigurationSection section = new MemoryConfiguration();
			codec_x.write(section, type.getX());
			codec_z.write(section, type.getZ());
			return section;
		}

	}

}
