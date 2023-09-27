package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.*;

import ru.practicum.category.model.Category;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;


    //AdminCategoryController:
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.newCategoryDtoToCategory(newCategoryDto));
        return CategoryMapper.categoryToCategoryDto(category);
    }

    public CategoryDto updateCategoryById(Long id, CategoryDto categoryDto) {

        Category category = shackIdCategory(id);
        category.setName(categoryDto.getName());
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategoryById(Long id) {
        shackIdCategory(id);
        categoryRepository.deleteById(id);
    }

    //PublicCategoryController:

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategory(int from, int size) {
        return categoryRepository.findAll(new Pagination(from, size, Sort.unsorted())).stream()
                .map(CategoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = shackIdCategory(id);
        return CategoryMapper.categoryToCategoryDto(category);
    }

    //PrivateMethods
    private Category shackIdCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found Category id" + id));
    }
}