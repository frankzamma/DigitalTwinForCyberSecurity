package it.unisa.zammarrelli.digitaltwinforcybersecurity.ai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GPTWrapper {
    private OpenAiService service;

    public GPTWrapper(String token) {
        this.service = new OpenAiService(token, Duration.ofSeconds(60));
    }

    public String analyze(String code){
        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "Locate the vulnerabilities in the source code and output the result in JSON.\n" +
                "The answer must be a JSON Array of objects they have three fields: \"name\", \"description\" and \"line\".\n" +
                "\"name\" is the name of the vulnerability.\n" +
                "\"description\" is a short description of the vulnerability.\n" +
                "\"line\" is the number of the line of code that contains the vulnerability." +
                "If there aren't any vulnerabilities, return an empty JSON array."));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), code));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-16k")
                .temperature(0.7d)
                .maxTokens(1024)
                .messages(messages).build();

        ChatCompletionResult result = this.service.createChatCompletion(request);

        ChatMessage message = result.getChoices().get(0).getMessage();
        System.out.println(message.getContent());

        return message.getContent();
        // example
        /*return "[" +
                "{\"name\":\"Vulnerability 1\", \"description\":\"Lorem ipsum dolor sit amet, consectetur" +
                " adipiscing elit. Mauris enim augue, dignissim quis imperdiet id, volutpat ut erat. " +
                "Mauris quis quam elementum, tincidunt lectus at, gravida urna. " +
                "Nulla iaculis dolor non pharetra tristique. Morbi faucibus orci erat, id finibus leo tempor vitae. I" +
                "n justo augue, pulvinar vitae pulvinar facilisis, venenatis a nisl. Nulla consequat accumsan dapibus. " +
                "Curabitur dictum est neque, id semper diam molestie vel. Nullam iaculis urna nec odio commodo, " +
                "vel gravida magna porta. Praesent eu odio fringilla, vestibulum arcu vel, porta nibh. Mauris " +
                "id suscipit ligula. Sed imperdiet tortor ut tortor sodales, " +
                "et tincidunt purus congue. Duis tristique velit lectus, " +
                "ut dictum augue venenatis vitae.\", \"line\":23}," +
                "    {\"name\":\"Vulnerability 2\", \"description\":\"Lorem ipsum dolor sit amet, consectetur" +
                " adipiscing elit. Mauris enim augue, dignissim quis imperdiet id, volutpat ut erat. " +
                "Mauris quis quam elementum, tincidunt lectus at, gravida urna. " +
                "Nulla iaculis dolor non pharetra tristique. Morbi faucibus orci erat, id finibus leo tempor vitae. I" +
                "n justo augue, pulvinar vitae pulvinar facilisis, venenatis a nisl. Nulla consequat accumsan dapibus. " +
                "Curabitur dictum est neque, id semper diam molestie vel. Nullam iaculis urna nec odio commodo, " +
                "vel gravida magna porta. Praesent eu odio fringilla, vestibulum arcu vel, porta nibh. Mauris " +
                "id suscipit ligula. Sed imperdiet tortor ut tortor sodales, " +
                "et tincidunt purus congue. Duis tristique velit lectus, " +
                "ut dictum augue venenatis vitae.\", \"line\":23}," +
                "    {\"name\":\"Vulnerability 3\", \"description\":\"Lorem ipsum dolor sit amet, consectetur" +
                " adipiscing elit. Mauris enim augue, dignissim quis imperdiet id, volutpat ut erat. " +
                "Mauris quis quam elementum, tincidunt lectus at, gravida urna. " +
                "Nulla iaculis dolor non pharetra tristique. Morbi faucibus orci erat, id finibus leo tempor vitae. I" +
                "n justo augue, pulvinar vitae pulvinar facilisis, venenatis a nisl. Nulla consequat accumsan dapibus. " +
                "Curabitur dictum est neque, id semper diam molestie vel. Nullam iaculis urna nec odio commodo, " +
                "vel gravida magna porta. Praesent eu odio fringilla, vestibulum arcu vel, porta nibh. Mauris " +
                "id suscipit ligula. Sed imperdiet tortor ut tortor sodales, " +
                "et tincidunt purus congue. Duis tristique velit lectus, " +
                "ut dictum augue venenatis vitae.\", \"line\":13}," +
                "    {\"name\":\"Vulnerability 4\", \"description\":\"Lorem ipsum dolor sit amet, consectetur" +
                " adipiscing elit. Mauris enim augue, dignissim quis imperdiet id, volutpat ut erat. " +
                "Mauris quis quam elementum, tincidunt lectus at, gravida urna. " +
                "Nulla iaculis dolor non pharetra tristique. Morbi faucibus orci erat, id finibus leo tempor vitae. I" +
                "n justo augue, pulvinar vitae pulvinar facilisis, venenatis a nisl. Nulla consequat accumsan dapibus. " +
                "Curabitur dictum est neque, id semper diam molestie vel. Nullam iaculis urna nec odio commodo, " +
                "vel gravida magna porta. Praesent eu odio fringilla, vestibulum arcu vel, porta nibh. Mauris " +
                "id suscipit ligula. Sed imperdiet tortor ut tortor sodales, " +
                "et tincidunt purus congue. Duis tristique velit lectus, " +
                "ut dictum augue venenatis vitae.\", \"line\":20}" +
                "]";*/
    }
}
