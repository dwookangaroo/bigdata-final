package com.example.mygooglemap

class sfdsdf {
    // csv파일 읽어오기
    var assetManager: `val` = resources.assets
    var inputStream: `val` = assetManager.open("posts.json")
    var jsonString: `val` = inputStream.bufferedReader().use {
        it.readText()
    }
}