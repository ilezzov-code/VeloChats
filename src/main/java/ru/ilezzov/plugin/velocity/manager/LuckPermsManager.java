package ru.ilezzov.plugin.velocity.manager;

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

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import ru.ilezzov.plugin.velocity.Main;

import java.util.UUID;

public class LuckPermsManager {
    private static final LuckPerms luckPerms = Main.getLuckPerms();

    public static User getUser(final UUID uuid) {
        return luckPerms.getUserManager().getUser(uuid);
    }

    public static String getPrefix(final User user) {
        final String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? prefix : "";
    }

    public static String getSuffix(final User user) {
        final String suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix != null ? suffix : "";
    }
}
