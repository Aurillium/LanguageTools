package space.aurillium.languagetools;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Main extends JavaPlugin {
	public static JavaPlugin plugin;
	private static Logger logger;
	private static HashMap<String, String> hashLanguage;
	public static Locale mainLocale;
	public static Translator translator;
	
	//public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
	
	@Override
	public void onEnable() {
		long started = new Date().getTime();
		
		plugin = JavaPlugin.getPlugin(Main.class);
		logger = this.getLogger();
		
		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();
		
		String version = Bukkit.getBukkitVersion().split("-")[0];
		String language = config.getString("language").toLowerCase();
		
		logger.info("Language '" + language + "' on version '" + version + "'.");
		
		try {
			hashLanguage = LanguageLoader.loadMinecraftLanguage(language, logger);
			if (hashLanguage == null) {
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Due to the above error the plugin could not be loaded.");
			Bukkit.getPluginManager().disablePlugin(plugin);
			return;
		}
		logger.info("Looking for resource pack...");
		
		try {
			HashMap<String, String> resourceMap = LanguageLoader.loadResourcePackLanguage(language, logger);
			if (resourceMap != null) {
				hashLanguage.putAll(resourceMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warning("Could not read resource pack language due to the above error.");
		}
		
		logger.info("Main locale loaded.");
		
		mainLocale = new Locale(hashLanguage);
		
		logger.info("Loading preload languages...");
		List<String> preloads = config.getStringList("preload");
		translator = new Translator();
		for (String preload : preloads) {
			try {
				logger.info("Loading '" + preload + "' on version '" + version + "'.");
				translator.add(LanguageLoader.loadLanguage(preload, logger, false));
			} catch (IOException e) {
				logger.warning("'" + preload + "' could not be loaded.");
				e.printStackTrace();
			}
		}
		logger.info("Preloads finished.");
		
		logger.info("Plugin loaded! (" + Long.toString(new Date().getTime() - started) + "ms)");
		
		/*//
		// Tests
		logger.info(mainLocale.enchantmentLevel(10));
		logger.info(mainLocale.translate(Enchantment.PROTECTION_FALL));
		logger.info(mainLocale.translate(PotionEffectType.FAST_DIGGING));
		logger.info(mainLocale.translate(Material.AXOLOTL_BUCKET));
		logger.info(mainLocale.translate(EntityType.MUSHROOM_COW));
		logger.info(mainLocale.translate(EntityType.RABBIT));
		logger.info(mainLocale.translate(Biome.OLD_GROWTH_PINE_TAIGA));
		logger.info(mainLocale.translate(Sound.BLOCK_BEEHIVE_EXIT));
		logger.info(mainLocale.translate(plugin.getServer().getWorld("world").getBlockAt(0, 0, 0)));
		logger.info(mainLocale.translate("deathScreen.quit.confirm"));
		ItemStack stack1 = new ItemStack(Material.DIAMOND_BLOCK, 12);
		ItemStack stack2 = new ItemStack(Material.AXOLOTL_BUCKET, 1);
		ItemStack stack3 = new ItemStack(Material.POTION, 1);
		ItemStack stack4 = new ItemStack(Material.TIPPED_ARROW, 54);
		logger.info(mainLocale.translate(stack1));
		logger.info(mainLocale.translate(stack2));
		logger.info(mainLocale.translate(stack3));
		logger.info(mainLocale.translate(stack4));
		PotionMeta potion = (PotionMeta)(stack4.getItemMeta());
		PotionData data = new PotionData(PotionType.REGEN, false, true);
		potion.setBasePotionData(data);
		stack3.setItemMeta(potion);
		stack4.setItemMeta(potion);
		logger.info(mainLocale.translate(stack3));
		logger.info(mainLocale.translate(stack4));
		try {
			Locale test1 = new Locale(LanguageLoader.loadMinecraftLanguage("lol_us", logger));
			HashMap<String, String> test2 = LanguageLoader.loadMinecraftLanguage("en_gb", logger);
			//Translator translator = new Translator(test1);
			translator.add(test2);
			translator.add(logger, "en_us");
			translator.add(logger, "en_gb");
			
			for (String key : translator.localeMap.keySet()) {
				logger.info(key);
			}
			
			// Now get the size of some locales
			logger.info(String.valueOf(translator.localeMap.size()));
			
			logger.info(translator.translate("lol_us", stack1));
			logger.info(translator.translate("en_us", stack1));
			logger.info(translator.translate("en_gb", stack1));
			
			logger.info(translator.translate("lol_us", stack2));
			logger.info(translator.translate("en_us", stack2));
			logger.info(translator.translate("en_gb", stack2));
			
			logger.info(translator.translate("lol_us", stack3));
			logger.info(translator.translate("en_us", stack3));
			logger.info(translator.translate("en_gb", stack3));
			
			logger.info(translator.translate("lol_us", stack4));
			logger.info(translator.translate("en_us", stack4));
			logger.info(translator.translate("en_gb", stack4));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
	}
	
	public String getPlayerLocale(Player player) {
		try {
			Method getLocale = player.getClass().getDeclaredMethod("getLocale");
			getLocale.setAccessible(true);
			return (String)getLocale.invoke(player);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
			logger.severe("Error when getting player locale:");
			logger.severe(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
}
