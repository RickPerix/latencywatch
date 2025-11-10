package me.rickperix.latencywatch;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main extends JavaPlugin {

    private static final String CURRENT_VERSION = "1.2";
    private static final String SPIGOT_ID = "130021";
    private static final int BSTATS_PLUGIN_ID = 27870;

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        copyLicenseFile();
        copyReadmeFile();
        getServer().getPluginManager().registerEvents(new JoinVersionListener(this), this);

        getCommand("ping").setExecutor(new PingCommand());
        getCommand("pinglist").setExecutor(new PingListCommand());
        getCommand("latencywatchreload").setExecutor(new ReloadCommand());

        logBanner();
        checkForUpdates();
        new Metrics(this, BSTATS_PLUGIN_ID);
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        getLogger().info("LatencyWatch configuration reloaded successfully!");
    }

    private void copyLicenseFile() {
        File target = new File(getDataFolder(), "LICENSE.txt");
        if (!target.exists()) {
            InputStream resource = getResource("LICENSE.txt");
            if (resource != null) {
                saveResource("LICENSE.txt", true);
                getLogger().info("LICENSE.txt successfully copied to plugin folder.");
            } else {
                getLogger().warning("LICENSE.txt not found inside plugin jar.");
            }
        }
    }

    private void copyReadmeFile() {
        File target = new File(getDataFolder(), "README.txt");
        if (!target.exists()) {
            InputStream resource = getResource("README.txt");
            if (resource != null) {
                saveResource("README.txt", false);
                getLogger().info("README.txt successfully copied to plugin folder.");
            } else {
                getLogger().warning("README.txt not found inside plugin jar.");
            }
        }
    }

    private void logBanner() {
        getLogger().info(" ");
        getLogger().info(".____            __                               __      __         __         .__     ");
        getLogger().info("|    |   _____ _/  |_  ____   ____   ____ ___.__./  \\    /  \\_____ _/  |_  ____ |  |__  ");
        getLogger().info("|    |   \\__  \\\\   __\\/ __ \\ /    \\_/ ___<   |  |\\   \\/\\/   /\\__  \\\\   __\\/ ___\\|  |  \\ ");
        getLogger().info("|    |___ / __ \\|  | \\  ___/|   |  \\  \\___\\___  | \\        /  / __ \\|  | \\  \\___|   Y  \\");
        getLogger().info("|_______ (____  /__|  \\___  >___|  /\\___  > ____|  \\__/\\  /  (____  /__|  \\___  >___|  /");
        getLogger().info("        \\/    \\/          \\/     \\/     \\/\\/            \\/        \\/          \\/     \\/ ");
        getLogger().info("==========================================================================================");
        getLogger().info("                                       By RickPerix                                       ");
        getLogger().info("                                 Successfully Activated!!                                 ");
        getLogger().info("==========================================================================================");
        getLogger().info(" ");
    }

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                URL url = new URL("https://api.spiget.org/v2/resources/" + SPIGOT_ID + "/versions/latest");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "LatencyWatch/" + CURRENT_VERSION);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(response.toString());
                String latestVersion = (String) obj.get("name");

                if (latestVersion == null || latestVersion.isEmpty()) {
                    getLogger().warning("Could not retrieve latest version info.");
                } else if (!CURRENT_VERSION.equals(latestVersion)) {
                    getLogger().warning("====================================");
                    getLogger().warning(" NEW UPDATE AVAILABLE!");
                    getLogger().warning(" Current version: " + CURRENT_VERSION);
                    getLogger().warning(" Latest version: " + latestVersion);
                    getLogger().warning(" Download: https://spigotmc.org/resources/" + SPIGOT_ID);
                    getLogger().warning("====================================");
                } else {
                    getLogger().info("You're using the latest version!");
                }

            } catch (Exception e) {
                getLogger().warning("Update check failed: " + e.getMessage());
            }
        });
    }
}