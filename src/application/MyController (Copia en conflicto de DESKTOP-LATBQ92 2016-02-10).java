package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class MyController implements Initializable {
	
    @FXML
    private MediaView media_view;

    @FXML
    private Media media_media;
    
    @FXML
    private MediaPlayer media_player;
    
    @FXML
    private Slider media_slider;

    @FXML
    private Button media_button;
    
    @FXML
    private Pane media_pane;
    
    @FXML
    private Text media_text;

    @FXML
    void onSubmitClick(ActionEvent event) {
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		media_slider.setVisible(false);
		media_view.setVisible(false);
		
		media_pane.setStyle("-fx-background-color: #000000");

		media_pane.setPrefWidth(application.Main.stage.getWidth());
		media_pane.setPrefHeight(application.Main.stage.getHeight());

		media_button.setTranslateX(media_pane.getWidth()-100);
		media_button.setTranslateY(media_pane.getHeight()-100);
		
		System.out.println(media_pane.getWidth());
		System.out.println(media_pane.getHeight());
		
		media_slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				media_player.pause();
			}
		});
		media_slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				media_player.seek(Duration.seconds(media_slider.getValue()));
				media_player.play();
			}
		});
		media_slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				media_player.pause();
				media_player.seek(Duration.seconds(media_slider.getValue()));
			}
		});
		
		media_button.setOnMouseClicked((click) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Video de la CAMARA");
			
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Videos (*.mp4)", "*.mp4");
			fileChooser.getExtensionFilters().add(extFilter);

			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
			
					File file = fileChooser.showOpenDialog(media_button.getScene().getWindow());
		             
		    		media_media =  new Media(file.toURI().toString());
		       		media_player = new MediaPlayer(media_media);
		       		media_view.setMediaPlayer(media_player);

		       		int view_media_width = 0;
		       		int view_media_height = 0;
		       		
		       		if (application.Main.stage.getWidth() < 700) {
		       			view_media_width = 640;
		       			view_media_height = 360;
		       		}
		       		else if (application.Main.stage.getWidth() < 1000) {
		       			view_media_width = 960;
		       			view_media_height = 540;
		       		} 
		       		else if (application.Main.stage.getWidth() < 1300) {
		       			view_media_width = 1280;
		       			view_media_height = 720;
		       		} 
		       		else if (application.Main.stage.getWidth() < 1650) {
		       			view_media_width = 1600;
		       			view_media_height = 900;
		       		} 
		       		else {
		       			view_media_width = 1920;
		       			view_media_height = 1080;
		       		}

		       		System.out.println("X: " + view_media_width);
		       		System.out.println("Y: " + view_media_height);
		       		
		       		media_view.setFitWidth(view_media_width);
		       		media_view.setFitHeight(view_media_height);

		       		// ----------------------------------

		    		Double sliderValue = 0.0;

		    		media_player.setOnReady(new Runnable() {
		    			@Override
		    			public void run() {
		    				media_slider.setMin(0.0);
		    				media_slider.setValue(0.0);
		    				media_slider.setMax(media_player.getTotalDuration().toSeconds());

		    				media_slider.setBlockIncrement(2.0);
		    				
		    				media_player.seek(Duration.seconds(sliderValue));
		    				media_slider.setValue(sliderValue);
		    			}
		    		});
		    		
		    		media_player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
		    			@Override
		    			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current) {
		    				media_slider.setValue(current.toSeconds());
		    				media_text.setText(updateValues((int) media_player.getCurrentTime().toSeconds()) + "/" + updateValues((int) media_player.getTotalDuration().toSeconds()));
		    			}
		    			
		    			private String updateValues(int totalSecs) {
		    				int hours = totalSecs / 3600;
		    				int minutes = (totalSecs % 3600) / 60;
		    				int seconds = totalSecs % 60;

		    				String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

		    				return timeString;
		    			}
		    		});			       		
		       		
		    		// ----------------------------------
		    		
		            return null; // nothing to return
		         }
		     });

			media_slider.setVisible(true);
			media_view.setVisible(true);
			
   			media_player.play();
   			
   			application.Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
   				boolean pause = false;

   				@Override
   				public void handle(KeyEvent t) {
					if(t.getCode()==KeyCode.SPACE) {
						if (pause) {
							media_player.play();
							pause = false;
						}
						else {
							media_player.pause();
							pause = true;
						}
					}
					if(t.getCode()==KeyCode.ENTER) {
						if (pause) {
							try {
								String UPLOAD_URL = "http://192.168.88.240:8080/servlet/upload-file";
								int BUFFER_SIZE = 4096;
						    
								media_view.setFitWidth(1920);
								media_view.setFitHeight(1080);
								
								BufferedImage output = new BufferedImage(700, 500, BufferedImage.TYPE_INT_ARGB);
								WritableImage snapshot = media_view.snapshot(new SnapshotParameters(), null);
								BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, output);

								media_view.setFitWidth(700);
								media_view.setFitHeight(500);
								
								ByteArrayOutputStream os = new ByteArrayOutputStream();
								ImageIO.write(image, "png", os);
								InputStream is = new ByteArrayInputStream(os.toByteArray());
								
								// creates a HTTP connection
								URL url;
								url = new URL(UPLOAD_URL);
								
						        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
						        httpConn.setUseCaches(false);
						        httpConn.setDoOutput(true);
						        httpConn.setRequestMethod("POST");
						        // sets file name as a HTTP header
						        httpConn.setRequestProperty("fileName", "testname.png");
						 
						        // opens output stream of the HTTP connection for writing data
						        OutputStream outputStream = httpConn.getOutputStream();
						 
						        // Opens input stream of the file for reading data

						        // FileInputStream inputStream = new FileInputStream(new File(""));
						        
						        byte[] buffer = new byte[BUFFER_SIZE];
						        int bytesRead = -1;
						 
						        // System.out.println("Start writing data...");
						        
						        while ((bytesRead = is.read(buffer)) != -1) {
						            outputStream.write(buffer, 0, bytesRead);
						        }
						 
						        System.out.println("Data was written.");
						        outputStream.close();
						        is.close();
						 
						        // always check HTTP response code from server
						        int responseCode = httpConn.getResponseCode();
						        if (responseCode == HttpURLConnection.HTTP_OK) {
						            // reads server's response
						            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
						            String response = reader.readLine();
						            System.out.println("Server's response: " + response);
						        } else {
						            System.out.println("Server returned non-OK code: " + responseCode);
						        }
							}
							catch (IOException e) { e.printStackTrace(); }
							
							media_player.play();
							pause = false;
						}
				    }
   				}
   			});
		});
	}
}
