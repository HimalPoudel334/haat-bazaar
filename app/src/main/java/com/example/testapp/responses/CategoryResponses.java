package com.example.testapp.responses;

import com.example.testapp.models.Category;

import java.util.List;

public class CategoryResponses {

    public class SingleCategoryResponse {
        private Category category;

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    public class MultiCategoryResponse {

        private List<Category> categories;

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> category) {
            this.categories = category;
        }
    }
}
