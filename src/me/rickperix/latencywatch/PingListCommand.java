package me.rickperix.latencywatch;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.ArrayList;

public class PingListCommand implements CommandExecutor {

    private static final int PAGE_SIZE = 5;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Main.getInstance().getConfig().getBoolean("latencywatch.enabled")) {
            sender.sendMessage(ChatColor.RED + "LatencyWatch is currently disabled.");
            return true;
        }

        List<String> excluded = Main.getInstance().getConfig().getStringList("latencywatch.excluded-players");

        int page = 1;
        if (args.length == 1) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid page number!");
                return true;
            }
        }

        List<Player> visiblePlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!excluded.contains(player.getName())) {
                visiblePlayers.add(player);
            }
        }

        int totalPages = (int) Math.ceil(visiblePlayers.size() / (double) PAGE_SIZE);
        if (page < 1 || page > totalPages) {
            sender.sendMessage(ChatColor.RED + "Page " + page + " does not exist!");
            return true;
        }

        sender.sendMessage(ChatColor.WHITE + "Online players' ping (Page " + page + "/" + totalPages + "):");

        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, visiblePlayers.size());

        for (int i = start; i < end; i++) {
            Player player = visiblePlayers.get(i);
            int ping = player.getPing();
            PingEvaluator.PingFeedback feedback = PingEvaluator.evaluate(ping);

            sender.sendMessage(feedback.getColor() + "- " + player.getName() + ": " + ping + " ms, " + feedback.getMessage());
        }

        if (page < totalPages) {
            sender.sendMessage(ChatColor.WHITE + "Execute " + ChatColor.GREEN + "/pinglist " + ChatColor.GREEN + (page + 1) + ChatColor.WHITE + " for the rest of the list.");
        }

        return true;
    }
}