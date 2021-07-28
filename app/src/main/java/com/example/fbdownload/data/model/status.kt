package com.example.fbdownload.data.model

import java.io.File

class Status(
    var file: File, //    public Bitmap getThumbnail() {
    //        return thumbnail;
    //    }
    //
    //    public void setThumbnail(Bitmap thumbnail) {
    //        this.thumbnail = thumbnail;
    //    }
    //    private Bitmap thumbnail;
    var title: String, path: String
) {

    var path: String
    var isVideo: Boolean

    init {
        title = title
        this.path = path
        val MP4 = ".mp4"
        isVideo = file.name.endsWith(MP4)
    }
}
