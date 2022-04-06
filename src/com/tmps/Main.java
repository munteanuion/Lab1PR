package com.tmps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String query = "http://me.utm.md";

        HttpURLConnection connection = null;

        StringBuilder sb = new StringBuilder();

        try{
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.connect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line;
                while((line = in.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");


                }
            }else{
                System.out.println("error connection, code response "+connection.getResponseCode()+connection.getResponseMessage());
            }
        }catch (Throwable cause){
            cause.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }

        String string = sb.toString();

        String[] parts = string.split("<img");

        String[] result1;
        String[] result2;

        ArrayList<String> imgArray = new ArrayList<String>();

        for (int i=1; i<parts.length; i++)
        {
            result1 = parts[i].split("src=\"",2);
            if(result1.length != 1)
            {
                result2 = result1[1].split("\"",2);
                if (result2[0].matches(".*.jpg.*") || result2[0].matches(".*.jpeg.*") || result2[0].matches(".*.png.*")){
                    imgArray.add(result2[0]);
                }
            }
        }
        for (int i=1; i<parts.length; i++)
        {
            result1 = parts[i].split("src='",2);
            if(result1.length != 1)
            {
                result2 = result1[1].split("'",2);
                if (result2[0].matches(".*.jpg.*") || result2[0].matches(".*.jpeg.*") || result2[0].matches(".*.png.*")){
                    imgArray.add(result2[0]);
                }
            }
        }

        System.out.println(imgArray);

    }
}
