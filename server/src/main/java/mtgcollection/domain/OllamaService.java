package mtgcollection.domain;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    private final OllamaChatModel chatModel;

    public OllamaService(OllamaChatModel chatModel){
        this.chatModel = chatModel;
    }

    public String getModelResponse(String message){
        ChatResponse response = chatModel.call(
                new Prompt(
                        message,
                        OllamaChatOptions.builder()
                                .model("llama3.2")
                                .temperature(0.4)
                                .build()
                ));
        return response.getResult().getOutput().getText();
    }
}
