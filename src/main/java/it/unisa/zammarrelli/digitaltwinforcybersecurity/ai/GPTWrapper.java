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
        this.service = new OpenAiService(token, Duration.ofSeconds(0));
        this.language = language;
    }

    public String analyzeFile(String code, boolean bigModel){
        return  analyze(   "Verify if the code contains vulnerability.\n" +
                "The answer must is a JSON Array that contains a object that have this fields:\n" +
                "- name: name of vulnerability.\n" +
                "- description: a brief description of vulnerability.\n" +
                "- line:  the line number of vulnerable code.\n" +
                "- severity: potential, medium or serious\".\n" +
                "- solution: a description to how to solve vulnerability.\n" +
                "- example_solution_code: example code to solve vulnerability.\n" +
                "If there are not any vulnerabilities write [].\n" +
                "Name, description and solution must be in " + this.language, code, bigModel);
    }

    public String analyzeLine(String line){

        return analyze("Verify if the line of code is vulnerable.\n" +
                "Answer must is a JSON Object that have 5 fields: \n" +
                "- vulnerable: yes or no\n" +
                "- severity: potential, medium, serious. If vulnerable = no this field could be \"\".\n" +
                "- description: a brief description of vulnerable. If vulnerable = no this field could be \"\".\n" +
                "- solution:  a description to how to solve. " +
                "- example_code : example code to solve vulnerability" +
                "Description and Solution must be in" + language.toString() , line, false);
    }


    private String analyze(String systemMessage, String userMessage, boolean bigModel){
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(bigModel ? "gpt-3.5-turbo-16k":"gpt-3.5-turbo")
                .temperature(0.55d)
                .maxTokens(bigModel ? 6000: 2048)
                .topP(0.7d)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);

        ChatMessage message = result.getChoices().get(0).getMessage();

        System.out.println(message.getContent());

        return message.getContent();

    }

    public List<String> getDynamicAnalysisTool(String language){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
                "Write the best free tools to perform dynamic analysis of security vulnerabilities" +
                        " of specified programming language.\n" +
                        "Format answer in this way:\n" +
                        "tool1;tool2;tool3\n"+
                "If there aren't any tools answer [-]"));

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), "Example"));
        messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), "tool1;tool2;tool3;tool4;tool5"));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), language));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(1d)
                .maxTokens(256)
                .topP(1d)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);
        String answer = result.getChoices().get(0).getMessage().getContent();

            String[] resultStrings = answer.equals("[-]") ? new String[]{}: answer.split(";");
            return new ArrayList<>(Arrays.stream(resultStrings).collect(Collectors.toList()));
    }


    public String fixJson(String jsonString){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),"Fix JSON formatting. If the text is plain text return []."));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), jsonString));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(1d)
                .maxTokens(1024)
                .topP(1d)
                .messages(messages).build();


        ChatCompletionResult result = this.service.createChatCompletion(request);

        return result.getChoices().get(0).getMessage().getContent();
    }


    public String getGuideForDynamicAnalysis(String programmingLanguage){
        return generateGuide("Create a guide in "+ this.language +" on what is dynamic analysis for vulnerabilities.\n" +
                "It must include a detailed list of principals tools to execute analysis on a software based on specified language.\n" +
                "The guide also include the motivations why It's important execute dynamic and a brief  guide to perform dynamic analysis.\n" +
                "Format the response in HTML.", programmingLanguage);
    }

    public String getGuideForDynamicAnalysis(String tool, String programmingLanguage){
        return generateGuide("Create a step-by-step guide in"+this.language+"to perform dynamic analysis " +
                "for vulnerabilities of a software written in specified language.\n" +
                "It explains how to install "+ tool + ", configure them and run the analysis.\n" +
                "The project it's already configured on Intellij.\n" +
                "Format the response in HTML.", programmingLanguage);
    }

    private String generateGuide(String prompt, String programmingLanguage){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),prompt));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), programmingLanguage));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-16k")
                .temperature(1d)
                .maxTokens(4096)
                .topP(1d)
                .messages(messages).build();


        ChatCompletionResult result = this.service.createChatCompletion(request);

        return result.getChoices().get(0).getMessage().getContent();
    }
}
