package com.groupdocs.annotation.sample;

import com.groupdocs.annotation.sample.operations.*;

/**
 * The type Main.
 */
public class Main {

    public static void main(String[] params) throws Exception {

        Utilities.applyLicense();

        CommonOperations.createDocument(Utilities.STORAGE_PATH, "source.pdf");
        CommonOperations.setDocumentAccessRights(Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        AnnotationOperations.createAndGetAnnotations(Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.gettingAllDocumentAnnotations(Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.resizeAndMoveAnnotations(Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.editTextFieldAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotationOperations.removeAnnotations(Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        ReplyOperations.processReplies(Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        CollaborationOperations.processCollaborations(Utilities.STORAGE_PATH, "source.pdf");
        CollaborationOperations.managingCollaboratorsRights(Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        ImportExportOperations.exportAnnotationsToPdf(Utilities.STORAGE_PATH, "source.pdf");
        ImportExportOperations.importAnnotationsFromPdf(Utilities.STORAGE_PATH, "document-annotated.pdf");

        System.out.println();

        DocumentOperations.gettingImageRepresentationOfDocument(Utilities.STORAGE_PATH, "source.pdf");
        DocumentOperations.gettingTextCoordinatesInImagePresentationOfDocument(Utilities.STORAGE_PATH, "source.pdf");

        System.out.println();

        AnnotateOperations.creatingTextAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingAreaAnnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingPointAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingStrikeoutAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingPolylineAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextFieldAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingWatermarkAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextReplacementAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingArrowAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingTextRedactionAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingResourceRedactionAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingUnderlineAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.creatingDistanceAnnotation(Utilities.STORAGE_PATH, "source.pdf");
        AnnotateOperations.addingAnnotationsToCellsDocument(Utilities.STORAGE_PATH, "source.xlsx");
        AnnotateOperations.addingAnnotationsToSlidesDocument(Utilities.STORAGE_PATH, "source.pptx");
        AnnotateOperations.addingAnnotationsToWordDocument(Utilities.STORAGE_PATH, "source.docx");
    }
}
