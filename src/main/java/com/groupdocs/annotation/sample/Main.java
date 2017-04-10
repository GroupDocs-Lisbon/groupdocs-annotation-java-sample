package com.groupdocs.annotation.sample;

import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.sample.operations.*;

import java.io.File;
import java.util.Collections;

/**
 * The type Main.
 */
public class Main {

    public static void main(String[] params) throws Exception {

        Utilities.applyLicense();

        AnnotationConfig cfg = new AnnotationConfig();
        // cfg.setFontDirectories(Collections.singletonList(Utilities.PROJECT_PATH + File.separator + "Fonts"));

        CommonOperations.createDocument(cfg, Utilities.STORAGE_PATH, "source.pdf");
        CommonOperations.setDocumentAccessRights(cfg, Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        AnnotationOperations.createAndGetAnnotations(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.gettingAllDocumentAnnotations(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.resizeAndMoveAnnotations(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.editTextFieldAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.removeAnnotations(cfg, Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        ReplyOperations.processReplies(cfg, Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        CollaborationOperations.processCollaborations(cfg, Utilities.STORAGE_PATH, "source.pdf");
        CollaborationOperations.managingCollaboratorsRights(cfg, Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        ImportExportOperations.exportAnnotationsToPdf(cfg, Utilities.STORAGE_PATH, "source.pdf");
        ImportExportOperations.importAnnotationsFromPdf(cfg, Utilities.STORAGE_PATH, "document-annotated.pdf");

        System.out.println();

        DocumentOperations.gettingImageRepresentationOfDocument(cfg, Utilities.STORAGE_PATH, "source.pdf");
        DocumentOperations.gettingTextCoordinatesInImagePresentationOfDocument(cfg, Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        AnnotateOperations.creatingTextAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingAreaAnnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingPointAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingStrikeoutAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingPolylineAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextFieldAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingWatermarkAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextReplacementAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingArrowAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextRedactionAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingResourceRedactionAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingUnderlineAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingDistanceAnnotation(cfg, Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.addingAnnotationsToCellsDocument(cfg, Utilities.STORAGE_PATH, "source.xlsx");
        //AnnotateOperations.addingAnnotationsToSlidesDocument(cfg, Utilities.STORAGE_PATH, "source.pptx"); //Removed from 17.1.0
        AnnotateOperations.addingAnnotationsToWordDocument(cfg, Utilities.STORAGE_PATH, "source.docx");
    }
}
