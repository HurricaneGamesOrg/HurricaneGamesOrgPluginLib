package org.hurricanegames.pluginlib.utils.bukkit.types.world;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.hurricanegames.pluginlib.configurations.ConfigurationEntryCodec;
import org.hurricanegames.pluginlib.configurations.typeserializers.TypeSerializer;

public class Region2D implements Iterable<Coord2D> {

	private final Coord2D min;
	private final Coord2D max;

	public Region2D(Coord2D min, Coord2D max) {
		if ((min.getX() > max.getX()) || (min.getZ() > max.getZ())) {
			throw new IllegalArgumentException("min must be less or equal max");
		}
		this.min = min;
		this.max = max;
	}

	public Coord2D getMin() {
		return min;
	}

	public Coord2D getMax() {
		return max;
	}

	public boolean contains(Coord2D coord) {
		return coord.isInAABB(min, max);
	}

	public boolean contains(Coord2D coordMin, Coord2D coordMax) {
		return
			(coordMin.getX() >= min.getX()) &&
			(coordMin.getZ() >= min.getZ()) &&
			(coordMax.getX() <= max.getX()) &&
			(coordMax.getZ() <= max.getZ());
	}

	public Coord2D getCenter() {
		return new Coord2D(center(min.getX(), max.getX()), center(min.getZ(), max.getZ()));
	}

	protected static int center(int min, int max) {
		return ((max + min) >> 1);
	}

	public List<Coord2D> getAll() {
		return min.getTo(max);
	}

	@Override
	public Iterator<Coord2D> iterator() {
		return min.iterateTo(max).iterator();
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Region2D other = (Region2D) obj;
		return Objects.equals(max, other.max) && Objects.equals(min, other.min);
	}

	@Override
	public Region2D clone() {
		return new Region2D(getMin().clone(), getMax().clone());
	}

	@Override
	public String toString() {
		return min + "->" + max;
	}


	public static class Region2DSerializer implements TypeSerializer<Region2D> {

		public static final Region2DSerializer INSTANCE = new Region2DSerializer();

		protected Region2DSerializer() {
		}

		protected static final ConfigurationEntryCodec<Coord2D> codec_min = new ConfigurationEntryCodec<>("min", Coord2D.Coord2DSerializer.INSTANCE);
		protected static final ConfigurationEntryCodec<Coord2D> codec_max = new ConfigurationEntryCodec<>("max", Coord2D.Coord2DSerializer.INSTANCE);

		@Override
		public Region2D deserialize(Object object) {
			if (object instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) object;
				Coord2D min = codec_min.read(section);
				Coord2D max = codec_max.read(section);
				if ((min != null) && (max != null)) {
					return new Region2D(min, max);
				}
			}
			return null;
		}

		@Override
		public Object serialize(Region2D type) {
			ConfigurationSection section = new MemoryConfiguration();
			codec_min.write(section, type.getMin());
			codec_max.write(section, type.getMax());
			return section;
		}

	}

}
