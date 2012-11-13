package com.guigarage.fx.grid.behavior;

import com.guigarage.fx.grid.GridCell;
import com.sun.javafx.scene.control.behavior.CellBehaviorBase;

public class GridCellBehavior<T> extends CellBehaviorBase<GridCell<T>> {
	public GridCellBehavior(GridCell<T> control) {
		super(control);
	}
}
