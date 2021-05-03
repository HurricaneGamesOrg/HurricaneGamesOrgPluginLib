package org.hurricanegames.pluginlib.utils.bukkit.types.world;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.util.Vector;
import org.hurricanegames.pluginlib.configurations.ConfigurationEntryCodec;
import org.hurricanegames.pluginlib.configurations.typeserializers.IntegerTypeSerializer;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class Coord3D {

	protected final int x;
	protected final int y;
	protected final int z;

	public Coord3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Coord2D to2D() {
		return new Coord2D(x, z);
	}

	public Block toBlock(World world) {
		return world.getBlockAt(x, y, z);
	}

	public Vector toVector() {
		return new Vector(x, y, z);
	}

	public Location toLocation(World world) {
		return new Location(world, x, y, z, 0, 0);
	}


	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Coord3D other = (Coord3D) obj;
		return (x == other.x) && (y == other.y) && (z == other.z);
	}

	@Override
	protected Coord3D clone() {
		return new Coord3D(x, y, z);
	}

	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}


	public static Coord3D of(Block block) {
		return new Coord3D(block.getX(), block.getY(), block.getZ());
	}

	public static Coord3D of(Vector vector) {
		return new Coord3D(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
	}

	public static Coord3D of(Location location) {
		return new Coord3D(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}


	public static class Coord3DSerializer implements TypeSerializer<Coord3D> {

		public static final Coord3DSerializer INSTANCE = new Coord3DSerializer();

		protected static final ConfigurationEntryCodec<Integer> codec_x = new ConfigurationEntryCodec<>("x", IntegerTypeSerializer.INSTANCE);
		protected static final ConfigurationEntryCodec<Integer> codec_y = new ConfigurationEntryCodec<>("y", IntegerTypeSerializer.INSTANCE);
		protected static final ConfigurationEntryCodec<Integer> codec_z = new ConfigurationEntryCodec<>("z", IntegerTypeSerializer.INSTANCE);

		@Override
		public Coord3D deserialize(Object object) {
			if (object instanceof Vector) {
				return Coord3D.of((Vector) object);
			} else if (object instanceof Location) {
				return Coord3D.of((Location) object);
			} else if (object instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) object;
				return new Coord3D(codec_x.read(section), codec_y.read(section), codec_z.read(section));
			}
			return null;
		}

		@Override
		public Object serialize(Coord3D type) {
			ConfigurationSection section = new MemoryConfiguration();
			codec_x.write(section, type.getX());
			codec_y.write(section, type.getY());
			codec_z.write(section, type.getZ());
			return section;
		}

	}

}
