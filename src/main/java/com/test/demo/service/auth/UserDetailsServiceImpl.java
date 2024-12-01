package com.test.demo.service.auth;

import com.test.demo.exceptions.EmailNotFoundException;
import com.test.demo.exceptions.EmailNotVerifiedException;
import com.test.demo.exceptions.UserDisabledException;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    ///  Serwis do logownaia

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        ///  kiedy sie logujesz, sprawdzany jest e-mail a nie username, bo nadpisujemy to tutaj
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email));

        if (!user.isEnabled()) {
            throw new UserDisabledException("User account is disabled. Contact us");
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email waiting for verification");
        }

        return new CustomUserDetails(user);
    }

}