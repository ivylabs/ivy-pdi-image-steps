package com.ivyis.di.trans.steps.image.base64;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This class contains the methods to set and retrieve the status of the step data.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class ImageBase64Data extends BaseStepData implements StepDataInterface {
  RowMetaInterface outputRowMeta = null;
  RowMetaInterface insertRowMeta = null;
  int fieldnr = 0;
  int nrPrevFields = 0;

  int indexOfFileFromPathField = -1;
  String hashFieldName;
  String widthFieldName;
  String heightFieldName;
  boolean hashWithHeightWidth;
  boolean imageFromWeb;
  String messageDigestAlg;
}
