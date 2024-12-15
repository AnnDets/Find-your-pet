
document.getElementById('register').addEventListener('click', function() {
    event.preventDefault(); // Останавливаем стандартное поведение кнопки
    const username = document.getElementById('username').value;
    const adress = document.getElementById('adress').value;
    const telephone = document.getElementById('telephone').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const repassword = document.getElementById('repassword').value;
    const agreementChecked = document.getElementById('agreement').checked;

    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('errorMessage2').style.display = 'none';
    document.getElementById('errorMessage3').style.display = 'none';

    if (username === '' || adress === '' || telephone === '' || email === '' || password === '' || repassword === '') {
        document.getElementById('errorMessage').style.display = 'block';
    } else if(password != repassword){
        document.getElementById('errorMessage2').style.display = 'block';
    } else if (!agreementChecked) {
        document.getElementById('errorMessage3').style.display = 'block';
    } else {
        document.getElementById('errorMessage').style.display = 'none';
        alert('Регистрация успешна!');
        // Если все проверки пройдены, перенаправляем на домашнюю страницу
    window.location.href = 'home.html'; // Укажите адрес вашей домашней страницы
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const langData = {
        ru: {
            registration: "Регистрация",
            username: "Введите ваше имя:",
            adress: "Введите ваш адрес:",
            telephone: "Введите ваш телефон:",
            email: "Введите ваш email:",
            password: "Введите пароль:",
            repassword: "Повторите пароль:",
            mandatoryFields: "Поля со * являются обязательными",
            agreement: "Я согласен с условиями",
            register: "Зарегистрироваться",
            errorMessage: "Ошибка: Пожалуйста, заполните все поля.",
            errorMessage2: "Ошибка: Пароль не совпадает.",
            errorMessage3: "Ошибка: Согласитесь с политикой конфиденциальности."
        },
        en: {
                registration: "Registration",
                username: "Enter your name:",
                adress: "Enter your address:",
                telephone: "Enter your phone:",
                email: "Enter your email:",
                password: "Enter password:",
                repassword: "Repeat password:",
                mandatoryFields: "Fields marked with * are mandatory",
                agreement: "I agree to the terms",
                register: "Register",
                errorMessage: "Error: Please fill in all fields.",
                errorMessage2: "Error: Password does not match.",
                errorMessage3: "Error: Agree to the privacy policy."
        }
    };

    const languageSwitcher = document.getElementById("languageSwitcher");
    
    const updateLanguage = () => {
        currentLang = document.getElementById("languageSwitcher").value;
        console.log("Current language data:", currentLang);

        
        // Обновляем текстовые поля напрямую
        document.getElementById("registration").textContent = langData[currentLang].registration;

        const texts = [
            langData[currentLang].username,
            langData[currentLang].adress,
            langData[currentLang].telephone,
            langData[currentLang].email,
            langData[currentLang].password,
            langData[currentLang].repassword
        ];

        const styledTexts = document.querySelectorAll(".styled-text");
        styledTexts.forEach((element, index) => {
        element.textContent = texts[index];
        });
        //document.querySelector(".styled-redtext").textContent = langData[currentLang].mandatoryFields;
        document.getElementById("agreement").nextSibling.textContent = langData[currentLang].agreement;
        document.getElementById("register").textContent = langData[currentLang].register;

        const errors=[
            langData[currentLang].errorMessage,
            langData[currentLang].errorMessage2,
            langData[currentLang].errorMessage3];

        const errorTexts = document.querySelectorAll(".error");
        errorTexts.forEach((element, index) => {
        element.textContent = errors[index];
        });
    };
    // Слушатель изменения языка
    languageSwitcher.addEventListener("change", updateLanguage);

    // Инициализация
    updateLanguage();
});
