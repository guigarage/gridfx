package com.guigarage.fx.grid.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.GridView;
import com.guigarage.fx.grid.behavior.GridViewBehavior;
import com.guigarage.fx.grid.cell.DefaultGridCell;
import com.sun.javafx.scene.control.skin.SkinBase;

public class GridViewSkin<T> extends SkinBase<GridView<T>, GridViewBehavior<T>> {

	public GridViewSkin(GridView<T> control) {
		super(control, new GridViewBehavior<>(control));
		final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>() {
			@Override
			public void onChanged(Change<? extends T> change) {
				while (change.next()) {
					int start = change.getFrom();
					int end = change.getTo();
					for (int i = start; i < end; i++) {
						if (change.wasAdded()) {
							addCell(i);
						} else if (change.wasPermutated()) {
							// TODO: what to do know??
							updateAllCells();
						} else if (change.wasRemoved()) {
							removeCell(i);
						} else if (change.wasReplaced()) {
							replaceCell(i);
						} else if (change.wasUpdated()) {
							updateCell(i);
						}
					}
				}
			}
		};

		ChangeListener<Number> layoutListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				requestLayout();
			}

		};
		
		ObservableList<T> currentList = getSkinnable().itemsProperty().get();
		if (currentList != null) {
			currentList.addListener(listViewItemsListener);
		}

		getSkinnable().itemsProperty().addListener(
				new ChangeListener<ObservableList<? extends T>>() {

					@Override
					public void changed(
							ObservableValue<? extends ObservableList<? extends T>> arg0,
							ObservableList<? extends T> arg1,
							ObservableList<? extends T> arg2) {
						if (arg1 != null) {
							arg1.removeListener(listViewItemsListener);
						}
						if (arg2 != null) {
							arg2.addListener(listViewItemsListener);
						}
					}
				});
		updateAllCells();
		
		getSkinnable().cellHeightProperty().addListener(layoutListener);
		getSkinnable().cellWidthProperty().addListener(layoutListener);
		getSkinnable().verticalCellSpacingProperty().addListener(layoutListener);
		getSkinnable().horizontalCellSpacingProperty().addListener(layoutListener);
	}

	private void updateAllCells() {
		// Remove all cells
		getChildren().clear();
		// Add all cells
		for (T item : getSkinnable().getItems()) {
			GridCell<T> cell = createCell();
			cell.setItem(item);
			getChildren().add(cell);
		}
		requestLayout();
	}

	private void removeCell(int index) {
		getChildren().remove(index);
		requestLayout();
	}

	private void replaceCell(int index) {
		getChildren().remove(index);
		addCell(index);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateCell(int index) {
		T item = getSkinnable().getItems().get(index);
		((GridCell) getChildren().get(index)).setItem(item);
	}

	private void addCell(int index) {
		T item = getSkinnable().getItems().get(index);
		GridCell<T> cell = createCell();
		cell.setItem(item);
		getChildren().add(index, cell);
		requestLayout();
	}

	public GridCell<T> createCell() {
		GridCell<T> cell;
		if (getSkinnable().getCellFactory() != null) {
			cell = getSkinnable().getCellFactory().call(getSkinnable());
		} else {
			cell = createDefaultCellImpl();
		}
		return cell;
	}

	protected GridCell<T> createDefaultCellImpl() {
		return new DefaultGridCell<T>();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		double currentWidth = getWidth();
		double cellWidth = getSkinnable().cellWidthProperty().doubleValue();
		double cellHeight = getSkinnable().cellHeightProperty().doubleValue();
		double horizontalCellSpacing = getSkinnable()
				.horizontalCellSpacingProperty().doubleValue();
		double verticalCellSpacing = getSkinnable()
				.verticalCellSpacingProperty().doubleValue();

		double xPos = horizontalCellSpacing;
		double yPos = verticalCellSpacing;

		for (Node child : getChildren()) {
			if (xPos + horizontalCellSpacing + cellWidth
					+ horizontalCellSpacing > currentWidth) {
				// wir fangen in der nächsten Zeile am Anfang an
				xPos = horizontalCellSpacing;
				yPos = yPos + verticalCellSpacing + cellHeight
						+ verticalCellSpacing;
			}
			child.relocate(xPos, yPos);
			child.resize(cellWidth, cellHeight);
			xPos = xPos + cellWidth + horizontalCellSpacing;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.layout.StackPane#getContentBias() Höhe des Grids ist
	 * von dessen Breite abhängig. Je mehr Zellen in eine Zeile passen desto
	 * geringer wird die Höhe
	 */
	@Override
	public Orientation getContentBias() {
		return Orientation.HORIZONTAL;
	}

	@Override
	protected double computeMinHeight(double width) {
		return getSkinnable().cellHeightProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue();
	}

	@Override
	protected double computeMinWidth(double height) {
		return getSkinnable().cellWidthProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue();
	}

	@Override
	protected double computeMaxHeight(double width) {
		return Double.MAX_VALUE;
	}

	@Override
	protected double computeMaxWidth(double height) {
		return Double.MAX_VALUE;
	}

	@Override
	protected double computePrefHeight(double width) {
		double cellWidth = getSkinnable().cellWidthProperty().doubleValue();
		double cellHeight = getSkinnable().cellHeightProperty().doubleValue();
		double horizontalCellSpacing = getSkinnable()
				.horizontalCellSpacingProperty().doubleValue();
		double verticalCellSpacing = getSkinnable()
				.verticalCellSpacingProperty().doubleValue();
		double completeCellWidth = cellWidth + horizontalCellSpacing + horizontalCellSpacing;
		double completeCellHeight = cellHeight + verticalCellSpacing + verticalCellSpacing;

		double maxCellsInRow = width / completeCellWidth;
		System.out.println("maxCellsInRow: " + maxCellsInRow);
		maxCellsInRow = Math.max(maxCellsInRow, 1);
		int rowCount = (int)((double)getSkinnable().getItems().size() / (double)maxCellsInRow + 0.5d);
		System.out.println("rowCount: " + rowCount);
		return rowCount * completeCellHeight;
	}

	@Override
	protected double computePrefWidth(double height) {
		double cellWidth = getSkinnable().cellWidthProperty().doubleValue();
		double cellHeight = getSkinnable().cellHeightProperty().doubleValue();
		double horizontalCellSpacing = getSkinnable()
				.horizontalCellSpacingProperty().doubleValue();
		double verticalCellSpacing = getSkinnable()
				.verticalCellSpacingProperty().doubleValue();
		double completeCellWidth = cellWidth + horizontalCellSpacing + horizontalCellSpacing;
		double completeCellHeight = cellHeight + verticalCellSpacing + verticalCellSpacing;

		int maxCellsInColumn = (int) (height / completeCellHeight - 0.5);
		System.out.println("maxCellsInColumn: " + maxCellsInColumn);
		maxCellsInColumn = Math.max(maxCellsInColumn, 1);
		int columnCount = (int)((double)getSkinnable().getItems().size() / (double)maxCellsInColumn + 0.5);
		System.out.println("columnCount: " + columnCount);
		return columnCount * completeCellWidth;
	}
}
