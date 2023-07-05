# Spam Detector



## Developers:
* #### Ryan Liu
* #### Jathushan Vishnukaran

## Project Overview
Our project uses naive Bayes spam filtering to find the probability that an email, given in a text file, is spam.
The training files are used to create a map that associates each word to the probability of a file being spam given that it contains the word.

## API
After training the model, we use to test on additional spam and ham files. Then we store the data in an object of the class TestFile. We calculate the precision and accuracy. Then send those data in a JSON file to the server by creating an HTTP GET request for the API.

The Rest API module is created using Jakarta RESTful Web Service. Then the server is ran through glassfish.

## How to Run
In order to clone and run our application, follow these steps:
1. Clone the project from GitHub into a local directory (e.g. a folder named “SpamDetection”)
2. Open the directory from Step 1 in Intellij IDEA and run the Maven build script
3. Set up Glassfish configuration by following the instructions of Intellij IDEA
    -  when prompted to select deployment artifacts, choose the option ending in “:war exploded”
4. Run the Glassfish server and wait for deployment
5. Open “index.html”

## Resources
main.css - https://github.com/h5bp/main.css
normalize.css - https://github.com/necolas/normalize.css
loader.css - https://cssloaders.github.io
HTML Boilerplate - https://html5boilerplate.com
Improvement Ideas - https://www.baeldung.com/cs/naive-bayes-classification-performance
