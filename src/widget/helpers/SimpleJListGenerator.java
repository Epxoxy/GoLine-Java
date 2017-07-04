package widget.helpers;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import widget.MyCellRenderer;

public class SimpleJListGenerator {
	public static JList<String> build(String[] values){
		JList<String> modeSelection = new JList<String>();
		modeSelection.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] hole = values;
			public int getSize() {
				return hole.length;
			}
			public String getElementAt(int index) {
				return hole[index];
			}
		});
		modeSelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modeSelection.setCellRenderer(new MyCellRenderer<String>());
		return modeSelection;
	}
}
