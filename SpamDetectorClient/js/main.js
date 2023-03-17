
function retrieveSpamResults() {
  // Send a GET request to the /spam endpoint of the server
  fetch('/spam', {
    method: 'GET',function retrieveSpamResults() {
    // Send a GET request to the /spam endpoint of the server
    fetch('/spam', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        // Parse the response data as JSON
        return response.json();
      })
      .then(data => {
        // Populate the UI with the retrieved data
        // For example, if you have a table with id "spam-table", you can fill it like this:
        const spamTable = document.getElementById('spam-table');
        data.forEach(spamResult => {
          const row = spamTable.insertRow(-1);
          const fileNameCell = row.insertCell(0);
          const spamProbabilityCell = row.insertCell(1);
          const actualClassCell = row.insertCell(2);
          fileNameCell.innerHTML = spamResult.file;
          spamProbabilityCell.innerHTML = spamResult.spamProbRounded;
          actualClassCell.innerHTML = spamResult.actualClass;
        });
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

// Call the retrieveSpamResults() function when the page is loaded
  window.onload = retrieveSpamResults;
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      // Parse the response data as JSON
      return response.json();
    })
    .then(data => {
      // Populate the UI with the retrieved data
      // For example, if you have a table with id "spam-table", you can fill it like this:
      const spamTable = document.getElementById('spam-table');
      data.forEach(spamResult => {
        const row = spamTable.insertRow(-1);
        const fileNameCell = row.insertCell(0);
        const spamProbabilityCell = row.insertCell(1);
        const actualClassCell = row.insertCell(2);
        fileNameCell.innerHTML = spamResult.file;
        spamProbabilityCell.innerHTML = spamResult.spamProbRounded;
        actualClassCell.innerHTML = spamResult.actualClass;
      });
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

// Call the retrieveSpamResults() function when the page is loaded
window.onload = retrieveSpamResults;


(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const accuracyInput = document.getElementById("accuracy");
        accuracyInput.value = data.precision;
      })
  .catch((err) => {console.log("something went wrong: " + err);});
  }
)();

(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/precision",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const precisionInput = document.getElementById("precision");
        precisionInput.value = data.precision;
      })
  .catch((err) => {console.log("something went wrong: " + err);});
  }
)();
