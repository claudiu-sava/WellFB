package com.claudiusava.WellFB.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.naming.AuthenticationException;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model,
                            HttpServletRequest request){

        model.addAttribute("title", "Login");

        String err = request.getParameter("error");

        if(err != null){
            System.out.println(err);
        }

        return "login";

    }

    @GetMapping("/login-error")
    public String login(HttpServletRequest request,
                        Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

}
