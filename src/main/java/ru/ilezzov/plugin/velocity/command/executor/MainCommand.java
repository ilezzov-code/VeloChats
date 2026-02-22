package ru.ilezzov.plugin.velocity.command.executor;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import ru.ilezzov.plugin.velocity.Main;
import ru.ilezzov.plugin.velocity.manager.PlaceholderManager;
import ru.ilezzov.plugin.velocity.message.PluginMessage;
import ru.ilezzov.plugin.velocity.permission.Permissions;

import java.util.List;

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
public class MainCommand implements SimpleCommand {
    private final PlaceholderManager placeholder = new PlaceholderManager();
    private final Main plugin = Main.getInstance();

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] arguments = invocation.arguments();

        if (arguments.length == 0) {
            source.sendMessage(PluginMessage.mainCommand(placeholder));
            return;
        }

        switch (arguments[0]) {
            case "reload" -> handleReload(source);
            default -> source.sendMessage(PluginMessage.mainCommand(placeholder));
        }
    }

    private void handleReload(final CommandSource source) {
        if (!Main.reloadFiles()) {
            source.sendMessage(PluginMessage.hasErrorReload(placeholder));
            return;
        }

        Main.getCommandManager().reloadCommand();
        source.sendMessage(PluginMessage.reload(placeholder));
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of("reload");
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(Permissions.MAIN_COMMAND_RELOAD);
    }

}
