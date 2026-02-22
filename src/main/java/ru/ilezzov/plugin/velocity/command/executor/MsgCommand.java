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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ru.ilezzov.plugin.velocity.Main.getReplyManager;
import static ru.ilezzov.plugin.velocity.Main.getSpyManager;
import static ru.ilezzov.plugin.velocity.manager.LuckPermsManager.*;
import static ru.ilezzov.plugin.velocity.message.PluginMessage.playerNotFound;
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

public class MsgCommand implements SimpleCommand {
    private final ProxyServer proxyServer;
    private final Config.MsgSection.PermissionSection permission;
    private final Config.MsgSection.ChatFilterSection filterSection;

    private final CooldownManager cooldownManager;
    private final PlaceholderManager placeholder = new PlaceholderManager();
    private final ChatFilterManager chatFilterManager;

    public MsgCommand(final ProxyServer proxyServer, final long cooldown, final Config.MsgSection.PermissionSection permission, final Config.MsgSection.ChatFilterSection filterSection) {
        this.proxyServer = proxyServer;
        this.permission = permission;
        this.cooldownManager = new CooldownManager(cooldown);
        this.filterSection = filterSection;

        this.placeholder.addPlaceholder("%msg-command%", Main.getConfig().msg.command);
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

        if (arguments.length < 2) {
            source.sendMessage(PluginMessage.msgHelpMessage(placeholder));
            return;
        }

        final String targetName = arguments[0];
        final Player target = this.proxyServer.getPlayer(targetName).orElse(null);
        placeholder.addPlaceholder("%target_nickname%", target);

        if (target == null) {
            source.sendMessage(playerNotFound(placeholder));
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            source.sendMessage(PluginMessage.writeYourselfMessage(placeholder));
            return;
        }

        final ServerConnection senderConnection = player.getCurrentServer().orElse(null);
        final ServerConnection targetConnection = target.getCurrentServer().orElse(null);

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

        String text = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        if (!player.hasPermission(permission.useColor)) {
            text = MiniMessage.miniMessage().stripTags(text);
        } else {
            text = stripNonStyleTags(text);
        }

        final User senderPlayer = getUser(player.getUniqueId());
        final User targetPlayer = getUser(target.getUniqueId());

        placeholder.addPlaceholder("%player_prefix%", getPrefix(senderPlayer))
                .addPlaceholder("%player_nickname%", player.getUsername())
                .addPlaceholder("%player_suffix%", getSuffix(senderPlayer))
                .addPlaceholder("%target_prefix%", LuckPermsManager.getPrefix(targetPlayer))
                .addPlaceholder("%target_nickname%", target.getUsername())
                .addPlaceholder("%target_suffix%", LuckPermsManager.getSuffix(targetPlayer))
                .addPlaceholder("%message%", text);

        final Component sender = PluginMessage.msgSenderFormat(placeholder);
        final Component receiver = PluginMessage.msgReceiverFormat(placeholder);

        player.sendMessage(sender);
        target.sendMessage(receiver);
        getReplyManager().addReply(target.getUniqueId(), player.getUniqueId());

        this.cooldownManager.newCooldown(player.getUniqueId());

        CompletableFuture.runAsync(() -> {
            getSpyManager().sendMessage(PluginMessage.spyMessage(placeholder));
        });
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if (invocation == null) {
            return CompletableFuture.completedFuture(this.proxyServer.getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList()));
        }

        final String[] arguments = invocation.arguments();

        if (arguments.length == 0) {
            return CompletableFuture.completedFuture(this.proxyServer.getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList()));
        }

        if (arguments.length == 1) {
            final String currentArgument = arguments[0];

            return CompletableFuture.completedFuture(this.proxyServer.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .filter(s -> s.startsWith(currentArgument))
                    .collect(Collectors.toList()));
        }

        return CompletableFuture.completedFuture(List.of());
    }
}
