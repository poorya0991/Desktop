package com.mrpteam.amozesh.Models;

import java.io.Serializable;

public class VidModel implements Serializable{
    String name,img,video;
    int id;
    public VidModel(int id, String name, String img,String video ){
        this.id=id;
        this.name=name;
        this.video=video;
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

    public String getVideo() {
        return video;
    }
}

