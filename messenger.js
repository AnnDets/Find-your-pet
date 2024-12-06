document.getElementById('send-btn').addEventListener('click', sendMessage);
document.getElementById('message-input').addEventListener('keydown', handleKeyPress);
document.getElementById('file-upload').addEventListener('change', handleFileUpload);

const chatHistory = document.getElementById('chat-history');
const messageInput = document.getElementById('message-input');
const chatHeader = document.getElementById('chat-header');
const userList = document.querySelector('.user-list');

let userAvatarUrl = 'https://via.placeholder.com/40?text=U'; 
let userName = 'Вы';
let otherUserAvatarUrl = 'https://via.placeholder.com/40?text=B'; 
let otherUserName = 'Другой пользователь';

// Список доступных пользователей
const users = {
  user1: { avatar: 'https://via.placeholder.com/40?text=A', name: 'Анна' },
  user2: { avatar: 'https://via.placeholder.com/40?text=B', name: 'Дарья' },
  user3: { avatar: 'https://via.placeholder.com/40?text=C', name: 'Степан' },
};

// Обработка загрузки аватара
function handleAvatarUpload(event) {
  const file = event.target.files[0];
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader();
    reader.onload = function(e) {
      userAvatarUrl = e.target.result;
    };
    reader.readAsDataURL(file);
  } else {
    alert('Пожалуйста, загрузите изображение.');
  }
}

// Отправка сообщения
function sendMessage() {
  const message = messageInput.value.trim();

  if (message !== '') {
    const messageElement = createMessageElement('user', message);
    chatHistory.appendChild(messageElement);
    messageInput.value = '';

    // Scroll to bottom
    chatHistory.scrollTop = chatHistory.scrollHeight;
  }
}

// Обработка нажатия клавиши
function handleKeyPress(event) {
  if (event.key === 'Enter') {
    event.preventDefault(); // Предотвращает добавление новой строки в поле ввода
    sendMessage();
  }
}

// Обработка загрузки файлов
function handleFileUpload(event) {
  const file = event.target.files[0];

  if (file) {
    const fileReader = new FileReader();

    fileReader.onload = function(e) {
      const fileElement = createFileElement('user', file, e.target.result);
      chatHistory.appendChild(fileElement);
    };

    fileReader.readAsDataURL(file);
  }
}

// Создание элемента сообщения
function createMessageElement(sender, message) {
  const messageElement = document.createElement('div');
  messageElement.classList.add('message');

  const avatar = document.createElement('img');
  avatar.classList.add('avatar');
  avatar.src = sender === 'user' ? userAvatarUrl : otherUserAvatarUrl;

  const content = document.createElement('div');
  content.classList.add('content');
  content.innerText = message;

  const timestamp = document.createElement('div');
  timestamp.classList.add('timestamp');
  timestamp.innerText = new Date().toLocaleTimeString();

  content.appendChild(timestamp);
  messageElement.appendChild(avatar);
  messageElement.appendChild(content);

  return messageElement;
}

// Создание элемента для файла
function createFileElement(sender, file, fileUrl) {
  const fileElement = document.createElement('div');
  fileElement.classList.add('file-message', 'message');

  const avatar = document.createElement('img');
  avatar.classList.add('avatar');
  avatar.src = sender === 'user' ? userAvatarUrl : otherUserAvatarUrl;

  const content = document.createElement('div');
  content.classList.add('content');

  if (file.type.startsWith('image/')) {
    const img = document.createElement('img');
    img.src = fileUrl;
    img.alt = file.name;
    content.appendChild(img);
  } else if (file.type.startsWith('video/')) {
    const video = document.createElement('video');
    video.controls = true;
    video.src = fileUrl;
    content.appendChild(video);
  } else if (file.type.startsWith('audio/')) {
    const audio = document.createElement('audio');
    audio.controls = true;
    audio.src = fileUrl;
    content.appendChild(audio);
  } else {
    const link = document.createElement('a');
    link.href = fileUrl;
    link.target = '_blank';
    link.innerText = `Скачать файл: ${file.name}`;
    content.appendChild(link);
  }

  const timestamp = document.createElement('div');
  timestamp.classList.add('timestamp');
  timestamp.innerText = new Date().toLocaleTimeString();

  content.appendChild(timestamp);
  fileElement.appendChild(avatar);
  fileElement.appendChild(content);

  return fileElement;
}

// Переключение между пользователями
function switchUser(userId) {
  const user = users[userId];
  otherUserAvatarUrl = user.avatar;
  otherUserName = user.name;
  updateChatHeader();
  loadChatHistory();
}

// Обновление шапки чата
function updateChatHeader() {
  chatHeader.innerHTML = `
    <img src="${otherUserAvatarUrl}" alt="${otherUserName}" class="header-avatar">
    <span class="header-name">${otherUserName}</span>
  `;
}

// Загрузка истории сообщений
function loadChatHistory() {
  chatHistory.innerHTML = ''; // Очищаем чат
  if (!otherUserName) return;

  messages.forEach(msg => {
    const messageElement = createMessageElement(msg.sender, msg.text);
    chatHistory.appendChild(messageElement);
  });

  chatHistory.scrollTop = chatHistory.scrollHeight; // Прокрутка в конец
}

// Инициализация списка пользователей
function initUserList() {
  Object.keys(users).forEach(userId => {
    const user = users[userId];
    const userItem = document.createElement('div');
    userItem.classList.add('user-item');
    userItem.setAttribute('onclick', `switchUser('${userId}')`);
    userItem.innerHTML = `
      <img src="${user.avatar}" alt="${user.name}" class="user-avatar">
      <span class="user-name">${user.name}</span>
    `;
    userList.appendChild(userItem);
  });
}

// Обработка загрузки аватара
function handleAvatarUpload(event) {
  const file = event.target.files[0];
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader();
    reader.onload = function(e) {
      userAvatarUrl = e.target.result;
      // Обновляем аватар в заголовке чата (при необходимости)
      const headerAvatar = document.querySelector('.header-avatar');
    };
    reader.readAsDataURL(file);
  } else {
    alert('Пожалуйста, загрузите изображение.');
  }
}

// Обработка загрузки файлов
function handleFileUpload(event) {
  const file = event.target.files[0];

  if (file) {
    const fileReader = new FileReader();

    fileReader.onload = function(e) {
      const fileElement = createFileElement('user', file, e.target.result);
      chatHistory.appendChild(fileElement);
    };

    fileReader.readAsDataURL(file);
  }
}

// Инициализация чата с первым пользователем
initUserList();
switchUser('user1');