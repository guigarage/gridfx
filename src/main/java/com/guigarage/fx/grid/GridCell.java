package com.guigarage.fx.grid;

import javafx.scene.control.IndexedCell;

import com.guigarage.fx.grid.skin.GridCellSkin;

public class GridCell<T> extends IndexedCell<T> {

	public GridCell() {
		getStyleClass().add("grid-cell");
	}
	
	public void setCssDependency() {
		setSkinClassName(GridCellSkin.class.getName());
	}
	
	@Override
	protected String getUserAgentStylesheet() {
		return GridView.class.getResource("gridview.css").toExternalForm();
	}
}