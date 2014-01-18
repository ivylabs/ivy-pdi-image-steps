/*
 * Pentaho Data Integration ImageCrop
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
public class ImageResizeStepData extends BaseStepData implements StepDataInterface {
    RowMetaInterface outputRowMeta = null;
    RowMetaInterface insertRowMeta = null;
    int fieldnr = 0;
    int nrPrevFields = 0;

    int indexOfFileFromPathField = -1;
    int indexOfFileToPathField = -1;
    int indexOfTargetWidthField = -1;
    int indexOfTargetHeightField = -1;
}
