package com.ivyis.di.trans.steps.image.converter;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageConverter;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * This class is responsible to processing the data rows.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class ImageConverterStep extends BaseStep implements StepInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = ImageConverterStep.class;
  private ImageConverterStepMeta meta;
  private ImageConverterStepData data;

  public ImageConverterStep(
      StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
    super(s, stepDataInterface, c, t, dis);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    this.meta = (ImageConverterStepMeta) smi;
    this.data = (ImageConverterStepData) sdi;

    final Object[] r = getRow();
    if (r == null) {
      setOutputDone();
      return false;
    }

    if (first) {
      first = false;
      data.outputRowMeta = super.getInputRowMeta();
      data.nrPrevFields = data.outputRowMeta.size();
      meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

      cachePosition();
    } // end if first

    final Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());
    for (int i = 0; i < data.nrPrevFields; i++) {
      outputRow[i] = r[i];
    }

    // Crop image process
    try {
      final RowMetaInterface rowMeta = getInputRowMeta();
      final String fileFromPath = rowMeta.getString(r, data.indexOfFileFromPathField);
      final String fileToPath = rowMeta.getString(r, data.indexOfFileToPathField);

      final ImagePlus imp = IJ.openImage(fileFromPath);
      final ImageConverter ic = new ImageConverter(imp);
      switch (ConverterType.fromValue(meta.getConverterType())) {
        case HSB_TO_RGB:
          ic.convertHSBToRGB();
          break;
        case RGB_STACK_TO_RGB:
          ic.convertRGBStackToRGB();
          break;
        case TO_GRAY_16:
          ic.convertToGray16();
          break;
        case TO_GRAY_32:
          ic.convertToGray32();
          break;
        case TO_GRAY_8:
          ic.convertToGray8();
          break;
        case TO_HSB:
          ic.convertToHSB();
          break;
        case TO_RGB:
          ic.convertToRGB();
          break;
        case TO_RGB_STACK:
          ic.convertToRGBStack();
          break;
        default:
          break;
      }
      final FileSaver fs = new FileSaver(imp);
      switch (FileTypeSaver.fromValue(meta.getExtensionFileTo())) {
        case BMP:
          fs.saveAsBmp(fileToPath);
          break;
        case FITS:
          fs.saveAsFits(fileToPath);
          break;
        case GIF:
          fs.saveAsGif(fileToPath);
          break;
        case JPEG:
          fs.saveAsJpeg(fileToPath);
          break;
        case LUT:
          fs.saveAsLut(fileToPath);
          break;
        case PGM:
          fs.saveAsPgm(fileToPath);
          break;
        case PNG:
          fs.saveAsPng(fileToPath);
          break;
        case RAW:
          fs.saveAsRaw(fileToPath);
          break;
        case RAW_STACK:
          fs.saveAsRawStack(fileToPath);
          break;
        case TEXT:
          fs.saveAsText(fileToPath);
          break;
        case TIFF:
          fs.saveAsTiff(fileToPath);
          break;
        case ZIP:
          fs.saveAsZip(fileToPath);
          break;
        default:
          break;
      }
    } catch (Exception e) {
      logError(e.getMessage(), e);
    }
    putRow(data.outputRowMeta, outputRow);

    if (checkFeedback(getLinesRead())) {
      if (log.isBasic()) {
        logBasic("Linenr " + getLinesRead()); // Some basic logging
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    meta = (ImageConverterStepMeta) smi;
    data = (ImageConverterStepData) sdi;

    if (super.init(smi, sdi)) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
    super.dispose(smi, sdi);
  }

  /**
   * Run is were the action happens.
   */
  public void run() {
    logBasic("Starting to run...");
    try {
      while (processRow(meta, data) && !isStopped()) {
        continue;
      }
    } catch (Exception e) {
      logError("Unexpected error : " + e.toString());
      logError(Const.getStackTracker(e));
      setErrors(1);
      stopAll();
    } finally {
      dispose(meta, data);
      logBasic("Finished, processing " + getLinesRead() + " rows");
      markStop();
    }
  }

  /**
   * Checks the fields positions.
   * 
   * @throws KettleStepException the kettle step exception.
   */
  private void cachePosition() throws KettleStepException {
    if (meta.getFileFromPath() != null && data.indexOfFileFromPathField < 0) {
      data.indexOfFileFromPathField = getInputRowMeta().indexOfValue(meta.getFileFromPath());
      if (data.indexOfFileFromPathField < 0) {
        final String message =
            "Unable to find table name field [" + meta.getFileFromPath() + "] in input row";
        logError(message);
        throw new KettleStepException(message);
      }
    }

    if (meta.getFileToPath() != null && data.indexOfFileToPathField < 0) {
      data.indexOfFileToPathField = getInputRowMeta().indexOfValue(meta.getFileToPath());
      if (data.indexOfFileToPathField < 0) {
        final String message =
            "Unable to find table name field [" + meta.getFileToPath() + "] in input row";
        logError(message);
        throw new KettleStepException(message);
      }
    }
  }
}
