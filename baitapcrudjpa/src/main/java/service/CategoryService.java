package service;

import java.util.List;

import entity.Category;

public interface CategoryService {
	List<Category> findAll();
	void create(Category category);
	void update(Category category);
	void delete(int id);
	Category findById(int id);
	List<Category> findByUser(int userId);
}
