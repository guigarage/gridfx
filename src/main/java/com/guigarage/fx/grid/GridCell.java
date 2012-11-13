package com.guigarage.fx.grid;

import com.guigarage.fx.grid.skin.GridCellSkin;

import javafx.scene.control.IndexedCell;

public class GridCell<T> extends IndexedCell<T> {

	public GridCell() {
		setSkinClassName(GridCellSkin.class.getName());
	}
}