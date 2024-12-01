package com.test.demo.controller;

import com.test.demo.dto.UserDTO;
import com.test.demo.service.auth.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class UserController {
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/";
        }

        if (error == null) {
            model.addAttribute("pageTitle", "Login");
            return "login";
        }

        String errorMessage = Optional.ofNullable(request.getSession(false))
                .map(session -> (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION))
                .map(AuthenticationException::getMessage)
                .orElse("Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        logger.info("UserDTO: " + userDTO);

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }

        if (userService.findUserByEmail(userDTO.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "An account with this email already exists");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userDTO);
        } catch (Exception e) {
            logger.severe("Error occurred: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    @RequestMapping("/pre-home")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        if (!userService.verifyUser(token)) {
            model.addAttribute("errorMessage", "Invalid token");
            return "redirect:/login";
        }
        model.addAttribute("errorMessage", "Email verified successfully");
        /* handling messages with redirects is not the best practice, why? */
        return "redirect:/login";
    }

}
