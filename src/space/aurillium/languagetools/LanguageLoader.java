package space.aurillium.languagetools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LanguageLoader {
	private static String pluginLangDir = "lang/";
	
	private static Path tempResourcePack = null;
	private static JSONObject assetObjects = null;
	
	public static HashMap<String, String> loadMinecraftLanguage(String language, Logger logger) throws IOException {
		String version = Bukkit.getBukkitVersion().split("-")[0];
		Path languagesDir = Path.of(Main.plugin.getDataFolder().getAbsolutePath(), pluginLangDir);
		
		HashMap<String, String> map = new HashMap<String, String>();
	
		if (!Files.exists(languagesDir)) {
			Files.createDirectory(languagesDir);
		}
		Path languageFile = languagesDir.resolve(language + "-" + version + ".json");
		JSONObject jsonLanguage;
		if (Files.exists(languageFile)) {
			if (logger != null) logger.info("Pre-existing language file found. Using that...");
			jsonLanguage = new JSONObject(Files.readString(languageFile));
			for (String key : jsonLanguage.keySet()) {
				map.put(key, jsonLanguage.getString(key));
			}
			if (logger != null) logger.info("Loaded " + language + " from local storage.");
			return map;
		} else {
			if (logger != null) logger.info("No local language file found. Checking server jar...");
			
			String resourcePath = "/assets/minecraft/lang/" + language + ".json";
			InputStream in = net.minecraft.MinecraftVersion.class.getResourceAsStream(resourcePath);
			if (in != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(in));
				jsonLanguage = new JSONObject(readAll(rd));
				for (String key : jsonLanguage.keySet()) {
					map.put(key, jsonLanguage.getString(key));
				}
				if (logger != null) {
					logger.info("Loaded " + language + " from server jar.");
					logger.info("Saving new language file...");
				}
				Files.createFile(languageFile);
				Files.writeString(languageFile, jsonLanguage.toString());
				return map;
			} else {
				//logger.info("No language file found. Downloading one from mcasset.cloud GitHub...");
				//String url = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/" + version + "/assets/minecraft/lang/" + language + ".json";
				
				if (logger != null) logger.info("Language file not in server jar. Downloading one from Minecraft...");
				
				if (assetObjects == null) {
					if (logger != null) logger.info("- Downloading manifest for " + version + "...");
					JSONObject manifest = readJsonFromURL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
					JSONArray versions = manifest.getJSONArray("versions");
					String launchMetaURL = null;
					int versionsLength = versions.length();
					for (int i = 0; i < versionsLength; i++) {
						JSONObject jsonVersion = versions.getJSONObject(i);
						if (jsonVersion.getString("id").equals(version)) {
							launchMetaURL = jsonVersion.getString("url");
							break;
						}
					}
					String assetIndexURL;
					if (launchMetaURL != null) {
						if (logger != null) logger.info("- Downloading asset index for " + version + "...");
						assetIndexURL = readJsonFromURL(launchMetaURL).getJSONObject("assetIndex").getString("url");
					} else {
						if (logger != null) logger.severe("Version '" + version + "' not found. Plugin failed to load.");
						return null;
					}
					
					JSONObject assetIndex = readJsonFromURL(assetIndexURL);
					assetObjects = assetIndex.getJSONObject("objects");
				}
				
				String langKey = "minecraft/lang/" + language + ".json";
				if (!assetObjects.has(langKey)) {
					if (logger != null) logger.severe("Cannot start plugin as language does not exist in this Minecraft version and there is no local version.");
					return null;
				}
				
				if (logger != null) logger.info("- Downloading " + language + ".json for " + version + "...");
				String langHash = assetObjects.getJSONObject(langKey).getString("hash");
				String url = "http://resources.download.minecraft.net/" + langHash.substring(0, 2) + "/" + langHash;
				jsonLanguage = readJsonFromURL(url);
				for (String key : jsonLanguage.keySet()) {
					map.put(key, jsonLanguage.getString(key));
				}
				if (logger != null) {
					logger.info("Loaded " + language + " from Minecraft.");
					logger.info("Saving new language file...");
				}
				Files.createFile(languageFile);
				Files.writeString(languageFile, jsonLanguage.toString());
				return map;
			}
		}
	}
	
	public static HashMap<String, String> loadResourcePackLanguage(String language, Logger logger) throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		
		if (tempResourcePack == null) {
			String resourcePackLocation;
			Server server = Bukkit.getServer();
			try {
				Method getResourcePack = server.getClass().getDeclaredMethod("getResourcePack");
				getResourcePack.setAccessible(true);
				resourcePackLocation = (String)getResourcePack.invoke(server);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
				if (logger == null) {
					logger = Main.plugin.getLogger();
				}
				logger.severe("Error getting server resource pack::");
				logger.severe(ExceptionUtils.getStackTrace(e));
				return null;
			}
			
			if (StringUtils.isEmpty(resourcePackLocation)) {
				if (logger != null) logger.info("No resource pack found.");
				return null;
			}
			
			tempResourcePack = Files.createTempFile(null, null);
			InputStream in = new URL(resourcePackLocation).openStream();
			Files.copy(in, tempResourcePack, StandardCopyOption.REPLACE_EXISTING);
		}
		
		FileSystem fileSystem = FileSystems.newFileSystem(tempResourcePack);
		Path jsonPath = fileSystem.getPath("/assets/minecraft/lang/" + language + ".json");
		if (Files.exists(jsonPath)) {
			JSONObject jsonRPLanguage = new JSONObject(Files.readString(jsonPath));
			for (String key : jsonRPLanguage.keySet()) {
				map.put(key.toLowerCase(), jsonRPLanguage.getString(key));
			}
			if (logger != null) logger.info("Loaded JSON language file.");
			return map;
		} else {
			if (logger != null) logger.info("Required language file not found.");
			return null;
		}
	}
	
	public static HashMap<String, String> loadLanguage(String language, Logger logger, Boolean requiresResourcePack) throws IOException {
		HashMap<String, String> locale = loadMinecraftLanguage(language, logger);
		if (locale == null) {
			return null;
		}
		HashMap<String, String> resourceLocale = loadResourcePackLanguage(language, logger);
		if (resourceLocale == null) {
			if (requiresResourcePack != null) {
				if (requiresResourcePack) {
					return null;
				} else {
					return locale;
				}
			} else {
				return locale;
			}
		}
		locale.putAll(resourceLocale);
		return locale;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	    }
	    return sb.toString();
	}

	private static JSONObject readJsonFromURL(String url) throws IOException, JSONException, MalformedURLException {
		InputStream is;
		is = new URL(url).openStream();
	    try {
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        String jsonText = readAll(rd);
	        JSONObject json = new JSONObject(jsonText);
	        is.close();
	        return json;
	    } catch (Exception e) {
	    	is.close();
	    	throw e;
	    }
	}
}
