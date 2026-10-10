package com.example.carlos.primeiro_projeto;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/advisor")
public class TechAdvisorController {
    private final ChatClient chatClient;
    private final ChatClient chatClientWithDefaultPersona;

    @Value("classpath:/prompts/recipe-prompt.st")
    private Resource recipePrompt;

    public TechAdvisorController(ChatClient.Builder chatClient,
                                 ChatClient.Builder chatClientWithDefaultPersona) {
        this.chatClient = chatClient.build();
        this.chatClientWithDefaultPersona = chatClientWithDefaultPersona.defaultSystem("You are a strict technical interviewer conducting a Java Backend interview"
                + "Ask probing follow-up questions in your response").build();
    }

    @GetMapping("/advise")
    public String Advise(){
        return chatClient.prompt("Explain Spring boot in simple terms.")
                .call()
                .content();
    }

    @GetMapping("/prompt-variables")
    public String PromptVariables(@RequestParam String language,@RequestParam String task){

        // Cria um modelo de prompt com variáveis para linguagem e tarefa.
        PromptTemplate promptTemplate = new PromptTemplate("""
                Write a short {language} code snippet that demonstrates how to {task}. Include comments to explain the code.
                """);

        // Cria um prompt preenchendo as variáveis com os valores fornecidos na requisição.
        Prompt prompt = promptTemplate.create(Map.of("language", language,"task", task));

        return "<pre>" + chatClient.prompt(prompt)
                .call()
                .content()
                + "</pre>";
    }

    @GetMapping("/advise-with-default-persona")
    public String AdviseWithDefaultPersona(@RequestParam String question){
        return chatClientWithDefaultPersona.prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/system-prompt")
    public String SystemPrompts(@RequestParam String person,@RequestParam String question){
        return chatClient.prompt("Explain Spring boot in simple terms.")
                .system("You are a helpful experienced" + person + "Tutor.")
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/external-prompt-file")
    public String externalPrompt(@RequestParam String ingredient){
        PromptTemplate promptTemplate = new PromptTemplate(recipePrompt);
        Prompt prompt = promptTemplate.create(Map.of("ingredient", ingredient));
        return "<pre>" + chatClient.prompt(prompt)
                .call()
                .content()
                + "</pre>";
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
        return "<pre>" + chatClient.prompt(prompt).call().content() + "</pre>";
    }

}
