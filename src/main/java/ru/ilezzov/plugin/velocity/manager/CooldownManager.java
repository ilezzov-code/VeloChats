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

import java.util.HashMap;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

public class CooldownManager {
    private final HashMap<UUID, Long> cooldowns;
    private final long cooldownTime;

    public CooldownManager(final long cooldownTime) {
        this.cooldowns = new HashMap<>();
        this.cooldownTime = cooldownTime * 1000;
    }

    public void newCooldown(final UUID uuid) {
        this.cooldowns.put(uuid, currentTimeMillis());
    }

    public long getCooldownTime(final UUID uuid) {
        if (!check(uuid))
            return 0;
        return this.cooldowns.get(uuid) + cooldownTime;
    }

    public boolean checkCooldown(final UUID uuid) {
        return getCooldownTime(uuid) >= currentTimeMillis();
    }

    public int getRemainingTime(final UUID uuid) {
        return (int) (getCooldownTime(uuid) - currentTimeMillis()) / 1000;
    }

    private boolean check(final UUID uuid) {
        return this.cooldowns.containsKey(uuid);
    }
}
