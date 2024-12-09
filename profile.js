const button = document.getElementById('Myfoto');
        const fileInput = document.getElementById('fileInput');

        // Добавляем обработчик события на нажатие кнопки
        button.addEventListener('click', function () {
            fileInput.click(); // "Кликаем" по скрытому input для выбора файла
        });

        // Обработчик события изменения выбора файла
        fileInput.addEventListener('change', function (event) {
            const file = event.target.files[0]; // Получаем файл из input

            if (file) {
                const reader = new FileReader(); // Создаем объект FileReader

                // Устанавливаем обработчик события, который загрузит изображение
                reader.onload = function (e) {
                    button.style.backgroundImage = `url(${e.target.result})`; // Изменяем фоновое изображение кнопки
                };

                // Читаем файл как Data URL для установки в качестве фонового изображения
                reader.readAsDataURL(file);
            }
        });
document.getElementById('Redact').addEventListener('click', function() {
    event.preventDefault(); // Останавливаем стандартное поведение кнопки
    const username = document.getElementById('username').value;
    const adress = document.getElementById('adress').value;
    const telephone = document.getElementById('telephone').value;
    const email = document.getElementById('email').value;

    document.getElementById('errorMessage').style.display = 'none';

    if (username === '' || adress === '' || telephone === '' || email === '') {
        document.getElementById('errorMessage').style.display = 'block';
    } else {
        document.getElementById('errorMessage').style.display = 'none';
    }

    // Создание объекта с данными
    const userData = {
        username: username,
        adress: adress,
        password: password,
        email: email
    };

    /*// Отправка данных на сервер
    fetch('https://26.100.72.2:8080/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка регистрации');
        }
        return response.json();
    })*/
    //.then(data => {
        alert('Изменения сохранены успешно!');
        
    
        // Здесь можно перенаправить пользователя или очистить поля формы
    //})
    //.catch(error => {
    //    alert('Ошибка: ' + error.message);
    //});
    
});

document.getElementById('MainPage').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'home.html';
});

document.getElementById('Chat').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'chat.html';
});

document.getElementById('Filter').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'filter.html';
});

document.getElementById('My_listing').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'mylisting.html';
});

document.getElementById('Instruction').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'instruction.html';
});
document.getElementById('About_us').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'aboutus.html';
});

document.getElementById('Exit').addEventListener('click', function() {
    event.preventDefault(); 
    alert('Вы вышли из профиля');
    window.location.href = 'homeguest.html';
});
