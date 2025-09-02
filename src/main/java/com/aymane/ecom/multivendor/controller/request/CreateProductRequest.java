package com.aymane.ecom.multivendor.controller.request;

import lombok.*;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateProductRequest {
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String color;
    private List<String> images;
    private String category1;
    private String category2;
    private String category3;
    private String sizes;
}
