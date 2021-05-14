package edu.wpi.teamC;

public class TranslateLanguageOption {
    private String text;
    private String ID;

    public TranslateLanguageOption(String text, String ID) {
        this.text = text;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return this.text;
    }
    public String getID(){
        return ID;
    }
}
