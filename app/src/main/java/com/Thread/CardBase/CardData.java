package com.Thread.CardBase;

public class CardData {
    int id;
    String title;
    public CardData(int id,String title){
        this.id = id;
        this.title= title;
    }
    public String getTitle() {
        return title;
    }
    public int getId(){
        return id;
    }
}
