package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 */
public class SpamDetector {

    public Map<String, Double> getPrecision(List<TestFile> testFiles) {
        int numTruePositives = 0;
        int numFalsePositives = 0;
        int numFiles = 0;

        for (TestFile test : testFiles) {
            if (test.getActualClass().equals("Spam")) {
                if (test.getSpamProbability() > 0.5) {
                    numTruePositives++;
                } else {
                    numFalsePositives++;
                }
            }
            numFiles++;
        }

        double precision = (double) numTruePositives / (numFalsePositives + numTruePositives);
        Map<String, Double> precisionMap = new TreeMap<>();
        precisionMap.put("precision", precision);
        return precisionMap;
    }

    public Map<String, Double> getAccuracy(List<TestFile> testFiles) {
        int numTruePositives = 0;
        int numFalsePositives = 0;
        int numFiles = 0;

        for (TestFile test : testFiles) {
            if (test.getActualClass().equals("Spam")) {
                if (test.getSpamProbability() > 0.5) {
                    numTruePositives++;
                } else {
                    numFalsePositives++;
                }
            }
            numFiles++;
        }

        double accuracy = (double) (numTruePositives + numFalsePositives) / numFiles;
        Map<String, Double> accuracyMap = new TreeMap<>();
        accuracyMap.put("accuracy", accuracy);
        return accuracyMap;
    }

    private int fileCount(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    public Map<String, Integer> wordFreqDir(File dir) throws IOException {
        Map<String, Integer> frequencies = new TreeMap<>();

        File[] filesInDir = dir.listFiles();
        if (filesInDir == null) {
            // handle the error here
            return frequencies;
        }

        int numFiles = filesInDir.length;

        // iterate over each file in the dir and count their words
        for (File file : filesInDir) {
            Map<String, Integer> wordMap = countWordFile(file);

            // merge the file wordMap into the global frequencies
            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                String word = keyIterator.next();
                int count = wordMap.get(word);

                if (frequencies.containsKey(word)) {
                    // increment
                    int oldCount = frequencies.get(word);
                    frequencies.put(word, count + oldCount);
                } else {
                    frequencies.put(word, count);
                }
            }

        }

        return frequencies;
    }

    private Map<String, Integer> countWordFile(File file) throws IOException {
        Map<String, Integer> wordMap = new TreeMap<>();
        if(file.exists()){
            // load all the data and process it into words
            Scanner scanner  = new Scanner(file);
            while(scanner.hasNext()){
                // ignore the casing for words
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)){
                    // add the word if not exists yet
                    if(!wordMap.containsKey(word)){
                        wordMap.put(word, 1);
                    }
                    // increment the count if exists
                    else{
                        int oldCount = wordMap.get(word);
                        wordMap.put(word, oldCount+1);
                    }
                }
            }
        }
        return wordMap;
    }

    private Boolean isWord(String word){
        if (word == null){
            return false;
        }

        String pattern = "^[a-zA-Z]*$";
        if(word.matches(pattern)){
            return true;
        }

        return false;

    }

    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
        List<TestFile> testFiles = new ArrayList<>();

        Map<String, Integer> hamWordCounts = wordFreqDir(new File(mainDirectory, "train/ham"));
        Map<String, Integer> spamWordCounts = wordFreqDir(new File(mainDirectory, "train/spam"));

        File testDirectory = new File(mainDirectory, "test");
        File[] testFilesArray = testDirectory.listFiles();
        assert testFilesArray != null;
        for (File file : testFilesArray) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                double spamProbability = 0.0;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (hamWordCounts.containsKey(word) && spamWordCounts.containsKey(word)) {
                            double hamCount = hamWordCounts.get(word);
                            double spamCount = spamWordCounts.get(word);
                            spamProbability += Math.log((spamCount / fileCount(new File(mainDirectory, "train/spam")))
                                    / (hamCount / fileCount(new File(mainDirectory, "train/ham"))));
                        }
                    }
                }
                spamProbability = 1.0 / (1.0 + Math.exp(-spamProbability));
                String actualClass = file.getParentFile().getName();
                TestFile testFile = new TestFile(file.getName(), spamProbability, actualClass);
                testFiles.add(testFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return testFiles;
    }


}