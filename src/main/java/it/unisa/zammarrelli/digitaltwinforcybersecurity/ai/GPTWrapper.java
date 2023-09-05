package it.unisa.zammarrelli.digitaltwinforcybersecurity.ai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import it.unisa.zammarrelli.digitaltwinforcybersecurity.common.Language;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GPTWrapper {
    private OpenAiService service;
    private Language language;
    public GPTWrapper(String token, Language language) {
        this.service = new OpenAiService(token, Duration.ofSeconds(60));
        this.language = language;
    }

    public String analyzeFile(String code){
        return  analyze(   "Individua le vulnerabilità visibili presenti nel codice sorgente.\n" +
                "La risposta deve essere un Array JSON che contiene degli oggetti con 5 campi:\n" +
                " \"name\": nome della vulnerabilità.\n" +
                " \"description\" : descrizione della vulnerabilità.\n" +
                " \"line\":  il numero della riga in cui la vulnerabilità è stata individuata\n" +
                " \"code\": il codice vulnerabile\n" +
                "\"level\": il livello di gravità della vulnerabilità (basso, medio e alto)"+
                "Name and Description will in "+ language.toString()+ ".", code);
    }

    public String analyzeLine(String line){

        return analyze("Verify if the line of code is vulnerable.\n" +
                "Answer must is a JSON Object that have 5 fields: \n" +
                "- vulnerable: yes or no\n" +
                "- description: a brief description of vulnerable. If vulnerable = no this field could be \"\".\n" +
                "- severity: potential, medium, serious. If vulnerable = no this field could be \"\".\n" +
                "- solution:  a description to how to solve  . " +
                "- example_code : An example of a solution code or \"\"" +
                "Description and Solution must be in" + language.toString() , line);
    }


    private String analyze(String systemMessage, String userMessage){
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

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

    public List<String> getDynamicAnalysisTool(String language){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Provide a list of  best free dynamic analysis tools" +
                        "for security vulnerabilities of specified programming language.\n" +
                        "Format list in this way: item1;item2;item3;item4;item5"));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), "Example"));
        messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), "tool1;tool2;tool3"));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), language));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(1d)
                .maxTokens(256)
                .topP(1d)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);

        String[] resultStrings = result.getChoices().get(0).getMessage().getContent().split(";");

        return new ArrayList<>(Arrays.stream(resultStrings).collect(Collectors.toList()));
    }

    public String getSolutionCode(){
        return "Potential Solution";
    }
}
