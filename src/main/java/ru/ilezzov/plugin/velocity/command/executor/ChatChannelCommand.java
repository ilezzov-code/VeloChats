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
import ru.ilezzov.plugin.velocity.manager.*;
import ru.ilezzov.plugin.velocity.message.PluginMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ru.ilezzov.plugin.velocity.Main.getConfig;
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
public class ChatChannelCommand implements SimpleCommand {
    private final ProxyServer proxyServer;
    private final Config.ChannelsSection.ChatChannel chatChannel;
    private final Config.ChannelsSection.ChatChannel.PermissionSection permission;
    private final Config.ChannelsSection.ChatChannel.ChatFilterSection filterSection;

    private final CooldownManager cooldownManager;
    private final PlaceholderManager placeholder = new PlaceholderManager();
    private final ChatFilterManager chatFilterManager;

    public ChatChannelCommand(final ProxyServer proxyServer, final long cooldown, final Config.ChannelsSection.ChatChannel chatChannel) {
        this.proxyServer = proxyServer;
        this.cooldownManager = new CooldownManager(cooldown);
        this.chatChannel = chatChannel;
        this.permission = chatChannel.permissions;
        this.filterSection = chatChannel.chatFilter;

        this.placeholder.addPlaceholder("%chat-command%", chatChannel.command);
        this.chatFilterManager = new ChatFilterManager(filterSection.mode, filterSection.servers);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        if (invocation == null) {
            return CompletableFuture.completedFuture(List.of());
        }

        final String[] arguments = invocation.arguments();
        if (arguments.length == 0) {
            return SimpleCommand.super.suggestAsync(invocation);
        }

        final String lastArg = arguments[arguments.length - 1];

        if (lastArg.startsWith("@")) {
            final String searchPrefix = lastArg.substring(1).toLowerCase();

            return CompletableFuture.supplyAsync(() -> this.proxyServer.getAllPlayers().stream()
                    .filter(player -> player.hasPermission(this.permission.read))
                    .filter(player -> player.getUsername().toLowerCase().startsWith(searchPrefix))
                    .map(player -> "@" + player.getUsername())
                    .toList());
        }

        return SimpleCommand.super.suggestAsync(invocation);
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] arguments = invocation.arguments();

        if (!(source instanceof Player player)) {
            source.sendMessage(PluginMessage.noConsoleMessage(placeholder));
            return;
        }

        if (!source.hasPermission(permission.write)) {
            source.sendMessage(PluginMessage.chatPermissionDenied(placeholder));
            return;
        }

        if (!source.hasPermission(permission.noCooldown)) {
            if (this.cooldownManager.checkCooldown(player.getUniqueId())) {
                placeholder.addPlaceholder("%time%", cooldownManager.getRemainingTime(player.getUniqueId()));
                source.sendMessage(PluginMessage.cooldownMessage(placeholder));
                return;
            }
        }

        if (arguments.length < 1) {
            source.sendMessage(PluginMessage.chatHelp(placeholder));
            return;
        }

        final ServerConnection senderConnection = player.getCurrentServer().orElse(null);
        final String senderServer = senderConnection != null ? senderConnection.getServerInfo().getName() : "none" ;

        placeholder.addPlaceholder("%player_server%", senderServer);

        final boolean deniedForResponse = this.chatFilterManager.check(senderServer);
        if (!deniedForResponse) {
            source.sendMessage(PluginMessage.chatAccessDeniedForSender(placeholder));
            return;
        }

        String text = String.join(" ", Arrays.copyOfRange(arguments, 0, arguments.length));
        if (!player.hasPermission(permission.useColor)) {
            text = MiniMessage.miniMessage().stripTags(text);
        } else {
            text = stripNonStyleTags(text);
        }

        final Config.MentionSection mention = getConfig().mention;
        List<String> mentionPlayers = new ArrayList<>();
        if (mention.enable) {
            if (player.hasPermission(mention.permission)) {
                final MentionManager.MentionResult mentionResult = MentionManager.replaceMention(proxyServer, text);
                text = mentionResult.message();
                mentionPlayers.addAll(mentionResult.players());
                placeholder.addPlaceholder("%msg_command%", getConfig().msg.command);
            }
        }

        final User senderPlayer = getUser(player.getUniqueId());
        placeholder.addPlaceholder("%player_prefix%", getPrefix(senderPlayer))
                .addPlaceholder("%player_nickname%", player.getUsername())
                .addPlaceholder("%player_suffix%", getSuffix(senderPlayer))
                .addPlaceholder("%message%", placeholder.replacePlaceholder(text));

        final Component message = PluginMessage.dynamicMessage(chatChannel.format, placeholder);

        this.cooldownManager.newCooldown(player.getUniqueId());
        if (mention.sound.enable) {
            proxyServer.getScheduler().buildTask(Main.getInstance(), () -> {
                ChatSenderManager.broadcast(
                        proxyServer,
                        message,
                        permission.read,
                        mentionPlayers,
                        mention.sound,
                        chatFilterManager
                );
            }).schedule();
        } else {
            proxyServer.getScheduler().buildTask(Main.getInstance(), () -> {
                ChatSenderManager.broadcast(
                        proxyServer,
                        message,
                        permission.read,
                        chatFilterManager
                );
            }).schedule();
        }
    }
}
