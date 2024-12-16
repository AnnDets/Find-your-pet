document.getElementById('safe').addEventListener('click', function(event) {
    event.preventDefault();

    const photoInput = document.getElementById('photo');
    const petname = document.getElementById('petname').value;
    const color = document.getElementById('color').value;
    const breed = document.getElementById('breed').value;
    const pet = document.getElementById('pet').value;
    const gender = document.getElementById('gender').value;
    const finddate = document.getElementById('finddate').value;
    const location = "Неизвестно";
    const description = document.getElementById('info').value;
    const accessory = document.getElementById('accessory').checked;

    document.getElementById('errorMessage').style.display = 'none';

    if (photoInput.files.length === 0 || !pet || !color || !breed || !gender || !finddate || !location) {
        document.getElementById('errorMessage').style.display = 'block';
        return;
    }

    // 1. Отправляем фото
    const formData = new FormData();
    formData.append('photo', photoInput.files[0]);

    fetch('uploadPhoto', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка при загрузке фото');
        }
        return response.json(); // Ожидается, что сервер вернет ID или URL фото
    })
    .then(photoData => {
        // Получаем ID или URL загруженного фото
        const photoId = photoData.id || photoData.url;
        const token = localStorage.getItem('token');
        // 2. Отправляем остальные данные
        const reportData = {
            id: 0, // Или значение по умолчанию
            token: token,
            species: pet,
            colors: [color],
            specialMarks: accessory ? ['с аксессуаром'] : [],
            photos: [photoId], // ID или URL загруженного фото
            breed: breed,
            description: description,
            foundDate: finddate,
            location: location,
            gender: gender
        };

        return fetch('uploadPetData', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reportData)
        });
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка при сохранении данных');
        }
        return response.json();
    })
    .then(data => {
        alert('Заявка сохранена!');
        window.location.href = 'home.html'; // Перенаправление
    })
    .catch(error => {
        console.error('Ошибка:', error);
        alert('Произошла ошибка при сохранении заявки.');
    });
});
