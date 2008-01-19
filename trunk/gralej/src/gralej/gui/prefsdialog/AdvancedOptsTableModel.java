package gralej.gui.prefsdialog;


import java.util.Collections;
import java.util.List;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import javax.swing.table.AbstractTableModel;

/** A table model for the JTable on the "advanced"
 * tab of GraleJ's preferences.
 * @author Niels Ott
 * @version $Id$
  */
public class AdvancedOptsTableModel extends AbstractTableModel implements GPrefsChangeListener {
	
	private static final long serialVersionUID = 2156889278746031347L;

	GralePreferences prefs;
	List<String> keyList;
	
	
	public AdvancedOptsTableModel(GralePreferences prefs) {
		this.prefs = prefs;
		keyList = prefs.getKeyList();
		Collections.sort(keyList);
		
		// register myself as preference listener to everything (empty prefix)
		prefs.addListener(this, "");
		
	}

	/**
	 * @return 2
	 */
	public int getColumnCount() {
		return 2;
	}

	/**
	 * @return number of preference key/value pairs available
	 */
	public int getRowCount() {
		return keyList.size();
	}

	/**
	 * @return the key name for preference number rowIndex if columnIndex = 0,
	 * otherwise the preference value of preference number rowIndex.
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {

		String buf =  keyList.get(rowIndex);
		if ( columnIndex == 1) {
			buf = prefs.get(buf);
		}
		return buf;
	}

	/**
	 * @return false for the left column, true for the right one.
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return ( columnIndex != 0);
	}

	/**
	 * notifies the JTable of an update of the GralePreferences backend.
	 */
	public void preferencesChange() {
		fireTableDataChanged();
	}
	
	/**
	 * @returns the column headers.
	 */
	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "key";
		} else if ( col == 1) {
			return "value";
		} else {
			return super.getColumnName(col);
		}

	}

	/**
	 * Used by JTable to set the value
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String value = (String)aValue;
		String key = keyList.get(rowIndex);
		prefs.put(key, value);
		
	}
	
	

}
