package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)  throws IOException {

        String nasaUrl = "https://api.nasa.gov/planetary/apod" +
                "?api_key=pU6GLrZhy9QWb3sORPXoZVzO9ZPOveEYNF7hEzrj" +
                "&date=2023-12-14";

        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(nasaUrl);
        CloseableHttpResponse response = client.execute(request);

//        // получаем в консоль содержимое json-файла как ответ на запрос
//        // с запрашиваемой информацией
//        Scanner scanner = new Scanner(response.getEntity().getContent());
//        System.out.println(scanner.nextLine());


        NasaAnswer answer = mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);

        CloseableHttpResponse image = client.execute(new HttpGet(answer.url));

        // в имени файла будет дата
        String fileName = "Image" + LocalDate.now() + ".jpg";
        // сохраняем картинку на ПК
        String[] answerSplitted = answer.url.split("/");
        FileOutputStream file = new FileOutputStream(answerSplitted[answerSplitted.length - 1]);
        image.getEntity().writeTo(file);
    }
}