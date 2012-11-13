package com.guigarage.fx.grid;

import com.guigarage.fx.grid.skin.GridViewSkin;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;

public class GridView<T> extends Control {

	// Inhalt des Grid
	private ObjectProperty<ObservableList<T>> items;

	// Factory für die Zellen
	private ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactory;

	private DoubleProperty cellWidth;
	
	private DoubleProperty cellHeight;
	
	private DoubleProperty horizontalCellSpacing;
	
	private DoubleProperty verticalCellSpacing;

	public GridView() {
		this(FXCollections.<T> observableArrayList());
	}

	public GridView(ObservableList<T> items) {
		setItems(items);
		setSkinClassName(GridViewSkin.class.getName());
        setCellHeight(40);
        setCellWidth(60);
        setHorizontalCellSpacing(10);
        setVerticalCellSpacing(10);
	}
	
	public final void setCellFactory(Callback<GridView<T>, GridCell<T>> value) {
		cellFactoryProperty().set(value);
	}

	public final Callback<GridView<T>, GridCell<T>> getCellFactory() {
		return cellFactory == null ? null : cellFactory.get();
	}

	public void setCellWidth(double value) {
		cellWidthProperty().set(value);
	}
	
	public void setCellHeight(double value) {
		cellHeightProperty().set(value);
	}
	
	public void setHorizontalCellSpacing(double value) {
		horizontalCellSpacingProperty().set(value);
	}
	
	public void setVerticalCellSpacing(double value) {
		verticalCellSpacingProperty().set(value);
	}
	
	public final DoubleProperty horizontalCellSpacingProperty() {
		if (horizontalCellSpacing == null) {
			horizontalCellSpacing = new SimpleDoubleProperty(this, "horizontalCellSpacing");
		}
		return horizontalCellSpacing;
	}
	
	public final DoubleProperty verticalCellSpacingProperty() {
		if (verticalCellSpacing == null) {
			verticalCellSpacing = new SimpleDoubleProperty(this, "verticalCellSpacing");
		}
		return verticalCellSpacing;
	}
	
	public final DoubleProperty cellWidthProperty() {
		if (cellWidth == null) {
			cellWidth = new SimpleDoubleProperty(this, "cellWidth");
		}
		return cellWidth;
	}
	
	public final DoubleProperty cellHeightProperty() {
		if (cellHeight == null) {
			cellHeight = new SimpleDoubleProperty(this, "cellHeight");
		}
		return cellHeight;
	}
	
	public final ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactoryProperty() {
		if (cellFactory == null) {
			cellFactory = new SimpleObjectProperty<Callback<GridView<T>, GridCell<T>>>(
					this, "cellFactory");
		}
		return cellFactory;
	}

	public final void setItems(ObservableList<T> value) {
		itemsProperty().set(value);
	}

	public final ObservableList<T> getItems() {
		return items == null ? null : items.get();
	}

	public final ObjectProperty<ObservableList<T>> itemsProperty() {
		if (items == null) {
			items = new SimpleObjectProperty<ObservableList<T>>(this, "items");
		}
		return items;
	}
}
