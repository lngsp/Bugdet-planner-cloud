package ro.budget.planner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.budget.planner.exceptions.UserNotFoundException;
import ro.budget.planner.models.User;
import ro.budget.planner.repo.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        User check_user = userRepository.findByUsername(user.getUsername());

        if(check_user != null){
            return null;
        }

        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
    }

    public Double getTotalAmount(String username){
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UserNotFoundException("User with username " + username + " not found.");
        }else{
            return user.getAmount();
        }
    }

    public String getUserEmail(String username){
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UserNotFoundException("User with username " + username + " not found.");
        }else{
            return user.getEmail();
        }
    }


    public void setTotalAmount(String username, Double amount){
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UserNotFoundException("User with username " + username + " not found.");
        }else{
            user.setAmount(amount);
            userRepository.save(user);
        }
    }



}
