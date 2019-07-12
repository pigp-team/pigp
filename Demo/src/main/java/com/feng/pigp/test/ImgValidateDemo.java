package com.feng.pigp.test;

import com.asprise.ocr.Ocr;

import java.io.File;

public class ImgValidateDemo {

    public static void main(String[] args) {

        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_SLOW); // English

        String s = ocr.recognize(new File[] {new File("C:\\Users\\feng\\Desktop\\pin.png")},
                Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);

        System.out.println("Result: " + s);
        ocr.stopEngine();
    }
}