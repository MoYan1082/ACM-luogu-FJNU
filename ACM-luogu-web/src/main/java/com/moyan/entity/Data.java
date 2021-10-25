package com.moyan.entity;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public List<User> data;
    public String time;
    public Data(){
        time = "";
        data = new ArrayList<User>();
    }
}
