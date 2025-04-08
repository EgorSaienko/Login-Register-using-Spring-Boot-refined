const emailInput = document.getElementById("userEmail");
const suggestionsBox = document.getElementById("suggestions");
const userIdField = document.getElementById("userId");

emailInput.addEventListener("input", () => {
    const query = emailInput.value;
    if (query.length >= 4) {
        fetch(`/user/search?query=${encodeURIComponent(query)}`)
            .then(res => res.json())
            .then(users => {
                suggestionsBox.innerHTML = "";
                if (users.length > 0) {
                    suggestionsBox.style.display = "block";
                    users.forEach(user => {
                        const div = document.createElement("div");
                        div.className = "suggestion-item";
                        div.textContent = `${user.email} (ID: ${user.id})`;
                        div.onclick = () => {
                            emailInput.value = user.email;
                            userIdField.value = user.id;
                            suggestionsBox.style.display = "none";
                        };
                        suggestionsBox.appendChild(div);
                    });
                } else {
                    suggestionsBox.style.display = "none";
                }
            });
    } else {
        suggestionsBox.style.display = "none";
    }
});

document.addEventListener("click", (e) => {
    if (!suggestionsBox.contains(e.target) && e.target !== emailInput) {
        suggestionsBox.style.display = "none";
    }
});

function validateForm() {
    const equipmentId = document.getElementById("equipmentId").value;
    if (equipmentId < 1) {
        alert("Equipment ID має бути додатнім числом.");
        return false;
    }
    return true;
}

function validateUpdateForm() {
    const equipmentId = document.getElementById("updateEquipmentId").value;
    if (equipmentId < 1) {
        alert("Equipment ID має бути додатнім числом.");
        return false;
    }
    return true;
}
