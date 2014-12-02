package com.ivyis.di.ui.trans.steps.image;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import org.pentaho.di.ui.core.dialog.ShowBrowserDialog;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.ui.util.ImageUtil;

import com.ivyis.di.trans.steps.image.hash.ImageHashStepMeta;

/**
 * This class is responsible for the UI in Spoon of image resize step.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class ImageHashStepDialog extends BaseStepDialog implements StepDialogInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = ImageHashStepDialog.class;

  private ImageHashStepMeta input;
  private CCombo wFileFromPathField, wMessageDigestAlg;
  private Text wHashFieldName, wWidthFieldName, wHeightFieldName;
  private Button wImageFromWeb, wHashWithHeightWidth;

  public ImageHashStepDialog(Shell parent, BaseStepMeta in, TransMeta transMeta, String sname) {
    super(parent, in, transMeta, sname);
    this.input = (ImageHashStepMeta) in;
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
    shell.setText(BaseMessages.getString(PKG, "ImageHash.Shell.Title"));

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
    wlFileFromPathField.setText(BaseMessages.getString(PKG, "ImageHash.FileFromPathField.Label"));
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


    // Image from web
    final Label wlImageFromWeb = new Label(shell, SWT.RIGHT);
    wlImageFromWeb.setText(BaseMessages.getString(PKG, "ImageHash.ImageFromWeb.Label"));
    props.setLook(wlImageFromWeb);
    final FormData fdlImageFromWeb = new FormData();
    fdlImageFromWeb.left = new FormAttachment(0, -margin);
    fdlImageFromWeb.top = new FormAttachment(wFileFromPathField, margin);
    fdlImageFromWeb.right = new FormAttachment(middle, -2 * margin);
    wlImageFromWeb.setLayoutData(fdlImageFromWeb);

    wImageFromWeb = new Button(shell, SWT.CHECK);
    props.setLook(wImageFromWeb);
    final FormData fdImageFromWeb = new FormData();
    fdImageFromWeb.left = new FormAttachment(middle, 0);
    fdImageFromWeb.right = new FormAttachment(100, 0);
    fdImageFromWeb.top = new FormAttachment(wFileFromPathField, margin);
    wImageFromWeb.setLayoutData(fdImageFromWeb);

    // Hash with height and width
    final Label wlHashWithHeightWidth = new Label(shell, SWT.RIGHT);
    wlHashWithHeightWidth.setText(BaseMessages
        .getString(PKG, "ImageHash.HashWithHeightWidth.Label"));
    props.setLook(wlHashWithHeightWidth);
    final FormData fdlHashWithHeightWidth = new FormData();
    fdlHashWithHeightWidth.left = new FormAttachment(0, -margin);
    fdlHashWithHeightWidth.top = new FormAttachment(wImageFromWeb, margin);
    fdlHashWithHeightWidth.right = new FormAttachment(middle, -2 * margin);
    wlHashWithHeightWidth.setLayoutData(fdlHashWithHeightWidth);

    wHashWithHeightWidth = new Button(shell, SWT.CHECK);
    props.setLook(wHashWithHeightWidth);
    final FormData fdHashWithHeightWidth = new FormData();
    fdHashWithHeightWidth.left = new FormAttachment(middle, 0);
    fdHashWithHeightWidth.right = new FormAttachment(100, 0);
    fdHashWithHeightWidth.top = new FormAttachment(wImageFromWeb, margin);
    wHashWithHeightWidth.setLayoutData(fdHashWithHeightWidth);

    // Message Digest Algorithm field
    final Label wlMessageDigestAlg = new Label(shell, SWT.RIGHT);
    wlMessageDigestAlg.setText(BaseMessages.getString(PKG, "ImageHash.MessageDigestAlg.Label"));
    props.setLook(wlMessageDigestAlg);
    final FormData fdlMessageDigestAlg = new FormData();
    fdlMessageDigestAlg.left = new FormAttachment(0, 0);
    fdlMessageDigestAlg.top = new FormAttachment(wHashWithHeightWidth, margin);
    fdlMessageDigestAlg.right = new FormAttachment(middle, -margin);
    wlMessageDigestAlg.setLayoutData(fdlMessageDigestAlg);

    wMessageDigestAlg = new CCombo(shell, SWT.BORDER);
    wMessageDigestAlg.setEditable(true);
    props.setLook(wMessageDigestAlg);
    wMessageDigestAlg.addModifyListener(lsMod);
    final FormData fdMessageDigestAlg = new FormData();
    fdMessageDigestAlg.left = new FormAttachment(middle, 0);
    fdMessageDigestAlg.top = new FormAttachment(wHashWithHeightWidth, margin);
    fdMessageDigestAlg.right = new FormAttachment(100, 0);
    wMessageDigestAlg.setLayoutData(fdMessageDigestAlg);
    wMessageDigestAlg
        .setItems(new String[] {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"});

    // Hash field name
    final Label wlHashFieldName = new Label(shell, SWT.RIGHT);
    wlHashFieldName.setText(BaseMessages.getString(PKG, "ImageHash.HashFieldName.Label"));
    props.setLook(wlHashFieldName);
    final FormData fdlHashFieldName = new FormData();
    fdlHashFieldName.left = new FormAttachment(0, 0);
    fdlHashFieldName.top = new FormAttachment(wMessageDigestAlg, margin);
    fdlHashFieldName.right = new FormAttachment(middle, -margin);
    wlHashFieldName.setLayoutData(fdlHashFieldName);

    wHashFieldName = new Text(shell, SWT.BORDER);
    wHashFieldName.setEditable(true);
    props.setLook(wHashFieldName);
    wHashFieldName.addModifyListener(lsMod);
    final FormData fdHashFieldName = new FormData();
    fdHashFieldName.left = new FormAttachment(middle, 0);
    fdHashFieldName.top = new FormAttachment(wMessageDigestAlg, margin);
    fdHashFieldName.right = new FormAttachment(100, 0);
    wHashFieldName.setLayoutData(fdHashFieldName);

    // Width field name
    final Label wlWidthFieldName = new Label(shell, SWT.RIGHT);
    wlWidthFieldName.setText(BaseMessages.getString(PKG, "ImageHash.WidthFieldName.Label"));
    props.setLook(wlWidthFieldName);
    final FormData fdlWidthFieldName = new FormData();
    fdlWidthFieldName.left = new FormAttachment(0, 0);
    fdlWidthFieldName.top = new FormAttachment(wHashFieldName, margin);
    fdlWidthFieldName.right = new FormAttachment(middle, -margin);
    wlWidthFieldName.setLayoutData(fdlWidthFieldName);

    wWidthFieldName = new Text(shell, SWT.BORDER);
    wWidthFieldName.setEditable(true);
    props.setLook(wWidthFieldName);
    wWidthFieldName.addModifyListener(lsMod);
    final FormData fdWidthFieldName = new FormData();
    fdWidthFieldName.left = new FormAttachment(middle, 0);
    fdWidthFieldName.top = new FormAttachment(wHashFieldName, margin);
    fdWidthFieldName.right = new FormAttachment(100, 0);
    wWidthFieldName.setLayoutData(fdWidthFieldName);

    // Height field
    final Label wlHeightFieldName = new Label(shell, SWT.RIGHT);
    wlHeightFieldName.setText(BaseMessages.getString(PKG, "ImageHash.HeightFieldName.Label"));
    props.setLook(wlHeightFieldName);
    final FormData fdlHeightFieldName = new FormData();
    fdlHeightFieldName.left = new FormAttachment(0, 0);
    fdlHeightFieldName.top = new FormAttachment(wWidthFieldName, margin);
    fdlHeightFieldName.right = new FormAttachment(middle, -margin);
    wlHeightFieldName.setLayoutData(fdlHeightFieldName);

    wHeightFieldName = new Text(shell, SWT.BORDER);
    wHeightFieldName.setEditable(true);
    props.setLook(wHeightFieldName);
    wHeightFieldName.addModifyListener(lsMod);
    final FormData fdHeightFieldName = new FormData();
    fdHeightFieldName.left = new FormAttachment(middle, 0);
    fdHeightFieldName.top = new FormAttachment(wWidthFieldName, margin);
    fdHeightFieldName.right = new FormAttachment(100, 0);
    wHeightFieldName.setLayoutData(fdHeightFieldName);

    // OK and cancel buttons
    wOK = new Button(shell, SWT.PUSH);
    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

    BaseStepDialog.positionBottomButtons(shell, new Button[] {wOK, wCancel}, margin, null);

    final Button button = new Button(shell, SWT.PUSH);
    button.setImage(ImageUtil.getImage(display, getClass(), "ivyis_logo.png"));
    button.setToolTipText("Ivy Information Systems");
    button.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        final ShowBrowserDialog sbd =
            new ShowBrowserDialog(shell, BaseMessages.getString(PKG,
                "ExportCmdLine.CommandLine.Title"),
                "<html><script>window.location=\"http://www.ivy-is.co.uk\"</script></html>");
        sbd.open();
      }
    });

    // Determine the largest button in the array
    Rectangle largest = null;
    button.pack(true);
    final Rectangle r = button.getBounds();
    if (largest == null || r.width > largest.width) {
      largest = r;
    }

    // Also, set the tooltip the same as the name if we don't have one...
    if (button.getToolTipText() == null) {
      button.setToolTipText(Const.replace(button.getText(), "&", ""));
    }

    // Make buttons a bit larger... (nicer)
    largest.width += 10;
    if ((largest.width % 2) == 1) {
      largest.width++;
    }

    BaseStepDialog.rightAlignButtons(new Button[] {button}, largest.width, margin, null);
    if (Const.isOSX()) {
      final List<TableView> tableViews = new ArrayList<TableView>();
      getTableViews(shell, tableViews);
      button.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          for (TableView view : tableViews) {
            view.applyOSXChanges();
          }
        }
      });
    }

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
    if (input.getHashFieldName() != null) {
      wHashFieldName.setText(input.getHashFieldName());
    }
    if (input.getHeightFieldName() != null) {
      wHeightFieldName.setText(input.getHeightFieldName());
    }
    if (input.getWidthFieldName() != null) {
      wWidthFieldName.setText(input.getWidthFieldName());
    }
    if (input.getMessageDigestAlg() != null) {
      wMessageDigestAlg.setText(input.getMessageDigestAlg());
    }
    wHashWithHeightWidth.setSelection(input.isHashWithHeightWidth());
    wImageFromWeb.setSelection(input.isImageFromWeb());
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
  public void getInfo(ImageHashStepMeta info) {
    input.setFileFromPath(wFileFromPathField.getText());
    input.setMessageDigestAlg(wMessageDigestAlg.getText());
    input.setHashFieldName(wHashFieldName.getText());
    input.setWidthFieldName(wWidthFieldName.getText());
    input.setHeightFieldName(wHeightFieldName.getText());
    input.setHashWithHeightWidth(wHashWithHeightWidth.getSelection());
    input.setImageFromWeb(wImageFromWeb.getSelection());
  }

  /**
   * Gets the table views.
   * 
   * @param parentControl the parent control
   * @param tableViews the table views
   * @return the table views
   */
  private static final void getTableViews(Control parentControl, List<TableView> tableViews) {
    if (parentControl instanceof TableView) {
      tableViews.add((TableView) parentControl);
    } else {
      if (parentControl instanceof Composite) {
        final Control[] children = ((Composite) parentControl).getChildren();
        for (Control child : children) {
          getTableViews(child, tableViews);
        }
      } else {
        if (parentControl instanceof Shell) {
          final Control[] children = ((Shell) parentControl).getChildren();
          for (Control child : children) {
            getTableViews(child, tableViews);
          }
        }
      }
    }
  }
}
