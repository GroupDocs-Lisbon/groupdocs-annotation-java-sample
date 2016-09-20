package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.results.*;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.handler.input.IDocumentDataHandler;
import com.groupdocs.annotation.handler.input.dataobjects.Document;
import com.groupdocs.annotation.sample.Utilities;

import java.io.File;

/**
 * @author Aleksey Permyakov (09.09.2016)
 */
public class AnnotationOperations {
    public static void createAndGetAnnotations(String storagePath, String documentName) throws Exception {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        IDocumentDataHandler documentRepository = annotator.getDocumentDataHandler();


        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create document data object in storage
        Document document = documentRepository.getDocument(documentName);

        long documentId = document == null ? annotator.createDocument(documentName) : document.getId();


        // Create annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        pointAnnotation.setAnnotationPosition(new Point(852.0, 81.0));
        pointAnnotation.setBox(new Rectangle(212f, 81f, 142f, 0.0f));
        pointAnnotation.setType(AnnotationType.Point);
        pointAnnotation.setPageNumber(0);
        pointAnnotation.setCreatorName("Anonym A.");
        pointAnnotation.setDocumentGuid(documentId);

        // Add annotation to storage
        CreateAnnotationResult createPointAnnotationResult = annotator.createAnnotation(pointAnnotation);
        System.out.println(createPointAnnotationResult);

        // Get annotation from storage
        GetAnnotationResult result = annotator.getAnnotation(createPointAnnotationResult.getGuid());
        System.out.println(result);
    }

    public static void gettingAllDocumentAnnotations(String storagePath, String fileName) throws Exception {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        annotator.getDocumentDataHandler();

        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create document data object in storage.
        long documentId = annotator.createDocument(fileName);
        // Create annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        pointAnnotation.setAnnotationPosition(new Point(852.0, 81.0));
        pointAnnotation.setBox(new Rectangle(212f, 81f, 142f, 0.0f));
        pointAnnotation.setType(AnnotationType.Point);
        pointAnnotation.setPageNumber(0);
        pointAnnotation.setCreatorName("Anonym A.");
        pointAnnotation.setDocumentGuid(documentId);

        // Add annotation to storage
        CreateAnnotationResult createPointAnnotationResult = annotator.createAnnotation(pointAnnotation);
        System.out.println(createPointAnnotationResult);

        // Get all annotations from storage
        ListAnnotationsResult listAnnotationsResult = annotator.getAnnotations(documentId);
        System.out.println(listAnnotationsResult);
    }

    public static void resizeAndMoveAnnotations(String storagePath, String fileName) throws Exception {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);

        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        annotator.getDocumentDataHandler();

        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }


        // Create document data object in storage.
        long documentId = annotator.createDocument(fileName);

        // Create annotation object
        AnnotationInfo areaAnnotation = new AnnotationInfo();
        areaAnnotation.setAnnotationPosition(new Point(852.0, 271.7));
        areaAnnotation.setBackgroundColor(3355443);
        areaAnnotation.setBox(new Rectangle(466f, 271f, 69f, 62f));
        areaAnnotation.setPageNumber(0);
        areaAnnotation.setPenColor(3355443);
        areaAnnotation.setType(AnnotationType.Area);
        areaAnnotation.setCreatorName("Anonym A.");
        areaAnnotation.setDocumentGuid(documentId);

        //Add annotation to storage
        CreateAnnotationResult createAreaAnnotationResult = annotator.createAnnotation(areaAnnotation);

        //Resize annotation
        final AnnotationSizeInfo annotationSizeInfo = new AnnotationSizeInfo();
        annotationSizeInfo.setHeight(80);
        annotationSizeInfo.setWidth(60);
        ResizeAnnotationResult resizeResult = annotator.resizeAnnotation(createAreaAnnotationResult.getId(), annotationSizeInfo);
        System.out.println(resizeResult);

        // Check data
        GetAnnotationResult resizedAnnotation = annotator.getAnnotation(createAreaAnnotationResult.getGuid());
        System.out.println(resizedAnnotation);

        //Move annotation marker
        MoveAnnotationResult moveAnnotationResult = annotator.moveAnnotationMarker(createAreaAnnotationResult.getId(), new Point(200, 200),/*NewPageNumber*/ 1);
        System.out.println(moveAnnotationResult);

        // Check data
        GetAnnotationResult movedAnnotation = annotator.getAnnotation(createAreaAnnotationResult.getGuid());
        System.out.println(movedAnnotation);

        // Set background color of annotation
        SaveAnnotationTextResult setBackgroundColorResult = annotator.setAnnotationBackgroundColor(createAreaAnnotationResult.getId(), 16711680);
        System.out.println(setBackgroundColorResult);

        //Check data
        GetAnnotationResult newBGColorAnnotation = annotator.getAnnotation(createAreaAnnotationResult.getGuid());
        System.out.println(newBGColorAnnotation);
    }

    public static void editTextFieldAnnotation(String storagePath, String fileName) throws Exception {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        annotator.getDocumentDataHandler();

        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create document data object in storage.
        long documentId = annotator.createDocument(fileName);

        // Create annotation object
        AnnotationInfo textFieldAnnotation = new AnnotationInfo();
        textFieldAnnotation.setAnnotationPosition(new Point(852.0, 201.0));
        textFieldAnnotation.setFieldText("text in the box");
        textFieldAnnotation.setFontFamily("Arial");
        textFieldAnnotation.setFontSize(10);
        textFieldAnnotation.setBox(new Rectangle(66f, 201f, 64f, 37f));
        textFieldAnnotation.setPageNumber(0);
        textFieldAnnotation.setType(AnnotationType.TextField);
        textFieldAnnotation.setCreatorName("Anonym A.");
        textFieldAnnotation.setDocumentGuid(documentId);

        final CreateAnnotationResult createTextFieldAnnotationResult = annotator.createAnnotation(textFieldAnnotation);

        // Update text in the annotation
        final TextFieldInfo textFieldInfo = new TextFieldInfo();
        textFieldInfo.setFieldText("new text");
        textFieldInfo.setFontFamily("Colibri");
        textFieldInfo.setFontSize(12);
        SaveAnnotationTextResult saveTextFieldResult = annotator.saveTextField(createTextFieldAnnotationResult.getId(), textFieldInfo);
        System.out.println(saveTextFieldResult);
        // Set text field color
        SaveAnnotationTextResult saveTextFieldColorResult = annotator.setTextFieldColor(createTextFieldAnnotationResult.getId(), 16753920);
        System.out.println(saveTextFieldColorResult);
    }

    public static void removeAnnotations(String storagePath, String fileName) throws Exception {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);

        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        annotator.getDocumentDataHandler();

        if(!new File(cfg.getStoragePath()).exists() && !new File(cfg.getStoragePath()).mkdirs())
        {
            System.out.println("Can't create directory!");
        }

        // Create document data object in storage.
        long documentId = annotator.createDocument(fileName);

        // Create annotation object
        AnnotationInfo pointAnnotation = new AnnotationInfo();
        pointAnnotation.setAnnotationPosition(new Point(852.0, 81.0));
        pointAnnotation.setBox(new Rectangle(212f, 81f, 142f, 0.0f));
        pointAnnotation.setType(AnnotationType.Point);
        pointAnnotation.setPageNumber(0);
        pointAnnotation.setCreatorName("Anonym A.");
        pointAnnotation.setDocumentGuid(documentId);


        // Add annotation to storage
        annotator.createAnnotation(pointAnnotation);

        // Get all annotations from storage
        ListAnnotationsResult listAnnotationsResult = annotator.getAnnotations(documentId);


        // Get annotation
        GetAnnotationResult annotation = annotator.getAnnotation(listAnnotationsResult.getAnnotations()[0].getGuid());

        // Delete annotation
        DeleteAnnotationResult deleteAnnotationResult = annotator.deleteAnnotation(annotation.getId());

        //Delete all annotations
        annotator.deleteAnnotations(documentId);

        System.out.println(deleteAnnotationResult);
    }

}
