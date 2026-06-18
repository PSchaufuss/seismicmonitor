async function loadAllAlerts() {
    const response = await fetch('/api/earthquake-alerts');
    const alerts = await response.json();
    showAlerts(alerts);
}

async function loadActiveAlerts() {
    const response = await fetch('/api/earthquake-alerts/active');
    const alerts = await response.json();
    showAlerts(alerts);
}

function showAlerts(alerts) {
    const tableBody = document.getElementById('alerts-table');
    tableBody.innerHTML = '';

    alerts.forEach(alert => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${alert.id}</td>
            <td>${alert.epicenterLatitude}</td>
            <td>${alert.epicenterLongitude}</td>
            <td>${alert.estimatedMagnitude}</td>
            <td>${alert.status}</td>
            <td>${alert.sensorReadingCount}</td>
            <td>${alert.userReportCount}</td>
            <td>
                <button onclick="updateStatus(${alert.id}, 'ACTIVE')">Set ACTIVE</button>
                <button onclick="updateStatus(${alert.id}, 'FALSE_ALARM')">Set FALSE_ALARM</button>
                <button onclick="updateStatus(${alert.id}, 'NOT_ACTIVE')">Set NOT_ACTIVE</button>
                <button onclick="createUserReport(${alert.id})">Report intensity 5</button>
                <button onclick="loadUserReports(${alert.id})">Show reports</button>
                <button onclick="loadSensorReadings(${alert.id})">Show readings</button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

async function updateStatus(alertId, status) {
    await fetch(`/api/earthquake-alerts/${alertId}/status?status=${status}`, {
        method: 'PATCH'
    });

    loadAllAlerts();
}

async function createUserReport(alertId) {
    await fetch(`/api/earthquake-alerts/${alertId}/user-reports`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            intensity: 5
        })
    });

    loadAllAlerts();
}

async function loadUserReports(alertId) {
    const response = await fetch(`/api/earthquake-alerts/${alertId}/user-reports`);
    const reports = await response.json();

    const container = document.getElementById('user-reports');
    container.innerHTML = `<h3>Reports for alert ${alertId}</h3>`;

    reports.forEach(report => {
        const p = document.createElement('p');
        p.innerText = `Report ID: ${report.id}, intensity: ${report.intensity}, reported at: ${report.reportedAt}`;
        container.appendChild(p);
    });
}

async function loadSensorReadings(alertId) {
    const response = await fetch(`/api/earthquake-alerts/${alertId}/sensor-readings`);
    const readings = await response.json();

    const container = document.getElementById('alert-sensor-readings');
    container.innerHTML = `<h3>Sensor readings for alert ${alertId}</h3>`;

    readings.forEach(reading => {
        const p = document.createElement('p');
        p.innerText = `Reading ID: ${reading.readingId}, sensor: ${reading.sensorId}, magnitude: ${reading.estimatedMagnitude}, distance: ${reading.estimatedDistanceToEpicenterKm} km`;
        container.appendChild(p);
    });
}

loadAllAlerts();