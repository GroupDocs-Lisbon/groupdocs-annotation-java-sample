package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.common.exception.AnnotatorException;
import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.results.CreateAnnotationResult;
import com.groupdocs.annotation.domain.results.GetCollaboratorsResult;
import com.groupdocs.annotation.domain.results.SetCollaboratorsResult;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.handler.input.IDocumentDataHandler;
import com.groupdocs.annotation.handler.input.IUserDataHandler;
import com.groupdocs.annotation.handler.input.dataobjects.Document;
import com.groupdocs.annotation.handler.input.dataobjects.User;
import com.groupdocs.annotation.sample.Utilities;

import java.io.File;

/**
 * @author Aleksey Permyakov (13.09.2016)
 */
public class CollaborationOperations {
    public static void processCollaborations(String storagePath, String fileName) {
        Utilities.cleanStorage();
        // Create instance of the annotation handler
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);

        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);
        IUserDataHandler userRepository = annotator.getUserDataHandler();
        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();

        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create owner.
        User owner = userRepository.getUserByEmail("john@doe.com");

        if(owner == null)
        {
            final User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("john@doe.com");
            userRepository.add(user);
            owner = userRepository.getUserByEmail("john@doe.com");
        }

        // Create document data object in storage
        Document document = documentRepository.getDocument(fileName);

        if(document != null && document.getOwnerId() != owner.getId())
        {
            documentRepository.remove(document);
            document = null;
        }
        long documentId = document == null ? annotator.createDocument(fileName, DocumentType.Pdf, owner.getId()) : document.getId();

        // Create reviewer.
        ReviewerInfo reviewerInfo = new ReviewerInfo();
        reviewerInfo.setPrimaryEmail("judy@doe.com");
        reviewerInfo.setFirstName("Judy");
        reviewerInfo.setLastName("Doe");
        reviewerInfo.setAccessRights(AnnotationReviewerRights.All);

        // Add collaboorator to the document. If user with UserName equals to reviewers PrimaryEmail is absent it will be created.
        SetCollaboratorsResult addCollaboratorResult = annotator.addCollaborator(documentId, reviewerInfo);
        System.out.println(addCollaboratorResult);

        // Get document collaborators
        GetCollaboratorsResult getCollaboratorsResult = annotator.getCollaborators(documentId);
        System.out.println(getCollaboratorsResult);

        // Update collaborator. Only color and access rights will be updated.
        reviewerInfo.setColor(3355443L);
        SetCollaboratorsResult updateCollaboratorResult = annotator.updateCollaborator(documentId, reviewerInfo);
        System.out.println(updateCollaboratorResult);

        // Get document collaborator by email
        ReviewerInfo getCollaboratorsResultByEmail = annotator.getDocumentCollaborator(documentId, reviewerInfo.getPrimaryEmail());
        System.out.println(getCollaboratorsResultByEmail);
        // Creates collaborators object from User object.
        User user = userRepository.getUserByEmail(reviewerInfo.getPrimaryEmail());
        ReviewerInfo collaboratorMetadataResult = annotator.getCollaboratorMetadata(user.getGuid());
        System.out.println(collaboratorMetadataResult);

        // Delete collaborator
        SetCollaboratorsResult deleteCollaboratorResult = annotator.deleteCollaborator(documentId, reviewerInfo.getPrimaryEmail());
        System.out.println(deleteCollaboratorResult);
    }

    public static void managingCollaboratorsRights(String storagePath, String fileName) {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);


        IUserDataHandler userRepository = annotator.getUserDataHandler();
        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();

        // Create storage folder
        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create owner.
        User johnOwner = userRepository.getUserByEmail("john@doe.com");

        if(johnOwner == null)
        {
            final User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("john@doe.com");
            userRepository.add(user);
            johnOwner = userRepository.getUserByEmail("john@doe.com");
        }

        // Create document data object in storage
        Document document = documentRepository.getDocument(fileName);
        long documentId = document == null ? annotator.createDocument(fileName, DocumentType.Pdf, johnOwner.getId()) : document.getId();

        // Create reviewer.
        ReviewerInfo reviewerInfo = new ReviewerInfo();
        reviewerInfo.setPrimaryEmail("judy@doe.com");
        reviewerInfo.setFirstName("Judy");
        reviewerInfo.setLastName("Doe");
        reviewerInfo.setAccessRights(AnnotationReviewerRights.CanView);

        // Add collaboorator to the document. If user with UserName equals to reviewers PrimaryEmail is absent it will be created.
        SetCollaboratorsResult addCollaboratorResult = annotator.addCollaborator(documentId, reviewerInfo);

        // Get document collaborators
        GetCollaboratorsResult getCollaboratorsResult = annotator.getCollaborators(documentId);
        User judy = userRepository.getUserByEmail("judy@doe.com");

        // Create annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        pointAnnotation.setAnnotationPosition(new Point(852.0, 81.0));
        pointAnnotation.setBox(new Rectangle(212f, 81f, 142f, 0.0f));
        pointAnnotation.setType(AnnotationType.Point);
        pointAnnotation.setPageNumber(0);
        pointAnnotation.setCreatorName("Anonym A.");

        // John try to add annotations
        CreateAnnotationResult johnResult = annotator.createAnnotation(pointAnnotation, documentId, johnOwner.getId());
        System.out.println(johnResult);

        // Judy try to add annotations
        try
        {
            CreateAnnotationResult judyResult = annotator.createAnnotation(pointAnnotation, documentId, judy.getId());
            System.out.println(judyResult);
        }
        catch (AnnotatorException e)
        {
            System.out.println(e.getMessage());
        }

        // Allow Judy create annotations.
        reviewerInfo.setAccessRights(AnnotationReviewerRights.CanAnnotate);
        SetCollaboratorsResult updateCollaboratorResult = annotator.updateCollaborator(documentId, reviewerInfo);
        System.out.println(updateCollaboratorResult);

        // Judy try to add annotations
        CreateAnnotationResult judyResultCanAnnotate = annotator.createAnnotation(pointAnnotation, documentId, judy.getId());
        System.out.println(judyResultCanAnnotate);
    }
}
