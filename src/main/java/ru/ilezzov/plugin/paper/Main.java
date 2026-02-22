package ru.ilezzov.plugin.paper;

import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.plugin.paper.handler.SoundPacketListener;

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
public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "velochats:sound", new SoundPacketListener());
        getLogger().info("Paper part of the plugin enabled");
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "velochats:sound");
    }
}
