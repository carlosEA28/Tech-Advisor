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
    public String logAdvisor(@RequestParam String topic){

        SimpleLoggerAdvisor advisor = SimpleLoggerAdvisor.builder().
                requestToString(request -> "PROMPT: " + request.prompt().getContents())
                .responseToString(response -> "RESPONSE: " + response.getResult().getOutput().toString())
                .build();

        return chatClient.prompt()
                .user("Tell me a funny joke about " + topic)
                .advisors(advisor)
                .call()
                .content();

    }

    @GetMapping(value = "/safeguards")
    public String safeGuards(@RequestParam String topic){

        SafeGuardAdvisor safeGuardAdvisor =  SafeGuardAdvisor.builder()
                .sensitiveWords(List.of("politics", "religion", "violence","drugs"))
                .failureResponse("Sorry, I cannot provide a joke about that topic.")
                .build();

        return chatClient.prompt()
                .user("Tell me a funny joke about " + topic)
                .advisors(safeGuardAdvisor)
                .call()
                .content();

    }
}
