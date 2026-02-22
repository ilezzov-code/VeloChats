package ru.ilezzov.plugin.velocity.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
public class PlaySoundManager {
    private final ProxyServer proxy;
    public static final MinecraftChannelIdentifier SOUND_CHANNEL =
            MinecraftChannelIdentifier.from("velochats:sound");

    public PlaySoundManager(ProxyServer proxy) {
        this.proxy = proxy;
    }

    public void sendSoundPacket(final Map<String, List<String>> playersByServer, final String soundName, final float volume, final float pitch) {
        playersByServer.forEach((serverName, players) -> {
            if (players.isEmpty()) return;

            final Optional<RegisteredServer> server = proxy.getServer(serverName);
            server.ifPresent(s -> {
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();

                out.writeUTF(soundName);
                out.writeFloat(volume);
                out.writeFloat(pitch);

                out.writeInt(players.size());
                for (final String playerName : players) {
                    out.writeUTF(playerName);
                }

                s.sendPluginMessage(SOUND_CHANNEL, out.toByteArray());
            });
        });
    };
}
