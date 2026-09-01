package mtgcollection.controller;

import mtgcollection.domain.OllamaService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
@CrossOrigin
public class OllamaController {

    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService){
        this.ollamaService = ollamaService;
    }

    @GetMapping("/{message}")
    public ResponseEntity<String> getModelResponse(@PathVariable String message){
        return new ResponseEntity<>(ollamaService.getModelResponse(message),HttpStatus.OK);
    }

}
