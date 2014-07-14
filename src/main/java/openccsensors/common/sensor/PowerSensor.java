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
import openccsensors.common.util.BuildcraftUtils;
import openccsensors.common.util.Ic2Utils;
import openccsensors.common.util.InventoryUtils;
import cpw.mods.fml.common.Loader;

public class PowerSensor extends TileSensor implements ISensor, IRequiresIconLoading, IGaugeSensor {

	private IIcon icon;
	private final String[] gaugeProperties = new String[] { "PowerPercentFull" };

	Class UEApi = null;

	public PowerSensor() {
		try {
			UEApi = Class.forName("universalelectricity.core.block.IElectrical");
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public boolean isValidTarget(Object target) {
		if (!(target instanceof TileEntity)) {
			return false;
		}
		if ("unknown" == InventoryUtils.getRawNameForStack(new ItemStack(((TileEntity) target).getBlockType(), 1, ((TileEntity) target).getBlockMetadata()))) {
			return false;
		}
		return ((Loader.isModLoaded("IC2") && Ic2Utils.isValidPowerTarget(target)) || (Loader.isModLoaded("BuildCraft|Core") && BuildcraftUtils
				.isValidPowerTarget(target)));
	}

	@Override
	public HashMap getDetails(World world, Object obj, ChunkCoordinates sensorPos, boolean additional) {
		HashMap response = super.getDetails((TileEntity) obj, sensorPos);
		if (Loader.isModLoaded("IC2")) {
			response.putAll(Ic2Utils.getPowerDetails(world, obj, additional));
		}
		if (Loader.isModLoaded("BuildCraft|Core")) {
			response.putAll(BuildcraftUtils.getPowerDetails(world, obj, additional));
		}
		return response;
	}

	@Override
	public String[] getCustomMethods(ISensorTier tier) {
		return null;
	}

	@Override
	public Object callCustomMethod(World world, ChunkCoordinates location, int methodID, Object[] args, ISensorTier tier) {
		return null;
	}

	@Override
	public String getName() {
		return "powerCard";
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public ItemStack getUniqueRecipeItem() {
		return new ItemStack(Items.coal);
	}

	@Override
	public void loadIcon(IIconRegister iconRegistry) {
		icon = iconRegistry.registerIcon("openccsensors:power");
	}

	@Override
	public String[] getGaugeProperties() {
		return gaugeProperties;
	}
}
