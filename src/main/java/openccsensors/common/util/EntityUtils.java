package openccsensors.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntityUtils {

	public static HashMap<String, Entity> getEntities(World world, ChunkCoordinates location, double radius, Class filter) {
		HashMap<String, Entity> map = new HashMap<String, Entity>();

		int dChunk = (int) Math.ceil(radius / 16.0F);

		int x = location.posX;
		int y = location.posY;
		int z = location.posZ;

		for (int dx = -dChunk; dx <= dChunk; dx++) {
			for (int dz = -dChunk; dz <= dChunk; dz++) {
				Chunk chunk = world.getChunkFromBlockCoords(x + 16 * dx, z + 16 * dz);
				for (List<Entity> subchunk : chunk.entityLists) {
					for (Entity entity : subchunk) {
						Vec3 livingPos = Vec3.createVectorHelper(entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5);
						if ((Vec3.createVectorHelper(location.posX, location.posY, location.posZ)).distanceTo(livingPos) <= radius
								&& filter.isAssignableFrom(entity.getClass())) {
							String targetName = (entity instanceof EntityPlayer) ? entity.getCommandSenderName() : entity.getCommandSenderName()
									+ entity.getEntityId();
							targetName = targetName.replaceAll("\\s", "");
							map.put(targetName, entity);
						}
					}
				}
			}
		}

		return map;
	}

	public static HashMap livingToMap(EntityLivingBase living, ChunkCoordinates sensorPos, boolean additional) {
		HashMap map = new HashMap();

		HashMap position = new HashMap();
		position.put("X", living.posX - sensorPos.posX);
		position.put("Y", living.posY - sensorPos.posY);
		position.put("Z", living.posZ - sensorPos.posZ);
		map.put("Position", position);

		map.put("Name", (living instanceof EntityPlayer) ? "Player" : living.getCommandSenderName());
		map.put("RawName", living.getClass().getName());
		map.put("IsPlayer", living instanceof EntityPlayer);

		if (additional) {

			map.put("HeldItem", InventoryUtils.itemstackToMap(living.getHeldItem()));

			HashMap armour = new HashMap();
			armour.put("Boots", InventoryUtils.itemstackToMap(living.getEquipmentInSlot(1)));
			armour.put("Leggings", InventoryUtils.itemstackToMap(living.getEquipmentInSlot(2)));
			armour.put("Chestplate", InventoryUtils.itemstackToMap(living.getEquipmentInSlot(3)));
			armour.put("Helmet", InventoryUtils.itemstackToMap(living.getEquipmentInSlot(4)));

			map.put("Armour", armour);
			map.put("Health", living.getHealth());
			map.put("IsAirborne", living.isAirBorne);
			map.put("IsBurning", living.isBurning());
			map.put("IsAlive", living.isEntityAlive());
			map.put("IsInWater", living.isInWater());
			map.put("IsOnLadder", living.isOnLadder());
			map.put("IsSleeping", living.isPlayerSleeping());
			map.put("IsRiding", living.isRiding());
			map.put("IsSneaking", living.isSneaking());
			map.put("IsSprinting", living.isSprinting());
			map.put("IsWet", living.isWet());
			map.put("IsChild", living.isChild());
			map.put("Yaw", living.rotationYaw);
			map.put("Pitch", living.rotationPitch);
			map.put("YawHead", living.rotationYawHead);

			HashMap potionEffects = new HashMap();
			Collection<PotionEffect> effects = living.getActivePotionEffects();
			int count = 1;
			for (PotionEffect effect : effects) {
				potionEffects.put(count, effect.getEffectName());
				count++;
			}
			map.put("PotionEffects", potionEffects);
		}

		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			map.put("Username", player.getCommandSenderName());
			map.put("IsBlocking", player.isBlocking());
			map.put("ExperienceTotal", player.experienceTotal);
			map.put("ExperienceLevel", player.experienceLevel);
			map.put("Experience", player.experience);
			if (additional) {
				map.put("FoodLevel", player.getFoodStats().getFoodLevel());
				map.put("Gamemode", player.capabilities.isCreativeMode);
				map.put("Inventory", InventoryUtils.invToMap(player.inventory));

				Vec3 posVec = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + 1.62F, player.posZ);
				Vec3 lookVec = player.getLook(1.0f);
				Vec3 targetVec = posVec.addVector(lookVec.xCoord * 10f, lookVec.yCoord * 10f, lookVec.zCoord * 10f);
				MovingObjectPosition mop = player.worldObj.rayTraceBlocks(posVec, targetVec);
				map.put("IsLookingAtBlock", mop.typeOfHit == MovingObjectType.ENTITY);
				if (mop.typeOfHit == MovingObjectType.ENTITY) {
					// int blockId = player.worldObj.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
					HashMap lookingAt = new HashMap();
					lookingAt.put("X", mop.blockX - sensorPos.posX);
					lookingAt.put("Y", mop.blockY - sensorPos.posY);
					lookingAt.put("Z", mop.blockZ - sensorPos.posZ);
					map.put("LookingAt", lookingAt);
				}
			}

			map.put("Experience", player.experience);
		}

		if (living instanceof EntityTameable) {
			EntityTameable tameable = (EntityTameable) living;
			map.put("IsSitting", tameable.isSitting());
			map.put("IsTamed", tameable.isTamed());
			if (tameable.isTamed()) {
				map.put("OwnerName", tameable.getOwnerName());
			}
			if (tameable instanceof EntityWolf) {
				EntityWolf wolf = (EntityWolf) tameable;
				map.put("IsAngry", wolf.isAngry());
				if (((EntityTameable) wolf).isTamed()) {
					map.put("CollarColor", wolf.getCollarColor());
				}
			}
		}

		return map;
	}
}