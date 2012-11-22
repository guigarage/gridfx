package com.guigarage.fx.grid.cell;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

import com.guigarage.fx.grid.GridCell;


public class ColorGridCell extends GridCell<Color> {
	
	public ColorGridCell() {
		getStyleClass().add("color-grid-cell");
		itemProperty().addListener(new ChangeListener<Color>() {

			@Override
			public void changed(ObservableValue<? extends Color> arg0,
					Color arg1, Color arg2) {
				getChildren().clear();
				//TODO: Die Properties des Rect sollten ins CSS wandern
				Rectangle colorRect = RectangleBuilder.create().fill(arg2)
						.stroke(Color.BLACK).build();
				colorRect.heightProperty().bind(heightProperty());
				colorRect.widthProperty().bind(widthProperty());				
				setGraphic(colorRect);
			}
		});
	}
}
