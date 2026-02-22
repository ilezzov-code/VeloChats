package ru.ilezzov.plugin.velocity.manager;

import ru.ilezzov.plugin.velocity.file.config.Config;
import ru.ilezzov.plugin.velocity.model.Response;

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
public class ChatFilterManager {
    private final Config.FilterMode mode;
    private final List<String> servers;

    public ChatFilterManager(final Config.FilterMode mode, final List<String> servers) {
        this.mode = mode;
        this.servers = servers;
    }


    public boolean check(final String playerServer) {
        if (this.mode == Config.FilterMode.DISABLE) {
            return true;
        }

        if (this.mode == Config.FilterMode.WHITELIST) {
            return this.servers.contains(playerServer);
        }

        return !this.servers.contains(playerServer);
    }

    public Response<FilterAccessDeniedFor> check(final String playerServer, final String targetServer) {
        if (this.mode == Config.FilterMode.DISABLE) {
            return Response.ok();
        }

        if (this.mode == Config.FilterMode.WHITELIST) {
            if (!this.servers.contains(playerServer)) {
                return Response.error(FilterAccessDeniedFor.FOR_SENDER);
            }
            if (!this.servers.contains(targetServer)) {
                return Response.error(FilterAccessDeniedFor.FOR_TARGET);
            }
            return Response.ok();
        }

        if (this.servers.contains(playerServer)) {
            return Response.error(FilterAccessDeniedFor.FOR_SENDER);
        }
        if (this.servers.contains(targetServer)) {
            return Response.error(FilterAccessDeniedFor.FOR_TARGET);
        }
        return Response.ok();

    }

    public enum FilterAccessDeniedFor {
        FOR_SENDER, FOR_TARGET;
    }

}
