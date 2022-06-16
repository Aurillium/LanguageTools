package space.aurillium.languagetools;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Translator {
	
	public HashMap<String, Locale> localeMap;
	
	public Translator(Locale ...locales) {
		localeMap = new HashMap<String, Locale>();
		add(locales);
	}
	
	@SafeVarargs
	public final void add(HashMap<String, String> ...maps) {
		for (HashMap<String, String> map : maps) {
			Locale locale = new Locale(map);
			localeMap.put(locale.locale, locale);
		}
	}
	public void add(Locale ...locales) {
		for (Locale locale : locales) {
			localeMap.put(locale.locale, locale);
		}
	}
	public void add(Logger logger, String ...localeNames) throws IOException {
		for (String localeName : localeNames) {
			Locale locale = new Locale(localeName.toLowerCase(), logger);
			localeMap.put(locale.locale, locale);
		}
	}
	public void add(String ...localeNames) throws IOException {
		for (String localeName : localeNames) {
			Locale locale = new Locale(localeName.toLowerCase());
			localeMap.put(locale.locale, locale);
		}
	}
	
	public Locale get(String localeName) {
		return this.localeMap.get(localeName);
	}
	public Boolean has(String localeName) {
		return this.localeMap.containsKey(localeName);
	}
	
	// One method to rule them all
	public String translate(String localeName, String key) {
		Locale locale = this.localeMap.get(localeName);
		if (locale != null) {
			return locale.translate(key);
		} else {
			return null;
		}
	}
	public String translate(String localeName, String key, Boolean nullIfNotFound) {
		Locale locale = this.localeMap.get(localeName);
		if (locale != null) {
			return locale.translate(key, nullIfNotFound);
		} else {
			return null;
		}
	}
	
	
	public String translate(String localeName, ItemStack stack) {
		return this.translate(localeName, LanguageKeys.item(stack));
	}
	public String translate(String localeName, Material material) {
		return this.translate(localeName, LanguageKeys.material(material));
	}
	public String translate(String localeName, Block block) {
		return this.translate(localeName, LanguageKeys.material(block.getType()));
	}
	public String translate(String localeName, Enchantment enchantment) {
		return this.translate(localeName, LanguageKeys.enchantment(enchantment));
	}
	public String translate(String localeName, Enchantment enchantment, int level) {
		return this.translate(localeName, LanguageKeys.enchantment(enchantment)) + " " + this.translate(localeName, LanguageKeys.enchantmentLevel(level));
	}
	public String translate(String localeName, Advancement advance) {
		return this.translate(localeName, LanguageKeys.advancement(advance));
	}
	public String translate(String localeName, EntityType entityType) {
		return this.translate(localeName, LanguageKeys.entity(entityType));
	}
	public String translate(String localeName, Entity entity) {
		return this.translate(localeName, LanguageKeys.entity(entity.getType()));
	}
	public String translate(String localeName, PotionEffectType effectType) {
		return this.translate(localeName, LanguageKeys.effect(effectType));
	}
	public String translate(String localeName, PotionEffect effect) {
		return this.translate(localeName, LanguageKeys.effect(effect.getType()));
	}
	public String translate(String localeName, Biome biome) {
		return this.translate(localeName, LanguageKeys.biome(biome));
	}
	public String translate(String localeName, Sound sound) {
		return this.translate(localeName, LanguageKeys.subtitle(sound));
	}
	
	public String translate(String localeName, ItemStack stack, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.item(stack), nullIfNotFound);
	}
	public String translate(String localeName, Material material, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.material(material), nullIfNotFound);
	}
	public String translate(String localeName, Block block, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.material(block.getType()), nullIfNotFound);
	}
	public String translate(String localeName, Enchantment enchantment, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.enchantment(enchantment), nullIfNotFound);
	}
	public String translate(String localeName, Enchantment enchantment, int level, Boolean nullIfNotFound) {
		String key1 = LanguageKeys.enchantment(enchantment);
		String key2 = LanguageKeys.enchantmentLevel(level);
		String val1 = this.translate(localeName, key1, nullIfNotFound);
		String val2 = this.translate(localeName, key2, nullIfNotFound);
		if (val1 != null && val2 != null) {
			return val1 + " " + val2;
		} else {
			return null;
		}
	}
	public String translate(String localeName, Advancement advance, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.advancement(advance), nullIfNotFound);
	}
	public String translate(String localeName, EntityType entityType, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.entity(entityType), nullIfNotFound);
	}
	public String translate(String localeName, Entity entity, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.entity(entity.getType()), nullIfNotFound);
	}
	public String translate(String localeName, PotionEffectType effectType, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.effect(effectType), nullIfNotFound);
	}
	public String translate(String localeName, PotionEffect effect, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.effect(effect.getType()), nullIfNotFound);
	}
	public String translate(String localeName, Biome biome, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.biome(biome), nullIfNotFound);
	}
	public String translate(String localeName, Sound sound, Boolean nullIfNotFound) {
		return this.translate(localeName, LanguageKeys.subtitle(sound), nullIfNotFound);
	}
}
