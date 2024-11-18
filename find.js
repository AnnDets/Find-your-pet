
document.getElementById('safe').addEventListener('click', function() {
    event.preventDefault();
    const photo = document.getElementById('photo').value;
    const petname = document.getElementById('petname').value;
    const color = document.getElementById('color').value;
    const breed = document.getElementById('breed').value;
    const pet = document.getElementById('pet').value;
    const gender = document.getElementById('gender').value;
    const finddate = document.getElementById('finddate').checked;
    const accessory = document.getElementById('accessory').checked;
    const info = document.getElementById('info').checked;

    document.getElementById('errorMessage').style.display = 'none';
    if (photo === '' || pet === '' || color === '' || gender === '' || finddate === '' || accessory === '') {
        document.getElementById('errorMessage').style.display = 'block';
    } else {
        document.getElementById('errorMessage').style.display = 'none';
        alert('Заявка сохранена!');
    window.location.href = 'home.html';
    }

    
});
