package com.example.carlos.primeiro_projeto;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advisor")
public class TechAdvisorController {
    private final ChatClient chatClient;

    public TechAdvisorController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping
    public String Advise(){
        return chatClient.prompt("Explain Spring boot in simple terms.")
                .call()
                .content();
    }

    @GetMapping("/conversation-history")
    public String getConversationHistory(@RequestParam String followUpQuestion) {
        // Cria a mensagem de sistema com as instrucoes de comportamento do assistente.
        Message systemMessage =  new SystemMessage("You are a helpful experienced Java Tutor tutor. ");
        // Cria a primeira mensagem do usuario, que inicia a conversa sobre Java records.
        Message fisrtMessage = new UserMessage("What is a java record ? ");
        // Cria a resposta anterior do assistente para preservar o contexto da conversa.
        Message priorReply = new AssistantMessage("A Java record is a special kind of class in Java that is designed to hold immutable data. It was introduced in Java 14 as a preview feature and became a standard feature in Java 16. Records provide a concise way to create classes that are primarily used for storing data without having to write boilerplate code for constructors, getters, equals(), hashCode(), and toString() methods.");
        // Converte a pergunta de acompanhamento recebida na requisicao em uma mensagem do usuario.
        Message newUserMessage = new UserMessage(followUpQuestion);

        // Organiza todas as mensagens na ordem em que aconteceram para formar o historico.
        Prompt prompt = new Prompt(List.of(systemMessage, fisrtMessage, priorReply, newUserMessage));

        // Envia o historico ao modelo, executa a chamada e retorna apenas o texto da resposta.
        return chatClient.prompt(prompt).call().content();
    }

}
