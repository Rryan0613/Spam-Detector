// Add summary stats to the table
function addDataToTable(tableId, data){
  let tableRef = document.getElementById(tableId);
  for (let c of data){
    let newRow = tableRef.insertRow(-1);
    let fileCell = newRow.insertCell(0);
    let spamCell = newRow.insertCell(1);
    let classCell = newRow.insertCell(2);

    let fileText = document.createTextNode(c["file"]);
    let spamPercent = parseFloat(c["spamProbRounded"]) * 100;
    let spamText = document.createTextNode(spamPercent.toFixed(4).toString());
    let classText = document.createTextNode(c["actualClass"]);

    fileCell.appendChild(fileText);
    spamCell.appendChild(spamText);
    classCell.appendChild(classText);
  }
}

// Put Accuracy into output box with the accuracy ID
function displayAccuracy(id, data) {
  let accuracyElem = document.getElementById(id);
  accuracyElem.innerHTML = data.accuracy;
}

// Put precision into output box with the precision ID
function displayPrecision(id, data) {
  let precisionElem = document.getElementById(id);
  precisionElem.innerHTML = data.precision;
}

function setLoading(id,loading){
  const tbody = document.getElementById(id);
  if(loading) {
    tbody.classList.add("loader");
    return;
  }
  tbody.classList.remove("loader");
}
