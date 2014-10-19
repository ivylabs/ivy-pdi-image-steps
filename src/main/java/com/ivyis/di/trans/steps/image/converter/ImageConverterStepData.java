package com.ivyis.di.trans.steps.image.converter;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This class contains the methods to set and retrieve the status of the step data.
 * 
 * @author <a href="mailto:jlatino@sapo.pt">Joel Latino</a>
 * @version $Revision: 666 $
 * 
 */
public class ImageConverterStepData extends BaseStepData implements StepDataInterface {
  RowMetaInterface outputRowMeta = null;
  RowMetaInterface insertRowMeta = null;
  int fieldnr = 0;
  int nrPrevFields = 0;

  int indexOfFileFromPathField = -1;
  int indexOfFileToPathField = -1;
}
