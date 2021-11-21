package org.hurricanegames.pluginlib.utils.bukkit.types.world;

import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.hurricanegames.pluginlib.configurations.ConfigurationEntryCodecDefaulting;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class Region3D {

	private final Coord3D min;
	private final Coord3D max;

	public Region3D(Coord3D min, Coord3D max) {
		if ((min.getX() > max.getX()) || (min.getY() > max.getY()) || (min.getZ() > max.getZ())) {
			throw new IllegalArgumentException("min must be less or equal max");
		}
		this.min = min;
		this.max = max;
	}

	public Coord3D getMin() {
		return min;
	}

	public Coord3D getMax() {
		return max;
	}

	public boolean contains(Coord3D coord) {
		return coord.isInAABB(min, max);
	}

	public boolean contains(Coord3D coordMin, Coord3D coordMax) {
		return
			(coordMin.getX() >= min.getX()) &&
			(coordMin.getY() >= min.getY()) &&
			(coordMin.getZ() >= min.getZ()) &&
			(coordMax.getX() <= max.getX()) &&
			(coordMax.getY() <= max.getY()) &&
			(coordMax.getZ() <= max.getZ());
	}

	public Coord3D getCenter() {
		return new Coord3D(center(min.getX(), max.getX()), center(min.getY(), max.getY()), center(min.getZ(), max.getZ()));
	}

	protected static int center(int min, int max) {
		return ((max + min) >> 1);
	}

	public Region2D to2D() {
		return new Region2D(min.to2D(), max.to2D());
	}


	@Override
	public int hashCode() {
		return Objects.hash(max, min);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Region3D other = (Region3D) obj;
		return Objects.equals(max, other.max) && Objects.equals(min, other.min);
	}

	@Override
	public Region3D clone() {
		return new Region3D(getMin().clone(), getMax().clone());
	}

	@Override
	public String toString() {
		return min + "->" + max;
	}


	public static class Region3DSerializer implements TypeSerializer<Region3D> {

		public static final Region3DSerializer INSTANCE = new Region3DSerializer();

		protected Region3DSerializer() {
		}

		protected final ConfigurationEntryCodecDefaulting<Coord3D> codec_min = new ConfigurationEntryCodecDefaulting<>("min", Coord3D.Coord3DSerializer.INSTANCE);
		protected final ConfigurationEntryCodecDefaulting<Coord3D> codec_max = new ConfigurationEntryCodecDefaulting<>("max", Coord3D.Coord3DSerializer.INSTANCE);

		@Override
		public Region3D deserialize(Object object) {
			if (object instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) object;
				Coord3D min = codec_min.read(section);
				Coord3D max = codec_max.read(section);
				if ((min != null) && (max != null)) {
					return new Region3D(min, max);
				}
			}
			return null;
		}

		@Override
		public Object serialize(Region3D type) {
			ConfigurationSection section = new MemoryConfiguration();
			codec_min.write(section, type.getMin());
			codec_max.write(section, type.getMax());
			return section;
		}

	}

}
