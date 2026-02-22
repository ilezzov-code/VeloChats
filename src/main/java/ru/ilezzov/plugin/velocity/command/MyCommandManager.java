package ru.ilezzov.plugin.velocity.command;

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

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import ru.ilezzov.plugin.velocity.Main;
import ru.ilezzov.plugin.velocity.command.executor.*;
import ru.ilezzov.plugin.velocity.file.config.Config;

import java.util.HashMap;
import java.util.Map;

public class MyCommandManager {
    private final ProxyServer proxyServer;

    private final CommandManager commandManager;
    private final Map<String, Command> commandMap;

    public MyCommandManager(final ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        this.commandManager = this.proxyServer.getCommandManager();
        this.commandMap = loadCommands();
    }

    public void reloadCommand() {
        unloadCommand();
        this.commandMap.putAll(loadCommands());
    }

    private void unloadCommand() {
        for (final String command : commandMap.keySet()) {
            this.commandManager.unregister(command);
        }

        this.commandMap.clear();
    }

    public Map<String, Command> loadCommands() {
        final Map<String, Command> commandMap = new HashMap<>();

        // Main command
        final String command = "velochats";
        final CommandMeta mainMeta = createMeta(command, new String[]{"vc"});
        final Command mainCommand = new MainCommand();

        this.commandManager.register(mainMeta, mainCommand);
        commandMap.put(command, mainCommand);

        // Msg command
        final Config.MsgSection msgSection = Main.getConfig().msg;
        final Config.MsgSection.PermissionSection msgPermission = msgSection.permissions;

        if (msgSection.enable) {
            final CommandMeta msgMeta = createMeta(msgSection.command, msgSection.aliases);
            final Command msgCommand = new MsgCommand(proxyServer, msgSection.cooldown, msgPermission, msgSection.chatFilter);

            this.commandManager.register(msgMeta, msgCommand);
            commandMap.put(msgSection.command, msgCommand);
        }

        // Reply Command
        final Config.MsgSection.ReplyCommandSection replyCommandSection = msgSection.replyCommand;

        if (replyCommandSection.enable) {
            final CommandMeta replyMeta = createMeta(replyCommandSection.command, replyCommandSection.aliases);
            final Command replyCommand = new ReplyCommand(proxyServer, msgSection.cooldown, msgPermission, msgSection.chatFilter);

            this.commandManager.register(replyMeta, replyCommand);
            commandMap.put(replyCommandSection.command, replyCommand);
        }

        // Spy Command
        final Config.MsgSection.SpyCommandSection spyCommandSection = msgSection.spyCommand;

        if (spyCommandSection.enable) {
            final CommandMeta spyMeta = createMeta(spyCommandSection.command, spyCommandSection.aliases);
            final Command spyCommand = new SpyCommand(proxyServer, msgSection.cooldown, msgPermission);

            this.commandManager.register(spyMeta, spyCommand);
            commandMap.put(spyCommandSection.command, spyCommand);
        }

        // Channels
        final Config.ChannelsSection channelsSection = Main.getConfig().channels;
        for (Config.ChannelsSection.ChatChannel chatChannel : channelsSection.getAllChannels()) {
            if (chatChannel.enable) {
                final CommandMeta chatMeta = createMeta(chatChannel.command, chatChannel.aliases);
                final Command chatCommand = new ChatChannelCommand(proxyServer, chatChannel.cooldown, chatChannel);

                this.commandManager.register(chatMeta, chatCommand);
                commandMap.put(spyCommandSection.command, chatCommand);
            }
        }

        return commandMap;
    }

    private CommandMeta createMeta(final String commandName, final String[] aliases) {
        return this.commandManager.metaBuilder(commandName)
                .plugin(Main.getInstance())
                .aliases(aliases)
                .build();
    }
}
