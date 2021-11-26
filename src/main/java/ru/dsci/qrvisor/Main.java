package ru.dsci.qrvisor;

import ru.dsci.qrvisor.bot.BotProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            BotProcessor botProcessor = BotProcessor.getInstance();
            log.info("Telegram bot started");
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}
