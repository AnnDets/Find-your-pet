let currentChatId;
document.addEventListener("DOMContentLoaded", () => {
    const userList = document.querySelector(".user-list");

    // Загрузка списка чатов из API
    fetch("chats", {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("token"), // Укажите источник токена
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Не удалось загрузить чаты");
            }
            return response.json();
        })
        .then(chats => {
            // Отображение чатов
            chats.forEach(chat => {
                const userItem = document.createElement("div");
                userItem.className = "user-item";
                currentChatId = chat.id;
                userItem.setAttribute("onclick", `switchUser(${chat.id})`);
                userItem.innerHTML = `
                    <img src="https://via.placeholder.com/40?text=${chat.user2Name.charAt(0)}" alt="${chat.user2Name}" class="user-avatar">
                    <span class="user-name">${chat.user2Name}</span>
                `;
                userList.appendChild(userItem);
            });
        })
        .catch(error => {
            console.error("Ошибка загрузки чатов:", error);
        });
});
let currentSocket;
function switchUser(chatId) {
    const chatHistory = document.getElementById("chat-history");
    currentChatId = chatId;
    // Закрываем предыдущий WebSocket, если он существует
    if (currentSocket) {
        currentSocket.close();
    }

    // Инициализация нового WebSocket соединения
    let a = `ws://192.168.211.111:8080/find-your-pet/chat?chatId=${chatId}`;
    currentSocket = new WebSocket(a);

    currentSocket.onopen = () => {
        console.log("WebSocket соединение установлено для чата", chatId);

    };

    currentSocket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        displayMessage(message.senderName, message.content);
    };

    currentSocket.onclose = () => {
        console.log(`WebSocket закрыт. Код: ${event.code}, причина: ${event.reason}`);
    };

    currentSocket.onerror = (error) => {
        console.error("WebSocket ошибка:", error);
    };
    fetchChatHistory(chatId);
}
// Функция загрузки истории чата
function fetchChatHistory(chatId) {
    fetch(`chats/messages?chatId=${chatId}`, {
        headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Не удалось загрузить сообщения");
            }
            return response.json();
        })
        .then(messages => {
            const chatHistory = document.getElementById("chat-history");
            chatHistory.innerHTML = "";
            messages.forEach(message => displayMessage(message.senderName, message.content));
        })
        .catch(error => {
            console.error("Ошибка загрузки сообщений:", error);
        });
}

// Функция отображения сообщения в интерфейсе
function displayMessage(senderName, content) {
    const chatHistory = document.getElementById("chat-history");
    const messageDiv = document.createElement("div");
    messageDiv.className = "message";
    messageDiv.textContent = `${senderName}: ${content}`;
    chatHistory.appendChild(messageDiv);
}

const inputField = document.getElementById("message-input");
const sendButton = document.getElementById("send-btn");

inputField.addEventListener("input", () => {
    sendButton.disabled = !inputField.value.trim();
});

document.getElementById("send-btn").addEventListener("click", () => {
    const inputField = document.getElementById("message-input");
    const messageContent = inputField.value.trim();

    if (messageContent && currentSocket && currentSocket.readyState === WebSocket.OPEN) {
        const message = {
            content: messageContent,
            chatId: currentChatId, // текущий chatId
                 // ID текущего пользователя
        };

        currentSocket.send(JSON.stringify(message)); // Отправляем сообщение через WebSocket

    } else {
        console.error("WebSocket не подключен или сообщение пустое.");
    }
    fetch("chats/messages/send", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded",
                            Authorization: "Bearer " + localStorage.getItem("token"),
                        },
                        body: `chatId=${encodeURIComponent(currentChatId)}&content=${encodeURIComponent(messageContent)}`,
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error("Не удалось отправить сообщение");
                            }
                            return response.json();
                        })

                        .catch(error => {
                            console.error("Ошибка отправки сообщения:", error);
                        });
    inputField.value = ""; // Очищаем поле ввода
    displayMessage("Я", messageContent)
});
