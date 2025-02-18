document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById("logoutButton");
    const authIndicator = document.getElementById("authIndicator");

    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            fetch('/logout', { method: 'POST' })
                .then(response => {
                    if (response.ok) {
                        setTimeout(() => {
                            if (authIndicator) {
                                authIndicator.style.display = "none"; // Убираем звездочку после выхода
                            }
                            window.location.href = "/";
                        }, 500);
                    }
                });
        });
    }
});

function submitReview() {
    const productId = window.location.pathname.split('/')[2];
    const comment = document.getElementById('comment').value.trim();
    const rating = document.getElementById('rating').value;

    if (!comment || rating < 1 || rating > 5) {
        alert("Пожалуйста, напишите отзыв и выберите рейтинг от 1 до 5.");
        return;
    }

    let formData = new FormData();
    formData.append("comment", comment);
    formData.append("rating", rating);

    fetch('/product/${productId}/reviews', {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        } else {
            return fetch('/product/${productId}/reviews');
        }
    })
    .then(response => response.text())
    .then(html => {
        document.getElementById('reviews-container').innerHTML = html;
        document.getElementById('comment').value = "";
        document.getElementById('rating').value = "1";
    })
    .catch(error => console.error("Ошибка:", error));
}