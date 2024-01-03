package com.example.germanbot.service;

import com.example.germanbot.config.BotConfig;
import com.example.germanbot.model.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;


    public TelegramBot(BotConfig config) {
        super(config.getToken());
        this.config = config;
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "start"));
        commandList.add(new BotCommand("/game", "guess a word"));
        try{
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error("Error during creating command list: " + e.getMessage());
        }
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

                case "/game":

                    break;

                default:
                    sendMessage(chatId, "no such command");
            }
        }
    }

    private void startCommandReceive(long chatId, String name){
        String answer = "Hi, " + name + "!";
        log.info("replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void startCommandReceiveGame(long chatId){
        Word word = new Word();
        sendMessage(chatId, "Переведи на немецкий: " + word.getRus());

    }

    private void sendMessage(long chatId, String text) {
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(text);

        try{
            execute(sm);
        } catch (TelegramApiException e){
            log.error("Error: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

}
