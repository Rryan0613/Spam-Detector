package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

public class SpamDetector {
    private Map<String, Integer> trainOnDirectory(File directory) throws IOException {
        Map<String, Integer> wordCounts = new HashMap<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split("\\s+");
                        for (String word : words) {
                            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }
        }
        return wordCounts;
    }


    private int fileCount(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
        List<TestFile> testFiles = new ArrayList<>();

        Map<String, Integer> hamWordCounts = trainOnDirectory(new File(mainDirectory, "train/ham"));
        Map<String, Integer> spamWordCounts = trainOnDirectory(new File(mainDirectory, "train/spam"));

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
