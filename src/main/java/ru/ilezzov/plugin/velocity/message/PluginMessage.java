package ru.ilezzov.plugin.velocity.message;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.plugin.velocity.manager.PlaceholderManager;
import ru.ilezzov.plugin.velocity.utils.LegacySerialize;

import static ru.ilezzov.plugin.velocity.Main.getConfig;
import static ru.ilezzov.plugin.velocity.Main.getMessages;

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
public class PluginMessage {
    public static Component reload(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.reload, placeholder);
    }

    public static Component hasErrorReload(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasErrorReload, placeholder);
    }

    public static Component hasErrorCheckVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasErrorCheckVersion, placeholder);
    }

    public static Component useLatestVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useLatestVersion, placeholder);
    }

    public static Component useOutdatedVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useOutdatedVersion, placeholder);
    }

    public static Component useSupportedVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useSupportedVersion, placeholder);
    }

    public static Component useBlacklistVersion(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.useBlacklistVersion, placeholder);
    }

    public static Component hasError(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.hasError, placeholder);
    }

    public static @NotNull Component noConsoleMessage(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.noConsole, placeholder);
    }

    public static @NotNull Component cooldownMessage(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.cooldown, placeholder);
    }

    public static @NotNull Component msgHelpMessage(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.msgHelp, placeholder);
    }

    public static @NotNull Component playerNotFound(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.playerNotFound, placeholder);
    }

    public static @NotNull Component writeYourselfMessage(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.writeYourself, placeholder);
    }

    public static Component msgSenderFormat(final PlaceholderManager placeholder) {
        return getComponent(getConfig().msg.format.sender, placeholder);
    }

    public static Component msgReceiverFormat(final PlaceholderManager placeholder) {
        return getComponent(getConfig().msg.format.receiver, placeholder);
    }

    public static Component spyMessage(final PlaceholderManager placeholder) {
        return getComponent(getConfig().msg.format.spy, placeholder);
    }

    public static @NotNull Component replyUsageMessage(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.replyHelp, placeholder);
    }

    public static Component replyNotFound(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.replyNotFound, placeholder);
    }

    public static @NotNull Component spyDisable(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.spyDisable, placeholder);
    }

    public static @NotNull Component spyEnable(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.spyEnable, placeholder);
    }

    public static Component chatAccessDeniedForSender(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.chatAccessDeniedForSender, placeholder);
    }

    public static Component chatAccessDeniedForTarget(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.chatAccessDeniedForTarget, placeholder);
    }

    public static @NotNull Component chatHelp(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.chatHelp, placeholder);
    }

    public static Component noPermission(final PlaceholderManager placeholder) {
        return getComponent(getMessages().plugin.noPermission, placeholder);
    }

    public static Component chatPermissionDenied(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.chatPermissionDenied, placeholder);
    }

    public static Component dynamicMessage(final String message, final PlaceholderManager placeholder) {
        return getComponent(message, placeholder);
    }

    public static @NotNull Component mainCommand(final PlaceholderManager placeholder) {
        return getComponent(getMessages().messages.mainCommand, placeholder);
    }

    private static Component getComponent(final String text, final PlaceholderManager manager) {
        String message = manager.replacePlaceholder(text);
        return LegacySerialize.serialize(message);
    }
}
