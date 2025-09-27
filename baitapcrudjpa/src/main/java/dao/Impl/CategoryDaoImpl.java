package dao.Impl;

import java.util.List;

import configs.JPAConfig;
import dao.CategoryDao;
import entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class CategoryDaoImpl implements CategoryDao{

	@Override
	public List<Category> findAll() {
		EntityManager enma = JPAConfig.getEntityManager();
		TypedQuery<Category> query = enma.createNamedQuery("Category.findAll", Category.class);
		
		return query.getResultList();
	}

	@Override
	public void create(Category category) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
		trans.begin();
		enma.persist(category);
		trans.commit();
		} catch (Exception e) {
		e.printStackTrace();
		trans.rollback();
		throw e;
		}finally {
		enma.close();
		}
	}

	@Override
	public void update(Category category) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
		trans.begin();
		enma.merge(category);
		trans.commit();
		} catch (Exception e) {
		e.printStackTrace();
		trans.rollback();
		throw e;
		}finally {
		enma.close();
		}

	}

	@Override
	public void delete(int id) {
	    EntityManager em = JPAConfig.getEntityManager();
	    EntityTransaction trans = em.getTransaction();

	    try {
	        trans.begin();
	        Category category = em.find(Category.class, id);
	        if (category != null) {
	            em.remove(category);
	        } else {
	            throw new RuntimeException("Không tìm thấy category có id = " + id);
	        }
	        trans.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	        if (trans.isActive()) {
	            trans.rollback();
	        }
	        throw new RuntimeException(e);
	    } finally {
	        em.close();
	    }
	}


	@Override
	public Category findById(int id) {
		EntityManager enma = JPAConfig.getEntityManager();
		Category category = enma.find(Category.class,id);
		return category;

	}

	@Override
	public List<Category> findByUser(int userId) {
		EntityManager em = JPAConfig.getEntityManager();
	    try {
	        return em.createQuery("SELECT c FROM Category c WHERE c.user.id = :uid", Category.class)
	                 .setParameter("uid", userId)
	                 .getResultList();
	    } finally {
	        em.close();
	    }
	}
	
}
