package com.moyan.entity;

import java.util.ArrayList;
import java.util.List;

public class DataWithRank {
    public List<UserWithRank> data;
    public String time;
    public DataWithRank(){
        time = "";
        data = new ArrayList<UserWithRank>();
    }
}
