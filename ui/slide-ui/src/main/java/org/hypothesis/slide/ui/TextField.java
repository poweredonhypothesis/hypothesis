/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.slide.ui;

import org.hypothesis.interfaces.DocumentConstants;
import org.hypothesis.interfaces.Field;
import org.hypothesis.slide.ui.annotations.FieldType;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings({ "serial" })
@FieldType(DocumentConstants.TEXT_FIELD)
public class TextField extends com.vaadin.ui.TextField implements Field {

	public TextField() {
		super();
	}

	@Override
	public void setValue(String newValue) throws com.vaadin.data.Property.ReadOnlyException {
		boolean readOnly = false;
		if (isReadOnly()) {
			readOnly = true;
			setReadOnly(false);
		}
		super.setValue(newValue);

		if (readOnly) {
			setReadOnly(true);
		}
	}

}
