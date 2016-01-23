package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import library.A_RayCode;

public class A_RayGUI extends Application { // TODO sign file
	
	private static final int EDITOR_WIDTH = 480;
	private static final int EDITOR_HEIGHT = 200;
	
	private static final int INPUT_WIDTH = EDITOR_WIDTH;
	private static final int INPUT_HEIGHT = 100;
	private static final int OUTPUT_WIDTH = INPUT_WIDTH;
	private static final int OUTPUT_HEIGHT = INPUT_HEIGHT;
	
	private static final Insets MAIN_PANE_INSETS = new Insets(10);
	private static final int PANE_SPACING = 10;
	
	private static final int WIDTH = 490;
	private static final int HEIGHT = 550;
	private static final Font MONOSPACE_FONT = Font.font("Courier New", FontPosture.REGULAR, 12);

	@Override
	public void start(Stage primaryStage) {
		
		Label inputLabel = new Label("Input");
		
		TextArea input = new TextArea();
		input.setFont(MONOSPACE_FONT);
		setSize(input, INPUT_WIDTH, INPUT_HEIGHT);
		
		Label outputLabel = new Label("Output");
		
		TextArea output = new TextArea();
		output.setEditable(false);
		output.setFont(MONOSPACE_FONT);
		setSize(output, OUTPUT_WIDTH, OUTPUT_HEIGHT);
		
		TextArea codeArea = new TextArea();
		codeArea.setFont(MONOSPACE_FONT);
		setSize(codeArea, EDITOR_WIDTH, EDITOR_HEIGHT);
		
		Button runButton = new Button("Run");
		runButton.setOnAction(e -> {
			try {
				output.setText(new A_RayCode(codeArea.getText(), input.getText()).runAndGetOutput());
			} catch (RuntimeException exception) {
				output.setText("Error");
				exception.printStackTrace();
			}
			
		});
		
		Button clearOutput = new Button("Clear Output");
		clearOutput.setOnAction(e -> {
			output.setText("");
		});
		
		HBox buttonPane = new HBox();
		buttonPane.setSpacing(PANE_SPACING);
		buttonPane.setAlignment(Pos.CENTER_RIGHT);
		buttonPane.getChildren().addAll(clearOutput, runButton);
		
		VBox mainPane = new VBox();
		mainPane.setPadding(MAIN_PANE_INSETS);
		mainPane.setSpacing(PANE_SPACING);
		
		mainPane.getChildren().addAll(codeArea, buttonPane, inputLabel, input, outputLabel, output);
		
		BorderPane content = new BorderPane(mainPane);
		
		Scene scene = new Scene(content, WIDTH, HEIGHT);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private void setSize(Control control, double width, double height) {
		control.setMaxSize(width, height);
		control.setMinSize(width, height);
		control.setPrefSize(width, height);
	}
	
}
