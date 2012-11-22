package com.guigarage.fx.grid.skin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Node;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.GridView;
import com.guigarage.fx.grid.behavior.GridViewBehavior;
import com.guigarage.fx.grid.cell.DefaultGridCell;
import com.sun.javafx.scene.control.skin.SkinBase;

public class GridViewSkin<T> extends SkinBase<GridView<T>, GridViewBehavior<T>> {

	private ListChangeListener<T> itemsListener;

	private ChangeListener<Number> layoutListener;

	private ChangeListener<ObservableList<T>> itemListChangedListener;

	public GridViewSkin(GridView<T> control) {
		super(control, new GridViewBehavior<>(control));

		layoutListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				requestLayout();
			}

		};

		itemsListener = new ListChangeListener<T>() {
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

		itemListChangedListener = new ChangeListener<ObservableList<T>>() {

			@Override
			public void changed(
					ObservableValue<? extends ObservableList<T>> arg0,
					ObservableList<T> oldList, ObservableList<T> newList) {
				if (oldList != null) {
					oldList.removeListener(itemsListener);
				}
				if (newList != null) {
					newList.addListener(itemsListener);
				}
				updateAllCells();
			}
		};

		getSkinnable().itemsProperty().addListener(itemListChangedListener);
		ObservableList<T> currentList = getSkinnable().itemsProperty().get();
		if (currentList != null) {
			currentList.addListener(itemsListener);
		}

		getSkinnable().cellHeightProperty().addListener(layoutListener);
		getSkinnable().cellWidthProperty().addListener(layoutListener);
		getSkinnable().verticalCellSpacingProperty()
				.addListener(layoutListener);
		getSkinnable().horizontalCellSpacingProperty().addListener(
				layoutListener);

		updateAllCells();
	}

	public void updateAllCells() {
		getChildren().clear();
		ObservableList<T> items = getSkinnable().getItems();
		if (items != null) {
			
			for (int index = 0; index < items.size(); index++) {
				T item = items.get(index);
				GridCell<T> cell = createCell();
				cell.setItem(item);
				cell.updateIndex(index);
				getChildren().add(cell);
			}
		}
		requestLayout();
	}

	private void removeCell(int index) {
		getChildren().remove(index);
		//TODO: Update Index for all following cells
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
		cell.updateIndex(index);
		getChildren().add(index, cell);
		//TODO: Update Index for all following cells
		requestLayout();
	}

	private GridCell<T> createCell() {
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
		double cellWidth = getSkinnable().getCellWidth();
		double cellHeight = getSkinnable().getCellHeight();
		double horizontalCellSpacing = getSkinnable().getHorizontalCellSpacing();
		double verticalCellSpacing = getSkinnable().getVerticalCellSpacing();
		
		double xPos = 0;
		double yPos = 0;
		
		HPos currentHorizontalAlignment = getSkinnable().getHorizontalAlignment(); 
		if(currentHorizontalAlignment != null) {
			if(currentHorizontalAlignment.equals(HPos.CENTER)) {
				xPos = (currentWidth % computeCellWidth()) / 2;
			} else if(currentHorizontalAlignment.equals(HPos.RIGHT)) {
				xPos = currentWidth % computeCellWidth();
			}
		}
		
		for (Node child : getChildren()) {
			if (xPos + horizontalCellSpacing + cellWidth
					+ horizontalCellSpacing > currentWidth) {
				// new line
				xPos = 0;
				if(currentHorizontalAlignment != null) {
					if(currentHorizontalAlignment.equals(HPos.CENTER)) {
						xPos = (currentWidth % computeCellWidth()) / 2;
					} else if(currentHorizontalAlignment.equals(HPos.RIGHT)) {
						xPos = currentWidth % computeCellWidth();
					}
				}
				
				yPos = yPos + verticalCellSpacing + cellHeight
						+ verticalCellSpacing;
			}
			child.relocate(xPos + horizontalCellSpacing, yPos + verticalCellSpacing);
			child.resize(cellWidth, cellHeight);
			xPos = xPos + horizontalCellSpacing + cellWidth + horizontalCellSpacing;;
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

	protected double computeCellWidth() {
		return getSkinnable().cellWidthProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue()
				+ getSkinnable().horizontalCellSpacingProperty().doubleValue();
	}
	
	protected double computeCellHeight() {
		return getSkinnable().cellHeightProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue()
				+ getSkinnable().verticalCellSpacingProperty().doubleValue();
	}
	
	@Override
	protected double computeMinHeight(double width) {
		return computeCellHeight();
	}

	@Override
	protected double computeMinWidth(double height) {
		return computeCellWidth();
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
		int maxCellsInRow = computeMaxCellsInRow(width);
		int rowCount = (int) Math.floor((double) getSkinnable().getItems().size()
				/ (double) maxCellsInRow);
		return rowCount * computeCellHeight();
	}

	@Override
	protected double computePrefWidth(double height) {
		int maxCellsInColumn = computeMaxCellsInColumn(height);
		int columnCount = (int) Math.floor((double) getSkinnable().getItems().size()
				/ (double) maxCellsInColumn);
		return columnCount * computeCellWidth();
	}

	public int computeRowIndexForItem(int itemIndex) {
		int maxCellsInRow = computeMaxCellsInRow();
		return itemIndex / maxCellsInRow;
	}
	
	public int computeColumnIndexForItem(int itemIndex) {
		int maxCellsInRow = computeMaxCellsInRow();
		return itemIndex % maxCellsInRow;
	}
	
	public int computeMaxCellsInRow() {
		return computeMaxCellsInRow(getWidth());
	}
	
	public int computeCurrentRowCount() {
		return (int)Math.ceil((double)getSkinnable().getItems().size() / (double)computeMaxCellsInRow());
	}
	
	public int computeMaxCellsInRow(double width) {
		return Math.max((int)Math.floor(width / computeCellWidth()), 1);
	}

	public int computeMaxCellsInColumn(double height) {
		return Math.max((int)Math.floor(height / computeCellHeight()), 1);
	}
}
