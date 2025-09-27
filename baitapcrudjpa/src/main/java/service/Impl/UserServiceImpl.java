package service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import configs.JPAConfig;
import dao.UserDao;
import dao.Impl.UserDaoImpl;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean resetPassword(String username, String newPassword) {
        return userDao.resetPassword(username, newPassword);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }

    @Override
    public User login(String username, String password) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            List<User> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0); // đăng nhập thành công
            } else {
                return null; // username hoặc password sai
            }
        } finally {
            em.close();
        }
    }

	@Override
	public boolean register(String email, String password, String username, String fullname, String phone) {
		if (checkExistEmail(email) || checkExistUsername(username)) {
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setRole(1);
        user.setCreateDate(LocalDateTime.now());
        create(user);
        return true;
	}
}
