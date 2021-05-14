package edu.wpi.teamC.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LanguageSingleton {

        // static variable single_instance of the singleton
        private static edu.wpi.teamC.repository.LanguageSingleton single_instance = null;

        public String s;

        // private constructor restricted to this class itself
        private LanguageSingleton()
        {
            s = "de";
        }

    public void setS(String s) {
        this.s = s;
    }

    // static method to create instance of singleton class
        public static edu.wpi.teamC.repository.LanguageSingleton getInstance()
        {
            if (single_instance == null)
                single_instance = new edu.wpi.teamC.repository.LanguageSingleton();

            return single_instance;
        }


    }



