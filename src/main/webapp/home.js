document.addEventListener("DOMContentLoaded", function () {
    const container = document.getElementById('threeone');

    // Вызов API
    fetch('getReport') // Замените URL на ваш API-эндпоинт
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка при получении данных из API");
            }
            return response.json();
        })
        .then(data => {
            // Ожидается массив объектов, например:
            // [
            //   { "id": 1, "url": "one.jpg", "name": "Pet Name 1" },
            //   { "id": 2, "url": "two.jpg", "name": "Pet Name 2" }
            // ]
            data.forEach((item, index) => {
                // Создание кнопки
                const button = document.createElement('button');
                button.classList.add('image-button');
                button.style.backgroundImage = `url(${item.photos[0]})`;

                button.id = `petButton${item.id}`; // Используем id из API
                console.log('${item.photos[0]}')
                // Добавление подсказки (например, имени)
                button.title = item.name;

                // Событие для клика по кнопке
                button.addEventListener('click', () => {
                    alert(`Вы выбрали: ${item.name}`); // Пример действия при нажатии
                });

                // Добавление кнопки в контейнер
                container.appendChild(button);
            });
        })
        .catch(error => {
            console.error("Ошибка загрузки изображений:", error);
        });
});

document.getElementById('find').addEventListener('click', async function(event) {
    event.preventDefault();

    const token = localStorage.getItem('token');
    if (token) {
        try {
            const response = await fetch('verifyToken', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                window.location.href = 'find.html';
            } else {
                alert('Сессия истекла. Пожалуйста, войдите снова.');
                localStorage.removeItem('token');
                window.location.href = 'Login.html';
            }
        } catch (error) {
            console.error('Ошибка проверки токена:', error);
            alert('Ошибка подключения к серверу.');
        }
    } else {
        alert('Вы должны войти в систему, чтобы продолжить.');
        window.location.href = 'Login.html';
    }
});

document.getElementById('lost').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'lost.html';
});


document.getElementById('Profile').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'profile.html';
});

document.getElementById('Chat').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'messenger.html';
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
    window.location.href = 'findi-your-pet/home.html';
});
