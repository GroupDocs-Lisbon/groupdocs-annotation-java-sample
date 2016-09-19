package com.groupdocs.annotation.sample.operations;

import com.aspose.ms.System.IO.Directory;
import com.groupdocs.annotation.domain.AnnotationReviewerRights;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.handler.input.IDocumentDataHandler;
import com.groupdocs.annotation.sample.Utilities;

/**
 * The type View generator.
 * @author Aleksey Permyakov
 */
public class CommonOperations {
    public static void createDocument(String storageFolder, String documentName) {
        Utilities.cleanStorage();
        // Create instance of the annotation handler
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storageFolder);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);
        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();
        if (!Directory.exists(cfg.getStoragePath())) {
            Directory.createDirectory(cfg.getStoragePath());
        }
        // Create document data object in storage.
        long documentId = annotator.createDocument(documentName);

        System.out.println("Document ID: " + documentId);
    }

    public static void setDocumentAccessRights(String storageFolder, String documentName) {
        Utilities.cleanStorage();
        // Create instance of the annotation handler
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storageFolder);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();
        if(!Directory.exists(cfg.getStoragePath()))
        {
            Directory.createDirectory(cfg.getStoragePath());
        }

        // Create document data object in storage.
        long documentId = annotator.createDocument(documentName);

        // Set document access rights
        annotator.setDocumentAccessRights(documentId, AnnotationReviewerRights.All);
    }
}
