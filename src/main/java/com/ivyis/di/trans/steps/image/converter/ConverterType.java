package com.ivyis.di.trans.steps.image.converter;

import org.pentaho.di.i18n.BaseMessages;

/**
 * 
 * @author latino
 * @since $version
 * 
 */
public enum ConverterType {

  HSB_TO_RGB("HSBToRGB"), RGB_STACK_TO_RGB("RGBStackToRGB"), TO_GRAY_16("ToGray16"), TO_GRAY_32(
      "ToGray32"), TO_GRAY_8(
      "ToGray8"), TO_HSB("ToHSB"), TO_RGB("ToRGB"), TO_RGB_STACK("ToRGBStack");

  /** for i18n purposes. **/
  private static final Class<?> PKG = ConverterType.class;

  private final String type;

  public String getType() {
    return type;
  }

  private ConverterType(String type) {
    this.type = type;
  }

  public static ConverterType fromValue(final String value) {
    for (ConverterType type : ConverterType.values()) {
      if (type.getType().equalsIgnoreCase(value)) {
        return type;
      }
    }
    return null;
  }

  public static String typeValueFromLabel(final String label) {
    for (ConverterType type : ConverterType.values()) {
      if (BaseMessages.getString(PKG, "ImageConverter." + type.getType() + ".Label")
          .equalsIgnoreCase(label)) {
        return type.getType();
      }
    }
    return null;
  }

  public static String[] getConverterTypesLabel() {
    int i = 0;
    final String[] converterTypes = new String[ConverterType.values().length];
    for (ConverterType type : ConverterType.values()) {
      converterTypes[i++] =
          BaseMessages.getString(PKG, "ImageConverter." + type.getType() + ".Label");
    }
    return converterTypes;
  }
}
