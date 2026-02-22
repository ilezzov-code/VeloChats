package ru.ilezzov.plugin.velocity.manager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.ilezzov.plugin.velocity.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
public class SpyManager {
    private final ProxyServer proxyServer;
    private final Set<UUID> playerEnabledSpy = new HashSet<>();

    public SpyManager(final ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public void add(final UUID uuid) {
        this.playerEnabledSpy.add(uuid);
    }

    public boolean contains(final UUID uuid) {
        return this.playerEnabledSpy.contains(uuid);
    }

    public void remove(final UUID uuid) {
        this.playerEnabledSpy.remove(uuid);
    }

    public void sendMessage(final Component component) {
        final String permission = Main.getConfig().msg.permissions.spy;

        this.playerEnabledSpy.stream().forEach(u -> {
            final Player player = this.proxyServer.getPlayer(u).orElse(null);

            if (player == null) {
                this.playerEnabledSpy.remove(u);
                return;
            }

            if (!player.hasPermission(permission)) {
                this.playerEnabledSpy.remove(u);
                return;
            }

            player.sendMessage(component);
        });
    }
}
