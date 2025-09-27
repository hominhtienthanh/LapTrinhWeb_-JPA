package service.Impl;

import java.util.List;

import configs.JPAConfig;
import dao.CategoryDao;
import dao.Impl.CategoryDaoImpl;
import entity.Category;
import jakarta.persistence.EntityManager;
import service.CategoryService;

public class CategoryServiceImpl implements CategoryService{
	CategoryDao cateDao = new CategoryDaoImpl();
	
	@Override
	public List<Category> findAll() {
		return cateDao.findAll();
	}

	@Override
	public void create(Category category) {
		cateDao.create(category);
		
	}

	@Override
	public void update(Category category) {
		cateDao.update(category);
	}

	@Override
	public void delete(int id) {
		cateDao.delete(id);
		
	}

	@Override
	public Category findById(int id) {
		return cateDao.findById(id);
	}

	@Override
	public List<Category> findByUser(int userId) {
		return cateDao.findByUser(userId);
	}
	
}
