package com.example.carlos.primeiro_projeto.memory;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("chatMemoryController")
@RequestMapping("/api/persistent")
public class PersistentChatMemory {
    private final MessageWindowChatMemory chatMemory;
    private final ChatClient chatClient;

    // chatMemoryRepository: é um repositório de memória de chat que armazena o histórico de mensagens da conversa.
    // Ele é usado para recuperar o histórico de mensagens armazenadas na memória.
    public PersistentChatMemory(ChatClient.Builder builder, ChatMemoryRepository chatMemoryRepository) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10).build();
        this.chatClient = builder.build();
    }

    // Endpoint que demonstra o uso de memória de chat para manter o contexto da conversa entre o usuário e o modelo de linguagem.
    //covenrsationId: é um identificador único para a conversa, que permite ao chatClient recuperar o histórico de mensagens armazenadas na memória.
    @GetMapping("/first")
    public String chatMemoryDemo(@RequestParam String message, @RequestParam String conversationId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                        .param(chatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
