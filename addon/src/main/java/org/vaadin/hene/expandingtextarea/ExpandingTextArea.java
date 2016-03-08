package org.vaadin.hene.expandingtextarea;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.vaadin.hene.expandingtextarea.widgetset.client.ui.ExpandingTextAreaServerRpc;

import com.vaadin.data.Property;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.TextArea;
import com.vaadin.util.ReflectTools;


/**
 * An extended version of {@link TextArea} that adapts its height to the
 * textual content.
 *
 * @author Henri Kerola / Vaadin
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
		setRows(getInitialRows());
		setValue("");
	}
	
	/**
	 * Constructs an empty <code>ExpandingTextArea</code> with given caption.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 */
	public ExpandingTextArea(String caption) {
		registerRpc(rpc);
		setRows(getInitialRows());
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
		registerRpc(rpc);
		setRows(getInitialRows());
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
	 * @param value
	 *            the initial text content of the editor.
	 */
	public ExpandingTextArea(String caption, String value) {
		registerRpc(rpc);
		setRows(getInitialRows());
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
		if (rows < getInitialRows()) {
			throw new IllegalArgumentException("rows must be >= " + getInitialRows());
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

	/**
	 * Return Initial Rows
	 */
	protected int getInitialRows() {
		return rows;
	}

	/**
	 * Set Initial Rows
	 */
	public void setInitialRows(int rows){
		ExpandingTextArea.this.rows=rows;
		if (rows < 1) {
			throw new IllegalArgumentException("Initial rows must be >= 1");
		}
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

	/**
	 * Sets the maximum allowed number of rows that the {@link ExpandingTextArea} will grow to,
	 * default is null that means that there is no limit for growing.
	 *
	 * Please note that max rows doesn't work very well with IE8.
	 *
	 * @param maxRows null or >= getInitialRows().
	 */
	public void setMaxRows(Integer maxRows) {
		if (maxRows != null && maxRows < getInitialRows()) {
			throw new IllegalArgumentException("maxRows must be >= " + getInitialRows());
		}
		if ((this.maxRows == null && maxRows != null)
				|| !this.maxRows.equals(maxRows)) {
			this.maxRows = maxRows;
			requestRepaint();
		}
	}

	/**
	 * Returns the maximum allowed number of rows that the {@link ExpandingTextArea} will grow to,
	 * default is null that means that there is no limit for growing.
	 *
	 * Please note that max rows doesn't work very well with IE8.
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * Adds a {@link RowsChangeListener} to the component.
	 */
	public void addRowsChangeListener(RowsChangeListener listener) {
		addListener(RowsChangeEvent.class, listener, ROWS_CHANGE_METHOD);
	}

	/**
	 * Removes a {@link RowsChangeListener} from the component.
	 */
	public void removeRowsChangeListener(RowsChangeListener listener) {
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

	/**
	 * Listener for row count changes. When text in {@link ExpandingTextArea}
	 * is modified (by user or programmatically) and the modification affects
	 * to row count, then possible {@link RowsChangeListener} added to the
	 * component are called.
	 *
	 * Please note, that for a disabled {@link ExpandingTextArea} listeners
	 * won't be called. This is because Vaadin doesn't allow a disabled
	 * component to call a RPC from client to server.
	 */
	public interface RowsChangeListener extends Serializable {

		void rowsChange(RowsChangeEvent event);
	}

}
