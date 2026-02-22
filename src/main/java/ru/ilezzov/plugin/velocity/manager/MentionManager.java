package ru.ilezzov.plugin.velocity.manager;

/*
 * Copyright (C) 2024-2026 ILeZzoV
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import ru.ilezzov.plugin.velocity.Main;
import ru.ilezzov.plugin.velocity.file.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionManager {
    private static final Pattern MENTION_PATTERN = Pattern.compile("@([A-Za-z0-9_]{3,16})");

    public static MentionResult replaceMention(final ProxyServer proxyServer, String message) {
        final Matcher matcher = MENTION_PATTERN.matcher(message);

        final StringBuilder result = new StringBuilder(message.length());
        final List<String> players = new ArrayList<>();
        final Config.MentionSection mention = Main.getConfig().mention;
        final String format = buildMiniMessage(mention.format, mention);

        int lastIndex = 0;
        while (matcher.find()) {
            final String player = matcher.group(1);
            final Player proxyPlayer = proxyServer.getPlayer(player).orElse(null);

            if (proxyPlayer == null) {
                continue;
            }

            result.append(message, lastIndex, matcher.start())
                    .append(format.replace("%nickname%", player));
            lastIndex = matcher.end();

            players.add(player);
        }

        result.append(message, lastIndex, message.length());
        return new MentionResult(result.toString(), players);
    }

    private static String buildMiniMessage(final String baseText, final Config.MentionSection mention) {
        final StringBuilder result = new StringBuilder();

        final Config.MentionSection.InteractionSection.ClickSection clickSection = mention.interaction.click;
        final boolean clickEnabled = clickSection.enable;
        final String clickAction = clickSection.action.toUpperCase(Locale.ROOT);
        final String clickValue = clickSection.value;

        if (clickEnabled) {
            result.append("<click:").append(clickAction).append(":\"").append(clickValue).append("\">");
        }

        final Config.MentionSection.InteractionSection.HoverSection hoverSection = mention.interaction.hover;
        final boolean hoverEnabled = hoverSection.enable;
        if (hoverEnabled) {
            final String hoverContent = String.join("<br>", hoverSection.value);
            result.append("<hover:show_text:\"").append(hoverContent).append("\">");
        }

        result.append(baseText);

        if (hoverEnabled) {
            result.append("</hover>");
        }
        if (clickEnabled) {
            result.append("</click>");
        }

        return result.toString();
    }

   public record MentionResult(String message, List<String> players) {}
}
