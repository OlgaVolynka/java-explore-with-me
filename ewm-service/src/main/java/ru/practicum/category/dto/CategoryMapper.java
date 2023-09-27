package ru.practicum.category.dto;

import ru.practicum.category.model.Category;

public class CategoryMapper {

    public static CategoryDto categoryToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
