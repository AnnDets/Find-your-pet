
document.getElementById('find').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'find.html';
});

document.getElementById('lost').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'lost.html';
});

document.getElementById('firstpetButton').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'petpage.html';
});

document.getElementById('secondpetButton').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'petpage.html';
});

document.getElementById('thirdpetButton').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'petpage.html';
});

document.getElementById('fourpetButton').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'petpage.html';
});

document.getElementById('fivepetButton').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'petpage.html';
});

document.getElementById('Profile').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'profile.html';
});

document.getElementById('Chat').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'chat.html';
});

document.getElementById('Filter').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'filter.html';
});

document.getElementById('My_listing').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'mylisting.html';
});

document.getElementById('Instruction').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'instruction.html';
});
document.getElementById('About_us').addEventListener('click', function() {
    event.preventDefault(); 
    window.location.href = 'aboutus.html';
});

document.getElementById('Exit').addEventListener('click', function() {
    event.preventDefault(); 
    alert('Вы вышли из профиля');
    window.location.href = 'homeguest.html';
});



document.addEventListener("DOMContentLoaded", () => {
    const langData = {
        ru: {
            application: "Заявка:",
            find: "Я нашёл животное",
            lost: "Я потерял животное",
            profile: "Профиль пользователя",
            chat: "Чат",
            filter: "Фильтр",
            myListing: "Мои объявления",
            instruction: "Инструкция по поиску",
            aboutUs: "О нас",
            exit: "Выход",
            happyPets: "Счастливые найдёныши:",
            aboutTitle: "О нас:",
            aboutDescription: "Мы команда...."
        },
        en: {
            application: "Application:",
            find: "I found a pet",
            lost: "I lost a pet",
            profile: "User profile",
            chat: "Chat",
            filter: "Filter",
            myListing: "My listings",
            instruction: "Search instructions",
            aboutUs: "About us",
            exit: "Exit",
            happyPets: "Happy pets:",
            aboutTitle: "About us:",
            aboutDescription: "We are a team...."
        }
    };

    const languageSwitcher = document.getElementById("languageSwitcher");
    
    const updateLanguage = () => {
        currentLang = document.getElementById("languageSwitcher").value;
        console.log("Current language data:", currentLang);

            // Обновляем текстовые элементы
            document.querySelector("#one .styled-headtext").textContent = langData[currentLang].application;
            document.querySelector("#oneone .styled-text").textContent = langData[currentLang].find;
            document.querySelector("#onetwo .styled-text").textContent = langData[currentLang].lost;
    
            document.getElementById("Profile").textContent = langData[currentLang].profile;
            document.getElementById("Chat").textContent = langData[currentLang].chat;
            document.getElementById("Filter").textContent = langData[currentLang].filter;
            document.getElementById("My_listing").textContent = langData[currentLang].myListing;
            document.getElementById("Instruction").textContent = langData[currentLang].instruction;
            document.getElementById("About_us").textContent = langData[currentLang].aboutUs;
            document.getElementById("Exit").textContent = langData[currentLang].exit;
    
            document.querySelector("#three .styled-text").textContent = langData[currentLang].happyPets;
            document.querySelector("#four .styled-text").textContent = langData[currentLang].aboutTitle;
            document.querySelector("#four .text").textContent = langData[currentLang].aboutDescription;
        };
    
        // Слушатель для переключения языка
        languageSwitcher.addEventListener("change", updateLanguage);
    
        // Устанавливаем начальный язык
        updateLanguage();
});
