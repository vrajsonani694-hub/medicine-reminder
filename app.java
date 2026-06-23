window.onload = function() {
    displayMedicines();

    if ("Notification" in window) {
        Notification.requestPermission();
    }
};

function addMedicine() {

    let medicine =
        document.getElementById("medicine").value.trim();

    let dosage =
        document.getElementById("dosage").value.trim();

    let time =
        document.getElementById("time").value;

    if (!medicine || !dosage || !time) {
        alert("Please fill all fields");
        return;
    }

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    medicines.push({
        medicine: medicine,
        dosage: dosage,
        time: time
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

    let container =
        document.getElementById("medicineContainer");

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    container.innerHTML = "";

    medicines.forEach((med, index) => {

        let div = document.createElement("div");

        div.className = "medicine-item";

        div.innerHTML = `
            <h3>${med.medicine}</h3>
            <p>Dosage: ${med.dosage}</p>
            <p>Time: ${med.time}</p>

            <button class="delete-btn"
            onclick="deleteMedicine(${index})">
            Delete
            </button>
        `;

        container.appendChild(div);
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

// Reminder Check Every 30 Seconds
setInterval(() => {

    let now = new Date();

    let currentTime =
        String(now.getHours()).padStart(2, "0")
        + ":" +
        String(now.getMinutes()).padStart(2, "0");

    let medicines =
        JSON.parse(localStorage.getItem("medicines")) || [];

    medicines.forEach(med => {

        if (med.time === currentTime) {

            alert(
                "💊 Time To Take: " +
                med.medicine +
                "\nDosage: " +
                med.dosage
            );

            // MP3 Alarm
            let audio = new Audio("reminder.mp3");
            audio.play();

            // Notification
            if (
                Notification.permission === "granted"
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
