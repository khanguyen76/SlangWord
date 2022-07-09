/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package slangword;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class SlangWord {
    private String FILE_SLANGWORD = "slangword.txt";
    private String FILE_SLANGWORD_ROOT = "slangword-root.txt";
    private String FILE_HISTORY = "slangword-history.txt";
    public TreeMap<String, List<String>> dictionary = new TreeMap<>();
    private static SlangWord obj = new SlangWord();

    public SlangWord() {
        try {
            readFile();
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

    public void readFile() {
        try {
            dictionary.clear();
            String dirPath = new java.io.File(".").getCanonicalPath();
            Scanner scanner = new Scanner(new File(dirPath + "\\" + FILE_SLANGWORD));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] arrSlangWordObj = line.split("`", 0);
                if (arrSlangWordObj.length == 2) {
                    List<String> value = new ArrayList<>();
                    String key = arrSlangWordObj[0];
                    String[] arrMeaning = arrSlangWordObj[1].split("\\|", 0);
                    for (String meaning : arrMeaning) {
                        String trimString = meaning.trim();
                        String capitalizeString = trimString.substring(0, 1).toUpperCase() + trimString.substring(1);
                        value.add(capitalizeString);
                    }
                    dictionary.put(key, value);
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveFile() {
        PrintWriter pw = null;
        try {
            String dirPath = new java.io.File(".").getCanonicalPath();
            pw = new PrintWriter(new File(dirPath + "\\" + FILE_SLANGWORD));
            Map<String, List<String>> slangMap = dictionary;
            for (String key : slangMap.keySet()) {
                String meanings = "";
                int i = 0;
                for (String meaning : slangMap.get(key)) {
                    meanings += meaning + (i == slangMap.get(key).size() - 1 ? "" : "| ");
                    i++;
                }
                pw.write(key + "`" + meanings + "\n");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }

    public Map<String, List<String>> getData() {
        return dictionary;
    }
//  SEARCH ---------------------------------------------------------------------------------

    public Map<String, List<String>> findItemByKey(String key) {
        Map<String, List<String>> results = new HashMap<>();
        if (!key.isEmpty()) {
            results = dictionary.entrySet().stream()
                    .filter(x -> x.getKey().toLowerCase().equals(key.toLowerCase()))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        } else {
            results = dictionary;
        }
        saveHistory(results);
        return results;
    }

    public Map<String, List<String>> findItemByValue(String value) {
        Map<String, List<String>> results = new HashMap<>();
        if (!value.isEmpty()) {
            results = dictionary.entrySet().stream()
                    .filter(x -> x.getValue().stream().filter(d -> d.toLowerCase().contains(value.toLowerCase())).count() > 0)
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().stream().filter(d -> d.toLowerCase().contains(value.toLowerCase())).collect(Collectors.toList())));

        } else {
            results = dictionary;
        }
        saveHistory(results);
        return results;
    }
//  HISTORY ---------------------------------------------------------------------------------

    public void saveHistory(Map<String, List<String>> slangMap) {
        FileWriter fw = null;
        try {
            String dirPath = new java.io.File(".").getCanonicalPath();
            fw = new FileWriter(new File(dirPath + "\\" + FILE_HISTORY), true);
            for (String key : slangMap.keySet()) {
                String meanings = "";
                int i = 0;
                for (String meaning : slangMap.get(key)) {
                    meanings += meaning + (i == slangMap.get(key).size() - 1 ? "" : "| ");
                    i++;
                }
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                fw.write(key+"`"+meanings+"`"+timestamp+"\n");
                fw.write(key + "`" + meanings + "\n");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
            }
        }
    }

    public LinkedHashMap<String, List<String>> readHistory() {
        LinkedHashMap<String, List<String>> historyMap = new LinkedHashMap<>();
        try {
            String dirPath = new java.io.File(".").getCanonicalPath();
            Scanner scanner = new Scanner(new File(dirPath + "\\" + FILE_HISTORY));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] arrSlangWordObj = line.split("`", 0);
                if (arrSlangWordObj.length == 2) {
                    List<String> value = new ArrayList<>();
                    String key = arrSlangWordObj[0];
                    String[] arrMeaning = arrSlangWordObj[1].split("\\|", 0);
                    for (String meaning : arrMeaning) {
                        String trimString = meaning.trim();
                        String capitalizeString = trimString.substring(0, 1).toUpperCase() + trimString.substring(1);
                        value.add(capitalizeString);
                    }
                    historyMap.put(key, value);
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return historyMap;
    }
//  ADD NEW --------------------------------------------------------------------------------- 

    public Boolean checkDuplicate(String slang, String meaning) {
        return dictionary.entrySet().stream().filter(x -> x.getKey().toLowerCase().equals(slang.toLowerCase())).count() > 0;
    }

    public void addNewSlangWord(String slang, String meaning) {
        List<String> meanings = new ArrayList<>();
        meanings.add(meaning);
        dictionary.put(slang, meanings);
        saveFile();
    }
    
    public void editSlangWord(String slang, String meaning) {
        List<String> meanings = new ArrayList<>();
        meanings.add(meaning);
        dictionary.put(slang, meanings);
        saveFile();
    }
    
    public void deleteSlangWord(String slang) {
        dictionary.remove(slang);
        saveFile();
    }
    
    public void reset(){
        try {
            dictionary.clear();
            String dirPath = new java.io.File(".").getCanonicalPath();
            Scanner scanner = new Scanner(new File(dirPath + "\\" + FILE_SLANGWORD_ROOT));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] arrSlangWord = line.split("`", 0);
                if (arrSlangWord.length == 2) {
                    List<String> value = new ArrayList<>();
                    String key = arrSlangWord[0];
                    String[] arrMeaning = arrSlangWord[1].split("\\|", 0);
                    for (String meaning : arrMeaning) {
                        String trimString = meaning.trim();
                        if(trimString.length() > 0){
                            String capitalizeString = trimString.substring(0, 1).toUpperCase() + trimString.substring(1);
                            value.add(capitalizeString);
                        }else{
                            value.add(trimString);
                        }
                    }
                    dictionary.put(key, value);
                }
            }
            scanner.close();
        } catch (IOException ex) {
            Logger.getLogger(SlangWord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object randomSlangWord () {
        Map<String, List<String>> slangMap = dictionary;
        Object[] Keys = slangMap.keySet().toArray();
        Object key = Keys[new Random().nextInt(Keys.length)];
        return key;
    }
}
