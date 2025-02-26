package org.example.onlinestore.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.onlinestore.entity.Product;
import org.example.onlinestore.repository.ProductRepository;
import org.example.onlinestore.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Product> getAllProducts() {
        logger.info("Запрос на получение всех товаров");
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        logger.info("Запрос на получение товаров по категории ID: {}", categoryId);
        return productRepository.findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId));
    }

    public Product getProductById(Long id) {
        logger.info("Поиск товара с ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Товар с ID {} не найден!", id);
                    return new RuntimeException("Товар с ID " + id + " не найден.");
                });
    }

    public List<Product> searchProducts(String query) {
        logger.info("Поиск товаров по запросу: {}", query);
        return productRepository.findAll((root, queryBuilder, criteriaBuilder) -> {
            String likePattern = "%" + query.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        });
    }

    public List<Product> filterProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, String sortPrice, String sortRating) {
        logger.info("Фильтрация товаров по категории: {}, мин. цена: {}, макс. цена: {}, сортировка по цене: {}, сортировка по рейтингу: {}",
                category, minPrice, maxPrice, sortPrice, sortRating);

        StringBuilder jpql = new StringBuilder("SELECT p FROM Product p LEFT JOIN p.reviews r WHERE 1=1");

        if (category != null && !category.isEmpty()) {
            jpql.append(" AND p.category.name = :category");
        }
        if (minPrice != null) {
            jpql.append(" AND p.price >= :minPrice");
        }
        if (maxPrice != null) {
            jpql.append(" AND p.price <= :maxPrice");
        }

        jpql.append(" GROUP BY p.id");

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

    public double getAverageRating(Long productId) {
        logger.info("Получение среднего рейтинга товара с ID: {}", productId);
        Double avgRating = reviewRepository.calculateAverageRating(productId);
        return (avgRating != null) ? avgRating : 0.0;
    }

    @Transactional
    public void updateAverageRating(Long productId) {
        logger.info("Обновление среднего рейтинга товара с ID: {}", productId);
        Product product = getProductById(productId);

        Double avgRating = reviewRepository.calculateAverageRating(productId);
        product.setAverageRating((avgRating != null) ? avgRating : 0.0);

        productRepository.save(product);
    }
}
