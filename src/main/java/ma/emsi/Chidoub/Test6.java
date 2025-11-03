package ma.emsi.Chidoub;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import ma.emsi.Chidoub.tools.MeteoTool;

import java.time.Duration;
import java.util.Scanner;

public class Test6 {
    public static void main(String[] args) {
        interface AssistantMeteo {
            String chat(String userMessage);
        }

        String llmkey = System.getenv("GEMINI_API_KEY");

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmkey)
                .modelName("gemini-2.0-flash")
                .temperature(0.3)
                .responseFormat(ResponseFormat.TEXT)
                .logRequestsAndResponses(true)
                .timeout(Duration.ofSeconds(80))
                .build();

        AssistantMeteo assistant =
                AiServices.builder(AssistantMeteo.class)
                        .chatModel(model)
                        .tools(new MeteoTool())  // Ajout de l'outil
                        .build();


        //String question = "Quel temps fait-il à Paris ?";
        //String reponse = assistant.chat(question);
        //System.out.println(reponse);
        try (
                Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question : ");
                String question = scanner.nextLine();
                if (question.isBlank()) {
                    continue;
                }
                System.out.println("==================================================");
                if ("fin".equalsIgnoreCase(question)) {
                    break;
                }
                String reponse = assistant.chat(question);
                System.out.println("Assistant : " + reponse);
                System.out.println("==================================================");
            }
        }
    }
}