package com.spamdetector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jdk.incubator.vector.VectorOperators;

@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();
    ObjectMapper mapper = new ObjectMapper();
    List<TestFile> testResults = new ArrayList<>();

    SpamResource(){
//        TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
    }

    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object
/*
{"spamProbRounded":"0.00000","file":"00006.654c4ec7c059531accf388a807064363","spamProbability":5.901245803391957E-62,"actualClass":"Ham"}
getSpamResults must get the SpamProbabilityRounded, fileName, spamProbability, actualClass
 */
        return null;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
        try {
            List<TestFile> testFiles = detector.trainAndTest(new File("data/test"));
            int truePositives = 0;
            int falsePositives = 0;
            int trueNegative = 0;
            int placeholder = 0;
            double accuracy = 0;
            double threshold = 0.5;

            for (TestFile file : testFiles) {
                if (file.getSpamProbability() >= threshold) {
                    String actualClass = file.getActualClass();
                    if (actualClass.equals("/spam")) {
                        truePositives++;
                    } else if (actualClass.equals("/ham")) {
                        falsePositives++;
                    }
                }

                if (file.getSpamProbability() <= threshold) {
                    String actualClass = file.getActualClass();
                    if (actualClass.equals("/spam")) {
                        placeholder++;
                    } else if (actualClass.equals("/ham")) {
                        trueNegative++;
                    }
                }
            }
            accuracy = (double) truePositives / (truePositives + falsePositives);
            accuracy = truePositives + trueNegative;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //      TODO: return the precision of the detector, return in a Response object
/*
this is supposed to take the formula of TruePositives / FalsePositives + TruePositives, take Accuracy as an example
 */
        return null;
    }

    private List<TestFile> trainAndTest() throws IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        File mainDirectory = null;
        return this.detector.trainAndTest(mainDirectory);
    }
}