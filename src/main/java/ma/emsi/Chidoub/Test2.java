package ma.emsi.Chidoub;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.time.Duration;
import java.util.Map;

public class Test2 {
    public static void main(String[] args) {
        PromptTemplate template = PromptTemplate.from("""
            Vous etes un traducteur qui connait tous les langues,
             vous devez traduire ce que l'utilisateur envoie : {{text}} en langue choisi : {{langue}}
           """);

        // Application du template avec les valeurs spécifiques
        Prompt prompt = template.apply(Map.of(
                "text", "Hola como estas?",
                "langue", "anglais"
        ));

        // Récupération de la clé API depuis les variables d'environnement
        String cle = System.getenv("GEMINI_API_KEY");
        ChatModel modele =
                GoogleAiGeminiChatModel.builder()
                        .apiKey(cle)
                        .modelName("gemini-2.0-flash")
                        .temperature(0.5)
                        .timeout(Duration.ofSeconds(80))
                        .responseFormat(ResponseFormat.JSON)
                        .build();

        // Envoi du prompt au modèle et récupération de la réponse
        String reponse = modele.chat(prompt.text());
        System.out.println(reponse);
    }
}