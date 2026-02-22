package ru.ilezzov.plugin.velocity.manager;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import ru.ilezzov.plugin.velocity.file.config.Config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ru.ilezzov.plugin.velocity.Main.getPlaySoundManager;

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
public class ChatSenderManager {
    // TODO: add play sound logic

    public static void broadcast(final ProxyServer proxyServer, final Component message, final String requiredPermission,
                                 final List<String> mentionPlayers, final Config.MentionSection.SoundSection sound, final ChatFilterManager manager) {
        final Map<String, Boolean> filterCache = new ConcurrentHashMap<>();
        final Map<String, List<String>> playersByServer = new ConcurrentHashMap<>();

        proxyServer.getAllPlayers().parallelStream().forEach(player -> {
            RegisteredServer server = player.getCurrentServer().map(ServerConnection::getServer).orElse(null);
            if (server == null) {
                return;
            }

            final String serverName = server.getServerInfo().getName();

            boolean isServerAllowed = filterCache.computeIfAbsent(serverName, manager::check);
            if (!isServerAllowed) {
                return;
            }

            if (!player.hasPermission(requiredPermission)) {
                return;
            }

            player.sendMessage(message);
            if (mentionPlayers == null || mentionPlayers.isEmpty()) {
                return;
            }

            if (mentionPlayers.contains(player.getUsername())) {
                playersByServer.computeIfAbsent(serverName, k -> new ArrayList<>())
                        .add(player.getUsername());
            }
        });

        getPlaySoundManager().sendSoundPacket(playersByServer, sound.key, sound.volume, sound.pitch);
    }

    public static void broadcast(final ProxyServer proxyServer, final Component message, final String requiredPermission, final ChatFilterManager manager) {
        final Map<String, Boolean> filterCache = new ConcurrentHashMap<>();

        proxyServer.getAllPlayers().parallelStream().forEach(player -> {
            RegisteredServer server = player.getCurrentServer().map(ServerConnection::getServer).orElse(null);
            if (server == null) {
                return;
            }

            final String serverName = server.getServerInfo().getName();

            boolean isServerAllowed = filterCache.computeIfAbsent(serverName, manager::check);
            if (!isServerAllowed) {
                return;
            }

            if (!player.hasPermission(requiredPermission)) {
                return;
            }

            player.sendMessage(message);
        });
    }
}
