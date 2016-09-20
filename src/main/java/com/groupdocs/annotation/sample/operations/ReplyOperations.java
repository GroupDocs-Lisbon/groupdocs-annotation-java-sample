package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.domain.AnnotationInfo;
import com.groupdocs.annotation.domain.AnnotationType;
import com.groupdocs.annotation.domain.Point;
import com.groupdocs.annotation.domain.Rectangle;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.results.*;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.handler.input.IDocumentDataHandler;
import com.groupdocs.annotation.handler.input.dataobjects.Document;
import com.groupdocs.annotation.sample.Utilities;

import java.io.File;

/**
 * @author Aleksey Permyakov (13.09.2016)
 */
public class ReplyOperations {
    public static void processReplies(String storagePath, String fileName) {
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
        final Document document = documentRepository.getDocument(fileName);

        long documentId = document == null ? annotator.createDocument(fileName) : document.getId();

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

        // Add simple reply to created annotation
        AddReplyResult addSimpleReplyResult =  annotator.createAnnotationReply(createPointAnnotationResult.getId(), "first question");
        // Edit created reply
        EditReplyResult editReplyResult = annotator.editAnnotationReply(addSimpleReplyResult.getReplyGuid(), "changed question");
        System.out.println(editReplyResult);

        // Create child reply. This reply will be linked to previously created reply.
        AddReplyResult addChildReplyResult = annotator.createAnnotationReply(createPointAnnotationResult.getId(), "answer", addSimpleReplyResult.getReplyGuid());
        System.out.println(addChildReplyResult);

        //Delete annotation reply by guid
        DeleteReplyResult deleteReplyResult = annotator.deleteAnnotationReply(addChildReplyResult.getReplyGuid());
        System.out.println(deleteReplyResult);

        // Delete all replies from annotation
        annotator.deleteAnnotationReplies(createPointAnnotationResult.getId());

        // List of replies after deleting all replies
        ListRepliesResult listRepliesResultAfterDeleteAll = annotator.listAnnotationReplies(createPointAnnotationResult.getId());
        System.out.println(listRepliesResultAfterDeleteAll);
    }
}
