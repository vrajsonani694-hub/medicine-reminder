window.onload = function () {
    displayMedicines();

    if (Notification.permission !== "granted") {
        Notification.requestPermission();
    }
};

function addMedicine() {

    let medicine = document.getElementById("medicine").value;
    let dosage = document.getElementById("dosage").value;
    let time = document.getElementById("time").value;

    if (!medicine || !dosage || !time) {
        alert("Please fill all fields");
        return;
    }

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    medicines.push({
        medicine,
        dosage,
        time
    });

    localStorage.setItem(
        "medicines",
        JSON.stringify(medicines)
    );

    document.getElementById("medicine").value = "";
    document.getElementById("dosage").value = "";
    document.getElementById("time").value = "";

    displayMedicines();
}

function displayMedicines() {

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    let container =
        document.getElementById("medicineContainer");

    container.innerHTML = "";

    medicines.forEach((med, index) => {

        container.innerHTML += `
        <div class="medicine-item">
            <h3>${med.medicine}</h3>
            <p>Dosage: ${med.dosage}</p>
            <p>Time: ${med.time}</p>

            <button onclick="deleteMedicine(${index})">
                Delete
            </button>
        </div>`;
    });
}

function deleteMedicine(index) {

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    medicines.splice(index, 1);

    localStorage.setItem(
        "medicines",
        JSON.stringify(medicines)
    );

    displayMedicines();
}

// Check every 30 seconds
setInterval(() => {

    let now = new Date();

    let currentTime =
        String(now.getHours()).padStart(2, "0")
        + ":"
        + String(now.getMinutes()).padStart(2, "0");

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    medicines.forEach(med => {

        if (med.time === currentTime) {

            alert(
                "💊 Time To Take: "
                + med.medicine
            );

            let alarm =
                new Audio("reminder.mp3");

            alarm.play();

            if (
                Notification.permission ===
                "granted"
            ) {
                new Notification(
                    "Medicine Reminder",
                    {
                        body:
                        med.medicine +
                        " - " +
                        med.dosage
                    }
                );
            }
        }
    });

}, 30000);