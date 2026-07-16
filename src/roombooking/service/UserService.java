package roombooking.service;

import users.RegisteredUser;
import users.UserRepository;
import users.Account;
import users.StudentUser;
import users.FacultyUser;
import users.StaffUser;
import users.PartnerUser;
import users.AdminUser;

public class UserService {
    private UserRepository userRepo;
    
    public UserService() {
        this.userRepo = new UserRepository();
    }
    
    public RegisteredUser findUserById(int userId) {
        return userRepo.findUserByID(userId);
    }
    
    public RegisteredUser findUserByEmail(String email) {
        for (RegisteredUser user : userRepo.getAllUsers()) {
            if (user.getAccount().getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    
    public boolean emailExists(String email) {
        return userRepo.emailExists(email);
    }
    
    public boolean validateUser(int userId) {
        return userRepo.findUserByID(userId) != null;
    }
    
    public void saveUser(RegisteredUser user) {
        userRepo.saveUser(user);
    }
    
    public void updateUser(RegisteredUser user) {
        userRepo.updateUser(user);
    }
    
    public void deleteUser(int userId) {
        userRepo.deleteUser(userId);
    }
}
