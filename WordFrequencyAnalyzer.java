package mayur.wordTopFrequency;

import com.google.gson.Gson;
import mayur.utility.TaskManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordFrequencyAnalyzer implements Runnable{

    private String filePath;

    public WordFrequencyAnalyzer (String filePath) {
        this.filePath = filePath;
    }

    public static Map<String, Integer> wordFrequencyMap(String document) {
        String doc = document.toLowerCase();

        String[] words = doc.trim().split("\\s+");

        // Storing the frequency of unique words
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(String word : words) {
            if(map.containsKey(word)) {
                int value = map.get(word);
                map.put(word, value + 1);
            }
            else {
                map.put(word, 1);
            }
        }

        // Sort HashMap by Value
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> wordFrequency = new LinkedHashMap<String, Integer>();
        for(Map.Entry<String, Integer> entry : list) {
            wordFrequency.put(entry.getKey(), entry.getValue());
        }

        return wordFrequency;
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public void run() {
        try {
            String document = readFile(filePath);

            Map<String, Integer> wordFrequency = wordFrequencyMap(document);

            // Print the Words by Top Frequency
            int limit = 9;
            for(Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                if(limit-- > 0) {
                    System.out.println(entry.getValue() + " " + entry.getKey());
                }
            }

            String json = new Gson().toJson(wordFrequency);
            System.out.println(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager(2);

        WordFrequencyAnalyzer file1 = new WordFrequencyAnalyzer("C:\\Users\\Mayur.Kharbas\\IdeaProjects\\backend\\data\\IndianNationalAnthem.txt");
        taskManager.waitTillQueueIsFreeAndAddTask(file1);
    }
}
