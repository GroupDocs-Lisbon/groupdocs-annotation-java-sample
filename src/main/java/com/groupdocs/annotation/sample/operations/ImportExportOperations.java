package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.domain.*;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.sample.Utilities;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Aleksey Permyakov (13.09.2016)
 */
public class ImportExportOperations {
    public static void exportAnnotationsToPdf(String storagePath, String fileName) throws Exception {
        Utilities.cleanStorage();
        // Create instance of annotator.
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        // Get stream of document(in code sample document previously was added to embedded
        // resources of the assembly. You can get stream of the document in other ways.)

        InputStream manifestResourceStream = new FileInputStream(storagePath + File.separator + fileName);
        List<AnnotationInfo> annotations = new ArrayList<AnnotationInfo>();

        // Area annotation with 2 replies
        AnnotationInfo areaAnnnotation = new AnnotationInfo();
        areaAnnnotation.setAnnotationPosition(new Point(852.0, 59.0));
        AnnotationReplyInfo[] annotationReplyInfos = new AnnotationReplyInfo[2];
        annotationReplyInfos[0] = new AnnotationReplyInfo();
        annotationReplyInfos[0].setMessage("Hello!");
        annotationReplyInfos[0].setRepliedOn(Calendar.getInstance().getTime());
        annotationReplyInfos[0].setUserEmail("John");

        annotationReplyInfos[1] = new AnnotationReplyInfo();
        annotationReplyInfos[1].setMessage("Hi!");
        annotationReplyInfos[1].setRepliedOn(Calendar.getInstance().getTime());
        annotationReplyInfos[1].setUserEmail("Judy");

        areaAnnnotation.setReplies(annotationReplyInfos);
        areaAnnnotation.setBackgroundColor(11111111);
        areaAnnnotation.setBox(new Rectangle(300f, 200f, 88f, 37f));
        areaAnnnotation.setPageNumber(0);
        areaAnnnotation.setPenColor(2222222);
        areaAnnnotation.setPenStyle((byte) 1);
        areaAnnnotation.setPenWidth((byte) 1);
        areaAnnnotation.setType(AnnotationType.Area);
        areaAnnnotation.setCreatorName("Anonym A.");

        annotations.add(areaAnnnotation);

        // Add annotation to the document. Annotator will return stream with annotated document.
        InputStream stream = annotator.exportAnnotationsToDocument(manifestResourceStream, annotations, DocumentType.Pdf);

        // Save result stream to file.
        OutputStream fileStream = new FileOutputStream(Utilities.OUTPUT_PATH + File.separator + "document-annotated.pdf");

        IOUtils.copy(stream, fileStream);
        System.out.println("Document exported!");
    }

    public static void importAnnotationsFromPdf(String storagePath, String fileName) throws FileNotFoundException {
        Utilities.cleanStorage();
        // Create instance of annotator.
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);

        // Create stream of the file.
        FileInputStream stream = new FileInputStream(Utilities.OUTPUT_PATH + File.separator + fileName);

        // Import annotations from the document. File type must be same as document native type.
        AnnotationInfo[] annotations = annotator.importAnnotations(stream, DocumentType.Pdf);
        System.out.println(annotations);

        System.out.println("Document imported!");
    }
}
