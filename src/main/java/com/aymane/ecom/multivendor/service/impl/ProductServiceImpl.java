package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.controller.request.CreateProductRequest;
import com.aymane.ecom.multivendor.exception.ProductException;
import com.aymane.ecom.multivendor.model.Category;
import com.aymane.ecom.multivendor.model.Product;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.repository.CategoryRepository;
import com.aymane.ecom.multivendor.repository.ProductRepository;
import com.aymane.ecom.multivendor.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) throws IllegalAccessException {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory1());
        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());

        if (category1 == null) {
            final Category category = Category.builder()
                    .categoryId(req.getCategory1())
                    .level(1)
                    .build();
            category1 = categoryRepository.save(category);
        }

        if (category2 == null) {
            final Category category = Category.builder()
                    .categoryId(req.getCategory2())
                    .level(2)
                    .parentCategory(category1)
                    .build();
            category2 = categoryRepository.save(category);
        }

        if (category3 == null) {
            final Category category = Category.builder()
                    .categoryId(req.getCategory3())
                    .level(3)
                    .parentCategory(category2)
                    .build();
            category3 = categoryRepository.save(category);
        }

        Product product = Product.builder()
                .seller(seller)
                .category(category3)
                .description(req.getDescription())
                .createdAt(LocalDateTime.now())
                .title(req.getTitle())
                .color(req.getColor())
                .sellingPrice(req.getSellingPrice())
                .images(req.getImages())
                .mrpPrice(req.getMrpPrice())
                .sizes(req.getSizes())
                .discountPercent(calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice()))
                .build();

        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(final int mrpPrice, final int sellingPrice) throws IllegalAccessException {
        if (mrpPrice <= 0) {
            throw new IllegalAccessException("Actual price must be greater than 0");
        }
        return 100 * (mrpPrice - sellingPrice) / mrpPrice;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        final Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        final Product productById = findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found with product id " + productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes,
                                        Integer minimumPrice, Integer maximumPrice, Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            if (colors != null && !colors.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }

            if (sizes != null && !sizes.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }

            if (minimumPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minimumPrice));
            }

            if (maximumPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maximumPrice));
            }

            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }

            if (stock != null) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_low":
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                            Sort.by("sellingPrice").ascending());
                    break;

                case "price_high":
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                            Sort.by("sellingPrice").descending());
                    break;

                default:
                    pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                            Sort.unsorted());

            }
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
