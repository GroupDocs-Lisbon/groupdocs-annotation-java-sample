package com.groupdocs.annotation.sample;


import com.groupdocs.annotation.common.license.License;

import java.io.File;

/**
 * The type Utilities.
 * @author Aleksey Permyakov
 */
public class Utilities {
    /**
     * The constant PROJECT_PATH.
     */
    public static String PROJECT_PATH = new File("Data").getAbsolutePath();
    /**
     * The constant STORAGE_PATH.
     */
    public static String STORAGE_PATH = PROJECT_PATH + File.separator + "Storage";
    /**
     * The constant OUTPUT_HTML_PATH.
     */
    public static String OUTPUT_PATH = PROJECT_PATH + File.separator + "Output";
    /**
     * The constant LICENSE_PATH.
     */
    private static String LICENSE_PATH = STORAGE_PATH + File.separator + "GroupDocs.Total.Java.lic";

    static {
        final File sp = new File(STORAGE_PATH);
        final File op = new File(OUTPUT_PATH);
        if ((!sp.exists() && !sp.mkdirs()) || (!op.exists() && !op.mkdirs())) {
            System.err.println("Can't create data directories!!!");
        }
    }

    static void applyLicense() {
        License license = new License();
        license.setLicense(LICENSE_PATH);
        System.out.println("GroupDocs.Annotation is licensed: "+ License.isValidLicense());
    }

    public static String getStoragePath(String fileName) {
        return STORAGE_PATH + File.separator + fileName;
    }

    public static String getOutputPath(String fileName) {
        return OUTPUT_PATH + File.separator + fileName;
    }
    public static void cleanStorage() {
        new File(STORAGE_PATH + File.separator + "GroupDocs.annotation.documents.xml").delete();
        new File(STORAGE_PATH + File.separator + "GroupDocs.annotation.replies").delete();
        new File(STORAGE_PATH + File.separator + "GroupDocs.annotations.xml").delete();
    }
}
