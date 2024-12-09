
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
