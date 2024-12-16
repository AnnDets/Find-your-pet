import React, { useState } from 'react';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);  // Сохранение токена в localStorage
                alert('Вход выполнен успешно');
            } else {
                setError('Неверные учетные данные');
            }
        } catch (error) {
            setError('Ошибка подключения к серверу');
            console.error('Ошибка подключения к серверу:', error);
        }
    };

    return (
        <div style={{ maxWidth: 300, margin: '0 auto', padding: 20 }}>
            <h2>Вход</h2>
            <form onSubmit={handleLogin}>
                <label>
                    Email:
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </label>
                <br />
                <label>
                    Пароль:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </label>
                <br />
                <button type="submit">Войти</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
}

export default Login;


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
    
    const  = document.getElementById("languageSwitcher");
    
    const updateLanguage = () => {
        currentLang = document.getElementById("languageSwitcher").value;
        console.log("Current language data:", currentLang);languageSwitcher
    
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
