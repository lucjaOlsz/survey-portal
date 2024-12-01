package com.test.demo.service.auth;

import com.test.demo.dto.UserDTO;
import com.test.demo.exceptions.InvalidTokenException;
import com.test.demo.exceptions.UserNotFoundException;
import com.test.demo.model.Role;
import com.test.demo.model.User;
import com.test.demo.repository.RoleRepository;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.email.EmailService;
import com.test.demo.service.email.EmailStrategy;
import com.test.demo.service.email.implementations.VerificationEmailStrategy;
import com.test.demo.utils.VerificationToken;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void saveUserWithRole(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void saveAdmin(User user) {
        saveUserWithRole(user, "ADMIN");
    }

    @Override
    public void saveUser(User user) {
        saveUserWithRole(user, "USER");
    }

    @Override
    @Transactional
    public void registerUser(UserDTO userDTO) throws MessagingException {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        saveUser(user);

        VerificationToken token = VerificationToken.create(user);
        String encodedToken = token.encode();

        /* Guess it works fast enough lol, consider async */
//        EmailStrategy verificationEmailStrategy = new VerificationEmailStrategy(user, encodedToken);
//        emailService.sendEmail(verificationEmailStrategy, user.getEmail());
    }

    public boolean verifyUser(String token) {
        VerificationToken verificationToken = VerificationToken.parse(token);

        if (!verificationToken.isValid()) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        String email = verificationToken.email();
        Optional<User> userOptional = userRepository.findByEmailAndEmailVerifiedFalse(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();
        user.setEmailVerified(true);
        userRepository.save(user);
        return true;
    }

}
