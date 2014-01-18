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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * This class is responsible for Push notification UI on Spoon.
 * 
 * @author <a href="mailto:jlatino@sapo.pt">Joel Latino</a>
 * @version $Revision: 666 $
 * 
 */
public class ImageResizeStepDialog extends BaseStepDialog implements StepDialogInterface {

    /** for i18n purposes. **/
    private static final Class<?> PKG = ImageResizeStepDialog.class;

    private ImageResizeStepMeta input;
    private CCombo wFileFromPathField, wFileToPathField, wTargetWidthField, wTargetHeightField;

    public ImageResizeStepDialog(Shell parent, BaseStepMeta in, TransMeta transMeta, String sname) {
        super(parent, in, transMeta, sname);
        this.input = (ImageResizeStepMeta) in;
    }

    /**
     * Opens a step dialog window.
     * 
     * @return the (potentially new) name of the step
     */
    public String open() {
        final Shell parent = getParent();
        final Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        props.setLook(shell);
        setShellImage(shell, this.input);

        final ModifyListener lsMod = new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                input.setChanged();
            }
        };
        backupChanged = input.hasChanged();

        final FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;
        shell.setLayout(formLayout);
        shell.setText(BaseMessages.getString(PKG, "ImageResize.Shell.Title"));

        final int middle = props.getMiddlePct();
        final int margin = Const.MARGIN;

        // Stepname line
        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
        props.setLook(wlStepname);
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment(0, 0);
        fdlStepname.right = new FormAttachment(middle, -margin);
        fdlStepname.top = new FormAttachment(0, margin);
        wlStepname.setLayoutData(fdlStepname);

        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment(middle, 0);
        fdStepname.top = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);
        wStepname.setLayoutData(fdStepname);

        // File path from field
        final Label wlFileFromPathField = new Label(shell, SWT.RIGHT);
        wlFileFromPathField.setText(BaseMessages.getString(PKG, "ImageResize.FileFromPathField.Label"));
        props.setLook(wlFileFromPathField);
        final FormData fdlFileFromPathField = new FormData();
        fdlFileFromPathField.left = new FormAttachment(0, 0);
        fdlFileFromPathField.top = new FormAttachment(wStepname, margin);
        fdlFileFromPathField.right = new FormAttachment(middle, -margin);
        wlFileFromPathField.setLayoutData(fdlFileFromPathField);

        wFileFromPathField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wFileFromPathField.setEditable(true);
        props.setLook(wFileFromPathField);
        wFileFromPathField.addModifyListener(lsMod);
        final FormData fdFileFromPathField = new FormData();
        fdFileFromPathField.left = new FormAttachment(middle, 0);
        fdFileFromPathField.top = new FormAttachment(wStepname, margin);
        fdFileFromPathField.right = new FormAttachment(100, 0);
        wFileFromPathField.setLayoutData(fdFileFromPathField);
        wFileFromPathField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent arg0) {
                getStreamFields(wFileFromPathField);
            }

            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        // File path to field
        final Label wlFileToPathField = new Label(shell, SWT.RIGHT);
        wlFileToPathField.setText(BaseMessages.getString(PKG, "ImageResize.FileToPathField.Label"));
        props.setLook(wlFileToPathField);
        final FormData fdlFileToPathField = new FormData();
        fdlFileToPathField.left = new FormAttachment(0, 0);
        fdlFileToPathField.top = new FormAttachment(wFileFromPathField, margin);
        fdlFileToPathField.right = new FormAttachment(middle, -margin);
        wlFileToPathField.setLayoutData(fdlFileToPathField);

        wFileToPathField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wFileToPathField.setEditable(true);
        props.setLook(wFileToPathField);
        wFileToPathField.addModifyListener(lsMod);
        final FormData fdFileToPathField = new FormData();
        fdFileToPathField.left = new FormAttachment(middle, 0);
        fdFileToPathField.top = new FormAttachment(wFileFromPathField, margin);
        fdFileToPathField.right = new FormAttachment(100, 0);
        wFileToPathField.setLayoutData(fdFileToPathField);
        wFileToPathField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent arg0) {
                getStreamFields(wFileToPathField);
            }

            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Target width field
        final Label wlTargetWidthField = new Label(shell, SWT.RIGHT);
        wlTargetWidthField.setText(BaseMessages.getString(PKG, "ImageResize.TargetWidthField.Label"));
        props.setLook(wlTargetWidthField);
        final FormData fdlTargetWidthField = new FormData();
        fdlTargetWidthField.left = new FormAttachment(0, 0);
        fdlTargetWidthField.top = new FormAttachment(wFileToPathField, margin);
        fdlTargetWidthField.right = new FormAttachment(middle, -margin);
        wlTargetWidthField.setLayoutData(fdlTargetWidthField);

        wTargetWidthField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wTargetWidthField.setEditable(true);
        props.setLook(wTargetWidthField);
        wTargetWidthField.addModifyListener(lsMod);
        final FormData fdTargetWidthField = new FormData();
        fdTargetWidthField.left = new FormAttachment(middle, 0);
        fdTargetWidthField.top = new FormAttachment(wFileToPathField, margin);
        fdTargetWidthField.right = new FormAttachment(100, 0);
        wTargetWidthField.setLayoutData(fdTargetWidthField);
        wTargetWidthField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent arg0) {
                getStreamFields(wTargetWidthField);
            }

            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Target height field
        final Label wlTargetHeightField = new Label(shell, SWT.RIGHT);
        wlTargetHeightField.setText(BaseMessages.getString(PKG, "ImageResize.TargetHeightField.Label"));
        props.setLook(wlTargetHeightField);
        final FormData fdlTargetHeightField = new FormData();
        fdlTargetHeightField.left = new FormAttachment(0, 0);
        fdlTargetHeightField.top = new FormAttachment(wTargetWidthField, margin);
        fdlTargetHeightField.right = new FormAttachment(middle, -margin);
        wlTargetHeightField.setLayoutData(fdlTargetHeightField);

        wTargetHeightField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wTargetHeightField.setEditable(true);
        props.setLook(wTargetHeightField);
        wTargetHeightField.addModifyListener(lsMod);
        final FormData fdTargetHeightField = new FormData();
        fdTargetHeightField.left = new FormAttachment(middle, 0);
        fdTargetHeightField.top = new FormAttachment(wTargetWidthField, margin);
        fdTargetHeightField.right = new FormAttachment(100, 0);
        wTargetHeightField.setLayoutData(fdTargetHeightField);
        wTargetHeightField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent arg0) {
                getStreamFields(wTargetHeightField);
            }

            public void focusLost(FocusEvent arg0) {
                // TODO Auto-generated method stub
            }
        });

        // OK and cancel buttons
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, null);

        // Add listeners
        lsCancel = new Listener() {
            public void handleEvent(Event e) {
                cancel();
            }
        };
        lsOK = new Listener() {
            public void handleEvent(Event e) {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };
        wStepname.addSelectionListener(lsDef);

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged(backupChanged);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return stepname;
    }

    /**
     * Gets the values in the stream for ComboBox.
     * 
     * @param cCombo the ComboBox.
     */
    private void getStreamFields(CCombo cCombo) {
        try {
            final String source = cCombo.getText();
            cCombo.removeAll();
            final RowMetaInterface r = transMeta.getPrevStepFields(stepname);
            if (r != null && !r.isEmpty()) {
                cCombo.setItems(r.getFieldNames());
                if (source != null) {
                    cCombo.setText(source);
                }
            }
        } catch (KettleException ke) {
            new ErrorDialog(shell, BaseMessages.getString(PKG, "SyslogMessageDialog.FailedToGetFields.DialogTitle"),
                    BaseMessages.getString(PKG, "SyslogMessageDialog.FailedToGetFields.DialogMessage"), ke);
        }
    }

    /**
     * Read data and place it in the dialog.
     */
    public void getData() {
        wStepname.selectAll();
        if (input.getFileFromPath() != null) {
            wFileFromPathField.setText(input.getFileFromPath());
        }
        if (input.getFileToPath() != null) {
            wFileToPathField.setText(input.getFileToPath());
        }
        if (input.getTargetWidth() != null) {
            wTargetWidthField.setText(input.getTargetWidth());
        }
        if (input.getTargetHeight() != null) {
            wTargetHeightField.setText(input.getTargetHeight());
        }
    }

    /**
     * Cancel.
     */
    private void cancel() {
        stepname = null;
        input.setChanged(backupChanged);
        dispose();
    }

    /**
     * Let the plugin know about the entered data.
     */
    private void ok() {
        if (!Const.isEmpty(wStepname.getText())) {
            stepname = wStepname.getText();
            getInfo(input);
            dispose();
        }
    }

    /**
     * Get the information.
     * 
     * @param info the push notification step meta data.
     */
    public void getInfo(ImageResizeStepMeta info) {
        input.setFileFromPath(wFileFromPathField.getText());
        input.setFileToPath(wFileToPathField.getText());
        input.setTargetWidth(wTargetWidthField.getText());
        input.setTargetHeight(wTargetHeightField.getText());
    }
}
