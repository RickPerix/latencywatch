package me.rickperix.latencywatch;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Main.getInstance().getConfig().getBoolean("latencywatch.enabled")) {
            sender.sendMessage(ChatColor.RED + "LatencyWatch is currently disabled.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /ping <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found or not online!");
            return true;
        }

        List<String> excluded = Main.getInstance().getConfig().getStringList("latencywatch.excluded-players");
        if (excluded.contains(target.getName())) {
            sender.sendMessage(ChatColor.RED + "Ping information for this player is excluded!");
            return true;
        }

        int ping = target.getPing();
        PingEvaluator.PingFeedback feedback = PingEvaluator.evaluate(ping);

        sender.sendMessage(feedback.getColor() + target.getName() + "'s ping is " + ping + " ms, " + feedback.getMessage());
        return true;
    }
}