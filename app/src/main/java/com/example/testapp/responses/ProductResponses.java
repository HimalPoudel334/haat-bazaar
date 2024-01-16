package com.example.testapp.responses;

import com.example.testapp.models.Product;

import java.util.List;

public class ProductResponses {

    public class SingleProductResponse {
        private Product product;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public class MultipleProductResonse {
        private List<Product> products;
        public List<Product> getProducts() {
            return products;
        }

        public void setProduct(List<Product> products) {
            this.products = products;
        }
    }

}

