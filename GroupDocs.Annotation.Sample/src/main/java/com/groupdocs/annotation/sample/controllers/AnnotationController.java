package com.groupdocs.annotation.sample.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.groupdocs.annotation.contracts.DocumentMetadata;
import com.groupdocs.annotation.contracts.dataobjects.AnnotationDataObject;
import com.groupdocs.annotation.contracts.dataobjects.AnnotationReplyDataObject;
import com.groupdocs.annotation.contracts.dataobjects.CollaboratorDataObject;
import com.groupdocs.annotation.datalayer.sample.JsonDataSaver;
import com.groupdocs.annotation.facade.AnnotationFacade;
import com.groupdocs.annotation.sample.application.AuthentifactionProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AnnotationController {

    JsonDataSaver storageManager;
    AnnotationFacade annotator;
    String Login = "Guest";

    public AnnotationController() {
        storageManager = new JsonDataSaver();
        annotator = new AnnotationFacade(storageManager);

    }

    @RequestMapping(value = {"/documents"}, method = {RequestMethod.GET})
    public ModelAndView documentsTable(HttpServletRequest request) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            Login = annotator.getUser(currentUserId, currentUserId).getLogin();
        } catch (Exception e) {
        }
        ModelAndView model = new ModelAndView("documents");
        model.addObject("userLogin", Login);
        List<DocumentMetadata> documents = null;
        try {
            documents = annotator.getDocumentsInfo(currentUserId);
        } catch (Exception e) {
        }

        model.addObject("documents", documents);
        return model;
    }

    @RequestMapping(value = {"/getPdfVersionOfDocument"}, method = {RequestMethod.POST})
    public ResponseEntity getClearDocument(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        DocumentMetadata documentMetadata = null;
        try {
            documentMetadata = annotator.getDocumentMetadata(fileId, currentUserId);
        } catch (Exception e) {
        }
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<byte[]> response = null;
        try {
            byte[] contents = annotator.getClearDocument(fileId, currentUserId).toByteArray();
            String documentName = documentMetadata.getName() + documentMetadata.getExtension();
            headers.setContentDispositionFormData(documentName, documentName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (Exception e) {
        }

        return response;
    }

    @RequestMapping(value = {"/getAnnotatedDocument"}, method = {RequestMethod.POST})
    public Object getAnnotatedDocument(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        DocumentMetadata documentMetadata = null;
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            documentMetadata = annotator.getDocumentMetadata(fileId, currentUserId);
        } catch (Exception e) {
        }
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<byte[]> response = null;
        try {
            byte[] contents = annotator.getAnnotatedDocument(fileId, currentUserId).toByteArray();
            String filename = documentMetadata.getName() + documentMetadata.getExtension();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/documents";
        }
        return response;
    }

    @RequestMapping(value = {"/deleteDocument"}, method = {RequestMethod.POST})
    public String deletedDocument(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            annotator.deleteDocument(fileId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/documents";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/documents";
        }
    }

    @RequestMapping(value = {"/uploadFile"}, method = {RequestMethod.POST})
    public String uploadDocument(HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        DocumentMetadata documentMetadata = new DocumentMetadata();
        try {
            documentMetadata.setName(file.getOriginalFilename());
            documentMetadata.setAuthor(annotator.getUser(currentUserId, currentUserId).getName());
            documentMetadata.setCreatedOn(new Date());
            documentMetadata.setOwnerId(currentUserId);
            documentMetadata.setExtension(documentMetadata.getName().substring(documentMetadata.getName().indexOf('.'), documentMetadata.getName().length()));
            annotator.loadDocument(file.getInputStream(), documentMetadata, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/documents";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/documents";
        }
    }

    @RequestMapping(value = {"/documentAnnotations"}, method = {RequestMethod.POST, RequestMethod.GET})
    public Object getAnnotations(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("annotations");
        model.addObject("userLogin", Login);
        DocumentMetadata documentMetadata;
        List<AnnotationDataObject> annotations;
        try {
            documentMetadata = annotator.getDocumentMetadata(fileId, currentUserId);
            annotations = annotator.getAnnotations(fileId, currentUserId);
            Map<String, String> idandAnnotationsMap = new TreeMap<>();
            if (annotations != null) {
                Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                idandAnnotationsMap = new TreeMap<>();
                for (AnnotationDataObject annotation : annotations) {
                    idandAnnotationsMap.put(annotation.getId(), gson.toJson(annotation));
                }
            }
            model.addObject("fileId", fileId);
            model.addObject("documentName", documentMetadata.getName());
            model.addObject("annotations", idandAnnotationsMap.entrySet());
            return model;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/documents";
        }
    }

    @RequestMapping(value = {"/allVersionsOfAnnotation"}, method = {RequestMethod.POST})
    public ModelAndView allVersionsOfAnnotation(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("allVersionsOfAnnotation");
        model.addObject("userLogin", Login);
        DocumentMetadata documentMetadata;
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        ArrayList<String> annotationsJson = new ArrayList<>();
        AnnotationDataObject annotation, nextAnnotation;
        try {
            documentMetadata = annotator.getDocumentMetadata(fileId, currentUserId);
            for (int i = 1;; i++) {
                annotation = annotator.getAnnotation(fileId, annotationId, currentUserId, i);
                nextAnnotation = annotator.getAnnotation(fileId, annotationId, currentUserId, i + 1);
                annotationsJson.add(gson.toJson(annotation));
                if (Objects.equals(annotation.getVersion(), nextAnnotation.getVersion())) {
                    break;
                }
            }
            model.addObject("fileId", fileId);
            model.addObject("documentName", documentMetadata.getName());
            model.addObject("annotations", annotationsJson);
            return model;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return model;
        }
    }

    @RequestMapping(value = {"/deleteAnnotation"}, method = {RequestMethod.POST})
    public String deleteAnnotation(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam("annotationId") String annotationId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            AnnotationDataObject tmpAnnotation = new AnnotationDataObject();
            tmpAnnotation.setId(annotationId);
            annotator.deleteAnnotation(fileId, tmpAnnotation, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:documentAnnotations?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:documentAnnotations?fileId=" + fileId;
        }
    }

    @RequestMapping(value = {"/editAnnotation"}, method = {RequestMethod.POST})
    public ModelAndView editAnnotation(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("editAnnotation");
        model.addObject("userLogin", Login);
        AnnotationDataObject annotation;
        try {
            annotation = annotator.getAnnotation(fileId, annotationId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            model.addObject("fileId", fileId);
            model.addObject("annotation", annotation);
            return model;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return model;
        }
    }

    @RequestMapping(value = {"/saveEditedAnnotation"}, method = {RequestMethod.POST})
    public String editAnnotation(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @ModelAttribute("annotation") AnnotationDataObject annotation,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        try {
            annotator.editAnnotation(fileId, annotation, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:documentAnnotations?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:documentAnnotations?fileId=" + fileId;
        }
    }

    @RequestMapping(value = {"/createAnnotation"}, method = {RequestMethod.GET})
    public ModelAndView createAnnotation(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        ModelAndView model = new ModelAndView("createAnnotation");
        model.addObject("userLogin", Login);
        model.addObject("fileId", fileId);
        model.addObject("annotation", new AnnotationDataObject());
        return model;
    }

    @RequestMapping(value = {"/addAnnotation"}, method = {RequestMethod.POST})
    public String createAnnotation(HttpServletRequest request,
            @ModelAttribute("annotation") AnnotationDataObject annotation,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            String annotationId = annotator.createAnnotation(fileId, annotation, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:documentAnnotations?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:documentAnnotations?fileId=" + fileId;
        }
    }

    @RequestMapping(value = {"/editAnnotationReplies"}, method = {RequestMethod.GET})
    public ModelAndView editAnnotationReplies(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("editAnnotationReplies");
        model.addObject("fileId", fileId);
        model.addObject("annotationId", annotationId);
        model.addObject("userLogin", Login);
        List<AnnotationReplyDataObject> replies;
        try {
            replies = annotator.getAnnotationReplies(fileId, annotationId, currentUserId);
            model.addObject("newReply", new AnnotationReplyDataObject());
            model.addObject("replies", replies);
            return model;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return model;
        }
    }

    @RequestMapping(value = {"/editAnnotationReply"}, method = {RequestMethod.POST})
    public String editAnnotationReply(HttpServletRequest request,
            @ModelAttribute("reply") AnnotationReplyDataObject reply,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            String replyId = annotator.addAnnotationReply(fileId, annotationId, reply, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        }
    }

    @RequestMapping(value = {"/createNewAnnotationReply"}, method = {RequestMethod.POST})
    public String createNewAnnotationReply(HttpServletRequest request,
            @ModelAttribute("reply") AnnotationReplyDataObject reply,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            String replyId = annotator.addAnnotationReply(fileId, annotationId, reply, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        }
    }

    @RequestMapping(value = {"/deleteAnnotationReply"}, method = {RequestMethod.POST})
    public String deleteAnnotationReply(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "annotationId", required = true) String annotationId,
            @RequestParam(value = "replyId", required = true) String replyId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            annotator.deleteAnnotationReply(fileId, annotationId, replyId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:editAnnotationReplies?fileId=" + fileId + "&annotationId=" + annotationId;
        }
    }

    @RequestMapping(value = {"/collaborators"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object collaborators(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
        ModelAndView model = new ModelAndView("collaborators");
        model.addObject("userLogin", Login);
        Map<String, CollaboratorDataObject> userCollaboratorModel = new HashMap<>();
        try {
            model.addObject("fileId", fileId);
            model.addObject("roles", annotator.getRoles(currentUserId));
            model.addObject("users", annotator.getUsers(currentUserId));
            List<CollaboratorDataObject> collaborators = annotator.getDocumentCollaborators(fileId, currentUserId);
            for (CollaboratorDataObject tmpCollaborator : collaborators) {
                userCollaboratorModel.put(annotator.getUser(tmpCollaborator.getUserId(), currentUserId).getName(), tmpCollaborator);
            }
            model.addObject("collaborators", userCollaboratorModel.entrySet());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/documents";
        }

        return model;
    }

    @RequestMapping(value = {"/addDocumentCollaborator"}, method = {RequestMethod.POST})
    public String addDocumentCollaborator(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            String userId = request.getParameterMap().get("UserId")[0];
            String role = request.getParameterMap().get("Role")[0];
            CollaboratorDataObject collaborator = new CollaboratorDataObject();
            collaborator.setUserId(userId);
            collaborator.setRole(role);
            annotator.addDocumentCollaborator(fileId, collaborator, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/collaborators?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/collaborators?fileId=" + fileId;
        }
    }

    @RequestMapping(value = {"/editDocumentCollaborator"}, method = {RequestMethod.POST})
    public String editDocumentCollaborator(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            String userId = request.getParameterMap().get("UserId")[0];
            String collaboratorId = request.getParameterMap().get("Id")[0];
            String role = request.getParameterMap().get("Role")[0];
            CollaboratorDataObject collaborator = new CollaboratorDataObject();
            collaborator.setUserId(userId);
            collaborator.setRole(role);
            collaborator.setId(collaboratorId);
            annotator.editDocumentCollaborator(fileId, collaborator, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/collaborators?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/collaborators?fileId=" + fileId;
        }
    }

    @RequestMapping(value = {"/deleteDocumentCollaborator"}, method = {RequestMethod.POST})
    public String deleteDocumentCollaborator(HttpServletRequest request,
            @RequestParam(value = "fileId", required = true) String fileId,
            @RequestParam(value = "collaboratorId", required = true) String collaboratorId,
            RedirectAttributes redirectAttrs) {
        try {
            String currentUserId = AuthentifactionProvider.getUserId(request.getCookies());
            annotator.deleteDocumentCollaborator(fileId, collaboratorId, currentUserId);
            redirectAttrs.addFlashAttribute("Notification", "Success!");
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-success");
            return "redirect:/collaborators?fileId=" + fileId;
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("Notification", e.getMessage());
            redirectAttrs.addFlashAttribute("NotificationCSS", "notificationbox nb-error");
            return "redirect:/collaborators?fileId=" + fileId;
        }
    }
}
