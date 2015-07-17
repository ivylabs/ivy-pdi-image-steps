package com.ivyis.di.trans.steps.image.base64;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
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
public class ImageBase64 extends BaseStep implements StepInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = ImageBase64.class;
  private ImageBase64Meta meta;
  private ImageBase64Data data;

  public ImageBase64(
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
    this.meta = (ImageBase64Meta) smi;
    this.data = (ImageBase64Data) sdi;

    final Object[] r = getRow();
    if (r == null) {
      setOutputDone();
      return false;
    }

    if (first) {
      first = false;
      data.outputRowMeta = getInputRowMeta().clone();
      data.nrPrevFields = data.outputRowMeta.size();
      meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

      cachePosition();
    } // end if first

    final Object[] outputRow = RowDataUtil.allocateRowData(data.nrPrevFields + 3);
    for (int i = 0; i < data.nrPrevFields; i++) {
      outputRow[i] = r[i];
    }

    // hash image process
    try {
      final RowMetaInterface rowMeta = getInputRowMeta();
      final String fileFromPath = rowMeta.getString(r, data.indexOfFileFromPathField);
      BufferedImage image = null;
      try {
        if (data.imageFromWeb) {
          image = ImageIO.read(new URL(fileFromPath));
        } else {
          image = ImageIO.read(new File(fileFromPath));
        }
      } catch (IIOException ei) {
        if (getStepMeta().isDoingErrorHandling()) {
          putError(getInputRowMeta(), r, 1L, ei.toString(), null, "IMGHASH004");
          return true;
        } else {
          throw new KettleException(BaseMessages.getString(PKG, "ImageHashStep.Exception.generic"),
              ei);
        }
      }

      if (image == null) {
        if (getStepMeta().isDoingErrorHandling()) {
          putError(getInputRowMeta(), r, 1L, "Image not found", null, "IMGHASH001");
          return true;
        } else {
          throw new KettleException("Image not found");
        }
      }
      final Integer width = image.getWidth();
      final Integer height = image.getHeight();

      int[] pixels = null;
      pixels = image.getRGB(0, 0, width, height, pixels, 0, width);
      int arrSize = pixels.length;
      if (data.hashWithHeightWidth) {
        arrSize = pixels.length + 2;
      }
      final int[] hash = new int[arrSize];
      for (int i = 0; i < pixels.length; i++) {
        hash[i] = (pixels[i] & 0xff);
      }
      if (data.hashWithHeightWidth) {
        hash[hash.length - 2] = width;
        hash[hash.length - 1] = height;
      }

      try {
        final String hashMD = buildDigest(hash, data.messageDigestAlg);
        // Some debug
        if (log.isDebug()) {
          logDebug("File from path: " + fileFromPath);
          logDebug("Hash: " + hashMD);
          logDebug("Width: " + width);
          logDebug("Height: " + height);
        }
        outputRow[data.nrPrevFields] = hashMD;
        outputRow[data.nrPrevFields + 1] = width.longValue();
        outputRow[data.nrPrevFields + 2] = height.longValue();
      } catch (Exception ex) {
        if (getStepMeta().isDoingErrorHandling()) {
          putError(getInputRowMeta(), r, 1L, "Error creating hash", null, "IMGHASH002");
          return true;
        } else {
          throw new KettleException("Error creating hash", ex);
        }
      }

      putRow(data.outputRowMeta, outputRow);

      if (checkFeedback(getLinesRead())) {
        if (log.isBasic()) {
          logBasic("Linenr " + getLinesRead()); // Some basic logging
        }
      }

    } catch (Exception e) {
      if (getStepMeta().isDoingErrorHandling()) {
        putError(getInputRowMeta(), r, 1L, e.toString(), null, "IMGHASH003");
        return true;
      } else {
        throw new KettleException(BaseMessages.getString(PKG, "ImageHashStep.Exception.generic"),
            e);
      }
    }
    return true;
  }


  private static String buildDigest(int[] pixels, String alg) throws KettleException {
    try {
      final MessageDigest md = MessageDigest.getInstance(alg);
      final byte messageDigest[] = md.digest(getByteArray(pixels));
      final StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        final String hex = Integer.toHexString(0xFF & messageDigest[i]);
        if (hex.length() == 1) {
          // could use a for loop, but we're only dealing with a single byte
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException ex) {
      throw new KettleException(BaseMessages.getString(PKG, "ImageHashStep.Exception.generic"),
          ex);
    }
  }

  public static byte[] getByteArray(int[] intArray) {
    final byte[] byteArray = new byte[intArray.length * 4];
    for (int i = 0, b = 0; i < intArray.length; i++) {
      byteArray[b++] = (byte) ((intArray[i] >> 24) & 0xff);
      byteArray[b++] = (byte) ((intArray[i] >> 16) & 0xff);
      byteArray[b++] = (byte) ((intArray[i] >> 8) & 0xff);
      byteArray[b++] = (byte) (intArray[i] & 0xff);
    }
    return byteArray;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    meta = (ImageBase64Meta) smi;
    data = (ImageBase64Data) sdi;

    data.hashFieldName = environmentSubstitute(meta.getHashFieldName());
    data.widthFieldName = environmentSubstitute(meta.getHashFieldName());
    data.heightFieldName = environmentSubstitute(meta.getHashFieldName());
    data.messageDigestAlg = environmentSubstitute(meta.getMessageDigestAlg());
    data.hashWithHeightWidth = meta.isHashWithHeightWidth();
    data.imageFromWeb = meta.isImageFromWeb();

    if (super.init(smi, sdi)) {
      return true;
    }
    return false;
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
  }
}
