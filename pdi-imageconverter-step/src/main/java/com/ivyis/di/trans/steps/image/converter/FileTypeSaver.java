package com.ivyis.di.trans.steps.image.converter;

import org.pentaho.di.i18n.BaseMessages;

public enum FileTypeSaver {

    BMP("Bmp"), FITS("Fits"), GIF("Gif"), JPEG("jpeg"), LUT("lut"), PGM("pgm"), PNG("Png"), RAW("Raw"), RAW_STACK(
            "RawStack"), TEXT("Text"), TIFF("Tiff"), ZIP("Zip");

    /** for i18n purposes. **/
    private static final Class<?> PKG = ConverterType.class;

    private final String type;

    private FileTypeSaver(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static String typeValueFromLabel(final String label) {
        for (FileTypeSaver type : FileTypeSaver.values()) {
            if (BaseMessages.getString(PKG, "ImageConverter." + type.getType() + ".Label").equalsIgnoreCase(label)) {
                return type.getType();
            }
        }
        return null;
    }
    
    public static FileTypeSaver fromValue(final String value) {
        for (FileTypeSaver type : FileTypeSaver.values()) {
            if (type.getType().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    public static String[] getFileTypesSaverLabel() {
        int i = 0;
        final String[] fileTypesSaverLabel = new String[FileTypeSaver.values().length];
        for (FileTypeSaver type : FileTypeSaver.values()) {
            fileTypesSaverLabel[i++] = BaseMessages.getString(PKG, "ImageConverter." + type.getType() + ".Label");
        }
        return fileTypesSaverLabel;
    }
}
