
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
