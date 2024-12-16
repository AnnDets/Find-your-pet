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


document.addEventListener("DOMContentLoaded", () => {
    const langData = {
        ru: {
            profileTitle: "Мой профиль:",
            name: "Имя:",
            telephone: "Телефон:",
            email: "Email:",
            address: "Адрес:",
            edit: "Редактировать",
            mainPage: "Главная страница",
            chat: "Чат",
            filter: "Фильтр",
            myListing: "Мои объявления",
            instruction: "Инструкция по поиску",
            aboutUs: "О нас",
            exit: "Выход",
            errorMessage: "Ошибка: Пожалуйста, заполните все поля."
        },
        en: {
            profileTitle: "My Profile:",
            name: "Name:",
            telephone: "Phone:",
            email: "Email:",
            address: "Address:",
            edit: "Edit",
            mainPage: "Main Page",
            chat: "Chat",
            filter: "Filter",
            myListing: "My Listings",
            instruction: "Search Instructions",
            aboutUs: "About Us",
            exit: "Exit",
            errorMessage: "Error: Please fill in all fields."
        }
    };

    const languageSwitcher = document.getElementById("languageSwitcher");

    const updateLanguage = () => {
        const currentLang = languageSwitcher.value;

        // Обновляем заголовок профиля
        document.querySelector("#one .styled-headtext").textContent = langData[currentLang].profileTitle;

        // Обновляем текстовые надписи
        document.querySelectorAll(".styled-text").forEach((element, index) => {
            const keys = ["name", "telephone", "email", "address"];
            if (keys[index]) {
                element.textContent = langData[currentLang][keys[index]];
            }
        });

        // Обновляем кнопки
        document.getElementById("Redact").textContent = langData[currentLang].edit;
        document.getElementById("MainPage").textContent = langData[currentLang].mainPage;
        document.getElementById("Chat").textContent = langData[currentLang].chat;
        document.getElementById("Filter").textContent = langData[currentLang].filter;
        document.getElementById("My_listing").textContent = langData[currentLang].myListing;
        document.getElementById("Instruction").textContent = langData[currentLang].instruction;
        document.getElementById("About_us").textContent = langData[currentLang].aboutUs;
        document.getElementById("Exit").textContent = langData[currentLang].exit;

        // Обновляем текст ошибки
        document.getElementById("errorMessage").textContent = langData[currentLang].errorMessage;
    };

    // Слушатель для переключения языка
    languageSwitcher.addEventListener("change", updateLanguage);

    // Устанавливаем начальный язык
    updateLanguage();
});
