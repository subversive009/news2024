package cn.edu.hbpu.news2022;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpGPTTest {
    @Test
    public static String sendPost(String data) throws JSONException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + "sk-DVtX85h0amfb3SFF3TfMdZ0hwSl8jRrLUmCUp5WiRoZ9AhXG");
        httpHeaders.add("Content-Type", "application/json");

        String requestJson = String.format(
                "{\n" +
                        "\"model\": \"gpt-3.5-turbo\",\n" +
                        "\"temperature\": 0,\n" +
                        "\"max_tokens\": 2048,\n" +
                        "\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]\n" +
                        "}", data
        );
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.chatanywhere.tech/v1/chat/completions", entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray choices = jsonObject.getJSONArray("choices");
        String text = choices.getJSONObject(0).getJSONObject("message").getString("content");
        return text;
    }

    public static void main(String[] args) throws JSONException {
        System.out.println(sendPost("你好"));
    }
}