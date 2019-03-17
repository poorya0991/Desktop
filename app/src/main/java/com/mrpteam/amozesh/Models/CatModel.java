package com.mrpteam.amozesh.Models;

import java.io.Serializable;

public class CatModel implements Serializable{
    String name,img;
    int id;
    public CatModel(int id, String name, String img ){
        this.id=id;
        this.name=name;

        this.img=img;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }


}

