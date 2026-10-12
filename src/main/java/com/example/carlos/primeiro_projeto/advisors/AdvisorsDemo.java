package com.example.carlos.primeiro_projeto.advisors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/advisors")
public class AdvisorsDemo {

    private ChatClient chatClient;

    public AdvisorsDemo(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // This endpoint demonstrates the use of a simple logger advisor to log the conversation with the chat client.
    @GetMapping(value = "/logging")
    public String logAdvisor(@RequestParam String topic) {
        SimpleLoggerAdvisor advisor = SimpleLoggerAdvisor.builder()
                .requestToString(request -> "PROMPT: " + request.prompt().getContents())
                .responseToString(response -> "RESPONSE: " + response.getResult().getOutput().toString())
                .build();

        return chatClient.prompt()
                .user("Tell me a funny joke about " + topic)
                .advisors(advisor)
                .call()
                .content();
    }

    // safeguards: e um advisor que permite definir palavras sensíveis e uma resposta de falha personalizada caso o modelo tente gerar uma resposta sobre um tópico sensível.
    @GetMapping(value = "/safeguards")
    public String safeGuards(@RequestParam String topic) {
        SafeGuardAdvisor safeGuardAdvisor = SafeGuardAdvisor.builder()
                .sensitiveWords(List.of("politics", "religion", "violence", "drugs"))
                .failureResponse("Sorry, I cannot provide a joke about that topic.")
                .build();

        return chatClient.prompt()
                .user("Tell me a funny joke about " + topic)
                .advisors(safeGuardAdvisor)
                .call()
                .content();
    }

    @GetMapping(value = "/persona")
    public String persona(@RequestParam String persona, @RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .advisors(new PersonaAdvisor(persona, 0))
                .call()
                .content();
    }

    @GetMapping(value = "/disclaimer")
    public String disclaimer(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .advisors(new DisclaimerAdvisor(0))
                .call()
                .content();
    }

    @GetMapping(value = "/timming")
    public String timming(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .advisors(new TimmingAdviser(0))
                .call()
                .content();
    }

    @GetMapping(value = "/chained")
    public String chained(@RequestParam String persona, @RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .advisors(new TimmingAdviser(0), new PersonaAdvisor(persona, 1), new SimpleLoggerAdvisor(1), new DisclaimerAdvisor(2))
                .call()
                .content();
    }
}

