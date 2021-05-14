package edu.wpi.teamC;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamC.repository.ConnectionSingleton;
import edu.wpi.teamC.repository.LanguageSingleton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Translate {
    static LanguageSingleton lang = LanguageSingleton.getInstance();

    public static void main(String[] args) throws IOException {
        String text = "Hello world!";
        //Translated text: Hallo Welt!
        lang.setS("es");
        String language = lang.s;
        lang.setS("en");
    }
    public static void translateTextFieldList(LinkedList<TextField> textFieldsToTranslate){
        ArrayList<TextField> a=new ArrayList<>();
        a.addAll(textFieldsToTranslate);
        translateTextFieldList(a);
    }

    public static void translateTextFieldList(ArrayList<TextField> textFieldsToTranslate) {
        String mainTranslation = "";
        for (TextField t : textFieldsToTranslate) {
            if (t != null) {
                mainTranslation = mainTranslation + " ==== " + t.getPromptText();
            } else {
                mainTranslation = mainTranslation + " ==== " + "null";
            }
        }
        try {
            mainTranslation = Translate.translate("en", lang.s, mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings = mainTranslation.split(" ?= ?= ?= ?= ?");

        for (int i = 0; i < textFieldsToTranslate.size(); i++) {
            if (textFieldsToTranslate.get(i) != null) {
                textFieldsToTranslate.get(i).setPromptText(translatedStrings[i + 1]);
            }
        }
    }

    public static void translateTextList(LinkedList<Text> textToTranslate){
        ArrayList<Text> a=new ArrayList<>();
        a.addAll(textToTranslate);
        translateTextList(a);
    }
    public static void translateTextList(ArrayList<Text> textToTranslate){
        String mainTranslation= "";
        for(Text t:textToTranslate){
            if(t!=null) {
                mainTranslation = mainTranslation + " ==== " + t.getText();
            }
            else {
                mainTranslation = mainTranslation + " ==== " + "null";
            }
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" ?= ?= ?= ?= ?");

        for(int i=0;i<textToTranslate.size();i++){
            if(textToTranslate.get(i)!=null) {
                textToTranslate.get(i).setText(translatedStrings[i + 1]);
            }
        }
    }
    public static void translateButtonList(LinkedList<ButtonBase> buttonsToTranslate){
        ArrayList<ButtonBase> a=new ArrayList<>();
        a.addAll(buttonsToTranslate);
        translateButtonList(a);
    }

    public static void translateButtonList(ArrayList<ButtonBase> buttonsToTranslate){
        String mainTranslation= "";
        for(ButtonBase b:buttonsToTranslate){
            if(b!=null) {
                mainTranslation = mainTranslation + " ==== " + b.getText();
            }
            else{
                mainTranslation = mainTranslation + " ==== " + "null";
            }
        }
        try {
            mainTranslation=Translate.translate("en",lang.s,mainTranslation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] translatedStrings=mainTranslation.split(" ?= ?= ?= ?= ?");

        for(int i=0;i<buttonsToTranslate.size();i++){
            if(buttonsToTranslate.get(i)!=null) {
                buttonsToTranslate.get(i).setText(translatedStrings[i + 1]);
            }
        }
    }

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        if (langFrom.equals(langTo)) {
            return text;
        }
        String defaultCharset = Charset.defaultCharset().displayName();
        String urlStr = "https://script.google.com/macros/s/AKfycbw7WYGqobDA3zWfl4drzZqHoOacvdBWQTywXwXwz062m5VcbubjEgpr1NR2ZLJLJpqh/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        String response = new String("");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        if (!langFrom.equals(langTo)) {
            while ((inputLine = in.readLine()) != null) {
                response=response+(inputLine);
            }
            in.close();
            String specialCharHandler= response;
            try {
                specialCharHandler = new String(response.getBytes(defaultCharset),StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(specialCharHandler.contains("&#39;")){
                specialCharHandler=specialCharHandler.replaceAll("&#39;","'");
            }
            return specialCharHandler;

        } else return text;

    }

}