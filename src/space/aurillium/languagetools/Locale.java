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

public class Locale {
	
	public HashMap<String, String> hashLanguage;
	public String locale;
	
	public Locale(HashMap<String, String> language) {
		this.hashLanguage = language;
		this.locale = language.get("language.code").toLowerCase();
	}
	public Locale(String locale) throws IOException {
		this.hashLanguage = LanguageLoader.loadLanguage(locale, null, null);
		this.locale = this.hashLanguage.get("language.code").toLowerCase();
	}
	public Locale(String locale, Logger logger) throws IOException {
		this.hashLanguage = LanguageLoader.loadLanguage(locale, logger, null);
		this.locale = this.hashLanguage.get("language.code").toLowerCase();
	}
	
	public String enchantmentLevel(int level) {
		String key = LanguageKeys.enchantmentLevel(level);
		return this.hashLanguage.getOrDefault(key, key);
	}
	public String enchantmentLevel(int level, Boolean nullIfNotFound) {
		String key = LanguageKeys.enchantmentLevel(level);
		return this.hashLanguage.getOrDefault(key, nullIfNotFound ? null : key);
	}
	
	public String advancementDescription(Advancement advancement) {
		String key = LanguageKeys.advancementDescription(advancement);
		return this.hashLanguage.getOrDefault(key, key);
	}
	public String advancementDescription(Advancement advancement, Boolean nullIfNotFound) {
		String key = LanguageKeys.advancementDescription(advancement);
		return this.hashLanguage.getOrDefault(key, nullIfNotFound ? null : key);
	}
	
	// One method to rule them all
	public String translate(String key) {
		return this.hashLanguage.getOrDefault(key, key);
	}
	public String translate(String key, Boolean nullIfNotFound) {
		return this.hashLanguage.getOrDefault(key, nullIfNotFound ? null : key);
	}
	
	public String translate(ItemStack stack) {
		return this.translate(LanguageKeys.item(stack));
	}
	public String translate(Material material) {
		return this.translate(LanguageKeys.material(material));
	}
	public String translate(Block block) {
		return this.translate(LanguageKeys.material(block.getType()));
	}
	public String translate(Enchantment enchantment) {
		return this.translate(LanguageKeys.enchantment(enchantment));
	}
	public String translate(Enchantment enchantment, int level) {
		return this.translate(LanguageKeys.enchantment(enchantment)) + " " + this.translate(LanguageKeys.enchantmentLevel(level));
	}
	public String translate(Advancement advance) {
		return this.translate(LanguageKeys.advancement(advance));
	}
	public String translate(EntityType entityType) {
		return this.translate(LanguageKeys.entity(entityType));
	}
	public String translate(Entity entity) {
		return this.translate(LanguageKeys.entity(entity.getType()));
	}
	public String translate(PotionEffectType effectType) {
		return this.translate(LanguageKeys.effect(effectType));
	}
	public String translate(PotionEffect effect) {
		return this.translate(LanguageKeys.effect(effect.getType()));
	}
	public String translate(Biome biome) {
		return this.translate(LanguageKeys.biome(biome));
	}
	public String translate(Sound sound) {
		return this.translate(LanguageKeys.subtitle(sound));
	}
	
	public String translate(ItemStack stack, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.item(stack), nullIfNotFound);
	}
	public String translate(Material material, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.material(material), nullIfNotFound);
	}
	public String translate(Block block, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.material(block.getType()), nullIfNotFound);
	}
	public String translate(Enchantment enchantment, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.enchantment(enchantment), nullIfNotFound);
	}
	public String translate(Enchantment enchantment, int level, Boolean nullIfNotFound) {
		String key1 = LanguageKeys.enchantment(enchantment);
		String key2 = LanguageKeys.enchantmentLevel(level);
		String val1 = this.hashLanguage.getOrDefault(key1, nullIfNotFound ? null : key1);
		String val2 = this.hashLanguage.getOrDefault(key2, nullIfNotFound ? null : key2);
		if (val1 != null && val2 != null) {
			return val1 + " " + val2;
		} else {
			return null;
		}
	}
	public String translate(Advancement advance, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.advancement(advance), nullIfNotFound);
	}
	public String translate(EntityType entityType, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.entity(entityType), nullIfNotFound);
	}
	public String translate(Entity entity, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.entity(entity.getType()), nullIfNotFound);
	}
	public String translate(PotionEffectType effectType, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.effect(effectType), nullIfNotFound);
	}
	public String translate(PotionEffect effect, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.effect(effect.getType()), nullIfNotFound);
	}
	public String translate(Biome biome, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.biome(biome), nullIfNotFound);
	}
	public String translate(Sound sound, Boolean nullIfNotFound) {
		return this.translate(LanguageKeys.subtitle(sound), nullIfNotFound);
	}
}
