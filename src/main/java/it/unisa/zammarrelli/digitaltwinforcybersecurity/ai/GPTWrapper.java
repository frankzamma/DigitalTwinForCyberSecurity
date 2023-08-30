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

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "Locate the vulnerabilities in the source code and output the result in JSON.\n" +
                "The answer must be a JSON Array of objects they have six fields: \"name\", \"description\", \"line\", \"code\", \"new_code\" and \"level\"\n" +
                "\n" +
                " \"name\" is the name of the vulnerability.\n" +
                " \"description\" is a short description of the vulnerability.\n" +
                " \"line\" is the number of the line of code that contains the vulnerability.\n" +
                " \"code\" is the vulnerable code.\n" +
                " \"new_code\" is correct commented code without vulnerability and without new methods.\n" +
                "\"level\": level of vulnerability (potential, real, serious)\n" +
                " \n" +
                " If there aren't any vulnerabilities, return an empty JSON array.\n" +
                "\n" +
                "Name and Description will in "+ language.toString()+ "."));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), code));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-16k")
                .temperature(0.7d)
                .maxTokens(1024)
                .topP(0.5)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);

        ChatMessage message = result.getChoices().get(0).getMessage();
        System.out.println(message.getContent());

        return message.getContent();
    }
}
