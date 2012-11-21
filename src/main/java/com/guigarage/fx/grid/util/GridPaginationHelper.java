package com.guigarage.fx.grid.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.util.Callback;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.GridView;
import com.sun.javafx.collections.ObservableListWrapper;

public class GridPaginationHelper<T, U extends GridView<T>> {

	private Pagination pagination;

	private ObservableList<T> items;

	private Callback<GridView<T>, GridCell<T>> gridCellFactory;

	private Callback<Integer, Node> pageFactory;

	private DoubleProperty cellWidth;

	private DoubleProperty cellHeight;

	private DoubleProperty horizontalCellSpacing;

	private DoubleProperty verticalCellSpacing;

	private ChangeListener<Number> defaultUpdateListener;
	
	@SuppressWarnings("unchecked")
	public GridPaginationHelper(Pagination pagination,
			final ObservableList<T> items,
			Callback<GridView<T>, GridCell<T>> gridCellFactory) throws InstantiationException, IllegalAccessException {
		this(pagination, items, gridCellFactory, (Class<U>) GridView.class);
	}

	public GridPaginationHelper(Pagination pagination,
			final ObservableList<T> items,
			Callback<GridView<T>, GridCell<T>> gridCellFactory,
			final Class<U> gridViewClass) throws InstantiationException, IllegalAccessException {
		this.pagination = pagination;
		this.items = items;
		this.gridCellFactory = gridCellFactory;
		
		defaultUpdateListener = new ChangeListener<Number>() {

			@Override
			public void changed(
					ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				update();
			}
		};

		pageFactory = new Callback<Integer, Node>() {

			@Override
			public Node call(Integer arg0) {
				GridView<T> gridView = null;
				try {
					gridView = gridViewClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				int startIndex = getCellStartIndexForPage(arg0);
				ObservableList<T> currentItems = new ObservableListWrapper<>(
						items.subList(
								startIndex,
								Math.min(startIndex
										+ calcMaxVisibleCellsPerPage(),
										items.size())));
				gridView.setItems(currentItems);
				gridView.setCellFactory(GridPaginationHelper.this.gridCellFactory);

				gridView.cellHeightProperty().bind(GridPaginationHelper.this.cellHeightProperty());
				gridView.cellWidthProperty().bind(GridPaginationHelper.this.cellWidthProperty());
				gridView.horizontalCellSpacingProperty().bind(GridPaginationHelper.this.horizontalCellSpacingProperty());
				gridView.verticalCellSpacingProperty().bind(GridPaginationHelper.this.verticalCellSpacingProperty());
				
				
				
				gridView.cellHeightProperty().addListener(defaultUpdateListener);
				gridView.cellWidthProperty().addListener(defaultUpdateListener);
				gridView.horizontalCellSpacingProperty().addListener(defaultUpdateListener);
				gridView.verticalCellSpacingProperty().addListener(defaultUpdateListener);
				
				return gridView;
			}
		};

		//TODO: Initialisierung der Werte, hier bin ich noch nicht ganz mit zufrieden...
		U dummyGridView = gridViewClass.newInstance();
		cellHeightProperty().setValue(dummyGridView.getCellHeight());
		cellWidthProperty().setValue(dummyGridView.getCellWidth());
		verticalCellSpacingProperty().setValue(dummyGridView.getVerticalCellSpacing());
		horizontalCellSpacingProperty().setValue(dummyGridView.getHorizontalCellSpacing());
		
		items.addListener(new ListChangeListener<T>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends T> arg0) {
				update();
			}
		});

		//TODO: Eigentlich ist das nicht richtig, da die Pagination auch die NavigationNodes beinhaltet und die Seite daher kleiner ist. Task bei JavaFX-Jira aufmachen
		pagination.widthProperty().addListener(defaultUpdateListener);

		//TODO: Eigentlich ist das nicht richtig, da die Pagination auch die NavigationNodes beinhaltet und die Seite daher kleiner ist. Task bei JavaFX-Jira aufmachen
		pagination.heightProperty().addListener(defaultUpdateListener);

		pagination.setPageFactory(pageFactory);
	}

	private int lastMaxVisibleCellsPerPage = -1;
	
	private int lastMaxRowsPerPage = -1;
	
	private void update() {
//		int firstCellOnPage = calcMaxVisibleCellsPerPage() * pagination.getCurrentPageIndex();
		pagination.setPageCount(calcPageCount());
//		pagination.setCurrentPageIndex((int) Math.floor(firstCellOnPage / calcMaxVisibleCellsPerPage()));
	}

	private int calcMaxVisibleCellsPerPage() {
		return Math.max(1, computeMaxCellsInOneRow() * computeMaxRowsPerPage());
	}

	private int computeMaxRowsPerPage() {
		double cellHeight = getCellHeight() + getVerticalCellSpacing() + getVerticalCellSpacing();
		return (int) Math.floor((pagination.getHeight() - 64) / cellHeight);
	}
	
	private int computeMaxCellsInOneRow() {
		double cellWidth = getHorizontalCellSpacing() + getCellWidth() + getHorizontalCellSpacing();
		return (int) Math.floor(pagination.getWidth() / cellWidth);
	}
	
	private int getCellStartIndexForPage(int pageIndex) {
		return calcMaxVisibleCellsPerPage() * pageIndex;
	}

	private int calcPageCount() {
		return (int) Math.floor(items.size() / calcMaxVisibleCellsPerPage());
	}

	public void setHorizontalCellSpacing(double value) {
		horizontalCellSpacingProperty().set(value);
	}

	public double getHorizontalCellSpacing() {
		return horizontalCellSpacing == null ? null : horizontalCellSpacing
				.get();
	}

	public final DoubleProperty horizontalCellSpacingProperty() {
		if (horizontalCellSpacing == null) {
			horizontalCellSpacing = new SimpleDoubleProperty(this,
					"horizontalCellSpacing");
		}
		return horizontalCellSpacing;
	}

	public void setVerticalCellSpacing(double value) {
		verticalCellSpacingProperty().set(value);
	}

	public double getVerticalCellSpacing() {
		return verticalCellSpacing == null ? null : verticalCellSpacing.get();
	}

	public final DoubleProperty verticalCellSpacingProperty() {
		if (verticalCellSpacing == null) {
			verticalCellSpacing = new SimpleDoubleProperty(this,
					"verticalCellSpacing");
		}
		return verticalCellSpacing;
	}

	public final DoubleProperty cellWidthProperty() {
		if (cellWidth == null) {
			cellWidth = new SimpleDoubleProperty(this, "cellWidth");
		}
		return cellWidth;
	}

	public void setCellWidth(double value) {
		cellWidthProperty().set(value);
	}

	public double getCellWidth() {
		return cellWidth == null ? null : cellWidth.get();
	}

	public final DoubleProperty cellHeightProperty() {
		if (cellHeight == null) {
			cellHeight = new SimpleDoubleProperty(this, "cellHeight");
		}
		return cellHeight;
	}

	public void setCellHeight(double value) {
		cellHeightProperty().set(value);
	}

	public double getCellHeight() {
		return cellHeight == null ? null : cellHeight.get();
	}
}
