document.getElementById('login').addEventListener('click', async function (event) {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const errorMessage = document.getElementById('errorMessage');

        // Очистка сообщения об ошибке
        errorMessage.textContent = '';

        if (!username || !password) {
            errorMessage.textContent = 'Пожалуйста, заполните все поля.';
            return;
        }

        try {
            const response = await fetch('login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token); // Сохранение токена в localStorage
                alert('Вход выполнен успешно');
                // Перенаправление на другую страницу, если нужно
                window.location.href = 'find-your-pet/home.html';
            } else {
                errorMessage.textContent = 'Неверные учетные данные.';
            }
        } catch (error) {
            errorMessage.textContent = 'Ошибка подключения к серверу.';
            console.error('Ошибка подключения к серверу:', error);
        }
    });