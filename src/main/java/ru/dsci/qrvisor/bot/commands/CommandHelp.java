package ru.dsci.qrvisor.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class CommandHelp extends Command {

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText("Функции чат-бота: \n" +
                "- считывание QR-кода: для считывания QR-кода сфотографируйте код и отправьте изображение в чат \n" +
                "- генерация QR-кода: для генерации QR-кода отправьте текст или ссылку в чат");
        super.processMessage(absSender, message, strings);
    }

    public CommandHelp() {
        super("help", "Справка \\help \n");
    }

}
