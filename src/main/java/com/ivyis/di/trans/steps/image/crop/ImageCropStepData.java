package com.ivyis.di.trans.steps.image.crop;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This class contains the methods to set and retrieve the status of the step data.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class ImageCropStepData extends BaseStepData implements StepDataInterface {
  RowMetaInterface outputRowMeta = null;
  RowMetaInterface insertRowMeta = null;
  int fieldnr = 0;
  int nrPrevFields = 0;

  int indexOfFileFromPathField = -1;
  int indexOfFileToPathField = -1;
  int indexOfAxisXField = -1;
  int indexOfAxisYField = -1;
  int indexOfTargetWidthField = -1;
  int indexOfTargetHeightField = -1;
}
