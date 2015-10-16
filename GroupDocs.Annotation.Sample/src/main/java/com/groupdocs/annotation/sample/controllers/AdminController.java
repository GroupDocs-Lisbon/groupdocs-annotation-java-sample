package com.groupdocs.annotation.sample.controllers;

import com.groupdocs.annotation.contracts.Role;
import com.groupdocs.annotation.contracts.dataobjects.UserDataObject;
import com.groupdocs.annotation.datalayer.sample.JsonDataSaver;
import com.groupdocs.annotation.facade.AnnotationFacade;
import com.groupdocs.annotation.sample.application.AuthentifactionProvider;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    JsonDataSaver storageManager;
    AnnotationFacade annotator;
    String Login = "Guest";

    public AdminController() {
        storageManager = new JsonDataSaver();
        annotator = new AnnotationFacade(storageManager);
    }

    @RequestMapping(value = {"/users"}, method = {RequestMethod.GET})
    public ModelAndView users(HttpServletRequest request) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("users");
        try {
            Login = annotator.getUser(currentUserId, currentUserId).getLogin();
        } catch (Exception e) {
        }
        model.addObject("userLogin", Login);
        List<UserDataObject> users = null;
        try {
            users = annotator.getUsers(currentUserId);
        } catch (Exception e) {
            model.addObject("Notification", e.getMessage());
            model.addObject("NotificationCSS", "notificationbox nb-error");
        }

        List<Role> roles = null;
        try {
            roles = annotator.getRoles(currentUserId);
        } catch (Exception e) {
            model.addObject("Notification", e.getMessage());
            model.addObject("NotificationCSS", "notificationbox nb-error");
        }

        model.addObject("users", users);
        model.addObject("roles", roles);
        return model;
    }

    @RequestMapping(value = {"/addUserRole"}, method = {RequestMethod.POST})
    public String addUserRole(HttpServletRequest request,
            @RequestParam(value = "userId", required = true) String userId,
            @ModelAttribute("user") UserDataObject user,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        String roleName = request.getParameterMap().get("roleName")[0];
        try {
            annotator.addRoleToUser(roleName, userId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/users";
        }
    }

    @RequestMapping(value = {"/deleteUserRole"}, method = {RequestMethod.GET})
    public String deleteUserRole(HttpServletRequest request,
            @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "roleName", required = true) String roleName,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            annotator.deleteRoleFromUser(roleName, userId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/users";
        }
    }

    @RequestMapping(value = {"/permissions"}, method = {RequestMethod.GET})
    public ModelAndView permissions(HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("permissions");
        try {
            Login = annotator.getUser(currentUserId, currentUserId).getLogin();
        } catch (Exception e) {
        }
        model.addObject("userLogin", Login);
        List<String> permissions;
        try {
            permissions = annotator.getPermissions(currentUserId);
            model.addObject("permissions", permissions);
        } catch (Exception e) {
            model.addObject("permissions", new ArrayList<String>());
            model.addObject("Notification", e.getMessage());
            model.addObject("NotificationCSS", "notificationbox nb-error");
        }

        return model;
    }

    @RequestMapping(value = {"/createPermission"}, method = {RequestMethod.POST})
    public String createPermission(HttpServletRequest request,
            @RequestParam(value = "permission", required = true) String permission,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            annotator.createPermission(permission, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/permissions";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/permissions";
        }
    }

    @RequestMapping(value = {"/deletePermission"}, method = {RequestMethod.GET})
    public String deletePermission(HttpServletRequest request,
            @RequestParam(value = "permission", required = true) String permission,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model;
        try {
            annotator.deletePermission(permission, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/permissions";

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/permissions";
        }
    }

    @RequestMapping(value = {"/roles"}, method = {RequestMethod.GET})
    public ModelAndView roles(HttpServletRequest request, boolean status) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("roles");
        try {
            Login = annotator.getUser(currentUserId, currentUserId).getLogin();
        } catch (Exception e) {
        }
        model.addObject("userLogin", Login);
        List<Role> roles;
        try {
            roles = annotator.getRoles(currentUserId);
            model.addObject("roles", roles);
            List<String> permissions = annotator.getPermissions(currentUserId);
            model.addObject("permissions", permissions);
        } catch (Exception e) {
            model.addObject("roles", new ArrayList<String>());
            model.addObject("Notification", e.getMessage());
            model.addObject("NotificationCSS", "notificationbox nb-error");
        }

        return model;
    }

    @RequestMapping(value = {"/addRolePermission"}, method = {RequestMethod.POST})
    public String addRolePermission(HttpServletRequest request,
            @RequestParam(value = "roleName", required = true) String roleName,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            annotator.addRolePermission(roleName, request.getParameterMap().get("newPermission")[0], currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/roles";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("status", e.getMessage());
            return "redirect:/roles";
        }

    }

    @RequestMapping(value = {"/deleteRolePermission"}, method = {RequestMethod.GET})
    public String deleteRolePermission(HttpServletRequest request,
            @RequestParam(value = "roleName", required = true) String roleName,
            @RequestParam(value = "deletedPermission", required = true) String deletedPermission,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            annotator.removeRolePermission(roleName, deletedPermission, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/roles";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/roles";
        }
    }

    @RequestMapping(value = {"/createRole"}, method = {RequestMethod.POST})
    public String createRole(HttpServletRequest request,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        String roleName = request.getParameterMap().get("newRole")[0];
        Role role = new Role();
        role.setName(roleName);
        try {
            annotator.createUserRole(role, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/roles";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/roles";
        }
    }

    @RequestMapping(value = {"/deleteRole"}, method = {RequestMethod.POST})
    public String deleteRole(HttpServletRequest request,
            @RequestParam(value = "role", required = true) String role,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            annotator.deleteUserRole(role, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/roles";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/roles";
        }
    }
}
