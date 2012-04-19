package org.vaadin.hene.expandingtextarea;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.vaadin.hene.expandingtextarea.widgetset.client.ui.ExpandingTextAreaServerRpc;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.TextArea;

/**
 * Server side component for the VExpandingTextArea widget.
 */
@SuppressWarnings("serial")
public class ExpandingTextArea extends TextArea {

	private static final Method ROWS_CHANGE_METHOD = ReflectTools.findMethod(
			RowsChangeListener.class, "rowsChange", RowsChangeEvent.class);

	private int rows = 2;
	private Integer maxRows = null;
	
	private ExpandingTextAreaServerRpc rpc = new ExpandingTextAreaServerRpc() {
		public void setRows(int rows) {
			ExpandingTextArea.this.rows = rows;
			fireRowsChangeEvent();
		}
	};

	/**
	 * Constructs an empty <code>ExpandingTextArea</code> with no caption.
	 */
	public ExpandingTextArea() {
		registerRpc(rpc);
		setRows(2);
		setValue("");
	}

	/**
	 * Constructs an empty <code>ExpandingTextArea</code> with given caption.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 */
	public ExpandingTextArea(String caption) {
		setRows(2);
		setCaption(caption);
	}

	/**
	 * Constructs a new <code>ExpandingTextArea</code> that's bound to the
	 * specified <code>Property</code> and has no caption.
	 * 
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public ExpandingTextArea(Property dataSource) {
		setRows(2);
		setPropertyDataSource(dataSource);
	}

	/**
	 * Constructs a new <code>ExpandingTextArea</code> that's bound to the
	 * specified <code>Property</code> and has the given caption
	 * <code>String</code>.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public ExpandingTextArea(String caption, Property dataSource) {
		this(dataSource);
		setCaption(caption);
	}

	/**
	 * Constructs a new <code>ExpandingTextArea</code> with the given caption
	 * and initial text contents. The editor constructed this way will not be
	 * bound to a Property unless
	 * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
	 * is called to bind it.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param text
	 *            the initial text content of the editor.
	 */
	public ExpandingTextArea(String caption, String value) {
		setRows(2);
		setValue(value);
		setCaption(caption);
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.addVariable(this, "rows", getRows());
		if (maxRows != null) {
			target.addAttribute("maxRows", maxRows);
		}
		super.paintContent(target);
	}

	@Override
	public void setRows(int rows) {
		if (rows < 2) {
			throw new IllegalArgumentException("rows must be >= 2");
		}
		if (this.rows != rows) {
			this.rows = rows;
			super.setRows(rows);
			fireRowsChangeEvent();
		}
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public void setHeight(float height, Unit unit) {
		if (height == -1) {
			super.setHeight(height, unit);
		} else {
			throw new IllegalArgumentException(
					"ExpandingTextArea supports only undefined height");
		}
	}

//	/**
//	 * Sets the maximum allowed number of rows that the TextArea will grow to,
//	 * default is null that means that there is no limit for growing.
//	 * 
//	 * @param maxRows
//	 */
//	public void setMaxRows(Integer maxRows) {
//		if (maxRows != null && maxRows < 2) {
//			throw new IllegalArgumentException("maxRows must be >= 2");
//		}
//		if ((this.maxRows == null && maxRows != null)
//				|| !this.maxRows.equals(maxRows)) {
//			this.maxRows = maxRows;
//			requestRepaint();
//		}
//	}
//
//	public int getMaxRows() {
//		return maxRows;
//	}

	public void addListener(RowsChangeListener listener) {
		addListener(RowsChangeEvent.class, listener, ROWS_CHANGE_METHOD);
	}

	public void removeListener(RowsChangeListener listener) {
		removeListener(RowsChangeEvent.class, listener, ROWS_CHANGE_METHOD);
	}

	private void fireRowsChangeEvent() {
		fireEvent(new RowsChangeEvent(this));
	}

	public class RowsChangeEvent extends Event {

		public RowsChangeEvent(ExpandingTextArea source) {
			super(source);
		}

		public ExpandingTextArea getExpandingTextArea() {
			return (ExpandingTextArea) getSource();
		}

		public int getRows() {
			return getExpandingTextArea().getRows();
		}
	}

	public interface RowsChangeListener extends Serializable {

		public void rowsChange(RowsChangeEvent event);
	}

}
