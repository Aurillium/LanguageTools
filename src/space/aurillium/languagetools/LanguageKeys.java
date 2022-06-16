package space.aurillium.languagetools;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class LanguageKeys {
	public static String item(ItemStack stack) {
		Material material = stack.getType();
		NamespacedKey identifier = material.getKey();
		String key;
		if (material.isBlock()) {
			key = "block.";
		} else {
			key = "item.";
		}
		key += identifier.getNamespace() + "." + identifier.getKey();
		if (
				material == Material.POTION ||
				material == Material.SPLASH_POTION ||
				material == Material.LINGERING_POTION ||
				material == Material.TIPPED_ARROW) {
			key += ".effect.";
			PotionMeta potion = (PotionMeta)(stack.getItemMeta());
			PotionType effect = potion.getBasePotionData().getType();
			switch (effect) {
			case WATER:
				key += "water";
				break;
			case AWKWARD:
				key += "awkward";
				break;
			case MUNDANE:
				key += "mundane";
				break;
			case THICK:
				key += "thick";
				break;
			case UNCRAFTABLE:
				key += "empty";
				break;
			default:
				key += effect.getEffectType().getKey().getKey();
				break;
			}
		}
		return key;
	}
	
	public static String material(Material material) {
		String key;
		if (material.isBlock()) {
			key = "block.";
		} else {
			key = "item.";
		}
		NamespacedKey identifier = material.getKey();
		key += identifier.getNamespace() + "." + identifier.getKey();
		return key;
	}
	
	public static String enchantment(Enchantment enchant) {
		NamespacedKey identifier = enchant.getKey();
		return "enchantment." + identifier.getNamespace() + "." + identifier.getKey();
	}
	public static String enchantmentLevel(int level) {
		return "enchantment.level." + Integer.toString(level);
	}
	public static String advancement(Advancement advance) {
		NamespacedKey identifier = advance.getKey();
		return "advancements." + identifier.getKey().replace('/', '.') + ".name";
	}
	public static String advancementDescription(Advancement advance) {
		NamespacedKey identifier = advance.getKey();
		return "advancements." + identifier.getKey().replace('/', '.') + ".description";
	}
	public static String entity(EntityType entityType) {
		NamespacedKey identifier = entityType.getKey();
		return "entity." + identifier.getNamespace() + "." + identifier.getKey();
	}
	public static String effect(PotionEffectType effectType) {
		NamespacedKey identifier = effectType.getKey();
		return "effect." + identifier.getNamespace() + "." + identifier.getKey();
	}
	public static String biome(Biome biome) {
		NamespacedKey identifier = biome.getKey();
		return "biome." + identifier.getNamespace() + "." + identifier.getKey();
	}
	public static String subtitle(Sound sound) {
		NamespacedKey identifier = sound.getKey();
		return "subtitles." + identifier.getKey();
	}
}
