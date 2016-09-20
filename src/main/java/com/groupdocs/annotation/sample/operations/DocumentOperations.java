package com.groupdocs.annotation.sample.operations;

import com.groupdocs.annotation.domain.PageData;
import com.groupdocs.annotation.domain.RowData;
import com.groupdocs.annotation.domain.config.AnnotationConfig;
import com.groupdocs.annotation.domain.containers.DocumentInfoContainer;
import com.groupdocs.annotation.domain.image.PageImage;
import com.groupdocs.annotation.domain.options.ImageOptions;
import com.groupdocs.annotation.handler.AnnotationImageHandler;
import com.groupdocs.annotation.sample.Utilities;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * @author Aleksey Permyakov (15.09.2016)
 */
public class DocumentOperations {
    public static void gettingImageRepresentationOfDocument(String storagePath, String fileName) throws IOException {
        Utilities.cleanStorage();
        InputStream document = new FileInputStream(storagePath + File.separator + fileName);
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);

        AnnotationImageHandler annotationHandler = new AnnotationImageHandler(cfg);

        List<PageImage> images = annotationHandler.getPages(document, new ImageOptions());

        // Save result stream to file.
        OutputStream outputStream = new FileOutputStream(Utilities.OUTPUT_PATH + File.separator + "image.png");
        final PageImage pageImage = images.get(0);
        IOUtils.copy(pageImage.getStream(), outputStream);
    }

    public static void gettingTextCoordinatesInImagePresentationOfDocument(String storagePath, String fileName) {
        Utilities.cleanStorage();
        AnnotationConfig cfg = new AnnotationConfig();
        cfg.setStoragePath(storagePath);
        AnnotationImageHandler annotator = new AnnotationImageHandler(cfg);
        annotator.createDocument(fileName);
        DocumentInfoContainer documentInfoContainer = annotator.getDocumentInfo(fileName);

        // Go through all pages
        for (PageData pageData : documentInfoContainer.getPages())
        {
            System.out.println("Page number: " + pageData.getNumber());

            //Go through all page rows
            for(int i = 0; i < pageData.getRows().size(); i++)
            {
                RowData rowData = pageData.getRows().get(i);

                // Write data to console
                System.out.println("Row: " + (i + 1));
                System.out.println("Text: " + rowData.getText());
                System.out.println("Text width: " + rowData.getLineWidth());
                System.out.println("Text height: " + rowData.getLineHeight());
                System.out.println("Distance from left: " + rowData.getLineLeft());
                System.out.println("Distance from top: " + rowData.getLineTop());

                // Get words
                String[] words = rowData.getText().split(" ");

                // Go through all word coordinates
                for(int j = 0; j < words.length; j++)
                {
                    int coordinateIndex = j == 0 ? 0 : j + 1;
                    // Write data to console
                    System.out.println();
                    System.out.println("Word:'" + words[j] + "'");
                    System.out.println("Word distance from left: " + rowData.getTextCoordinates().get(coordinateIndex));
                    System.out.println("Word width: " + rowData.getTextCoordinates().get(coordinateIndex + 1));
                    System.out.println();
                }
            }
        }
    }
}
