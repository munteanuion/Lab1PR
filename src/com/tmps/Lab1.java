package com.tmps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Lab1 {
    String query = "http://me.utm.md";

    HttpURLConnection connection = null;

    StringBuilder sb = new StringBuilder();
    String string;

    String[] parts;

    String[] result1;
    String[] result2;

    ArrayList<String> imgArray = new ArrayList<String>();

    public void MainMain()
    {
        try{
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);

            connection.connect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode())
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line;

                while((line = in.readLine()) != null)
                {
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

        string = sb.toString();
        parts = string.split("<img");

        for (int i=1; i < parts.length; i++)
        {
            ExtragerePath("src=\"","\"",i);
            ExtragerePath("src='","'",i);
        }

        System.out.println(imgArray);

        connection.disconnect();
    }

    private void ExtragerePath(String cond1,String cond2, int i)
    {
        result1 = parts[i].split(cond1,2);
        if(result1.length != 1)
        {
            result2 = result1[1].split(cond2,2);
            if (result2[0].matches(".*.jpg.*") || result2[0].matches(".*.jpeg.*") || result2[0].matches(".*.png.*")){
                imgArray.add(result2[0]);
            }
        }
    }
}


