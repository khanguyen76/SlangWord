/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package slangword;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class SlangWord {
    private String FILE_SLANGWORD = "slangword.txt";
    private String FILE_SLANGWORD_ROOT = "slangword-root.txt";
    public TreeMap<String, List<String>> dictionary = new TreeMap<>();
    private static SlangWord obj = new SlangWord();

    public SlangWord() {
        try {
            String dirPath = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + dirPath);
            readFile(dirPath+"\\"+FILE_SLANGWORD_ROOT);
        } catch (Exception e) {

        }
    }

    public static SlangWord getInstance() {
        if (obj == null) {
            synchronized (SlangWord.class) {
                if (obj == null) {
                    obj = new SlangWord();// instance will be created at request time
                }
            }
        }
        return obj;
    }

    public void readFile(String filePath) throws Exception  {
        dictionary.clear();
        Scanner scanner = new Scanner(new File(filePath));
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) { 
            String line = scanner.next();
            String[] arrSlangWordObj = line.split("`", 0);
            if(arrSlangWordObj.length == 2){
                List<String> value = new ArrayList<>();
                String key = arrSlangWordObj[0];
                String[] arrMeaning = arrSlangWordObj[1].split("\\|", 0);
                for (String meaning : arrMeaning) {
                    String trimString = meaning.trim();
                    String capitalizeString = trimString.substring(0, 1).toUpperCase() + trimString.substring(1);
                    value.add(capitalizeString);
                }
                dictionary.put(key,value);
            }
        }
        scanner.close();
    }
    
    public Map<String, List<String>> getData(){
        return dictionary;
    }
     
    public Map<String, List<String>> findItemByKey(String key){
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        if(!key.isEmpty()){
            results = dictionary.entrySet().stream()
                .filter(x -> x.getKey().toLowerCase().equals(key.toLowerCase()))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            
        }else{
            results = dictionary;
        }
        return results;
    }
}
