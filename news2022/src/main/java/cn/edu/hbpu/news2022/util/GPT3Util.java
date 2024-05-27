package cn.edu.hbpu.news2022.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class GPT3Util {
    public static String sendPost(String data) throws JSONException{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + "sk-DVtX85h0amfb3SFF3TfMdZ0hwSl8jRrLUmCUp5WiRoZ9AhXG");
        httpHeaders.add("Content-Type", "application/json");

        String escapedData = JSONObject.quote(data);
        String requestJson = String.format(
                "{\n" +
                        "\"model\": \"gpt-3.5-turbo\",\n" +
                        "\"temperature\": 0,\n" +
                        "\"max_tokens\": 2048,\n" +
                        "\"messages\": [{\"role\": \"user\", \"content\": %s}]\n" +
                        "}", escapedData
        );
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.chatanywhere.tech/v1/chat/completions", entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray choices = jsonObject.getJSONArray("choices");
        String text = choices.getJSONObject(0).getJSONObject("message").getString("content");
        return text;
    }
}