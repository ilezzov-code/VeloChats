package ru.ilezzov.plugin.velocity.command.executor;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import ru.ilezzov.plugin.velocity.file.config.Config;
import ru.ilezzov.plugin.velocity.manager.CooldownManager;
import ru.ilezzov.plugin.velocity.manager.PlaceholderManager;
import ru.ilezzov.plugin.velocity.message.PluginMessage;

import java.util.UUID;

import static ru.ilezzov.plugin.velocity.Main.getSpyManager;

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
public class SpyCommand implements SimpleCommand {
    private final ProxyServer proxyServer;
    private final long cooldown;
    private final Config.MsgSection.PermissionSection permission;

    private final CooldownManager cooldownManager;
    private final PlaceholderManager placeholder = new PlaceholderManager();

    public SpyCommand(final ProxyServer proxyServer, final long cooldown, final Config.MsgSection.PermissionSection permission) {
        this.proxyServer = proxyServer;
        this.cooldown = cooldown;
        this.permission = permission;
        this.cooldownManager = new CooldownManager(cooldown);
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();

        if (!(source instanceof Player player)) {
            source.sendMessage(PluginMessage.noConsoleMessage(placeholder));
            return;
        }

        if (!source.hasPermission(permission.spy)) {
            source.sendMessage(PluginMessage.noPermission(placeholder));
            return;
        }

        if (!source.hasPermission(permission.noCooldown)) {
            if (this.cooldownManager.checkCooldown(player.getUniqueId())) {
                placeholder.addPlaceholder("%time%", cooldownManager.getRemainingTime(player.getUniqueId()));
                source.sendMessage(PluginMessage.cooldownMessage(placeholder));
                return;
            }
        }

        final UUID playerUUID = player.getUniqueId();
        if (getSpyManager().contains(playerUUID)) {
            getSpyManager().remove(playerUUID);
            player.sendMessage(PluginMessage.spyDisable(placeholder));
        } else {
            getSpyManager().add(playerUUID);
            player.sendMessage(PluginMessage.spyEnable(placeholder));
        }

        this.cooldownManager.newCooldown(playerUUID);
    }
}
