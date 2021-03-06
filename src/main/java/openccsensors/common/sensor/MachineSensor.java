package openccsensors.common.sensor;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import openccsensors.api.IGaugeSensor;
import openccsensors.api.IRequiresIconLoading;
import openccsensors.api.ISensor;
import openccsensors.api.ISensorTier;
import openccsensors.common.util.Ic2Utils;
import openccsensors.common.util.Mods;

public class MachineSensor extends TileSensor implements ISensor, IRequiresIconLoading, IGaugeSensor {

	private IIcon icon;

	private final String[] gaugeProperties = new String[] { "HeatPercentage", "Progress" };

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}

	@Override
	public boolean isValidTarget(Object target) {
		return (Mods.IC2 && Ic2Utils.isValidMachineTarget(target));
	}

	@Override
	public void loadIcon(IIconRegister iconRegistry) {
		icon = iconRegistry.registerIcon("openccsensors:machine");
	}

	@Override
	public HashMap getDetails(World world, Object obj, ChunkCoordinates sensorPos, boolean additional) {
		TileEntity tile = (TileEntity) obj;
		HashMap response = super.getDetails(tile, sensorPos);
		if (Mods.IC2) {
			response.putAll(Ic2Utils.getMachineDetails(world, obj, additional));
		}
		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return null;
	}

	@Override
	public Object callCustomMethod(World world, ChunkCoordinates location, int methodID, Object[] args, ISensorTier tier) throws Exception {
		return null;
	}

	@Override
	public String getName() {
		return "machineCard";
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.redstone);
	}

}
