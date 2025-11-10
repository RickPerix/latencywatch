package me.rickperix.latencywatch;

import org.bukkit.ChatColor;

public class PingEvaluator {

    public static PingFeedback evaluate(int ping) {
        if (ping < 100) {
            return new PingFeedback(ChatColor.AQUA, "which is excellent! Connection is highly responsive and ideal for competitive gameplay.");
        } else if (ping < 200) {
            return new PingFeedback(ChatColor.GREEN, "which is decent! Connection should handle most activities without noticeable delay.");
        } else if (ping < 400) {
            return new PingFeedback(ChatColor.YELLOW, "which is unstable! May experience lag, especially during fast-paced interactions.");
        } else {
            return new PingFeedback(ChatColor.RED, "which is poor! Delays and interruptions are likely, and gameplay may be significantly affected.");
        }
    }

    public static class PingFeedback {
        private final ChatColor color;
        private final String message;

        public PingFeedback(ChatColor color, String message) {
            this.color = color;
            this.message = message;
        }

        public ChatColor getColor() {
            return color;
        }

        public String getMessage() {
            return message;
        }
    }
}