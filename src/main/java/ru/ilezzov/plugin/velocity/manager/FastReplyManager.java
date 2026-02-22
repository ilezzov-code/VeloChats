package ru.ilezzov.plugin.velocity.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import ru.ilezzov.plugin.velocity.Main;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

public class FastReplyManager {
    private final Cache<UUID, UUID> reply = Caffeine.newBuilder()
            .expireAfterWrite(Main.getConfig().msg.replyCommand.timeout, TimeUnit.SECONDS)
            .maximumSize(100)
            .recordStats()
            .build();


    public void addReply(final UUID player, final UUID target) {
        this.reply.put(player, target);
    }

    public UUID pool(final UUID player) {
        final UUID target = this.reply.getIfPresent(player);
        this.reply.invalidate(player);

        if (target == null) {
            return null;
        }

        return target;
    }

}
