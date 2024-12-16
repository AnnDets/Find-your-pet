document.getElementById('register').addEventListener('click', function (event) {
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
    } else if (password !== repassword) {
        document.getElementById('errorMessage2').style.display = 'block';
    } else if (!agreementChecked) {
        document.getElementById('errorMessage3').style.display = 'block';
    } else {
        // Формируем JSON для отправки
        const formData = {
            username: username,
            address: adress,
            phone: telephone,
            email: email,
            password: password,
        };

        // Отправляем запрос на сервер
        fetch( 'http://localhost:8080/find-your-pet/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then(response => {
                if (response.ok) {
                    alert('Регистрация успешна!');
                    window.location.href = 'home.html'; // Укажите адрес вашей домашней страницы
                    console.log("Ответ сервера: " + response)
                } else {
                    alert('Ошибка регистрации');
                }
            })
            .catch(error => {
                console.error('Ошибка соединения с сервером:', error);
                alert('Ошибка соединения');
            });
    }
});
