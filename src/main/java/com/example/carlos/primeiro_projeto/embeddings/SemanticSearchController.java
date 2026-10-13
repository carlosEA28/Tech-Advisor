package com.example.carlos.primeiro_projeto.embeddings;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.vectorstore.SimpleVectorStore;

@RestController
@RequestMapping("/api/semantic")
public class SemanticSearchController {

    private final EmbeddingModel embeddingModel;

    public SemanticSearchController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embed")
    public EmbeddingResult embed(String text) {
        // Esse método gera o embedding do texto fornecido usando o modelo de embedding e
        //  retorna um objeto EmbeddingResult contendo o texto, a dimensão do vetor e o próprio vetor.
        float[] vector = embeddingModel.embed(text);

        return new EmbeddingResult(text, vector.length, vector);
    }

    @GetMapping("/similarity")
    public SimilarityResult similarityResult(String text1, String text2) {
        // Esse método calcula a similaridade entre dois textos fornecidos usando o modelo de embedding e
        // retorna um objeto SimilarityResult contendo os textos, a similaridade e os vetores correspondentes.
        float[] vector1 = embeddingModel.embed(text1);
        float[] vector2 = embeddingModel.embed(text2);

        // Calcula a similaridade entre os vetores usando a função de similaridade cosseno
        double similarity = SimpleVectorStore.EmbeddingMath.cosineSimilarity(vector1, vector2);

        return new SimilarityResult(text1, text2, similarity);
    }
}
