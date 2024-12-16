document.getElementById('petForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const photo = document.getElementById('photo').files[0]; // Получаем файл
    const petname = document.getElementById('petname').value;
    const color = document.getElementById('color').value;
    const breed = document.getElementById('breed').value;
    const pet = document.getElementById('pet').value;
    const gender = document.getElementById('gender').value;
    const finddate = document.getElementById('finddate').value;
    const accessory = document.getElementById('accessory').checked;
    const info = document.getElementById('info').value;

    document.getElementById('errorMessage').style.display = 'none';

    if (!petname || !color || !breed || !pet || !gender || !finddate) {
        document.getElementById('errorMessage').style.display = 'block';
        return;
    }

    // JSON-объект для отправки текстовых данных
    const jsonData = {
        petname: petname,
        color: color,
        breed: breed,
        pet: pet,
        gender: gender,
        finddate: finddate,
        accessory: accessory,
        info: info
    };

    // Отправляем JSON данные
    fetch('http://localhost:8080/api/savePetData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to save pet data');
        }
        return response.json();
    })
    .then(data => {
        if (photo) {
            // Отправляем фото, если оно выбрано
            const formData = new FormData();
            formData.append('photo', photo);

            return fetch(`http://localhost:8080/api/uploadPhoto/${data.petId}`, { // Используем ID из ответа
                method: 'POST',
                body: formData
            });
        }
    })
    .then(() => {
        alert('Заявка сохранена!');
        window.location.href = 'home.html';
    })
    .catch(error => {
        console.error('Error saving pet data:', error);
        alert('Произошла ошибка при сохранении заявки.');
    });
});
