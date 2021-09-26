package com.example.mygooglemap;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class JavaCsvHelper {
    private String filePath;

    public JavaCsvHelper(String filePath){
        this.filePath = filePath;
    }

    public List<String[]> readAllCsvData(String fileName){
        File file = new File(filePath + "/" + fileName);

        try (FileReader fr = new FileReader(file);
             CSVReader reader = new CSVReader(fr)){
            return reader.readAll();
        } catch (IOException e){
            if(BuildConfig.DEBUG){
                e.printStackTrace();
            }

            return new ArrayList<>();
        }
    }
}
