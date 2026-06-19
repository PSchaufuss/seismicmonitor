async function loadSensorData(){
    const response = await fetch('/api/sensor-data');

    if (response.status === 403) {
        alert('Access denied: This page requires ADMIN role.');
        return;
    }

    const sensorData = await response.json();

    const tableBody = document.getElementById('sensor-data-table');
    tableBody.innerHTML = '';

    sensorData.forEach(reading => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${reading.id}</td>
            <td>${reading.readingId}</td>
            <td>${reading.sensorId}</td>
            <td>${reading.latitude}</td>
            <td>${reading.longitude}</td>
            <td>${reading.estimatedDistanceToEpicenterKm}</td>
            <td>${reading.estimatedMagnitude}</td>
            <td>${reading.recordedAt}</td>
        `;

        tableBody.appendChild(row);
    });
}

loadSensorData();