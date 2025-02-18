package org.example.onlinestore.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.onlinestore.entity.Product;
import org.example.onlinestore.repository.ProductRepository;
import org.example.onlinestore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Получить все товары.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Получить товары по категории.
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId));
    }

    /**
     * Получить товар по ID.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + id + " не найден."));
    }

    /**
     * Поиск товаров по строке.
     */
    public List<Product> searchProducts(String query) {
        return productRepository.findAll((root, queryBuilder, criteriaBuilder) -> {
            String likePattern = "%" + query.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        });
    }

    /**
     * Фильтрация товаров по параметрам.
     */
    public List<Product> filterProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, String sortPrice, String sortRating) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Product p LEFT JOIN p.reviews r WHERE 1=1");

        // Фильтры
        if (category != null && !category.isEmpty()) {
            jpql.append(" AND p.category.name = :category");
        }
        if (minPrice != null) {
            jpql.append(" AND p.price >= :minPrice");
        }
        if (maxPrice != null) {
            jpql.append(" AND p.price <= :maxPrice");
        }

        jpql.append(" GROUP BY p.id"); // Группировка по продуктам

        // Сортировка
        if ("asc".equalsIgnoreCase(sortPrice)) {
            jpql.append(" ORDER BY p.price ASC");
        } else if ("desc".equalsIgnoreCase(sortPrice)) {
            jpql.append(" ORDER BY p.price DESC");
        } else if ("asc".equalsIgnoreCase(sortRating)) {
            jpql.append(" ORDER BY AVG(r.rating) ASC");
        } else if ("desc".equalsIgnoreCase(sortRating)) {
            jpql.append(" ORDER BY AVG(r.rating) DESC");
        }

        TypedQuery<Product> query = entityManager.createQuery(jpql.toString(), Product.class);

        // Установка параметров
        if (category != null && !category.isEmpty()) {
            query.setParameter("category", category);
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }

        return query.getResultList();
    }

    /**
     * Обновление среднего рейтинга товара.
     */
    @Transactional
    public void updateAverageRating(Long productId) {
        Product product = getProductById(productId);

        String jpql = "SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId";
        Double avgRating = entityManager.createQuery(jpql, Double.class)
                .setParameter("productId", productId)
                .getSingleResult();

        product.setAverageRating(avgRating != null ? avgRating : 0.0);
        productRepository.save(product);
    }
}
