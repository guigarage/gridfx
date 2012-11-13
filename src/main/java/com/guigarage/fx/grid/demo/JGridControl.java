package com.guigarage.fx.grid.demo;

import com.guigarage.fx.grid.GridView;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;

public class JGridControl extends VBox {

	public JGridControl(@SuppressWarnings("rawtypes") GridView myGrid) {
		Slider columnWidthSlider = SliderBuilder.create().min(10).max(512).build();
		columnWidthSlider.valueProperty().bindBidirectional(myGrid.cellWidthProperty());
		getChildren().add(HBoxBuilder.create().children(new Label("ColumnWidth"), columnWidthSlider).build());
		
		Slider columnHeightSlider = SliderBuilder.create().min(10).max(512).build();
		columnHeightSlider.valueProperty().bindBidirectional(myGrid.cellHeightProperty());
		getChildren().add(HBoxBuilder.create().children(new Label("columnHeight"), columnHeightSlider).build());
		
		Slider horizontalCellSpacingSlider = SliderBuilder.create().min(0).max(64).build();
		horizontalCellSpacingSlider.valueProperty().bindBidirectional(myGrid.horizontalCellSpacingProperty());
		getChildren().add(HBoxBuilder.create().children(new Label("horizontalCellSpacing"), horizontalCellSpacingSlider).build());
		
		Slider verticalCellSpacingSlider = SliderBuilder.create().min(0).max(64).build();
		verticalCellSpacingSlider.valueProperty().bindBidirectional(myGrid.verticalCellSpacingProperty());
		getChildren().add(HBoxBuilder.create().children(new Label("verticalCellSpacing"), verticalCellSpacingSlider).build());
	}
}
