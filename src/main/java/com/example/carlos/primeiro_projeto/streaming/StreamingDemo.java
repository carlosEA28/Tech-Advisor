package com.example.carlos.primeiro_projeto.streaming;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/streaming")
public class StreamingDemo {

    private ChatClient chatClient;

    public StreamingDemo(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping(value = "/sse-streaming",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    //flux: O método Flux é usado para retornar uma sequência de elementos, que podem ser emitidos ao longo do tempo.
    // Isso é útil para casos em que queremos enviar dados em tempo real para o cliente, como em um streaming de eventos.
    public Flux<String> courseSummary(@RequestParam String topic){
        String prompt = """
                Write 5 fun facts about %s, one per line
                """.formatted(topic);

        //strem: O método stream() é usado para receber a resposta do modelo de linguagem em tempo real, à medida que o modelo gera a resposta.
        // Isso é útil para casos em que a resposta pode ser longa ou quando queremos exibir a resposta ao usuário à medida que ela é gerada.
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content();

    }
}
