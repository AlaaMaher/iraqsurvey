package com.example.asamir.iraqproject.Models;

import android.net.Uri;

public class PhotoModel {
    public Uri officephoto;
    public String ImageUrl;
   public String imageid;
    public PhotoModel(Uri officephoto) {
      this.officephoto=officephoto;
    }


    public Uri getOfficephoto() {
        return officephoto;
    }

    public void setOfficephoto(Uri officephoto) {
        this.officephoto = officephoto;
    }

    public PhotoModel(String imageUrl, String imageid) {
        ImageUrl = imageUrl;
        this.imageid = imageid;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getImageid() {
        return imageid;
    }
}
