package me.rickperix.latencywatch;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("latencywatch.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to reload LatencyWatch!");
            return true;
        }

        Main.getInstance().reloadPluginConfig();
        sender.sendMessage(ChatColor.GREEN + "LatencyWatch configuration reloaded successfully!");
        return true;
    }
}