package com.knu.cse.treereader.service;

import com.knu.cse.treereader.model.User;
import com.knu.cse.treereader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//로직
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUserId(String userId) {

        return userRepository.findByUserId(userId);
    }

    public User findByUserIdAndPassword(String userId, String password) {
        return userRepository.findByUserIdAndPassword(userId, password);
    }

    public User update(Integer id, User user) {
        User updated = userRepository.findOne(id);
        updated.setUserId(user.getUserId());
        updated.setPassword(user.getPassword());
        return userRepository.save(updated);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void delete(Integer id){
        userRepository.delete(id);
    }
}
