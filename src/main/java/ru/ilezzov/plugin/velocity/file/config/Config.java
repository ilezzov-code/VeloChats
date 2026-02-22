package ru.ilezzov.plugin.velocity.file.config;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    public boolean checkUpdates;
    public String lang;

    public String configVersion;


    public MentionSection mention;
    public MsgSection msg;
    public ChannelsSection channels;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MentionSection {
        public boolean enable;
        public String format;
        public String permission;

        public SoundSection sound;
        public InteractionSection interaction;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SoundSection {
            public boolean enable;
            public String key;
            public String source;
            public float volume;
            public float pitch;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InteractionSection {
            public HoverSection hover;
            public ClickSection click;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class HoverSection {
                public boolean enable;
                public String action;
                public String[] value;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ClickSection {
                public boolean enable;
                public String action;
                public String value;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MsgSection {
        public boolean enable;
        public String command;
        public String[] aliases;
        public long cooldown;

        public ChatFilterSection chatFilter;
        public ReplyCommandSection replyCommand;
        public SpyCommandSection spyCommand;
        public PermissionSection permissions;
        public FormatSection format;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ChatFilterSection {
            public FilterMode mode;
            public List<String> servers;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ReplyCommandSection {
            public boolean enable;
            public String command;
            public String[] aliases;
            public int timeout;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SpyCommandSection {
            public boolean enable;
            public String command;
            public String[] aliases;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PermissionSection {
            public String use;
            public String reply;
            public String spy;
            public String noCooldown;
            public String useColor;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FormatSection {
            public String sender;
            public String receiver;
            public String spy;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChannelsSection {
        private final Map<String, ChatChannel> channelMap = new HashMap<>();

        @JsonAnySetter
        public void addChannel(String name, ChatChannel channel) {
            channelMap.put(name, channel);
        }

        public List<ChatChannel> getAllChannels() {
            return new ArrayList<>(channelMap.values());
        }

        public ChatChannel getChannel(String name) {
            return channelMap.get(name);
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ChatChannel {
            public boolean enable;
            public String command;
            public String[] aliases;

            public ChatFilterSection chatFilter;

            public boolean whitelistMode;

            public List<String> serverList;

            public long cooldown;
            public String format;
            public PermissionSection permissions;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ChatFilterSection {
                public FilterMode mode;
                public List<String> servers;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PermissionSection {
                public String read;
                public String write;
                public String noCooldown;
                public String useColor;
            }
        }
    }

    public enum FilterMode {
        WHITELIST, BLACKLIST, DISABLE;

        @JsonCreator
        public static FilterMode getType(final String value) {
            return valueOf(value.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

    public Config() {}
}