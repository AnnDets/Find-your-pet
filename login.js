
document.getElementById('login').addEventListener('click', function() {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    document.getElementById('errorMessage').style.display = 'none';

    if (email === '' || password === '') {
        document.getElementById('errorMessage').style.display = 'block';
    } else {
        document.getElementById('errorMessage').style.display = 'none';
        
        alert('Вы вошли в аккаунт!');
        window.location.href = 'home.html';
    }

    // Создание объекта с данными
    const userData = {
        password: password,
        email: email
    };

});

document.addEventListener("DOMContentLoaded", () => {
    const langData = {
        ru: {
            title:"Вход",
            email: "Введите ваш email:",
            password: "Введите пароль:",
            login: "Войти",
            errorMessage: "Ошибка: Пожалуйста, заполните все поля."
        },
        en: {
            title:"Login",
            email: "Enter your email:",
            password: "Enter your password:",
            login: "Login",
            errorMessage: "Error: Please fill in all fields."
        }
    };
    
    const languageSwitcher = document.getElementById("languageSwitcher");
    
    const updateLanguage = () => {
        currentLang = document.getElementById("languageSwitcher").value;
        console.log("Current language data:", currentLang);
    
        // Переводим текстовые элементы
        document.getElementById("title").textContent = langData[currentLang].title;

        const texts = [
            langData[currentLang].email,
            langData[currentLang].password,
            langData[currentLang].login
        ];

        const styledTexts = document.querySelectorAll(".styled-text");
        styledTexts.forEach((element, index) => {
        element.textContent = texts[index];

        const errors=[
            langData[currentLang].errorMessage];

        const errorTexts = document.querySelectorAll(".error");
        errorTexts.forEach((element, index) => {
        element.textContent = errors[index];
        });
    });
};
    
// Слушатель изменения языка
languageSwitcher.addEventListener("change", updateLanguage);

// Инициализация
updateLanguage();
});
