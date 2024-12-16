
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




document.addEventListener("DOMContentLoaded", () => {
    const langData = {
        ru: {
            title: "Я нашёл животное",
            photo: "Прикрепите фото:",
            petName: "Кличка:",
            color: "Цвет:",
            breed: "Порода:",
            animal: "Животное:",
            gender: "Пол:",
            findDate: "Дата нахождения:",
            accessory: "Наличие аксессуаров:",
            additionalInfo: "Дополнительная информация:",
            mandatoryFields: "Поля со * являются обязательными",
            save: "Сохранить",
            errorMessage: "Ошибка: Пожалуйста, заполните все обязательные поля."
            // petOptions: {
            //     cat: "Кот",
            //     dog: "Собака",
            //     other: "Другое"
            // },
            // genderOptions: {
            //     boy: "Мальчик",
            //     girl: "Девочка",
            //     other: "Другое"
            // },
            // accessoryOptions: {
            //     no: "Нет",
            //     yes: "Да"
            // }
        },
        en: {
            title: "I found a pet",
            photo: "Attach a photo:",
            petName: "Pet name:",
            color: "Color:",
            breed: "Breed:",
            animal: "Animal:",
            gender: "Gender:",
            findDate: "Date found:",
            accessory: "Accessories present:",
            additionalInfo: "Additional information:",
            mandatoryFields: "Fields marked with * are mandatory",
            save: "Save",
            errorMessage: "Error: Please fill in all mandatory fields."
            // petOptions: {
            //     cat: "Cat",
            //     dog: "Dog",
            //     other: "Other"
            // },
            // genderOptions: {
            //     boy: "Boy",
            //     girl: "Girl",
            //     other: "Other"
            // },
            // accessoryOptions: {
            //     no: "No",
            //     yes: "Yes"
            // }
        }
    };
    const languageSwitcher = document.getElementById("languageSwitcher");

    const updateLanguage = () => {
        const currentLang = document.getElementById("languageSwitcher").value;
    
        // Безопасно обновляем текстовые элементы
        document.getElementById("title").textContent = langData[currentLang].title;

        const texts = [
            langData[currentLang].photo,
            langData[currentLang].petName,
            langData[currentLang].color,
            langData[currentLang].breed,
            langData[currentLang].animal,
            langData[currentLang].gender,
            langData[currentLang].findDate,
            langData[currentLang].accessory,
            langData[currentLang].additionalInfo,
            langData[currentLang].save
        ];

        const styledTexts = document.querySelectorAll(".styled-text");
        styledTexts.forEach((element, index) => {
        element.textContent = texts[index];
        });

        
        // Обновляем текст ошибки
        //document.querySelectorAll(".error")=langData[currentLang].errorMessage;   
    };
    

    // Слушатель для переключения языка
    languageSwitcher.addEventListener("change", updateLanguage);

    // Устанавливаем начальный язык
    updateLanguage();
});
