package org.unibl.etf.incidentservice.util;

import java.util.UUID;

public class FileHelper {

    public static String getUniqueFileName(String originalName) {
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        return UUID.randomUUID() + "." + extension;
    }

}
