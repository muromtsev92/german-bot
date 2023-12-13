package com.example.germanbot.service;

import com.example.germanbot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        super(config.getToken());
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch(messageText){
                case "/start":
                    startCommandReceive(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default:
                    sendMessage(chatId, "no such command");
            }
        }
    }

    private void startCommandReceive(long chatId, String name){
        String answer = "Hi, " + name + "!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(text);

        try{
            execute(sm);
        } catch (TelegramApiException e){

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
