package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.domain.AnnotationReviewerRights;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.handler.input.IDocumentDataHandler;
import com.groupdocs.annotation.sample.Utilities;

import java.io.File;

/**
 * The type View generator.
 * @author Aleksey Permyakov
 */
public class CommonOperations {
    public static void createDocument(AnnotationConfig cfg, String storageFolder, String documentName) {
        Utilities.cleanStorage();
        // Create instance of the annotation handler

        cfg.setStoragePath(storageFolder);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);
        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();
        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }
        // Create document data object in storage.
        long documentId = annotator.createDocument(documentName);

        System.out.println("Document ID: " + documentId);
    }

    public static void setDocumentAccessRights(AnnotationConfig cfg, String storageFolder, String documentName) {
        Utilities.cleanStorage();
        // Create instance of the annotation handler

        cfg.setStoragePath(storageFolder);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();
        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create document data object in storage.
        long documentId = annotator.createDocument(documentName);

        // Set document access rights
        annotator.setDocumentAccessRights(documentId, AnnotationReviewerRights.All);
    }
}
