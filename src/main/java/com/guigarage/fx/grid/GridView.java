package com.guigarage.fx.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.util.Callback;

import com.guigarage.fx.grid.util.SimpleStyleableDoubleProperty;
import com.sun.javafx.css.StyleableObjectProperty;
import com.sun.javafx.css.StyleableProperty;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;

public class GridView<T> extends Control {

	// Inhalt des Grid
	private ObjectProperty<ObservableList<T>> items;

	// Factory für die Zellen
	private ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactory;

	private DoubleProperty cellWidth;

	private DoubleProperty cellHeight;

	private DoubleProperty horizontalCellSpacing;

	private DoubleProperty verticalCellSpacing;

	private ObjectProperty<Pos> alignment;

	public GridView() {
		this(FXCollections.<T> observableArrayList());
	}

	public GridView(ObservableList<T> items) {
		getStyleClass().add("grid-view");
		setItems(items);
	}

	public void setHorizontalCellSpacing(double value) {
		horizontalCellSpacingProperty().set(value);
	}

	public double getHorizontalCellSpacing() {
		return horizontalCellSpacing == null ? 12.0 : horizontalCellSpacing
				.get();
	}

	public final DoubleProperty horizontalCellSpacingProperty() {
		if (horizontalCellSpacing == null) {
			horizontalCellSpacing = new SimpleStyleableDoubleProperty(this,
					"horizontalCellSpacing",
					StyleableProperties.HORIZONTAL_CELL_SPACING);
		}
		return horizontalCellSpacing;
	}

	public void setVerticalCellSpacing(double value) {
		verticalCellSpacingProperty().set(value);
	}

	public double getVerticalCellSpacing() {
		return verticalCellSpacing == null ? 12.0 : verticalCellSpacing.get();
	}

	public final DoubleProperty verticalCellSpacingProperty() {
		if (verticalCellSpacing == null) {
			verticalCellSpacing = new SimpleStyleableDoubleProperty(this,
					"verticalCellSpacing",
					StyleableProperties.VERTICAL_CELL_SPACING);
		}
		return verticalCellSpacing;
	}

	public final DoubleProperty cellWidthProperty() {
		if (cellWidth == null) {
			cellWidth = new SimpleStyleableDoubleProperty(this, "cellWidth",
					StyleableProperties.CELL_WIDTH);
		}
		return cellWidth;
	}

	public void setCellWidth(double value) {
		cellWidthProperty().set(value);
	}

	public double getCellWidth() {
		return cellWidth == null ? 64.0 : cellWidth.get();
	}

	public final DoubleProperty cellHeightProperty() {
		if (cellHeight == null) {
			cellHeight = new SimpleStyleableDoubleProperty(this, "cellHeight",
					StyleableProperties.CELL_HEIGHT);
		}
		return cellHeight;
	}

	public void setCellHeight(double value) {
		cellHeightProperty().set(value);
	}

	public double getCellHeight() {
		return cellHeight == null ? 64.0 : cellHeight.get();
	}

	public final ObjectProperty<Pos> alignmentProperty() {
		if (alignment == null) {
			alignment = new StyleableObjectProperty<Pos>(Pos.CENTER_LEFT) {

				@SuppressWarnings("rawtypes")
				@Override
				public StyleableProperty getStyleableProperty() {
					return StyleableProperties.ALIGNMENT;
				}

				@Override
				public Object getBean() {
					return GridView.this;
				}

				@Override
				public String getName() {
					return "alignment";
				}
			};
		}
		return alignment;
	}

	public final void setAlignment(Pos value) {
		alignmentProperty().set(value);
	}

	public final Pos getAlignment() {
		return alignment == null ? Pos.TOP_CENTER : alignment.get();
	}

	public final ObjectProperty<Callback<GridView<T>, GridCell<T>>> cellFactoryProperty() {
		if (cellFactory == null) {
			cellFactory = new SimpleObjectProperty<Callback<GridView<T>, GridCell<T>>>(
					this, "cellFactory");
		}
		return cellFactory;
	}

	public final void setCellFactory(Callback<GridView<T>, GridCell<T>> value) {
		cellFactoryProperty().set(value);
	}

	public final Callback<GridView<T>, GridCell<T>> getCellFactory() {
		return cellFactory == null ? null : cellFactory.get();
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

	@Override
	protected String getUserAgentStylesheet() {
		return GridView.class.getResource("gridview.css").toExternalForm();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private static class StyleableProperties {

		private static final StyleableProperty<GridView, Pos> ALIGNMENT = new StyleableProperty<GridView, Pos>(
				"-fx-alignment", new EnumConverter<Pos>(Pos.class),
				Pos.TOP_CENTER) {

			@Override
			public boolean isSettable(GridView n) {
				return n.alignment == null || !n.alignment.isBound();
			}

			@SuppressWarnings("unchecked")
			@Override
			public WritableValue<Pos> getWritableValue(GridView n) {
				return n.alignmentProperty();
			}

			@Override
			public Pos getInitialValue(GridView n) {
				return Pos.TOP_CENTER;
			}
		};

		private static final StyleableProperty<GridView, Number> CELL_WIDTH = new StyleableProperty<GridView, Number>(
				"-fx-cell-width", SizeConverter.getInstance(), 64.0) {

			@Override
			public boolean isSettable(GridView n) {
				return n.cellWidth == null || !n.cellWidth.isBound();
			}

			@Override
			public WritableValue<Number> getWritableValue(GridView n) {
				return n.cellWidthProperty();
			}
		};

		private static final StyleableProperty<GridView, Number> CELL_HEIGHT = new StyleableProperty<GridView, Number>(
				"-fx-cell-height", SizeConverter.getInstance(), 64.0) {

			@Override
			public boolean isSettable(GridView n) {
				return n.cellHeight == null || !n.cellHeight.isBound();
			}

			@Override
			public WritableValue<Number> getWritableValue(GridView n) {
				return n.cellHeightProperty();
			}
		};

		private static final StyleableProperty<GridView, Number> HORIZONTAL_CELL_SPACING = new StyleableProperty<GridView, Number>(
				"-fx-horizontal-cell-spacing", SizeConverter.getInstance(),
				12.0) {

			@Override
			public boolean isSettable(GridView n) {
				return n.horizontalCellSpacing == null
						|| !n.horizontalCellSpacing.isBound();
			}

			@Override
			public WritableValue<Number> getWritableValue(GridView n) {
				return n.horizontalCellSpacingProperty();
			}
		};

		private static final StyleableProperty<GridView, Number> VERTICAL_CELL_SPACING = new StyleableProperty<GridView, Number>(
				"-fx-vertical-cell-spacing", SizeConverter.getInstance(), 12.0) {

			@Override
			public boolean isSettable(GridView n) {
				return n.verticalCellSpacing == null
						|| !n.verticalCellSpacing.isBound();
			}

			@Override
			public WritableValue<Number> getWritableValue(GridView n) {
				return n.verticalCellSpacingProperty();
			}
		};

		private static final List<StyleableProperty> STYLEABLES;
		static {
			final List<StyleableProperty> styleables = new ArrayList<StyleableProperty>(
					Control.impl_CSS_STYLEABLES());
			Collections.addAll(styleables, ALIGNMENT, CELL_HEIGHT, CELL_WIDTH,
					HORIZONTAL_CELL_SPACING, VERTICAL_CELL_SPACING);
			STYLEABLES = Collections.unmodifiableList(styleables);
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<StyleableProperty> impl_CSS_STYLEABLES() {
		return GridView.StyleableProperties.STYLEABLES;
	}

	@SuppressWarnings("rawtypes")
	public List<StyleableProperty> impl_getStyleableProperties() {
		return impl_CSS_STYLEABLES();
	}
}
