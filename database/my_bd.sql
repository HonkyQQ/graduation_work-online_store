CREATE DATABASE online_store;

USE online_store;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,        
    full_name VARCHAR(255) NOT NULL,             
    username VARCHAR(100) NOT NULL UNIQUE,       
    email VARCHAR(100) NOT NULL UNIQUE,         
    phone_number VARCHAR(20),                   
    password VARCHAR(255) NOT NULL,             
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);


CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    quantity INT NOT NULL, 
    image_url VARCHAR(255), 
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5), 
    comment TEXT NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE cart_products (
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (cart_id, product_id),
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    user_id BIGINT NOT NULL,            
    delivery_address VARCHAR(255) NOT NULL, 
    recipient_name VARCHAR(100) NOT NULL,   
    phone_number VARCHAR(20) NOT NULL,     
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    total_price DECIMAL(10, 2) NOT NULL,   
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE 
);

CREATE TABLE order_products (
    order_id BIGINT NOT NULL, 
    product_id BIGINT NOT NULL, 
    quantity INT NOT NULL DEFAULT 1, 
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (order_id, product_id) 
);
INSERT INTO users (full_name, username, email, phone_number, password) 
VALUES 
('Иван Иванов', 'ivanov', 'ivanov@example.com', '+79991234567', 'password1'),
('Анна Смирнова', 'asmirnova', 'smirnova@example.com', '+79991234568', 'password2'),
('Пётр Петров', 'ppetrov', 'petrov@example.com', '+79991234569', 'password3'),
('Елена Алексеева', 'ealekseeva', 'alekseeva@example.com', '+79991234570', 'password4');

INSERT INTO categories (name) VALUES
('Техника'), 
('Мебель'), 
('Одежда'), 
('Игрушки'), 
('Книги'), 
('Спорттовары'), 
('Косметика');

INSERT INTO products (name, price, description, category_id, image_url, quantity) VALUES
-- Техника
('Смартфон X', 599.99, 'Мощный смартфон с AMOLED-экраном.', 1, '/images/smartphone_x.jpg', 10),
('Ноутбук Y', 999.99, 'Игровой ноутбук с мощной видеокартой.', 1, '/images/laptop_y.jpg', 5),
('Стиральная машина Z', 749.99, 'Ультрасовременная стиральная машина.', 1, '/images/washing_machine_z.jpg', 8),
('Телевизор Q', 899.99, '4K UHD телевизор.', 1, '/images/tv_q.jpg', 12),
('Микроволновка M', 129.99, 'Компактная и мощная.', 1, '/images/microwave_m.jpg', 15),

-- Мебель
('Диван "Комфорт"', 459.99, 'Мягкий и удобный диван.', 2, '/images/sofa_comfort.jpg', 7),
('Стол обеденный', 299.99, 'Большой деревянный стол.', 2, '/images/dining_table.jpg', 4),
('Кресло "Релакс"', 199.99, 'Удобное кресло для отдыха.', 2, '/images/armchair_relax.jpg', 6),
('Шкаф-купе', 399.99, 'Просторный шкаф для одежды.', 2, '/images/wardrobe.jpg', 10),
('Кровать двухместная', 499.99, 'Кровать с ортопедическим матрасом.', 2, '/images/bed.jpg', 5),

-- Одежда
('Куртка зимняя', 89.99, 'Тёплая куртка для холодной зимы.', 3, '/images/winter_jacket.jpg', 20),
('Кроссовки спортивные', 59.99, 'Для занятий бегом.', 3, '/images/sports_shoes.jpg', 25),
('Футболка хлопковая', 19.99, 'Удобная футболка из натурального хлопка.', 3, '/images/cotton_tshirt.jpg', 30),
('Джинсы классические', 49.99, 'Модные джинсы.', 3, '/images/jeans.jpg', 18),
('Шапка шерстяная', 14.99, 'Тёплая зимняя шапка.', 3, '/images/wool_hat.jpg', 40),

-- Игрушки
('Кубик Рубика', 9.99, 'Классическая головоломка.', 4, '/images/rubik_cube.jpg', 50),
('Мягкая игрушка "Медведь"', 19.99, 'Плюшевый медведь.', 4, '/images/teddy_bear.jpg', 60),
('Набор LEGO', 49.99, 'Конструктор для детей.', 4, '/images/lego_set.jpg', 15),
('Машинка на радиоуправлении', 29.99, 'Спортивная модель.', 4, '/images/rc_car.jpg', 30),
('Пазлы 1000 элементов', 14.99, 'Сложная головоломка.', 4, '/images/puzzle.jpg', 25),

-- Книги
('Гарри Поттер и философский камень', 14.99, 'Первая книга знаменитой серии.', 5, '/images/harry_potter.jpg', 100),
('Властелин Колец: Братство Кольца', 19.99, 'Эпическое фэнтези.', 5, '/images/lotr_fellowship.jpg', 75),
('Кулинарная книга', 24.99, '100 лучших рецептов.', 5, '/images/cookbook.jpg', 50),
('Книга по программированию Java', 34.99, 'Справочник для начинающих.', 5, '/images/java_book.jpg', 40),
('Атлас мира', 29.99, 'Содержит карты всех стран.', 5, '/images/atlas_world.jpg', 60),

-- Спорттовары
('Тренажёр беговой', 699.99, 'Домашний тренажёр для бега.', 6, '/images/treadmill.jpg', 10),
('Гантели 10 кг', 39.99, 'Для занятий спортом.', 6, '/images/dumbbells.jpg', 20),
('Мяч футбольный', 24.99, 'Профессиональный футбольный мяч.', 6, '/images/football.jpg', 50),
('Набор для тенниса', 49.99, 'Две ракетки и мяч.', 6, '/images/tennis_set.jpg', 30),
('Эспандер для рук', 9.99, 'Тренировка кистей и предплечий.', 6, '/images/hand_expander.jpg', 40),

-- Косметика
('Крем для лица', 14.99, 'Увлажняющий крем.', 7, '/images/face_cream.jpg', 150),
('Тушь для ресниц', 12.99, 'Для объёмных ресниц.', 7, '/images/mascara.jpg', 200),
('Шампунь для волос', 9.99, 'Для всех типов волос.', 7, '/images/shampoo.jpg', 120),
('Парфюм женский', 49.99, 'Нежный аромат.', 7, '/images/perfume.jpg', 70),
('Бальзам для губ', 5.99, 'С натуральными маслами.', 7, '/images/lip_balm.jpg', 250);

-- Добавление отзывов на товары
INSERT INTO reviews (comment, rating, product_id, user_id) VALUES
('Прекрасный товар!', 5, 1, 1),
('Соответствует описанию, но немного дороговато.', 4, 1, 2),
('Качественная сборка и хороший дизайн.', 5, 2, 3),
('Проблем с работой не было.', 4, 2, 4),
('Очень мощный ноутбук!', 5, 3, 1),
('Немного шумит, но в остальном всё отлично.', 4, 3, 2),
('Отличная стиральная машина.', 5, 4, 3),
('Пользуюсь несколько месяцев, проблем нет.', 5, 4, 4),
('Телевизор с хорошим изображением.', 5, 5, 1),
('Рекомендую всем!', 5, 5, 2),
('Диван удобный, понравился всей семье.', 5, 6, 3),
('Ткань качественная, выглядит стильно.', 5, 6, 4),
('Стол хорошо вписался в интерьер.', 5, 7, 1),
('Прочный и удобный.', 4, 7, 2),
('Кресло - то, что нужно для отдыха.', 5, 8, 3),
('Отличное качество!', 5, 8, 4),
('Шкаф вместительный, понравился дизайн.', 5, 9, 1),
('Всё супер!', 5, 9, 2),
('Кровать удобная, спать одно удовольствие.', 5, 10, 3),
('Матрас тоже хороший.', 4, 10, 4),
('Куртка тёплая, спасает от морозов.', 5, 11, 1),
('Советую всем.', 5, 11, 2),
('Кроссовки лёгкие, отлично для бега.', 5, 12, 3),
('Рекомендую спортсменам.', 5, 12, 4),
('Футболка удобная, носится долго.', 5, 13, 1),
('Материал натуральный.', 5, 13, 2),
('Джинсы идеальны для повседневной носки.', 5, 14, 3),
('Ткань приятная.', 4, 14, 4),
('Шапка согревает зимой.', 5, 15, 1),
('Не пожалел о покупке.', 5, 15, 2),
('Кубик Рубика - интересная головоломка.', 5, 16, 3),
('Долго не надоедает.', 5, 16, 4),
('Медведь милый и мягкий.', 5, 17, 1),
('Ребёнок доволен.', 5, 17, 2),
('LEGO всегда на высоте!', 5, 18, 3),
('Конструктор супер!', 5, 18, 4),
('Машинка быстрая, управление простое.', 5, 19, 1),
('Дети в восторге.', 5, 19, 2),
('Пазлы сложные, но увлекательные.', 5, 20, 3),
('Качество отличное.', 5, 20, 4),
('Гарри Поттер захватывает!', 5, 21, 1),
('Перечитываю каждый год.', 5, 21, 2),
('Властелин Колец - шедевр.', 5, 22, 3),
('Фэнтези на высоте.', 5, 22, 4),
('Кулинарная книга удобна в использовании.', 5, 23, 1),
('Рецепты простые.', 5, 23, 2),
('Книга по Java полезна для новичков.', 5, 24, 3),
('Много практики.', 5, 24, 4),
('Атлас красивый и информативный.', 5, 25, 1),
('Качество печати отличное.', 5, 25, 2),
('Тренажёр стоит своих денег.', 5, 26, 3),
('Доволен покупкой.', 5, 26, 4),
('Гантели удобны для дома.', 5, 27, 1),
('Качественный материал.', 5, 27, 2),
('Мяч соответствует стандартам.', 5, 28, 3),
('Понравилось.', 5, 28, 4),
('Теннисный набор удобный.', 5, 29, 1),
('Доволен покупкой.', 5, 29, 2),
('Эспандер удобен для тренировок.', 5, 30, 3),
('Прост в использовании.', 5, 30, 4),
('Крем хорошо увлажняет.', 5, 31, 1),
('Подходит для чувствительной кожи.', 5, 31, 2),
('Тушь делает ресницы объёмными.', 5, 32, 3),
('Хорошо смывается.', 5, 32, 4),
('Шампунь универсальный.', 5, 33, 1),
('Рекомендую.', 5, 33, 2),
('Парфюм с приятным ароматом.', 5, 34, 3),
('Долго держится.', 5, 34, 4),
('Бальзам питает губы.', 5, 35, 1),
('Компактный и удобный.', 5, 35, 2),
('Пришёл в мятой упаковке', 3, 35, 3),
('Вроде подходит', 4, 35, 4);

INSERT INTO orders (user_id, delivery_address, recipient_name, phone_number, total_price) VALUES
(1, 'г. Москва, ул. Ленина, д. 10', 'Иван Иванов', '+79991234567', 1059.98);

INSERT INTO order_products (order_id, product_id, quantity) VALUES
(1, 3, 1),
(1, 2, 1);

ALTER TABLE users MODIFY COLUMN full_name VARCHAR(255) NULL;
