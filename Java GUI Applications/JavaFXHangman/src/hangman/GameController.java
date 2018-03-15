package hangman;

/*
 * COMP585 Spring 2018
 * Project 3
 *
 * Game Controller
 * take inputs, set labels, draw gallows and hanging man
 * 
 * Yixin Chen
 * Mar 12, 2018
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class GameController {
	private final ExecutorService executorService;
	private final Game game;
	
	//gallows and hanging man
	private Line gallows1, gallows2, gallows3, gallows4, rope;
    private Circle head, headHang;
    private Line leftArm, leftArmHang, rightArm, rightArmHang, body, leftLeg, rightLeg;
	private final int WIDTH = 500, HEIGHT = 400;
	PathTransition path;
	List<Integer> drawCount= new ArrayList<Integer>();
	
	public GameController(Game game) {
		this.game = game;
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
	}

	@FXML
	private Pane board; //Hangman board
	@FXML
	private Label targetWord; //display target word
	@FXML
	private Label missedLetters; //display missed letters
	@FXML
	private Label statusLabel; //show game status
	@FXML
	private Label enterALetterLabel;
	@FXML
	private Label movesLeft; //show remain moves
	@FXML
	private TextField textField ; //input a letter

    public void initialize(){
		System.out.println("in initialize");
		
		drawGallows();
		addTextBoxListener();
		setUpStatusLabelBindings();
		
		//add key listener
        textField.setOnKeyPressed(e-> {
			sendKeyCode(e.getCode());
		});
	}
    
    //input letters and set labels
	private void addTextBoxListener() {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				//check if the input is a letter
				if(newValue.length() > 0 && Character.isLetter(newValue.charAt(newValue.length()-1))) {
					System.out.print("input: " + newValue);					
					game.makeMove(newValue.substring(newValue.length()-1));
					//if lose (no move left), display the target word
					if(game.getRemainMoves() == 0 ) {
						targetWord.textProperty().bind(Bindings.format("%s", "Target: " + game.getAnswer().toUpperCase() + ". Your answer: " + game.getCurrentAnswer().toUpperCase()));
					}
					//else, display the current answer
					else {
						targetWord.textProperty().bind(Bindings.format("%s", "Target: " + game.getCurrentAnswer().toUpperCase()));
					}
					
					//display missed letters and moves left
					missedLetters.textProperty().bind(Bindings.format("%s", "Missed Letters: " + game.getMissedLetters().toUpperCase()));
					movesLeft.textProperty().bind(Bindings.format("%s", "You have " + game.getRemainMoves() + " moves left."));
					
					//draw a part of hanging man according number of moves
					drawHangman(game.getMove());
				}
				//if the input is not a letter, show error alert
				if(newValue.length() > 0 && !Character.isLetter(newValue.charAt(newValue.length()-1))) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Input Error");
					alert.setHeaderText(null);
					alert.setContentText("Please Enter A Letter!");
					alert.showAndWait();
					textField.clear();
				}
			}
		});
	}
	
	//display initial game status
	private void setUpStatusLabelBindings() {
		System.out.println("in setUpStatusLabelBindings");
		statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
		enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));
		targetWord.textProperty().bind(Bindings.format("%s", "Target: " + game.getCurrentAnswer()));
		missedLetters.textProperty().bind(Bindings.format("%s", "Missed Letters: "));
		movesLeft.textProperty().bind(Bindings.format("%s", "You have " + game.getRemainMoves() + " moves left."));
		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
	}

	//build gallows
	private void drawGallows() {
		 
		 //bottom line
         gallows1 = new Line(WIDTH*0.05, HEIGHT*0.9, WIDTH*0.35, HEIGHT*0.9);
         gallows1.setStrokeWidth(5);
         gallows1.setStrokeLineCap(StrokeLineCap.ROUND);

         // Upward line
         gallows2 = new Line((gallows1.getEndX() + gallows1.getStartX())/2, gallows1.getStartY(), (gallows1.getEndX() + gallows1.getStartX())/2, HEIGHT * 0.05);
         gallows2.setStrokeWidth(5);
         gallows2.setStrokeLineCap(StrokeLineCap.BUTT);
         
         // Rightward line
         gallows3 = new Line(gallows2.getEndX(), gallows2.getEndY(), WIDTH * 0.6, gallows2.getEndY());
         gallows3.setStrokeWidth(5);
         gallows3.setStrokeLineCap(StrokeLineCap.ROUND);
         
         //Up right ward line
         gallows4 = new Line(gallows2.getEndX(), (gallows2.getStartY() - gallows2.getEndY()) / 4, gallows3.getStartX() + (gallows3.getEndX() - gallows3.getStartX())/3, gallows2.getEndY());
         gallows4.setStrokeWidth(5);
         gallows4.setStrokeLineCap(StrokeLineCap.BUTT);
         
         // Downward line
         rope = new Line(gallows3.getEndX(), gallows3.getEndY(), gallows3.getEndX(), gallows3.getEndY() + HEIGHT * 0.15 +  WIDTH * 0.06);
         rope.setStrokeWidth(2);
         rope.setStrokeLineCap(StrokeLineCap.ROUND);
         
         board.getChildren().addAll(gallows1, gallows2, gallows3, gallows4, rope);
	}
	
	//draw parts of Hanging man based on number of wrong moves
    private void drawHangman(int moves) {
    	if(!drawCount.contains(moves)) {
    		drawCount.add(moves);
            switch (moves) {
	            case 1: drawHead(); break;
	            case 2: drawBody(); break;
	            case 3: drawLeftArm(); break;
	            case 4: drawRightArm(); break;
	            case 5: drawLeftLeg(); break;
	            case 6: drawRightLeg(); break;
	            case 7: animateHang(); break;
            }
    	}
    }
    
    private void animateHang() {
    	//change positions of head and arms to hang positions
    	head.setVisible(false);
    	headHang.setVisible(true);
        rightArm.setVisible(false);
        leftArm.setVisible(false);
        rightArmHang.setVisible(true);
        leftArmHang.setVisible(true);
        
        //set translate X Property
        head.translateXProperty().addListener((observable, oldValue, newValue) -> {
        	headHang.setTranslateX(newValue.doubleValue());
            body.setTranslateX(newValue.doubleValue());
            leftArm.setTranslateX(newValue.doubleValue());
            rightArm.setTranslateX(newValue.doubleValue());
            leftArmHang.setTranslateX(newValue.doubleValue());
            rightArmHang.setTranslateX(newValue.doubleValue());
            leftLeg.setTranslateX(newValue.doubleValue());
            rightLeg.setTranslateX(newValue.doubleValue());
        });

        //set translate Y Property
        head.translateYProperty().addListener((observable, oldValue, newValue) -> {
        	headHang.setTranslateY(newValue.doubleValue());
            body.setTranslateY(newValue.doubleValue());
            leftArm.setTranslateY(newValue.doubleValue());
            rightArm.setTranslateY(newValue.doubleValue());
            leftArmHang.setTranslateY(newValue.doubleValue());
            rightArmHang.setTranslateY(newValue.doubleValue());
            leftLeg.setTranslateY(newValue.doubleValue());
            rightLeg.setTranslateY(newValue.doubleValue());
        });
        
        //set move path
        Line line = new Line(rope.getEndX(), rope.getEndY() -2 * WIDTH * 0.06, rope.getEndX(), rope.getEndY() - WIDTH * 0.06);
/*      line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        board.getChildren().add(line);
        line.setStrokeWidth(2);*/
        path = new PathTransition(Duration.seconds(2), line, head);
        path.setCycleCount(Transition.INDEFINITE);
        path.setAutoReverse(true);
        path.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        path.play();
    }
	
	private void drawHead() {
        double radius = WIDTH * 0.06;
        //original head
        head = new Circle(rope.getEndX(), rope.getEndY() - radius, radius, Color.WHITE);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(3);
        board.getChildren().add(head);
        
        //head after losing a game
        headHang = new Circle(rope.getEndX() + radius, rope.getEndY(), radius, Color.WHITE);
        headHang.setStroke(Color.BLACK);
        headHang.setStrokeWidth(3);
        headHang.setVisible(false); //default, not visible
        board.getChildren().add(headHang);
     }

     private void drawBody() {
         double startY = head.getCenterY() + head.getRadius();
         double x = head.getCenterX();
         body = new Line(x, startY, x, startY + HEIGHT * 0.2);
         body.setStrokeWidth(3);
         body.setStrokeLineCap(StrokeLineCap.ROUND);
         board.getChildren().add(body);
     }

     private void drawLeftArm() {
    	 //original left arm
         leftArm = createArm(-1, -1, 225);
         leftArm.setStrokeWidth(3);
         leftArm.setStrokeLineCap(StrokeLineCap.ROUND);        
         board.getChildren().add(leftArm);
         
         //left arm after losing a game
         leftArmHang = createArm(-1, 1, 225);
         leftArmHang.setStrokeWidth(3);
         leftArmHang.setStrokeLineCap(StrokeLineCap.ROUND);
         leftArmHang.setVisible(false); //default, not visible
         board.getChildren().add(leftArmHang);
     }

     private void drawRightArm() {
    	 //original right arm
         rightArm = createArm(1, -1, 315);
         rightArm.setStrokeWidth(3);
         rightArm.setStrokeLineCap(StrokeLineCap.ROUND);
         board.getChildren().add(rightArm);
         
         //right arm after losing a game
         rightArmHang = createArm(1, 1, 315);
         rightArmHang.setStrokeWidth(3);
         rightArmHang.setStrokeLineCap(StrokeLineCap.ROUND);
         rightArmHang.setVisible(false); //default, not visible
         board.getChildren().add(rightArmHang);
     }

     private void drawLeftLeg() {
         leftLeg = createLeg(-1);
         leftLeg.setStrokeWidth(3);
         leftLeg.setStrokeLineCap(StrokeLineCap.ROUND);
         board.getChildren().add(leftLeg);
     }

     private void drawRightLeg() {
         rightLeg = createLeg(1);
         rightLeg.setStrokeWidth(3);
         rightLeg.setStrokeLineCap(StrokeLineCap.ROUND);
         board.getChildren().add(rightLeg);
     }

     private Line createLeg(int dX) {
         double x = body.getEndX();
         double y = body.getEndY();
         return new Line(x, y, x + head.getRadius() * dX * 1.5, y + WIDTH * 0.15);
     }
     
     private Line createArm(int dX, int dY, double angle) {
         double x = body.getStartX();
         double y = body.getStartY();
         return new Line(x, y + WIDTH * 0.05, x + head.getRadius() * dX * 2, y + WIDTH * 0.05 + WIDTH * 0.06 * dY);
     }
		
	@FXML
	//reset everything and restart game
	private void newHangman(){
		if(game.getMove() == 7) {
			path.stop();
		}
		game.reset();
		board.getChildren().clear();
		drawCount= new ArrayList<Integer>();
		textField.clear();
		drawGallows();
		setUpStatusLabelBindings();	
	}

	@FXML
	//quit game
	private void quit() {
		board.getScene().getWindow().hide();
	}
	
    //add key listener, ENTER - restart game after finish, Other keys - clear textField 
	public void sendKeyCode(KeyCode key){
        if (key == KeyCode.ENTER && !game.getPlayingStatus()) {
           newHangman();
        }
        else {
        	textField.clear();
        }
    }

}