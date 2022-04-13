package com.tmps;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Lab1 lab1 = new Lab1(2);
        // Semaphore thread = new Semaphore(2);

        //lab1.downloadImage = thread;

        lab1.ExtragereArrImg();
        System.out.println(lab1.imgArrayClean);
        lab1.start();
    }
}
