package com.example.carlos.primeiro_projeto.structured;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/structured")
public class StructuredDemo {

    private ChatClient chatClient;

    public StructuredDemo(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/course-summary")
    public CourseSummary courseSummary(@RequestParam String topic){
        String prompt = """
                Generate a course summary for a begginer-friendly course on "%s".
                Include a title, the difficulty level, a list of 4-6 learning goals,
                and estimated number of hours to complete the course.
                """.formatted(topic);

        //entity: O metodo entity() é usado para mapear a resposta do modelo de linguagem para uma classe Java específica.
        // Neste caso, estamos mapeando a resposta para a classe CourseSummary, que é uma record class que contém os campos title, level, learningGoals e estimatedHours.
        CourseSummary courseSummary = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(CourseSummary.class);

    // agora podemos acessar os campos no retorno
        System.out.println("Title: " + courseSummary.title());
        System.out.println("Level: " + courseSummary.level());
        System.out.println("Learning Goals: " + courseSummary.learningGoals());
        System.out.println("Estimated Hours: " + courseSummary.estimatedHours());

        return courseSummary;

    }
}
