package com.shopingweb.product_service.Service;


import com.shopingweb.product_service.dto.Userdto;
import com.shopingweb.product_service.model.User;
import com.shopingweb.product_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
