package com.example.carlos.primeiro_projeto.memory;

import org.springframework.ai.chat.messages.Message;

// Classe que representa uma mensagem de chat com um papel (role) e conteúdo (content).
public record ChatMessageView(String role, String content) {

    // Método estático que cria uma instância de ChatMessageView a partir de um objeto Message.
    public  static  ChatMessageView from(Message message){
        return new ChatMessageView(message.getMessageType().getValue(), message.getText());
    }
}
