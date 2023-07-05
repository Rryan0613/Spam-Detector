let apiCallURLData = "http://localhost:8080/spamDetector-1.0/api/spam";
let apiCallURLAccuracy = "http://localhost:8080/spamDetector-1.0/api/spam/accuracy";
let apiCallURLPrecision = "http://localhost:8080/spamDetector-1.0/api/spam/precision";
function requestSpamData(callURL) {
  const tableId = "chart";
  const tbodyId = "data";
  setLoading(tbodyId,true);
  fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        },
    }).then(response => response.json())
      .then(response => {
        console.log(JSON.stringify(response))
        setLoading(tbodyId,false);
        // Call function to add data to table
        addDataToTable(tbodyId, response);
      })
      .catch((err) => {
        console.log("Something went wrong: " + err);
      });
}

function requestAccuracy(callURL) { // fetch accuracy from server
  const id = "accuracy";
  setLoading(id,true);
  fetch(callURL, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
    },
    }).then(response => response.json())
      .then(response => {
        console.log(JSON.stringify(response));
        setLoading(id,false);
        // Call function to calculate
        displayAccuracy(id, response)
      })
      .catch((err) => {
        console.log("Something went wrong: " + err);
      });
}

function requestPrecision(callURL) { // fetch precision from server
  const id = "precision";
  setLoading(id,true);
  fetch(callURL, {
    method: 'GET',
    headers: {
      'Accept': 'application/json'
    },
  }).then(response => response.json())
    .then(response => {
      console.log(JSON.stringify(response));
      setLoading(id,false);
      // Call function to calculate
      displayPrecision("precision", response)
    })
    .catch((err) => {
      console.log("Something went wrong: " + err)
    });
}

(function () { // calling
  // initialization
  requestSpamData(apiCallURLData)
  requestAccuracy(apiCallURLAccuracy)
  requestPrecision(apiCallURLPrecision)
})()

