document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Вы не авторизованы!');
        window.location.href = 'login.html';
        return;
    }

    try {
        // Запрос к серверу для получения данных пользователя
        const response = await fetch(`user`, {
            method: 'GET',
            headers: {
                'token': token,
            },
        });

        if (!response.ok) {
            throw new Error('Не удалось получить данные пользователя.');
        }

        const userData = await response.json();

        // Заполнение полей профиля
        document.getElementById('username').value = userData.name || '';
        document.getElementById('telephone').value = userData.phone || '';
        document.getElementById('email').value = userData.email || '';
        document.getElementById('adress').value = userData.address || '';
        document.getElementById('Myfoto').style.backgroundImage = `url(${userData.photo || 'default-photo.jpg'})`;
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось загрузить данные профиля.');
    }
});
