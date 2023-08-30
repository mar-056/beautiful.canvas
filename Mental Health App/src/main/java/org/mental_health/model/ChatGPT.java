/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2023
 * Instructor: Prof. Brian King
 *
 * Name: Yuki Yao
 * Section: 02, 10 am
 * Date: 4/22/2023
 * Time: 3:04 PM
 *
 * Project: csci205_final_project
 * Package: org.mental_health.model
 * Class: ChatGPT
 *
 * Description:
 *
 * ****************************************
 */
package org.mental_health.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * ChatGPT Class that works with the AI to give it text from the user to respond to and then return the AI's response
 */
public class ChatGPT {
    public static String chatGPT(String text) throws Exception {
        //set url and connect
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection  con = (HttpURLConnection) new URL(url).openConnection();

        //set connection specs
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer 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");

        //data specs
        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        //get stream
        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        //buffer read stream from connection
        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}