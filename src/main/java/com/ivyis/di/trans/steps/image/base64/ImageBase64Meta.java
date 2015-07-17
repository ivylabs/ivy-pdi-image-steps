package com.ivyis.di.trans.steps.image.base64;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

import com.ivyis.di.ui.trans.steps.image.ImageBase64Dialog;

/**
 * This class is responsible for implementing functionality regarding step meta. All Kettle steps
 * have an extension of this where private fields have been added with public accessors.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
@Step(id = "ImageBase64", name = "ImageBase64.Step.Name",
    description = "ImageBase64.Step.Description",
    categoryDescription = "ImageBase64.Step.Category",
    image = "com/ivyis/di/trans/steps/image/ImageBase64.png",
    i18nPackageName = "com.ivyis.di.trans.steps.image",
    casesUrl = "https://github.com/ivylabs", documentationUrl = "https://github.com/ivylabs",
    forumUrl = "https://github.com/ivylabs")
public class ImageBase64Meta extends BaseStepMeta implements StepMetaInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = ImageBase64Meta.class;

  private String fileFromPath, hashFieldName, widthFieldName, heightFieldName,
      messageDigestAlg = null;
  private boolean imageFromWeb, hashWithHeightWidth;

  public String getFileFromPath() {
    return fileFromPath;
  }

  public void setFileFromPath(String fileFromPath) {
    this.fileFromPath = fileFromPath;
  }

  public String getHashFieldName() {
    return hashFieldName;
  }

  public void setHashFieldName(String hashFieldName) {
    this.hashFieldName = hashFieldName;
  }

  public String getWidthFieldName() {
    return widthFieldName;
  }

  public void setWidthFieldName(String widthFieldName) {
    this.widthFieldName = widthFieldName;
  }

  public String getHeightFieldName() {
    return heightFieldName;
  }

  public void setHeightFieldName(String heightFieldName) {
    this.heightFieldName = heightFieldName;
  }

  public String getMessageDigestAlg() {
    return messageDigestAlg;
  }

  public void setMessageDigestAlg(String messageDigestAlg) {
    this.messageDigestAlg = messageDigestAlg;
  }

  public boolean isImageFromWeb() {
    return imageFromWeb;
  }

  public void setImageFromWeb(boolean imageFromWeb) {
    this.imageFromWeb = imageFromWeb;
  }

  public boolean isHashWithHeightWidth() {
    return hashWithHeightWidth;
  }

  public void setHashWithHeightWidth(boolean hashWithHeightWidth) {
    this.hashWithHeightWidth = hashWithHeightWidth;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getXML() {
    final StringBuilder retval = new StringBuilder();
    retval.append("    " + XMLHandler.addTagValue("fileFromPath", fileFromPath));
    retval.append("    " + XMLHandler.addTagValue("messageDigestAlg", messageDigestAlg));
    retval.append("    " + XMLHandler.addTagValue("hashFieldName", hashFieldName));
    retval.append("    " + XMLHandler.addTagValue("widthFieldName", widthFieldName));
    retval.append("    " + XMLHandler.addTagValue("heightFieldName", heightFieldName));
    retval.append("    " + XMLHandler.addTagValue("imageFromWeb", imageFromWeb));
    retval.append("    " + XMLHandler.addTagValue("hashWithHeightWidth", hashWithHeightWidth));

    return retval.toString();
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public void readRep(Repository rep, ObjectId idStep, List<DatabaseMeta> databases,
      Map<String, Counter> counters)
      throws KettleException {
    try {
      fileFromPath = rep.getStepAttributeString(idStep, "fileFromPath");
      messageDigestAlg = rep.getStepAttributeString(idStep, "messageDigestAlg");
      hashFieldName = rep.getStepAttributeString(idStep, "hashFieldName");
      widthFieldName = rep.getStepAttributeString(idStep, "widthFieldName");
      heightFieldName = rep.getStepAttributeString(idStep, "heightFieldName");
      imageFromWeb = "Y".equalsIgnoreCase(rep.getStepAttributeString(idStep, "imageFromWeb"));
      hashWithHeightWidth =
          "Y".equalsIgnoreCase(rep.getStepAttributeString(idStep, "hashWithHeightWidth"));
    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "ImageHash.Exception.UnexpectedErrorInReadingStepInfo"), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public void saveRep(Repository rep, ObjectId idTransformation, ObjectId idStep)
      throws KettleException {
    try {
      rep.saveStepAttribute(idTransformation, idStep, "fileFromPath", fileFromPath);
      rep.saveStepAttribute(idTransformation, idStep, "messageDigestAlg", messageDigestAlg);
      rep.saveStepAttribute(idTransformation, idStep, "hashFieldName", hashFieldName);
      rep.saveStepAttribute(idTransformation, idStep, "widthFieldName", widthFieldName);
      rep.saveStepAttribute(idTransformation, idStep, "heightFieldName", heightFieldName);
      rep.saveStepAttribute(idTransformation, idStep, "imageFromWeb", imageFromWeb);
      rep.saveStepAttribute(idTransformation, idStep, "hashWithHeightWidth", hashWithHeightWidth);
    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "ImageHash.Exception.UnableToSaveStepInfoToRepository") + idStep, e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info,
      StepMeta nextStep, VariableSpace space) {
    final ValueMetaInterface keyFieldMeta = new ValueMeta(hashFieldName,
        ValueMetaInterface.TYPE_STRING);
    keyFieldMeta.setOrigin(origin);
    r.addValueMeta(keyFieldMeta);

    final ValueMetaInterface widthFieldMeta = new ValueMeta(widthFieldName,
        ValueMetaInterface.TYPE_INTEGER);
    widthFieldMeta.setOrigin(origin);
    r.addValueMeta(widthFieldMeta);

    final ValueMetaInterface heightFieldMeta = new ValueMeta(heightFieldName,
        ValueMetaInterface.TYPE_INTEGER);
    heightFieldMeta.setOrigin(origin);
    r.addValueMeta(heightFieldMeta);
    return;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    return super.clone();
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleXMLException
   */
  @Override
  public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
      throws KettleXMLException {
    readData(stepnode);
  }

  /**
   * Reads data from XML transformation file.
   * 
   * @param stepnode the step XML node.
   * @throws KettleXMLException the kettle XML exception.
   */
  public void readData(Node stepnode) throws KettleXMLException {
    try {
      fileFromPath = XMLHandler.getTagValue(stepnode, "fileFromPath");
      messageDigestAlg = XMLHandler.getTagValue(stepnode, "messageDigestAlg");
      hashFieldName = XMLHandler.getTagValue(stepnode, "hashFieldName");
      widthFieldName = XMLHandler.getTagValue(stepnode, "widthFieldName");
      heightFieldName = XMLHandler.getTagValue(stepnode, "heightFieldName");
      imageFromWeb = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "imageFromWeb"));
      hashWithHeightWidth =
          "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "hashWithHeightWidth"));
    } catch (Exception e) {
      throw new KettleXMLException(BaseMessages.getString(PKG,
          "ImageHash.Exception.UnexpectedErrorInReadingStepInfo"), e);
    }
  }

  /**
   * Sets the default values.
   */
  public void setDefault() {}

  /**
   * {@inheritDoc}
   */
  public boolean supportsErrorHandling() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta,
      RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {}

  /**
   * Get the Step dialog, needs for configure the step.
   * 
   * @param shell the shell.
   * @param meta the associated base step metadata.
   * @param transMeta the associated transformation metadata.
   * @param name the step name
   * @return The appropriate StepDialogInterface class.
   */
  public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta,
      String name) {
    return new ImageBase64Dialog(shell, (BaseStepMeta) meta, transMeta, name);
  }

  /**
   * Get the executing step, needed by Trans to launch a step.
   * 
   * @param stepMeta The step info.
   * @param stepDataInterface the step data interface linked to this step. Here the step can store
   *        temporary data, database connections, etc.
   * @param cnr The copy nr to get.
   * @param transMeta The transformation info.
   * @param disp The launching transformation.
   * @return The appropriate StepInterface class.
   */
  public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr,
      TransMeta transMeta,
      Trans disp) {
    return new ImageBase64(stepMeta, stepDataInterface, cnr, transMeta, disp);
  }

  /**
   * Get a new instance of the appropriate data class. This data class implements the
   * StepDataInterface. It basically contains the persisting data that needs to live on, even if a
   * worker thread is terminated.
   * 
   * @return The appropriate StepDataInterface class.
   */
  public StepDataInterface getStepData() {
    return new ImageBase64Data();
  }

}
