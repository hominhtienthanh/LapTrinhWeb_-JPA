package service;

import java.util.List;
import entity.User;

public interface UserService {
	List<User> findAll();
	User login(String username, String password);
	boolean register(String email, String password, String username, String fullname, String phone);
    User findById(int id);
    void create(User user);
    boolean checkExistEmail(String email);
	boolean checkExistUsername(String username);
	boolean checkExistPhone(String phone);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean resetPassword(String username, String newPassword);
}
