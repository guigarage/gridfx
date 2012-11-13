package com.guigarage.fx.grid.demo;

import java.net.URL;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.guigarage.fx.grid.GridCell;
import com.guigarage.fx.grid.GridView;

public class JGridFXDemo5 extends Application {

	private GridView<Movie> myGrid;

	private BorderPane root;
	
	public static void main(String[] args) {
		JGridFXDemo4.launch();
	}

	private class Movie {

		// previewUrl in JSON
		private String previewUrl;

		private String previewImageUrl;

	}

	private class MovieGridCell extends GridCell<Movie> {

		private ScaleTransition scaleInTransition;

		private ScaleTransition scaleOutTransition;

		private ImageView previewView;

		public MovieGridCell() {
			previewView = new ImageView();
			previewView.setPreserveRatio(true);
			previewView.fitHeightProperty().bind(MovieGridCell.this.heightProperty());
			previewView.fitWidthProperty().bind(MovieGridCell.this.widthProperty());
			
			previewView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent arg0) {
					if(getItem().previewUrl != null) {
						MediaView mediaView = new MediaView();
						System.out.println(getItem().previewUrl);
						Media myMedia = new Media(getItem().previewUrl);
//						Media myMedia = new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv");
						MediaPlayer mediaPlayer = new MediaPlayer(myMedia);
						mediaView.setMediaPlayer(mediaPlayer);
						mediaView.setStyle("-fx-border-color: black;");
						mediaView.setPreserveRatio(true);
						root.setCenter(mediaView);
						mediaPlayer.play();
					} else {
						System.out.println("Kein Film vorhanden");
					}
				}
			});
			
			getChildren().add(previewView);
			
			itemProperty().addListener(new ChangeListener<Movie>() {

				@Override
				public void changed(ObservableValue<? extends Movie> arg0,
						Movie arg1, Movie arg2) {
					previewView.setImage(new Image(arg2.previewImageUrl, false));
				}
			});

			addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent arg0) {
							toFront();
							if (scaleOutTransition != null) {
								scaleOutTransition.stop();
							}
							scaleInTransition = ScaleTransitionBuilder.create()
									.fromX(MovieGridCell.this.getScaleX())
									.toX(1.3)
									.fromY(MovieGridCell.this.getScaleY())
									.toY(1.3)
									.interpolator(Interpolator.EASE_IN)
									.duration(Duration.millis(200))
									.node(MovieGridCell.this).build();
							scaleInTransition.play();
						}
					});

			addEventHandler(MouseEvent.MOUSE_EXITED,
					new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent arg0) {
							if (scaleInTransition != null) {
								scaleInTransition.stop();
							}
							scaleOutTransition = ScaleTransitionBuilder
									.create()
									.fromX(MovieGridCell.this.getScaleX())
									.toX(1.0)
									.fromY(MovieGridCell.this.getScaleY())
									.toY(1.0)
									.interpolator(Interpolator.EASE_OUT)
									.duration(Duration.millis(120))
									.node(MovieGridCell.this).build();
							scaleOutTransition.play();
						}
					});
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("JGridFX Demo 4");

		final ObservableList<Movie> list = FXCollections
				.<Movie> observableArrayList();
		myGrid = new GridView<>(list);

		myGrid.cellWidthProperty().set(200);
		myGrid.cellHeightProperty().set(200);
		
		myGrid.setCellFactory(new Callback<GridView<Movie>, GridCell<Movie>>() {

			@Override
			public GridCell<Movie> call(GridView<Movie> arg0) {
				return new MovieGridCell();
			}
		});
		root = new BorderPane();
		root.setCenter(myGrid);

		HBox searchBox = new HBox();
		searchBox.getChildren().add(new Label("Search:"));
		final TextField searchField = new TextField();
		searchBox.getChildren().add(searchField);
		searchField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					int limit = 10;
					URL searchUrl = new URL("https://itunes.apple.com/search?term="
							+ searchField.getText().replace(" ", "+")
							+ "&limit=" + limit + "&entity=movie");
					String jsonString = IOUtils.toString(searchUrl);
					System.out.println(jsonString);
					JSONObject jsonObject = new JSONObject(jsonString);
					JSONArray resultArray = jsonObject.getJSONArray("results");
					for(int i = 0; i < resultArray.length(); i++) {
						JSONObject resultObject = resultArray.getJSONObject(i);
						Movie movie = new Movie();
						if(resultObject.has("artworkUrl100")) {
							movie.previewImageUrl = resultObject.getString("artworkUrl100").replace("100x100", "400x400");
						}
						if(resultObject.has("previewUrl")) {
							movie.previewUrl = resultObject.getString("previewUrl");
						}
						list.add(movie);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		root.setTop(searchBox);
		
		root.setBottom(new JGridControl(myGrid));
		Scene scene = new Scene(root, 540, 210);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}