package com.groupdocs.annotation.sample.controllers;

import com.groupdocs.annotation.contracts.dataobjects.UserDataObject;
import com.groupdocs.annotation.datalayer.sample.JsonDataSaver;
import com.groupdocs.annotation.facade.AnnotationFacade;
import com.groupdocs.annotation.sample.application.AuthentifactionProvider;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
    JsonDataSaver storageManager;
    AnnotationFacade annotator;
    String Login = "Guest";
    public AccountController() {
        storageManager = new JsonDataSaver();
        storageManager.setStoragePath("C:\\Users\\Yura\\Documents\\Work\\annotation\\java\\samples\\GroupDocs.Annotation.Sample\\src\\main\\resources\\");
        annotator = new AnnotationFacade(storageManager);
        AuthentifactionProvider.setStorageManager(storageManager);
    }

    @RequestMapping(value = {"/", "/login"}, method = {RequestMethod.GET})
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = {"/signin"}, method = {RequestMethod.POST})
    public String signIn(HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttrs) {
        String login = request.getParameterMap().get("login")[0];
        String password = request.getParameterMap().get("password")[0];
        Cookie userCookie;
        try {
            if (AuthentifactionProvider.validate(login, password)) {
                userCookie = AuthentifactionProvider.logIn(annotator.getCurrentUserId(login));
                response.addCookie(userCookie);
            } else {
                redirectAttrs.addFlashAttribute("Notification", "Validation error!");
                redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
                return "redirect:/login";
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/login";
        }

        return "redirect:documents";
    }

    @RequestMapping(value = {"/logout"}, method = {RequestMethod.GET})
    public String logOut(HttpServletRequest request,
            HttpServletResponse response) {
        Cookie userCookie = AuthentifactionProvider.logOut();
        response.addCookie(userCookie);
        return "redirect:/login";
    }

    @RequestMapping(value = {"/register"}, method = {RequestMethod.GET})
    public String registerPage() {
        return "register";
    }

    @RequestMapping(value = {"/signup"}, method = {RequestMethod.POST})
    public String signUp(HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttrs) {
        String login = request.getParameterMap().get("login")[0];
        String password = request.getParameterMap().get("password")[0];
        String id;
        Cookie userCookie;
        try {
            UserDataObject user = new UserDataObject();
            user.setLogin(login);
            user.setPassword(password);
            id = annotator.createUser(user, null);
            userCookie = AuthentifactionProvider.logIn(id);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/register";
        }
        redirectAttrs.addFlashAttribute("Notification", "Success!");
        redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
        response.addCookie(userCookie);
        return "redirect:/documents";
    }

    @RequestMapping(value = {"/profile"}, method = {RequestMethod.GET})
    public ModelAndView profile(
            HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            Login = annotator.getUser(currentUserId, currentUserId).getLogin();
        } catch (Exception e) {
        }
        ModelAndView model = new ModelAndView("profile");
        UserDataObject user = new UserDataObject();
        model.addObject("userLogin", Login);
        try {
            user = annotator.getUser(currentUserId, currentUserId);
        } catch (Exception e) {
            model.addObject("Notification", e.getMessage());
            model.addObject("NotificationCSS", "notificationbox nb-error");
        }
        model.addObject("roles", new ArrayList<>(user.getRoles()));
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value = {"/editProfile"}, method = {RequestMethod.POST})
    public String editProfile(UserDataObject user,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            this.annotator.editUser(user, currentUserId);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");

        }

        return "redirect:/profile";
    }

}
