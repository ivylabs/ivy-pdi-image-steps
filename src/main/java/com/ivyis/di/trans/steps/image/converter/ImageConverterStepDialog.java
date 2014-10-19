package com.ivyis.di.trans.steps.image.converter;

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
 * This class is responsible for the UI in Spoon of image converter step.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class ImageConverterStepDialog extends BaseStepDialog implements StepDialogInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = ImageConverterStepDialog.class;

  private ImageConverterStepMeta input;
  private CCombo wFileFromPathField, wFileToPathField, wExtensionFileToField, wConverterTypeField;

  public ImageConverterStepDialog(Shell parent, BaseStepMeta in, TransMeta transMeta,
      String sname) {
    super(parent, in, transMeta, sname);
    this.input = (ImageConverterStepMeta) in;
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
    shell.setText(BaseMessages.getString(PKG, "ImageConverter.Shell.Title"));

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
    wlFileFromPathField.setText(BaseMessages.getString(PKG,
        "ImageConverter.FileFromPathField.Label"));
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
        return;
      }
    });

    // File path to field
    final Label wlFileToPathField = new Label(shell, SWT.RIGHT);
    wlFileToPathField.setText(BaseMessages.getString(PKG, "ImageConverter.FileToPathField.Label"));
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
        return;
      }
    });

    // Target width field
    final Label wlExtensionFileToField = new Label(shell, SWT.RIGHT);
    wlExtensionFileToField.setText(BaseMessages.getString(PKG,
        "ImageConverter.ExtensionFileToField.Label"));
    props.setLook(wlExtensionFileToField);
    final FormData fdlExtensionFileToField = new FormData();
    fdlExtensionFileToField.left = new FormAttachment(0, 0);
    fdlExtensionFileToField.top = new FormAttachment(wFileToPathField, margin);
    fdlExtensionFileToField.right = new FormAttachment(middle, -margin);
    wlExtensionFileToField.setLayoutData(fdlExtensionFileToField);

    wExtensionFileToField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
    wExtensionFileToField.setEditable(true);
    props.setLook(wExtensionFileToField);
    wExtensionFileToField.addModifyListener(lsMod);
    final FormData fdExtensionFileToField = new FormData();
    fdExtensionFileToField.left = new FormAttachment(middle, 0);
    fdExtensionFileToField.top = new FormAttachment(wFileToPathField, margin);
    fdExtensionFileToField.right = new FormAttachment(100, 0);
    wExtensionFileToField.setLayoutData(fdExtensionFileToField);
    wExtensionFileToField.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent arg0) {
        wExtensionFileToField.setItems(FileTypeSaver.getFileTypesSaverLabel());
      }

      public void focusLost(FocusEvent arg0) {
        return;
      }
    });

    // Target height field
    final Label wlConverterTypeField = new Label(shell, SWT.RIGHT);
    wlConverterTypeField.setText(BaseMessages.getString(PKG,
        "ImageConverter.ConverterTypeField.Label"));
    props.setLook(wlConverterTypeField);
    final FormData fdlConverterTypeField = new FormData();
    fdlConverterTypeField.left = new FormAttachment(0, 0);
    fdlConverterTypeField.top = new FormAttachment(wExtensionFileToField, margin);
    fdlConverterTypeField.right = new FormAttachment(middle, -margin);
    wlConverterTypeField.setLayoutData(fdlConverterTypeField);

    wConverterTypeField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
    wConverterTypeField.setEditable(true);
    props.setLook(wConverterTypeField);
    wConverterTypeField.addModifyListener(lsMod);
    final FormData fdConverterTypeField = new FormData();
    fdConverterTypeField.left = new FormAttachment(middle, 0);
    fdConverterTypeField.top = new FormAttachment(wExtensionFileToField, margin);
    fdConverterTypeField.right = new FormAttachment(100, 0);
    wConverterTypeField.setLayoutData(fdConverterTypeField);
    wConverterTypeField.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent arg0) {
        wConverterTypeField.setItems(ConverterType.getConverterTypesLabel());
      }

      public void focusLost(FocusEvent arg0) {
        return;
      }
    });

    // OK and cancel buttons
    wOK = new Button(shell, SWT.PUSH);
    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

    BaseStepDialog.positionBottomButtons(shell, new Button[] {wOK, wCancel}, margin, null);

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
      new ErrorDialog(shell, BaseMessages.getString(PKG,
          "SyslogMessageDialog.FailedToGetFields.DialogTitle"),
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
    if (input.getConverterType() != null) {
      wConverterTypeField.setText(input.getConverterType());
    }
    if (input.getExtensionFileTo() != null) {
      wExtensionFileToField.setText(input.getExtensionFileTo());
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
  public void getInfo(ImageConverterStepMeta info) {
    input.setFileFromPath(wFileFromPathField.getText());
    input.setFileToPath(wFileToPathField.getText());
    input.setConverterType(ConverterType.typeValueFromLabel(wConverterTypeField.getText()));
    input.setExtensionFileTo(FileTypeSaver.typeValueFromLabel(wExtensionFileToField.getText()));
  }
}
