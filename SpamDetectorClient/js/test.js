// Sample data to test the UI
// let ourData = {
//   "data": [
//     {
//       "File": "spam_001.txt",
//       "Spam Probability (%)": "85",
//       "Class": "spam"
//     },
//     {
//       "File": "spam_002.txt",
//       "Spam Probability (%)": "95",
//       "Class": "spam"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_002.txt",
//       "Spam Probability (%)": "5",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     },
//     {
//       "File": "ham_001.txt",
//       "Spam Probability (%)": "10",
//       "Class": "ham"
//     }
//   ]
// };

// Add summary stats to the table
function addDataToTable(tableId, data){

  let tableRef = document.getElementById(tableId);
  for (c in data.data){
    let newRow = tableRef.insertRow(-1);
    let fileCell = newRow.insertCell(0);
    let spamCell = newRow.insertCell(1);
    let classCell = newRow.insertCell(2);

    let fileText = document.createTextNode(data.data[c].File);
    let spamText = document.createTextNode(data.data[c]['Spam Probability (%)']);
    let classText = document.createTextNode(data.data[c].Class);

    fileCell.appendChild(fileText);
    spamCell.appendChild(spamText);
    classCell.appendChild(classText);
  }
}
// Accuracy = (numTruePositives + numTrueNegatives) / numFiles
// Put Accuracy into output box with the accuracy ID
function calculateAccuracy(id, data) {
  let numTruePositives = data.numTruePositives;
  let numTrueNegatives = data.numTrueNegatives;
  let numFiles = data.numFiles;

  let numCorrectGuesses = numTruePositives + numTrueNegatives;

  let accuracy = numCorrectGuesses / numFiles;

  let accuracyElem = document.getElementById(id);
  accuracyElem.innerHTML = accuracy;
}
// Precision = numTruePositives / (numTruePositives + numFalsePositives)
// Put precision into output box with the precision ID
function calculatePrecision(id, data) {
  let numTruePositives = data.numTruePositives;
  let numFalsePositives = data.numFalsePositives;

  let total = numTruePositives + numFalsePositives;

  let precision = numTruePositives / total;

  let precisionElem = document.getElementById(id);
  precisionElem.innerHTML = precision;
}
// Testing the UI
// addDataToTable("chart",ourData)
// calculateAccuracy("accuracy", {numTruePositives: 10, numTrueNegatives: 20, numFiles: 50});
// calculatePrecision("precision", {numTruePositives: 10, numFalsePositives: 20, numFiles: 50})
