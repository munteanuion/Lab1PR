package com.tmps;

import com.sun.jdi.InternalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Lab1 extends Thread {
    Semaphore downloadImage;

    String query = "http://me.utm.md";

    HttpURLConnection connection = null;

    StringBuilder sb = new StringBuilder();
    String response;
    String[] parts;
    String[] result1;
    String[] result2;

    ArrayList<String> imgArray = new ArrayList<String>();
    ArrayList<String> imgArrayClean = new ArrayList<String>();

    Set<String> imgs = new HashSet<>();

    public Lab1(int countForSemaphore) {
        this.downloadImage = new Semaphore(countForSemaphore);
    }

    public void ExtragereArrImg()
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

        response = sb.toString();
        parts = response.split("<img");

        for (int i=1; i < parts.length; i++)
        {
            ExtragerePath("src=\"","\"",i);
            ExtragerePath("src='","'",i);
        }

        connection.disconnect();

        imgArray.forEach((n) ->
        {
            result1 = n.split("/");
            imgArrayClean.add(result1[result1.length-1]);
        });
    }

    private void ExtragerePath(String cond1,String cond2, int i)
    {
        result1 = parts[i].split(cond1,2);
        if(result1.length != 1)
        {
            result2 = result1[1].split(cond2,2);
            if (result2[0].matches(".*.jpg.*") || result2[0].matches(".*.jpeg.*") || result2[0].matches(".*.png.*"))
            {
                imgArray.add(result2[0]);
            }
        }
    }

    @Override
    public void run()
    {
        imgArray.forEach((n) ->
        {
            String url;
            if (n.contains("http")){
                url = "";
            }else{
                url = query+"/";
            }
            try
            {
                downloadImage.acquire();
                System.out.println(url+n);
                try(InputStream in = new URL(url+n).openStream()){
                    Files.copy(in, Paths.get("C:\\Users\\ionio\\IdeaProjects\\lab1-PR\\src\\img\\"+imgArrayClean.get(imgArray.indexOf(n))), StandardCopyOption.REPLACE_EXISTING);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }  finally {
                    downloadImage.release();
                }

            }catch (InternalException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}


