package com.android.jh.runtimepermission;

import java.util.ArrayList;

/**
 * 전화번호부 POJO(Pure old Java Object)
 */

public class Contact {
    private int id;
    private String name;
    private ArrayList<String> number ;

    public Contact(){
        number = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(ArrayList<String> number) {
        this.number = number;
    }

    public void addNumber(String number) {
        this.number.add(number);
    }
    public void removeNumber(String number){
        this.number.remove(number);
    }
}
