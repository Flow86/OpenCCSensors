package openccsensors.common.sensor;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.IRequiresIconLoading;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.AppliedEnergisticsUtils;
import openccsensors.common.util.InventoryUtils;
import openccsensors.common.util.Mods;

public class InventorySensor extends TileSensor implements ISensor, IRequiresIconLoading, IGaugeSensor {

	private IIcon icon;
	private final String[] gaugeProperties = new String[] { "InventoryPercentFull" };

	@Override
	public boolean isValidTarget(Object target) {
		return target instanceof IInventory || (Mods.AE && AppliedEnergisticsUtils.isValidTarget(target));
	}

	@Override
	public HashMap getDetails(World world, Object obj, ChunkCoordinates sensorPos, boolean additional) {

		TileEntity tile = (TileEntity) obj;

		HashMap response = super.getDetails(tile, sensorPos);

		if (Mods.AE && AppliedEnergisticsUtils.isValidTarget(obj)) {
			response.putAll(AppliedEnergisticsUtils.getTileDetails(obj, additional));
		} else {
			response.putAll(InventoryUtils.getInventorySizeCalculations((IInventory) tile));
			if (additional) {
				response.put("Slots", InventoryUtils.invToMap((IInventory) tile));
			}
		}

		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return new String[] { "getMapData"
		/*
		 * "getBeeInfo", "getMystcraftBookInfo"
		 */
		};
	}

	@Override
	public Object callCustomMethod(World world, ChunkCoordinates location, int methodID, Object[] args, ISensorTier tier) throws Exception {

		if (args.length != 2) {
			throw new Exception("This method expects two parameters");
		}

		if (args[1] instanceof Double) {
			args[1] = ((Double) args[1]).intValue();
		}

		if (!(args[0] instanceof String) || !(args[1] instanceof Integer)) {
			throw new Exception("Incorrect parameters. It should be target name, then slot number");
		}

		String targetName = (String) args[0];
		int slot = ((Integer) args[1]) - 1;

		HashMap targets = getTargets(world, location, tier);

		switch (methodID) {
		case 0:
			return InventoryUtils.getMapData(world, targets, targetName, slot);
		case 1:
			return null;
		case 2:
			return null;
		}
		return null;
	}

	@Override
	public String getName() {
		return "inventoryCard";
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public void loadIcon(IIconRegister iconRegistry) {
		icon = iconRegistry.registerIcon("openccsensors:inventory");
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Blocks.chest);
	}

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}

}
