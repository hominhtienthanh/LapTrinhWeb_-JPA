package dao;

import java.util.List;

import entity.Category;

public interface CategoryDao {
	List<Category> findAll();
	void create(Category category);
	void update(Category category);
	void delete(int id);
	Category findById(int id);
	List<Category> findByUser(int userId);
}
