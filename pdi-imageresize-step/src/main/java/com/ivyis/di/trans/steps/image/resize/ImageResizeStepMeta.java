/*
 * Pentaho Data Integration ImageResize
 * 
 * Copyright (c) 2009 about.me/latinojoel
 * 
 * Licensed under the GNU General Public License, Version 3.0; you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * The program is provided "as is" without any warranty express or implied, including the warranty of non-infringement
 * and the implied warranties of merchantibility and fitness for a particular purpose. The Copyright owner will not be
 * liable for any damages suffered by you as a result of using the Program. In no event will the Copyright owner be
 * liable for any special, indirect or consequential damages or lost profits even if the Copyright owner has been
 * advised of the possibility of their occurrence.
 */
package com.ivyis.di.trans.steps.image.resize;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
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

/**
 * This class is responsible for implementing functionality regarding step meta. All Kettle steps have an extension of
 * this where private fields have been added with public accessors.
 * 
 * @author <a href="mailto:jlatino@sapo.pt">Joel Latino</a>
 * @version $Revision: 666 $
 * 
 */
public class ImageResizeStepMeta extends BaseStepMeta implements StepMetaInterface {

    /** for i18n purposes. **/
    private static final Class<?> PKG = ImageResizeStepMeta.class;

    private String fileFromPath, fileToPath, targetWidth, targetHeight = null;

    public String getFileFromPath() {
        return fileFromPath;
    }

    public void setFileFromPath(String fileFromPath) {
        this.fileFromPath = fileFromPath;
    }

    public String getFileToPath() {
        return fileToPath;
    }

    public void setFileToPath(String fileToPath) {
        this.fileToPath = fileToPath;
    }

    public String getTargetWidth() {
        return targetWidth;
    }

    public void setTargetWidth(String targetWidth) {
        this.targetWidth = targetWidth;
    }

    public String getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(String targetHeight) {
        this.targetHeight = targetHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXML() {
        final StringBuilder retval = new StringBuilder();
        retval.append("    " + XMLHandler.addTagValue("fileFromPath", fileFromPath));
        retval.append("    " + XMLHandler.addTagValue("fileToPath", fileToPath));
        retval.append("    " + XMLHandler.addTagValue("targetWidth", targetWidth));
        retval.append("    " + XMLHandler.addTagValue("targetHeight", targetHeight));

        return retval.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws KettleException
     */
    @Override
    public void readRep(Repository rep, ObjectId idStep, List<DatabaseMeta> databases, Map<String, Counter> counters)
            throws KettleException {
        try {
            fileFromPath = rep.getStepAttributeString(idStep, "fileFromPath");
            fileToPath = rep.getStepAttributeString(idStep, "fileToPath");
            targetWidth = rep.getStepAttributeString(idStep, "targetWidth");
            targetHeight = rep.getStepAttributeString(idStep, "targetHeight");
        } catch (Exception e) {
            throw new KettleException(BaseMessages.getString(PKG,
                    "ImageResize.Exception.UnexpectedErrorInReadingStepInfo"), e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws KettleException
     */
    @Override
    public void saveRep(Repository rep, ObjectId idTransformation, ObjectId idStep) throws KettleException {
        try {
            rep.saveStepAttribute(idTransformation, idStep, "fileFromPath", fileFromPath);
            rep.saveStepAttribute(idTransformation, idStep, "fileToPath", fileToPath);
            rep.saveStepAttribute(idTransformation, idStep, "targetWidth", targetWidth);
            rep.saveStepAttribute(idTransformation, idStep, "targetHeight", targetHeight);
        } catch (Exception e) {
            throw new KettleException(BaseMessages.getString(PKG,
                    "ImageResize.Exception.UnableToSaveStepInfoToRepository") + idStep, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep,
            VariableSpace space) {
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
            fileToPath = XMLHandler.getTagValue(stepnode, "fileToPath");
            targetWidth = XMLHandler.getTagValue(stepnode, "targetWidth");
            targetHeight = XMLHandler.getTagValue(stepnode, "targetHeight");
        } catch (Exception e) {
            throw new KettleXMLException(BaseMessages.getString(PKG,
                    "ImageResize.Exception.UnexpectedErrorInReadingStepInfo"), e);
        }
    }

    /**
     * Sets the default values.
     */
    public void setDefault() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta,
            RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {
        // CheckResult cr = null;
        // if (prev == null || prev.size() == 0) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NotReceivingFields"), stepMeta);
        // remarks.add(cr);
        // } else {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.StepRecevingData", prev.size() + ""), stepMeta);
        // remarks.add(cr);
        // if (prev.indexOfValue(registrationId) < 0) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NoRegistrationIdFound"), stepMeta);
        // remarks.add(cr);
        // }
        // if (retrying && "0".equals(retryNumber)) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NoRetryNumberDefined"), stepMeta);
        // remarks.add(cr);
        // }
        // if (retrying && "0".equals(delayBeforeLastRetry)) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NoDelayBeforeLastRetryDefined"), stepMeta);
        // remarks.add(cr);
        // }
        //
        // String errorMessage = "";
        // boolean errorFound = false;
        // // Starting from selected fields in ...
        // for (String f : fieldStream) {
        // if (prev.indexOfValue(f) < 0) {
        // errorMessage += "\t\t" + f + Const.CR;
        // errorFound = true;
        // }
        // }
        // if (errorFound) {
        // errorMessage = BaseMessages.getString(PKG, "AndroidPushNotification.CheckResult.FieldsFound",
        // errorMessage);
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, errorMessage, stepMeta);
        // remarks.add(cr);
        // } else {
        // if (fieldStream.size() > 0) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.AllFieldsFound"), stepMeta);
        // remarks.add(cr);
        // } else {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NoFieldsEntered"), stepMeta);
        // remarks.add(cr);
        // }
        // }
        //
        // // See if we have input streams leading to this step!
        // if (input.length > 0) {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.StepRecevingData2"), stepMeta);
        // remarks.add(cr);
        // } else {
        // cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString(PKG,
        // "AndroidPushNotification.CheckResult.NoInputReceivedFromOtherSteps"), stepMeta);
        // remarks.add(cr);
        // }
        // }
    }

    /**
     * Get the Step dialog, needs for configure the step.
     * 
     * @param shell the shell.
     * @param meta the associated base step metadata.
     * @param transMeta the associated transformation metadata.
     * @param name the step name
     * @return The appropriate StepDialogInterface class.
     */
    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new ImageResizeStepDialog(shell, (BaseStepMeta) meta, transMeta, name);
    }

    /**
     * Get the executing step, needed by Trans to launch a step.
     * 
     * @param stepMeta The step info.
     * @param stepDataInterface the step data interface linked to this step. Here the step can store temporary data,
     *            database connections, etc.
     * @param cnr The copy nr to get.
     * @param transMeta The transformation info.
     * @param disp The launching transformation.
     * @return The appropriate StepInterface class.
     */
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
            Trans disp) {
        return new ImageResizeStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    /**
     * Get a new instance of the appropriate data class. This data class implements the StepDataInterface. It basically
     * contains the persisting data that needs to live on, even if a worker thread is terminated.
     * 
     * @return The appropriate StepDataInterface class.
     */
    public StepDataInterface getStepData() {
        return new ImageResizeStepData();
    }

}
