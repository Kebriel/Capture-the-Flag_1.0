package kebriel.ctf.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class ItemBuilder {
	
	private ItemStack item;
	
	public ItemBuilder(Material mat) {
		this.item = new ItemStack(mat);
	}
	
	public ItemBuilder(ItemStack stack) {
		this.item = stack;
	}
	
	public ItemBuilder(ItemStack stack, String name) {
		this.item = stack;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}
	
	@SuppressWarnings("deprecation")
	public ItemBuilder(ItemStack stack, String name, byte data) {
		this.item = stack;
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		MaterialData mdata = stack.getData();
		mdata.setData(data);
		stack.setData(mdata);
	}
	
	public ItemBuilder(Material mat, String displayName) {
		this.item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}
	
	public ItemBuilder(Material mat, String displayName, byte data) {
		this.item = new ItemStack(mat, 1, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}
	
	public ItemBuilder addLore(String line) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.getLore() == null) {
			lore = new ArrayList<String>();
		}else {
			lore = meta.getLore();
		}
		lore.add(line);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setLore(int index, String newLine) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.getLore() == null) {
			lore = new ArrayList<String>();
		}else {
			lore = meta.getLore();
		}
		lore.set(index, newLine);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setUnreakable() {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment ench, int lvl) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, lvl, true);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder hideAttributes() {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addFlag(ItemFlag flag) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setAttackDamage(double damage) {
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		 NBTTagCompound compound = nmsStack.getTag();
	        if (compound == null) {
	           compound = new NBTTagCompound();
	            nmsStack.setTag(compound);
	            compound = nmsStack.getTag();
	        }
	        NBTTagList modifiers = new NBTTagList();
	        NBTTagCompound healthboost = new NBTTagCompound();
	        healthboost.set("AttributeName", new NBTTagString("generic.attackDamage"));
	        healthboost.set("Name", new NBTTagString("generic.attackDamage"));
	        healthboost.set("Amount", new NBTTagDouble(damage));
	        healthboost.set("Operation", new NBTTagInt(0));
	        healthboost.set("UUIDLeast", new NBTTagInt(894654));
	        healthboost.set("UUIDMost", new NBTTagInt(2872));
	        modifiers.add(healthboost);
	        compound.set("AttributeModifiers", modifiers);
	        nmsStack.setTag(compound);
	        item = CraftItemStack.asBukkitCopy(nmsStack);
	        return this;
	}
	
	public ItemBuilder dyeLeather(Color color) {
		  LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		  meta.setColor(Color.fromRGB(color.asRGB()));
		  item.setItemMeta(meta);
		  return this;
	}
	
	public ItemBuilder addAffectToPotion(PotionEffectType type, int strength, int duration, boolean splash) {
		PotionEffect effect = new PotionEffect(type, duration, strength, true, false);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(effect, true);
		item.setItemMeta(meta);
		if(splash) {
			Potion potion = Potion.fromItemStack(item);
			potion.setSplash(true);
			item = potion.toItemStack(1);
		}
		return this;
	}
	
	public ItemBuilder addColorToPot(PotionType type) {
		Potion potion = Potion.fromItemStack(item);
		potion.setType(type);
		item = potion.toItemStack(1);
		return this;
	}
	
	public ItemBuilder setPotionAsSplash() {
		Potion potion = Potion.fromItemStack(item);
		potion.setSplash(true);
		item = potion.toItemStack(1);
		return this;
	}
	
	public ItemBuilder getSkull(Player p) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(p.getName());
		item.setItemMeta(meta);
		return this;
	}
	
	
	public ItemStack toItem() {
		return item;
	}

}
