package it.unisa.zammarrelli.digitaltwinforcybersecurity.ai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GPTWrapper {
    private OpenAiService service;
    private Language language;
    public GPTWrapper(String token, Language language) {
        this.service = new OpenAiService(token, Duration.ofSeconds(60));
        this.language = language;
    }

    public String analyze(String code){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Individua le vulnerabilità visibili presenti nel codice sorgente.\n" +
                        "La risposta deve essere un Array JSON che contiene degli oggetti con 5 campi:\n" +
                        " \"name\": nome della vulnerabilità.\n" +
                        " \"description\" : descrizione della vulnerabilità.\n" +
                        " \"line\":  il numero della riga in cui la vulnerabilità è stata individuata\n" +
                        " \"code\": il codice vulnerabile\n" +
                        "\"level\": il livello di gravità della vulnerabilità (basso, medio e alto)"+
                "Name and Description will in "+ language.toString()+ "."));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), code));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(1d)
                .maxTokens(1024)
                .topP(1d)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);

        ChatMessage message = result.getChoices().get(0).getMessage();
        System.out.println(message.getContent());

        return message.getContent();
    }


    public String getSolutionCode(){
        return "Potential Solution";
    }
}
