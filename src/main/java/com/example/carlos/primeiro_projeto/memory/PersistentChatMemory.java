package com.example.carlos.primeiro_projeto.memory;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("persistentChatMemoryController")
@RequestMapping("/api/persistent")
public class PersistentChatMemory {
    private final MessageWindowChatMemory chatMemory;
    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;

    // chatMemoryRepository: é um repositório de memória de chat que armazena o histórico de mensagens da conversa.
    // Ele é usado para recuperar o histórico de mensagens armazenadas na memória.
    public PersistentChatMemory(ChatClient.Builder builder, ChatMemoryRepository chatMemoryRepository) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10).build();
        this.chatClient = builder.build();
        this.chatMemoryRepository = chatMemoryRepository;
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

    @GetMapping("/conversations")
    public String getConversations() {
        return chatMemoryRepository.findConversationIds().toString();
    }

    // Endpoint que retorna o histórico de mensagens de uma conversa específica, dado o seu ID.
    // o stream foi usado pq, vai ser devolvido uma lista de mensagens, e o stream vai percorrer essa lista e transformar cada mensagem em uma string,
    // e depois juntar todas as strings em uma única string separada por vírgulas.
    @GetMapping("/conversations/{conversationId}")
    public List<ChatMessageView> getConversationById(@PathVariable String conversationId) {
        return chatMemoryRepository.findByConversationId(conversationId)
                .stream()
                .map(ChatMessageView::from)
                .toList();
    }

    @DeleteMapping("/conversations/{conversationId}")
    public void deleteConversationById(@PathVariable String conversationId) {
        chatMemoryRepository.deleteByConversationId(conversationId);
    }

    @GetMapping("/users/{userId}/sessions/{sessionId}/chat")
    public String getConversationByIdAndSession (@PathVariable String userID, @PathVariable String sessionId) {
        String conversationId = userID + "-" + sessionId;
        return chatClient.prompt()
                .user("message")
                .advisors(a -> a.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                        .param(chatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}
