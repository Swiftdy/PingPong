package multiplayer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static javafx.scene.input.KeyCode.*;

public class Main extends Application {
    public int PlayerOneScore = 0;
    public int PlayerTwoScore = 0;
    public static final int GoalKeeper_Height = 120;
    public static final int GoalKeeper_Width = 25;
    public static double width = 1270;
    public static double height = 720;
    public static double playerOneYPos = height / 2;
    public static double playerTwoYPos = height / 2;
    public static final double speedConstant = 1.1;
    public double playerOneXPos = 5.0;
    public double playerTwoXPos = width - GoalKeeper_Width - 5;
    public double BallYSpeed = 1;
    public double BallXSpeed = 2;
    public double MoveSpeed = 10;
    public double BallXPosition = width / 2;
    public double BallYPosition = height / 2;
    public static String startText = "Click Enter to start the game";
    public boolean GameRunning;
    public boolean PlayOneUP = false;
    public boolean PlayTwoUP = false;
    public boolean PlayOneDown = false;
    public boolean PlayTwoDown = false;
    public boolean playerOneturn = true;
    public boolean playerTwoturn = true;
    public int touches = 0;
    public boolean PvC = false;
    public static final double BallRadius = 15;
    public Color EntitiesColor = Color.WHITE;
    public Color Background = Color.BLACK;

    public boolean Multiplayer = false;

    // Multiplayer
    public String ip = "83.227.81.158";
    public int port = 22222;
    public Thread thread;

    public Socket socket;
    public ObjectOutputStream  output;
    public ObjectInputStream  input;
    public int errors = 0;
    public ServerSocket serverSocket;
    public boolean connected = false;
    public boolean NoConnectionWithPartner = false;
    public boolean isplayerOne = false;
    public boolean isplayerTwo = false;



    public void start(Stage stage) {
        Canvas canvas = new Canvas(width, height);

        /*Gets the Graphical Context från Canvas för att kunna plasera ut de olika vektorerna på Canvasen*/
        GraphicsContext gc = canvas.getGraphicsContext2D();

        /*Skappar en Tidslinje för att */
        Timeline TL = new Timeline(new KeyFrame(Duration.millis(10), e -> { StartGame.DrawGame(this, gc); }));
        StartGame.DrawGame(this, gc);
        TL.setCycleCount(Timeline.INDEFINITE);
        canvas.setFocusTraversable(true);
        new Thread(() -> {
            while(true) {
                if(Multiplayer & connected) {
                    if(isplayerOne) {
                        Object[] location = {playerOneYPos, BallYPosition, BallXPosition, BallXSpeed, BallYSpeed, GameRunning};
                        try {
                            output.writeObject(location);
                            output.flush();
                        } catch (SocketException e) {
                            Multiplayer = false;
                        } catch (IOException e) {

                        }
                        try {
                            playerTwoYPos = input.readDouble();
                        } catch (IOException e) {

                        }
                    }
                    if(isplayerTwo) {
                        try {
                            Object[] location = (Object[]) input.readObject();
                            playerOneYPos = (double) location[0];
                            BallYPosition = (double) location[1];
                            BallXPosition = (double) location[2];
                            BallXSpeed = (double) location[3];
                            BallYSpeed = (double) location[4];
                            GameRunning = (boolean) location[5];
                        } catch (IOException e) {

                        } catch (ClassNotFoundException e) {

                        }

                        Double player2 = playerTwoYPos;
                        try {
                            output.writeDouble(player2);
                            output.flush();
                        }catch (SocketException e) {
                            Multiplayer = false;
                        }  catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                try {
                    thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while(true) {
                if (PlayOneUP && GameRunning) {
                    if (playerOneYPos > 0) {
                        playerOneYPos = playerOneYPos - MoveSpeed;
                    }
                }
                if (PlayOneDown && GameRunning) {
                    if (playerOneYPos + GoalKeeper_Height < height) {
                        playerOneYPos = playerOneYPos + MoveSpeed;
                    }
                }
                if(!PvC) {
                    if (PlayTwoUP && GameRunning) {
                        if (playerTwoYPos > 0) {
                            playerTwoYPos = playerTwoYPos - MoveSpeed;
                        }
                    }
                    if (PlayTwoDown && GameRunning) {
                        if (playerTwoYPos + GoalKeeper_Height < height) {
                            playerTwoYPos = playerTwoYPos + MoveSpeed;
                        }
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        EventHandler<KeyEvent> handler1 = key -> {
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
                ip = JOptionPane.showInputDialog("IP");
                Multiplayer = true;
                playerOneYPos = height / 2;
                playerTwoYPos = height / 2;


            }
            if(!GameRunning && key.getCode() == DIGIT2) {
                PvC = true;
            }
            if(!GameRunning && key.getCode() == DIGIT1) {
                PvC = false;
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
