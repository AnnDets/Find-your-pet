/**
 * Устанавливает cookie с указанным именем, значением и сроком действия.
 * @param {string} name - Имя cookie.
 * @param {string} value - Значение cookie.
 * @param {number} days - Срок действия cookie в днях.
 */
export function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = `${name}=${value}; expires=${date.toUTCString()}; path=/`;
}

/**
 * Получает значение cookie по имени.
 * @param {string} name - Имя cookie.
 * @returns {string|null} - Значение cookie или null, если cookie не найден.
 */
export function getCookie(name) {
    const cookies = document.cookie.split("; ");
    for (let cookie of cookies) {
        const [key, value] = cookie.split("=");
        if (key === name) {
            return value;
        }
    }
    return null;
}

/**
 * Удаляет cookie с указанным именем.
 * @param {string} name - Имя cookie.
 */
export function deleteCookie(name) {
    setCookie(name, "", -1); // Устанавливаем срок действия в прошлом, чтобы удалить cookie.
}
