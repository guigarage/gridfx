package com.guigarage.fx.grid.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.guigarage.fx.grid.GridView;

public class JGridFXDemo2 extends Application {

	private GridView<String> myGrid;
	
	public static void main(String[] args) {
		JGridFXDemo2.launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JGridFX Demo 2");
		
		final ObservableList<String> list = FXCollections.<String>observableArrayList();
		myGrid = new GridView<>(list);
		
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");

		final BorderPane root = new BorderPane();
		root.setTop(myGrid);
		
		Scene scene = new Scene(root, 540, 210);

		
		root.addEventHandler(ZoomEvent.ANY, new EventHandler<ZoomEvent>() {

			@Override
			public void handle(ZoomEvent arg0) {
				System.out.println("Zoom");
			}
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
