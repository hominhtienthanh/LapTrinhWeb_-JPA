package dao;

import java.util.List;
import entity.User;

public interface UserDao {
    List<User> findAll();
    User findById(int id);
    void create(User user);
    boolean checkExistEmail(String email);
	boolean checkExistUsername(String username);
	boolean checkExistPhone(String phone);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean resetPassword(String username, String newPassword);
}
