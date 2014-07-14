package openccsensors.api;

import net.minecraft.util.IIcon;

public class SensorCard {

	private final ISensor sensor;
	private final ISensorTier tier;

	public SensorCard(ISensor sensor, ISensorTier tier) {
		this.sensor = sensor;
		this.tier = tier;
	}

	public ISensorTier getTier() {
		return tier;
	}

	public ISensor getSensor() {
		return sensor;
	}

	public IIcon getIconForRenderPass(int pass) {
		if (pass == 0) {
			return getSensor().getIcon();
		}
		return getTier().getIcon();
	}
}
