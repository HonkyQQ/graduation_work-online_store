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
                                authIndicator.style.display = "none";
                            }
                            window.location.href = "/";
                        }, 500);
                    }
                });
        });
    }

    // Загружаем отзывы при загрузке страницы
    const productId = window.location.pathname.split('/')[2];
    loadReviews(productId);
});

function submitReview() {
    const productId = window.location.pathname.split('/')[2]; // Получаем ID товара из URL
    const comment = document.getElementById('comment').value.trim();
    const rating = document.getElementById('rating').value;

    if (!comment || rating < 1 || rating > 5) {
        alert("Пожалуйста, напишите отзыв и выберите рейтинг от 1 до 5.");
        return;
    }

    const formData = new URLSearchParams();
    formData.append("comment", comment);
    formData.append("rating", rating);

    fetch(`/product/${productId}/reviews`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData.toString()
    })
    .then(response => {
        if (response.ok) {
            return Promise.all([
                fetch(`/product/${productId}/reviews`).then(res => res.text()),
                fetch(`/product/${productId}/average-rating`).then(res => res.text())
            ]);
        } else {
            throw new Error("Ошибка при добавлении отзыва");
        }
    })
    .then(([reviewsHtml, newRating]) => {
        document.getElementById('reviews-container').innerHTML = reviewsHtml;
        document.getElementById('product-rating').textContent = newRating + " ★"; // Обновляем рейтинг
        document.getElementById('comment').value = "";
        document.getElementById('rating').value = "1";
    })
    .catch(error => console.error("Ошибка:", error));
}

// Загрузка отзывов при открытии страницы
function loadReviews(productId) {
    fetch(`/product/${productId}/reviews`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('reviews-container').innerHTML = html;
        })
        .catch(error => console.error("Ошибка загрузки отзывов:", error));
}