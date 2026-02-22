package ru.ilezzov.plugin.velocity.command.executor;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.model.user.User;
import ru.ilezzov.plugin.velocity.Main;
import ru.ilezzov.plugin.velocity.file.config.Config;
import ru.ilezzov.plugin.velocity.manager.ChatFilterManager;
import ru.ilezzov.plugin.velocity.manager.CooldownManager;
import ru.ilezzov.plugin.velocity.manager.LuckPermsManager;
import ru.ilezzov.plugin.velocity.manager.PlaceholderManager;
import ru.ilezzov.plugin.velocity.message.PluginMessage;
import ru.ilezzov.plugin.velocity.model.Response;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ru.ilezzov.plugin.velocity.Main.getReplyManager;
import static ru.ilezzov.plugin.velocity.Main.getSpyManager;
import static ru.ilezzov.plugin.velocity.manager.LuckPermsManager.*;
import static ru.ilezzov.plugin.velocity.utils.LegacySerialize.stripNonStyleTags;

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
public class ReplyCommand implements SimpleCommand {
    private final PlaceholderManager placeholder = new PlaceholderManager();

    private final CooldownManager cooldownManager;
    private final ProxyServer proxyServer;
    private final Config.MsgSection.PermissionSection permission;
    private final Config.MsgSection.ChatFilterSection filterSection;

    private final ChatFilterManager chatFilterManager;

    public ReplyCommand(final ProxyServer proxyServer, final long cooldown, final Config.MsgSection.PermissionSection permission, final Config.MsgSection.ChatFilterSection filterSection) {
        this.proxyServer = proxyServer;
        this.cooldownManager = new CooldownManager(cooldown);
        this.permission = permission;
        this.filterSection = filterSection;

        this.placeholder.addPlaceholder("%reply-command%", Main.getConfig().msg.replyCommand.command);
        this.chatFilterManager = new ChatFilterManager(filterSection.mode, filterSection.servers);
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] arguments = invocation.arguments();

        if (!(source instanceof Player player)) {
            source.sendMessage(PluginMessage.noConsoleMessage(placeholder));
            return;
        }

        if (!player.hasPermission(permission.use)) {
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

        if (arguments.length == 0) {
            source.sendMessage(PluginMessage.replyUsageMessage(placeholder));
            return;
        }

        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = getReplyManager().pool(playerUUID);

        if (targetUUID == null) {
            source.sendMessage(PluginMessage.replyNotFound(placeholder));
            return;
        }

        final Player targetProxyPlayer = this.proxyServer.getPlayer(targetUUID).orElse(null);
        if (targetProxyPlayer == null) {
            source.sendMessage(PluginMessage.replyNotFound(placeholder));
            return;
        }

        final ServerConnection senderConnection = player.getCurrentServer().orElse(null);
        final ServerConnection targetConnection = targetProxyPlayer.getCurrentServer().orElse(null);

        final String senderServer = senderConnection != null ? senderConnection.getServerInfo().getName() : "none" ;
        final String targetServer = targetConnection != null ? targetConnection.getServerInfo().getName() : "none";

        placeholder.addPlaceholder("%player_server%", senderServer);
        placeholder.addPlaceholder("%target_server%", targetServer);

        final Response<ChatFilterManager.FilterAccessDeniedFor> deniedForResponse = this.chatFilterManager.check(senderServer, targetServer);

        if (!deniedForResponse.success()) {
            final ChatFilterManager.FilterAccessDeniedFor deniedFor = deniedForResponse.data();

            if (deniedFor == ChatFilterManager.FilterAccessDeniedFor.FOR_SENDER) {
                source.sendMessage(PluginMessage.chatAccessDeniedForSender(placeholder));
            } else {
                source.sendMessage(PluginMessage.chatAccessDeniedForTarget(placeholder));
            }

            return;
        }

        String text = String.join(" ", Arrays.copyOfRange(arguments, 0, arguments.length));
        if (!player.hasPermission(permission.useColor)) {
            text = MiniMessage.miniMessage().stripTags(text);
        } else {
            text = stripNonStyleTags(text);
        }

        final User senderPlayer = getUser(player.getUniqueId());
        final User targetPlayer = getUser(targetProxyPlayer.getUniqueId());

        placeholder.addPlaceholder("%player_prefix%", getPrefix(senderPlayer))
                .addPlaceholder("%player_nickname%", player.getUsername())
                .addPlaceholder("%player_suffix%", getSuffix(senderPlayer))
                .addPlaceholder("%target_prefix%", LuckPermsManager.getPrefix(targetPlayer))
                .addPlaceholder("%target_nickname%", targetProxyPlayer.getUsername())
                .addPlaceholder("%target_suffix%", LuckPermsManager.getSuffix(targetPlayer))
                .addPlaceholder("%message%", text);

        final Component sender = PluginMessage.msgSenderFormat(placeholder);
        final Component receiver = PluginMessage.msgReceiverFormat(placeholder);

        player.sendMessage(sender);
        targetProxyPlayer.sendMessage(receiver);
        getReplyManager().addReply(targetUUID, player.getUniqueId());

        this.cooldownManager.newCooldown(playerUUID);

        CompletableFuture.runAsync(() -> getSpyManager().sendMessage(PluginMessage.spyMessage(placeholder)));
    }
}
