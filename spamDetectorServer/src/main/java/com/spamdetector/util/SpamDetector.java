package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

/**
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {
    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
        // Provide the probability map
        Map<String,Double> probMap = trainModel(mainDirectory);

        // Get the files
        File[] spamFiles = new File(mainDirectory,"/test/spam").listFiles();
        File[] hamFiles = new File(mainDirectory,"/test/ham").listFiles();
        if(spamFiles == null || hamFiles == null){
            throw new NullPointerException("One of the file arrays is empty:");
        }

        // Generate the list of TestFile objects
        List<TestFile> testFiles = new ArrayList<>();
        testFiles.addAll(toTestFiles(spamFiles,probMap,"Spam"));
        testFiles.addAll(toTestFiles(hamFiles,probMap,"Ham"));

        return testFiles;
    }

    public Map<String,Double> trainModel(File mainDirectory) throws IOException {
        Map<String,Double> probabilities = new HashMap<>();

        File[] spamFiles = new File(mainDirectory,"/train/spam").listFiles();
        File[] hamFiles = new File(mainDirectory,"/train/ham").listFiles();
        File[] hamFiles2 = new File(mainDirectory,"/train/ham2").listFiles();
        double numHam = hamFiles.length + hamFiles2.length;
        double numSpam = spamFiles.length;

        // Get maps for freq of spam and ham words
        Map<String,Integer> trainSpamFreq = new HashMap<>();
        Map<String,Integer> trainHamFreq = new HashMap<>();
        addWordFreqs(trainSpamFreq,spamFiles);
        addWordFreqs(trainHamFreq,hamFiles);
        addWordFreqs(trainHamFreq,hamFiles2);

        // Iterate through every word encountered
        Set<String> words = new HashSet<>();
        words.addAll(trainSpamFreq.keySet());
        words.addAll(trainHamFreq.keySet());
        for(String word : words){
            // Start with 1 to avoid infinity error
            trainSpamFreq.put(word,trainSpamFreq.getOrDefault(word,1));
            trainHamFreq.put(word,trainHamFreq.getOrDefault(word,1));
            double probW_S = trainSpamFreq.get(word) / numSpam;
            double probW_H = trainHamFreq.get(word) / numHam;
            double probS_W = probW_S / (probW_H + probW_S);
            probabilities.put(word,probS_W);
        }
        return probabilities;
    }

    public ArrayList<TestFile> toTestFiles(File[] files, Map<String,Double> probMap, String actualClass) throws IOException {
        ArrayList<TestFile> testFiles = new ArrayList<>();
        for(File file : files){
            double probability = calculateSpamProbability(file,probMap);
            testFiles.add(new TestFile(file.getName(),probability,actualClass));
        }
        return testFiles;
    }

    // Returns a HashMap associating words to the number of files in which they occur
    public void addWordFreqs(Map<String,Integer> wordFreqs, File[] files) throws IOException {
        for(File file : Objects.requireNonNull(files)){
            Set<String> words = getWordsFromFile(file);

            for(String word : words){
                word = word.toLowerCase();
                // Start with 1 to offset adding 1 in trainModel()
                int count = wordFreqs.getOrDefault(word, 1);
                wordFreqs.put(word, count + 1);
            }
        }
    }

    public HashSet<String> getWordsFromFile(File file) throws IOException {
        HashSet<String> words = new HashSet<>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while((line = in.readLine()) != null){
            String[] strings = line.split(" ");
            for(String word : strings){
                if(!word.equals("")){
                    words.add(word);
                }
            }
        }
        return words;
    }

    public double calculateSpamProbability(File file, Map<String,Double> probMap) throws IOException {
        Set<String> words = getWordsFromFile(file);
        double n = 0;
        for(String word : words){
            if(probMap.containsKey(word)){
                n += Math.log(1-probMap.get(word)) - Math.log(probMap.get(word));
            }
        }
        return 1/(1+Math.pow(Math.E,n));
    }
}

