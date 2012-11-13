package com.guigarage.fx.grid.skin;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.behavior.GridCellBehavior;
import com.sun.javafx.scene.control.skin.CellSkinBase;

public class GridCellSkin<T> extends CellSkinBase<GridCell<T>, GridCellBehavior<T>>{

	public GridCellSkin(GridCell<T> control) {
		super(control, new GridCellBehavior<>(control));
	}
	
}
