package multiplayer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static javafx.scene.input.KeyCode.*;

public class Main extends Application {
    public static double width = 1270;
    public static double height = 720;
    public static double playerOneYPos = height / 2;
    public static double playerTwoYPos = height / 2;
    public static final double speedConstant = 1.1;
    public int PlayerOneScore = 0;
    public int PlayerTwoScore = 0;
    public static final int GoalKeeper_Height = 120;
    public static final int GoalKeeper_Width = 25;
    public double playerOneXPos = 5.0;
    public double playerTwoXPos = width - GoalKeeper_Width - 5;
    public static double BallYSpeed = 1;
    public static double BallXSpeed = 2;
    public static double MoveSpeed = 10;
    public static double BallXPosition = width / 2;
    public static double BallYPosition = height / 2;
    public static String startText = "Click Enter to start the game";
    public static boolean GameRunning;
    public static boolean PlayOneUP = false;
    public static boolean PlayTwoUP = false;
    public static boolean PlayOneDown = false;
    public static boolean PlayTwoDown = false;
    public boolean playerOneturn = true;
    public boolean playerTwoturn = true;
    public int touches = 0;
    public static boolean PvC = false;
    public static final double BallRadius = 15;
    public Color EntitiesColor = Color.WHITE;
    public Color Background = Color.BLACK;

    public static boolean Multiplayer = false;

    // Multiplayer
    public String ip = "localhost";
    public int port = 22222;
    Thread thread;
    public Socket socket;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;
    public int errors = 0;
    public ServerSocket serverSocket;
    public static boolean connected = false;
    public boolean NoConnectionWithPartner = false;
    public static boolean isplayerOne = false;
    public static boolean isplayerTwo = false;



    public void start(Stage stage) {
        Canvas canvas = new Canvas(width, height);

        /*Gets the Graphical Context från Canvas för att kunna plasera ut de olika vektorerna på Canvasen*/
        GraphicsContext gc = canvas.getGraphicsContext2D();

        /*Skappar en Tidslinje för att */
        Timeline TL = new Timeline(new KeyFrame(Duration.millis(10), e -> { StartGame.DrawGame(this, gc); }));
        StartGame.DrawGame(this, gc);
        TL.setCycleCount(Timeline.INDEFINITE);
        canvas.setFocusTraversable(true);

        controls.start();
        EventHandler<KeyEvent> handler1 = (KeyEvent key) -> {
            if (key.getCode() == W) {
                if(Multiplayer) {
                    if(isplayerOne) {
                        PlayOneUP = true;
                    }
                }else {
                    PlayOneUP = true;
                }
            }
            if (key.getCode() == S) {
                if(Multiplayer) {
                    if(isplayerOne) {
                        PlayOneDown = true;
                    }
                }else {
                    PlayOneDown = true;
                }

            }
            if (key.getCode() == UP) {
                if(Multiplayer) {
                    if(isplayerTwo) {
                        PlayTwoUP = true;
                    }
                }else {
                    PlayTwoUP = true;
                }
            }
            if (key.getCode() == DOWN) {
                if(Multiplayer) {
                    if(isplayerTwo) {
                        PlayTwoDown = true;
                    }
                }else {
                    PlayTwoDown = true;
                }

            }
            if(key.getCode() == ENTER & !isplayerTwo){
                GameRunning = true;
            }
            if(!GameRunning && key.getCode() == DIGIT3) {
                PlayerOneScore = 0;
                PlayerTwoScore = 0;
                playerOneYPos = height / 2;
                playerTwoYPos = height / 2;

                Stage stage1 = new Stage();
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Scene scene = new Scene(grid, 300, 275);
                stage1.setScene(scene);

                Text scenetitle = new Text("Multiplayer");
                scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
                grid.add(scenetitle, 0, 0, 2, 1);

                Label Iplabel = new Label("IP:");
                grid.add(Iplabel, 0, 1);

                TextField IPField = new TextField();
                grid.add(IPField, 1, 1);

                Label portlabel = new Label("Port:");
                grid.add(portlabel, 0, 2);

                TextField portField = new TextField();
                grid.add(portField, 1, 2);

                Button connect = new Button("Connect");

                connect.setOnAction(e-> {
                    if (!IPField.getText().contains("localhost")) {
                        ip = IPField.getText();
                    }
                    System.out.println(ip);
                    try {
                        port = Integer.parseInt(portField.getText());
                    } catch (Exception ex) {

                    }
                    if(!ip.isEmpty()) {
                        if(!(port == 0)) {
                            Multiplayer = true;
                            stage1.hide();
                        }
                    }else {
                        System.out.println("noo");
                    }


                });
                grid.add(connect, 1, 3);
                stage1.show();
            }
            if(!GameRunning && key.getCode() == DIGIT2) {
                PvC = true;
                PlayerOneScore = 0;
                PlayerTwoScore = 0;
            }
            if(!GameRunning && key.getCode() == DIGIT1) {
                PvC = false;
                PlayerOneScore = 0;
                PlayerTwoScore = 0;
            }
        };
        EventHandler<KeyEvent> handler2 = key -> {
            if (key.getCode() == W) {
                PlayOneUP = false;
            }
            if (key.getCode() == S) {
                PlayOneDown = false;
            }
            if (key.getCode() == UP) {
                PlayTwoUP = false;
            }
            if (key.getCode() == DOWN) {
                PlayTwoDown = false;
            }
        };
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, handler1);
        canvas.addEventHandler(KeyEvent.KEY_RELEASED, handler2);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });

        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        scene.setFill(Color.BLACK);
        canvas.setStyle("-fx-effect: dropshadow(gaussian, white, 5, 1.0, 0, 0);");
        stage.show();
        TL.play();
    }



}
