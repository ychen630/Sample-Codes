package util;
/*
 * Toggle Switch
 * For use in setting user privacy reference
 */
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ToggleSwitch extends HBox {
	
	private final Label label = new Label();
	private final Button button = new Button();
	
	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
		
	public ToggleSwitch(boolean status) {
		init();
		if(status) setOn();
		else setOff();
		switchedOn.addListener((a,b,c) -> {
			if (c) setOn();
			else setOff();
		});
	}
	
	private void init() {
		label.setFont(new Font("Arial", 15));
		label.setTextFill(Color.WHITE);		
		getChildren().addAll(label, button);	
		button.setOnAction((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		label.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle();
		bindProperties();
	}
	
	private void setStyle() {
		setMaxHeight(30);
		setMaxWidth(110);
		setMinHeight(30);
		setMinWidth(110);
		label.setAlignment(Pos.CENTER);
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}

	
	private void setOn() {
		label.setText("Show");
		setStyle("-fx-background-color: green;");
		label.toFront();
	}
	
	private void setOff() {
		label.setText("Hide");
		setStyle("-fx-background-color: grey;");
		button.toFront();
	}
	
	public boolean getStatus() {
		if(label.getText().equals("Show")) return true;
		else return false;
	}
}
