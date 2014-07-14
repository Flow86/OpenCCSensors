package openccsensors.common.util;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;

public class AppliedEnergisticsUtils {

	private static final String ME_INTERFACE_CLASS = "appeng.tile.misc.TileInterface";

	public static boolean isValidTarget(Object target) {
		return target != null && target.getClass().getName() == ME_INTERFACE_CLASS;
	}

	public static HashMap getTileDetails(Object obj, boolean additional) {
		HashMap response = new HashMap();

		if (!(obj instanceof TileEntity)) {
			return response;
		}

		response.put("Powered", false);

		IGridHost gh = (IGridHost) obj;

		if (gh == null)
			return response;

		IGridNode gn = gh.getGridNode(ForgeDirection.UNKNOWN);

		if (gn == null)
			return response;

		IGrid gg = gn.getGrid();

		if (gg == null)
			return response;

		IEnergyGrid eg = gg.getCache(IEnergyGrid.class);

		if (eg != null) {
			response.put("Powered", eg.isNetworkPowered());
			response.put("StoredPower", eg.getStoredPower());
			response.put("MaxStoredPower", eg.getMaxStoredPower());
			response.put("AvgPowerProduction", eg.getAvgPowerInjection());
			response.put("AvgPowerUsage", eg.getAvgPowerUsage());
			response.put("IdlePowerUsage", eg.getIdlePowerUsage());
		}

		IStorageGrid sg = gg.getCache(IStorageGrid.class);
		if (sg != null) {
			IMEMonitor<IAEItemStack> im = sg.getItemInventory();
			IItemList<IAEItemStack> il = im.getStorageList();

			HashMap stacks = new HashMap();

			int i = 0;
			for (IAEItemStack is : il) {
				i++;
				stacks.put(i, InventoryUtils.itemstackToMap(is.getItemStack()));
			}

			response.put("Slots", stacks);
		}

		/*
		 * ICellRegistry cellRegistry = AEApi.instance().registries(). IMEInventoryHandler<IAEItemStack> invHandler = cellRegistry.getCellInventory(itemStack,
		 * StorageChannel.ITEMS);
		 * 
		 * 
		 * IWirelessAccessPoint wap = (IWirelessAccessPoint) obj; IPowerChannelState pcs = (IPowerChannelState)obj;
		 * 
		 * if (wap != null && pcs != null && pcs.isPowered()) {
		 * 
		 * ICellInventoryHandler imivh = wap.getGrid()..getFullCellArray(); ICellInventory imiv = imivh.getCellInv(); response.put("Powered", true);
		 * 
		 * // user IMEInventory for remaining item types and count. response.put("FreeTypes", (int) imiv.getRemainingItemTypes()); response.put("FreeCount",
		 * (int) imiv.getRemainingItemCount()); response.put("FreeBytes", (int) imiv.getFreeBytes()); response.put("UsedBytes", (int) imiv.getUsedBytes());
		 * response.put("TotalBytes", (int) imiv.getTotalBytes());
		 * 
		 * long totalBytes = imiv.getTotalBytes();
		 * 
		 * double percent = (double) 100 / totalBytes * imiv.getUsedBytes(); percent = Math.max(Math.min(percent, 100), 0); response.put("InventoryPercentFull",
		 * percent);
		 * 
		 * if (additional) { IItemList<IAEItemStack> list = imiv.getAvailableItems(AEApi.instance().storage().createItemList());
		 * 
		 * HashMap stacks = new HashMap();
		 * 
		 * int i = 0; for (IAEItemStack stack : list) { i++; stacks.put(i, InventoryUtils.itemstackToMap(stack.getItemStack())); }
		 * 
		 * response.put("UsedTypes", imiv.getStoredItemTypes()); response.put("UsedCount", imiv.getStoredItemCount()); response.put("Slots", stacks);
		 * 
		 * response.put("CanHoldNewItems", imiv.canHoldNewItem()); response.put("Priority", imivh.getPriority()); //response.put("SystemPower",
		 * gi.getPowerUsageAvg()); } gi.triggerPowerUpdate();
		 * 
		 * }
		 */

		return response;
	}
}
