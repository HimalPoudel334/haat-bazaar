package com.example.testapp.responses;

import com.example.testapp.models.ProductImage;

import java.util.List;

public class ProductImageResponse {
    private List<ProductImage> productImages;
    public List<ProductImage> getProductExtraImages() {
        return productImages;
    }

    public void setProductExtraImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
}
