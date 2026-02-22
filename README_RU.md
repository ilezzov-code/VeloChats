<div align="center">
    <img src="img/logo/BannerRoundedRu.png" alt="">
    <h4>Плагин для Velocity Minecraft</h4>
    <h1>Меж серверные чаты и личные сообщения </h1>
</div>

### <a href="https://github.com/ilezzov-code/velochats"><img src="img/flags/en.svg" width=15px> Select English README.md</a>

##  <a>Оглавление</a>

- [Описание](#about)
- [Особенности](#features)
- [Config.yml](#config)
- [Команды](#commands)
- [Ссылки](#links)
- [Поддержать разработчика](#donate)
- [Сообщить об ошибке](https://github.com/ilezzov-code/velochats/issues)


## <a id="about">Описание</a>

**VeloChats** — это уникальный плагин, который позволяет создавать бесконечное количество чатов в сети Velocity, а также общаться игрокам между серверами в личных сообщениях

## <a id="features">Особенности</a>

* **[🔥]** Бесконечное количество кастомных чатов [подробнее »](#custom_chats)
* **[🔥]** Полная кастомизация каждой команды и прав [подробнее »](#custom_commands)
* Личные сообщения и быстрые ответы [подробнее »](#private_messages)
* Режим слежки [подробнее »](#spy_mode)
* Система упоминаний [подробнее »](#mention_system)
* Белый и черный список серверов [подробнее »](#server_filtering)
* Задержка на использование каждой команды
* Поддержка цветов MiniMessage и старых форматов форматов [подробнее »](#color_support)
* Поддержка 2-х языков (Russian, English) + возможность создать собственный перевод
* Полная кастомизация:
    * Отключение / Включение каждой возможности
    * Задержка на использование каждой команды
    * Подробная система прав


## <a id="custom_chats">Кастомные чаты</a>

Вы можете создать бесконечное количество чатов, а также отдельно настроить права и формат сообщений для каждого из них

Для создания нового чата, добавьте в `config,yml` в секцию `channels` новый чат. Пример ниже

```yml
channels:
  media-chat:
    enable: true
    command: "media-chat"
    aliases: ["mc", "mediachat"]
    chat-filter:
      mode: WHITELIST
      servers:
        - hub
        - grief-1
        - grief-2
    cooldown: 10
    permissions:
      read: "velochat.chat.media_chat.read"
      write: "velochat.chat.media_chat.write"
      no-cooldown: "velochat.chat.media_chat.cooldown"
      use-color: "velochat.media_chat.use_color"

    format: "<aqua>Медиа Чат <dark_gray>| %player_prefix% %player_nickname% %player_suffix% <yellow>(%player_server%)</yellow> <dark_gray>» <gray>%message%"
```

Вот как выглядят кастомные чаты в игре:

![CustomChat1](img/screens/custom_chat_1.png)
![CustomChat2](img/screens/custom_chat_2.png)


## <a id="custom_commands">Кастомизация каждой команды и прав</a>

Вы можете изменить имя каждой команде плагина, а также отдельно настроить права на использование, задержку и так далее. Найдите в `config.yml` нужную команду и настройте ее как вам угодно. Пример на команде /msg:

```yml
msg:
  enable: true
  command: "msg-global"
  aliases: ["m-global"]
  cooldown: 10
  permissions:
    use: "velochat.msg.use"
    no-cooldown: "velochat.msg.cooldown"
    use-color: "velochat.msg.use_color"
```

## <a id = "private_messages">Личные сообщение</a>

Вы можете включить/отключить личные сообщения между серверами. Для этого найдите в `config.yml` секцию `msg` и настройке ее под себя. 

Также плагин поддерживает быстрые ответы при помощи команды `/r`. Ее вы тоже можете настроить под себя

Вот как выглядят личные сообщения в игре: 

![PrivateMessage](img/screens/private_message_1.png)

## <a id = "spy_mode">Режим слежки</a>

Если вы разрешили личные сообщения, то также сможете использовать режим слежки, или же `Spy Mode`.

Для настройки в `config.yml` найдите секцию `spy-command`

Вот как выглядит режим слежки в игре: 

![SpyMode](img/screens/spy_mode_1.png)

## <a id = "mention_system">Система упоминаний</a>

Плагин поддерживает систему упоминаний. Вы можете настроить стиль упоминания, звук, а также действие при наведении и нажатии на упоминание.

Для настройки в `config.yml` найдите секцию `mention`

Для поддержки звукового оформления, установить плагин на каждый Backend сервер.

Вот как выглядит система упоминаний в игре:
![Mention](img/screens/mention.png)
![MentionHover](img/screens/mention_hover.png)

## <a id = "server_filtering">Белый и черный список серверов</a>

Для личных сообщений и каждого чата плагина Вы можете настроить фильтрацию серверов.

```yml
    # Режим фильтрации:
    # Filtering mode:
    # WHITELIST — Разрешает использование только на серверах из списка
    # WHITELIST — Allows use only on servers in the list
    # BLACKLIST — Разрешает использование на всех серверах кроме тех, которые в списке
    # BLACKLIST — Allows use on all servers except those in the list
    # DISABLE — Не использовать режим фильтрации
    # DISABLE — Do not use filtering mode
    mode: WHITELIST
    # Список серверов
    # List of servers
    servers:
      - hub
      - grief-1
      - grief-2
```

Таким образом Вы сможете исключить те сервера, на которых не должна работать какая-либо функция плагина



## <a id="config">Config.yml</a>

<details>
    <summary>Посмотреть config.yml</summary>

```yaml
# ██╗░░░██╗███████╗██╗░░░░░░█████╗░░█████╗░██╗░░██╗░█████╗░████████╗
# ██║░░░██║██╔════╝██║░░░░░██╔══██╗██╔══██╗██║░░██║██╔══██╗╚══██╔══╝
# ╚██╗░██╔╝█████╗░░██║░░░░░██║░░██║██║░░╚═╝███████║███████║░░░██║░░░
# ░╚████╔╝░██╔══╝░░██║░░░░░██║░░██║██║░░██╗██╔══██║██╔══██║░░░██║░░░
# ░░╚██╔╝░░███████╗███████╗╚█████╔╝╚█████╔╝██║░░██║██║░░██║░░░██║░░░
# ░░░╚═╝░░░╚══════╝╚══════╝░╚════╝░░╚════╝░╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░

# Socials / Ссылки:
# • Contact with me / Связаться: https://t.me/ilezovofficial
# • Telegram Channel / Телеграм канал: https://t.me/ilezzov
# • GitHub: https://github.com/ilezzov-code

# By me coffee / Поддержать разработчика:
# • YooMoney: https://yoomoney.ru/to/4100118180919675
# • Telegram Gift: https://t.me/ilezovofficial
# • TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
# • BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
# • Card: 2200 7007 3348 7101 (T-Bank)

# Check for updates / Проверять наличие обновлений
check-updates: true

# Языковой файл. Поддерживаемые (ru-RU, en-US), вы можете загрузить свой перевод, создав файл в папке messages/custom.yml
# Language file. Supported (ru-RU, en-US), you can upload your own translation by creating a file in messages/custom.yml
lang: "ru-RU"

# Настройка упоминая игроков (@nickname)
# Player mention settings (@nickname)
mention:
  # Включить упоминания
  # Enable mentions
  enable: true
  # Формат упоминания
  # Mention format
  format: "<yellow><u>@%nickname%</u></yellow>"
  # Право на использование упоминаний
  # Permission to use mentions
  permission: "velochat.mention"
  # Звук при упоминании
  # Mention sound
  # Для корректной работы установите VeloChat на все сервера, где хотите поддерживать воспроизведение звука
  # For correct operation, install VeloChat on all servers where you want to support sound playback
  sound:
    # Включить звук
    # Enable sound
    enable: true
    # Имя звука
    # Sound name
    value: "MINECRAFT_EXPIRIENCE_PICK_UP"
    # Громкость
    # Volume
    volume: 1.0
    pitch: 1.2
  # Действие при наведении и нажатии
  # Hover and click actions
  interaction:
    # Всплывающее сообщения
    # Hover message
    hover:
      # Включить текст при наведении
      # Enable hover text
      enable: true
      # Типы: SHOW_TEXT (показать текст)
      # Types: SHOW_TEXT
      action: SHOW_TEXT
      # Текст
      # Text
      value:
        - "&7Нажми, чтобы написать сообщение %player_prefix% %player_nickname% %player_suffix%"
        - "&e/%msg_command% %player_nickname%"

    # Действие при нажатии
    # Click action
    click:
      # Включить действие при нажатии
      # Enable click action
      enable: true
      # Типы: OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND, COPY_TO_CLIPBOARD
      # Types: OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND, COPY_TO_CLIPBOARD
      action: SUGGEST_COMMAND
      # Значение
      # Value
      value: "/%msg_command% %player_nickname% "


# Настройка личных сообщений
# Private message settings
msg:
  # Включить личные сообщения
  # Enable private messages
  enable: true
  # Основная команда
  # Main command
  command: "msg-global"
  # Синонимы команды
  # Command aliases
  aliases: ["m-global"]
  # Задержка на использование в секундах
  # Cooldown in seconds
  cooldown: 10
  # Сервера, на которых должна быть доступна команда.
  # Servers where the command should be available.
  # Если игрок находится на запрещенном сервере, он не сможет ни отправлять, ни получать сообщения
  # If a player is on a restricted server, they will not be able to send or receive messages
  chat-filter:
    # Режим фильтрации:
    # Filtering mode:
    # WHITELIST — Разрешает использование только на серверах из списка
    # WHITELIST — Allows use only on servers in the list
    # BLACKLIST — Разрешает использование на всех серверах кроме тех, которые в списке
    # BLACKLIST — Allows use on all servers except those in the list
    # DISABLE — Не использовать режим фильтрации
    # DISABLE — Do not use filtering mode
    mode: WHITELIST
    # Список серверов
    # List of servers
    servers:
      - hub
      - grief-1
      - grief-2

  # Настройки команды дя быстрых ответов
  # Reply command settings
  reply-command:
    # Включить команду
    # Enable command
    enable: true
    # Основная команда
    # Main command
    command: "reply-global"
    # Синонимы команды
    # Command aliases
    aliases: [r-g]
    # Сколько по времени хранить игрока для быстрого ответа (в секундах)
    # How long to store the player for a quick reply (in seconds)
    timeout: 180

  # Настройки команды для режима отслеживания
  # Spy mode command settings
  spy-command:
    # Включить команду
    # Enable command
    enable: true
    # Основная команда
    # Main command
    command: "spy-global"
    # Синонимы команды
    # Command aliases
    aliases: ["s-g"]

  # Права
  # Permissions
  permissions:
    # Разрешить использовать
    # Permission to use
    use: "velochat.msg.use"
    # Разрешить отправку быстрых ответов
    # Permission to reply
    reply: "velochat.msg.reply"
    # Разрешить отслеживать чужие переписки
    # Permission to spy
    spy: "velochat.msg.spy"
    # Разрешить писать без задержки
    # Permission for no cooldown
    no-cooldown: "velochat.msg.cooldown"
    # Разрешить использовать цвета
    # Permission to use colors
    use-color: "velochat.msg.use_color"

  # Плейсхолдеры
  # Placeholders
  # %player_prefix% %player_nickname% %player_suffix% %player_server% — для игрока, отправляющего сообщение
  # %player_prefix% %player_nickname% %player_suffix% %player_server% — for the player sending the message
  # %target_prefix% <target_nickname> <target_suffix> %target_server% — для игрока, принимающего сообщение
  # %target_prefix% <target_nickname> <target_suffix> %target_server% — for the player receiving the message
  # %message% — сообщение
  # %message% — the message
  # Формат сообщений
  # Message format
  format:
    # Для отправителя
    # For the sender
    sender: "<prefix> <gray>[Я -> %target_prefix% <target_nickname> <yellow>(%target_server%)</yellow><gray>] » <white>%message%</white>"
    # Для получателя
    # For the receiver
    receiver: "<prefix> <gray>[%player_prefix% %player_nickname% <yellow>(%player_server%)</yellow> <gray>-> Я] » <white>%message%</white>"
    # /spy режим
    # /spy mode
    spy: "<prefix> <gray>[%player_prefix% %player_nickname% <yellow>(%player_server%)</yellow> <gray>-> %target_prefix% <target_nickname> <yellow>(%target_server%)</yellow><gray>] » <white>%message%</white>"

# Определение каналов чата
# Chat channels definition
channels:
  # Донатерский чат
  # Donator chat
  donate-chat:
    # Включить чат
    # Enable chat
    enable: true
    # Команда для отправки сообщения
    # Command to send message
    command: "donate-chat"
    # Синонимы команды
    # Command aliases
    aliases: ["dc", "donchat"]
    # Сервера, на которых должна быть доступна команда.
    # Servers where the command should be available.
    # Если игрок находится на запрещенном сервере, он не сможет ни отправлять, ни получать сообщения
    # If a player is on a restricted server, they will not be able to send or receive messages
    chat-filter:
      # Режим фильтрации:
      # Filtering mode:
      # WHITELIST — Разрешает использование только на серверах из списка
      # WHITELIST — Allows use only on servers in the list
      # BLACKLIST — Разрешает использование на всех серверах кроме тех, которые в списке
      # BLACKLIST — Allows use on all servers except those in the list
      # DISABLE — Не использовать режим фильтрации
      # DISABLE — Do not use filtering mode
      mode: WHITELIST
      # Список серверов
      # List of servers
      servers:
        - hub
        - grief-1
        - grief-2
    # Задержка на отправку сообщений в мс
    # Cooldown for sending messages in ms
    cooldown: 10
    # Права
    # Permissions
    permissions:
      # Разрешить читать чат
      # Permission to read chat
      read: "velochat.chat.donate_chat.read"
      # Разрешить писать в чат
      # Permission to write to chat
      write: "velochat.chat.donate_chat.write"
      # Разрешить писать без задержки
      # Permission for no cooldown
      no-cooldown: "velochat.chat.donate_chat.cooldown"
      # Разрешить использовать цвета
      # Permission to use colors
      use-color: "velochat.msg.use_color"

    format: "<yellow>Чат-донаторов <dark_gray>| %player_prefix% %player_nickname% %player_suffix% <yellow>(%player_server%)</yellow> <dark_gray>» <gray>%message%"

  # Staff-чат
  # Staff chat
  staff-chat:
    # Включить чат
    # Enable chat
    enable: true
    # Команда для отправки сообщения
    # Command to send message
    command: "staff-chat"
    # Синонимы команды
    # Command aliases
    aliases: [ "sc", "staffchat" ]
    # Сервера, на которых должна быть доступна команда.
    # Servers where the command should be available.
    # Если игрок находится на запрещенном сервере, он не сможет ни отправлять, ни получать сообщения
    # If a player is on a restricted server, they will not be able to send or receive messages
    chat-filter:
      # Режим фильтрации:
      # Filtering mode:
      # WHITELIST — Разрешает использование только на серверах из списка
      # WHITELIST — Allows use only on servers in the list
      # BLACKLIST — Разрешает использование на всех серверах кроме тех, которые в списке
      # BLACKLIST — Allows use on all servers except those in the list
      # DISABLE — Не использовать режим фильтрации
      # DISABLE — Do not use filtering mode
      mode: WHITELIST
      # Список серверов
      # List of servers
      servers:
        - hub
        - grief-1
        - grief-2
    # Задержка на отправку сообщений в мс
    # Cooldown for sending messages in ms
    cooldown: 10
    # Формат сообщений
    # Message format
    format: "<blue>Чат-персонала <dark_gray>| %player_prefix% %player_nickname% %player_suffix% <yellow>(%player_server%)</yellow> <dark_gray>» <gray>%message%"

    # Права
    # Permissions
    permissions:
      # Разрешить читать чат
      # Permission to read chat
      read: "velochat.chat.staff_chat.read"
      # Разрешить писать в чат
      # Permission to write to chat
      write: "velochat.chat.staff_chat.write"
      # Разрешить писать без задержки
      # Permission for no cooldown
      no-cooldown: "velochat.chat.staff_chat.cooldown"
      # Разрешить использовать цвета
      # Permission to use colors
      use-color: "velochat.msg.use_color"

# Internal configuration version. Do not modify!
# Внутренняя версия конфигурации. Не редактируйте!
config-version: ${project.version}
```

</details>

## <a id="commands">Команды (/команда → /псевдоним1, /псевдоним2, ... ※ `право`)</a>

### /velochats reload → /vc reload ※ `velochats.main_comamnd.reload`

* Перезагрузить конфигурацию плагина

## <a id="color_support">Форматирование текста</a>

Плагин поддерживает любые виды форматирования текста в Minecraft

- **LEGACY** — Цвет через & / § и HEX через &#rrggbb / §#rrggbb или &x&r&r&g&g&b&b / §x§r§r§g§g§b§b
- **LEGACY_ADVANCED** — Цвет и HEX через &##rrggbb / §##rrggbb
- **MINI_MESSAGE** — Цвет через <цвет> Подробнее — https://docs.advntr.dev/minimessage/index.html

И все форматы доступные на https://www.birdflop.com/resources/rgb/
Вы можете использовать все форматы одновременно в одном сообщении.

## <a id="links">Ссылки</a>

* Связаться: https://t.me/ilezovofficial
* Telegram Channel: https://t.me/ilezzov
* Modrinth: https://modrinth.com/plugin/velochats

## <a id="donate">Поддержать разработчика</a>

* DA: https://www.donationalerts.com/r/ilezov
* YooMoney: https://yoomoney.ru/to/4100118180919675
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
* BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
* Card: 5536914188326494

## Found an issue or have a question? Create a new issue — https://github.com/ilezzov-code/velochats/issues/new

## <a id="license">Лицензия</a>

Этот проект распространяется под лицензией `GPL-3.0 License`. Подробнее см. в файле [LICENSE](LICENSE).
