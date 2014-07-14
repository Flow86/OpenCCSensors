package openccsensors.common.item.meta;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import openccsensors.OpenCCSensors;
import openccsensors.api.IItemMeta;
import openccsensors.api.IRequiresIconLoading;

public class ItemMetaAdvancedAmplifier implements IItemMeta, IRequiresIconLoading {

	private final int id;
	private IIcon icon;

	@SuppressWarnings("unchecked")
	public ItemMetaAdvancedAmplifier(int id) {
		this.id = id;

		OpenCCSensors.Items.genericItem.addMeta(this);

		CraftingManager
				.getInstance()
				.getRecipeList()
				.add(new ShapedOreRecipe(newItemStack(1), new Object[] { "igi", "rdr", "igi", Character.valueOf('i'), new ItemStack(Items.iron_ingot),
						Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('r'), new ItemStack(Items.redstone), Character.valueOf('d'),
						new ItemStack(Items.diamond), }));
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean displayInCreative() {
		return true;
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public void loadIcon(IIconRegister iconRegistry) {
		icon = iconRegistry.registerIcon("openccsensors:advancedAmplifier");
	}

	@Override
	public ItemStack newItemStack(int size) {
		return new ItemStack(OpenCCSensors.Items.genericItem, size, getId());
	}

	@Override
	public String getName() {
		return "advancedAmplifier";
	}
}
