package ma.emsi.Chidoub;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

import java.time.Duration;

public class Test1 {

    public static void main(String[] args) {

        // Récupération de la clé API depuis les variables d'environnement
        String cle = System.getenv("GEMINI_API_KEY");

        // Création du modèle Gemini avec quelques paramètres personnalisés
        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.0-flash")      // Nom du modèle utilisé
                .temperature(0.7)                   // Niveau de créativité
                .timeout(Duration.ofSeconds(80))    // Délai maximum de réponse
                .responseFormat(ResponseFormat.JSON)// Format de la réponse
                .build();

        // Construction de la requête de conversation
        ChatRequest requete = ChatRequest.builder()
                .temperature(0.7) // Température plus basse pour la cohérence
                .messages(
                        SystemMessage.from("Réponds en Majuscule"), // Contexte système
                        UserMessage.from("Quelle est la distance en la Terre et le soleil ?") // Message utilisateur
                )
                .build();

        // Envoi de la requête et réception de la réponse
        ChatResponse reponse = modele.chat(requete);

        // Affichage du texte de la réponse et du nombre total de tokens utilisés
        System.out.println(reponse.aiMessage().text());
        System.out.println("Tokens used: " + reponse.tokenUsage().totalTokenCount());
    }
}
