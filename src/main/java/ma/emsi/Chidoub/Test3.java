package ma.emsi.Chidoub;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3 {
    public static void main(String[] args) {

        String key = System.getenv("GEMINI_API_KEY");
        if (key == null || key.isBlank()) {
            System.err.println("GEMINI_API_KEY not set");
            return;
        }


        EmbeddingModel modele = GoogleAiEmbeddingModel.builder()
                .apiKey(key)
                .modelName("text-embedding-004")
                .taskType(GoogleAiEmbeddingModel.TaskType.SEMANTIC_SIMILARITY)
                .outputDimensionality(300) //Demensionnalité de l'embedding demandée par Mr. Richard
                .timeout(Duration.ofSeconds(1))
                .build();

        String phrase1 = "Comment puis-je me rendre à la gare ?";
        String phrase2 = "Où se trouve la gare la plus proche ?";

        Response<Embedding> reponse1 = modele.embed(phrase1);
        Response<Embedding> reponse2 = modele.embed(phrase2);

        Embedding emb1 = reponse1.content();
        Embedding emb2 = reponse2.content();

        if (emb1 == null || emb2 == null) {
            System.err.println("Failed to obtain embeddings.");
            return;
        }

        double similarite = CosineSimilarity.between(emb1, emb2);
        System.out.println("Similarité cosinus : " + similarite);
    }
}