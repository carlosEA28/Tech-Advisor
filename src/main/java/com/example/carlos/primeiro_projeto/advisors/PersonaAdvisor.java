package com.example.carlos.primeiro_projeto.advisors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;

// Este é um exemplo de um advisor personalizado que adiciona uma persona ao prompt do usuário antes de enviar a requisição ao cliente de chat.
public class PersonaAdvisor implements BaseAdvisor {

    private final String persona;
    int order;

    public PersonaAdvisor(String persona, int order) {
        this.persona = persona;
        this.order = order;
    }

    // o metodo before é chamado antes de enviar a requisição ao cliente de chat.
    // Ele recebe o ChatClientRequest e o AdvisorChain como parâmetros. O ChatClientRequest contém o prompt do usuário, que é um objeto Prompt.
    // O AdvisorChain permite que você chame outros advisors na cadeia, se necessário.
    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        Prompt augmented = chatClientRequest.prompt()
                .augmentSystemMessage("You are " + persona + ". Stay in character while answering." + ". Answer about the topic in 2 lines max.");

        return chatClientRequest.mutate().prompt(augmented).build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
