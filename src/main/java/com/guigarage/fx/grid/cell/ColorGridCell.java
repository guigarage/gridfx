package com.guigarage.fx.grid.cell;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.GridView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;


public class ColorGridCell extends GridCell<Color> {
	
	public ColorGridCell() {
		getStyleClass().add("color-grid-cell");
		//TODO: die Basis-Cells sollten einen eigenen Eintrag im CSS bekommen und nicht das setCssDependency nutzen
		setCssDependency();
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
				getChildren().add(colorRect);
			}
		});
	}
	
	@Override
	protected String getUserAgentStylesheet() {
		return GridView.class.getResource("gridview.css").toExternalForm();
	}
}
