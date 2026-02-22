package ru.ilezzov.plugin.paper.handler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
public class SoundPacketListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player unused, byte[] message) {
        if (!channel.equals("velochats:sound")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        final String soundName = in.readUTF();
        final float volume = in.readFloat();
        final float pitch = in.readFloat();

        final int count = in.readInt();
        for (int i = 0; i < count; i++) {
            final String targetName = in.readUTF();
            final Player target = Bukkit.getPlayerExact(targetName);

            if (target != null) {
                target.playSound(target.getLocation(), soundName, volume, pitch);
            }
        }
    }
}