package multiplayer;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

class misc {
    static void listenForServerRequest(Main main) {
        Socket socket = null;
        try {
            socket = main.serverSocket.accept();
            socket.setTcpNoDelay(true);
            main.output = new ObjectOutputStream(socket.getOutputStream());
            main.input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client have connected");
            main.connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean connect(Main main) {
        try {
            main.socket = new Socket(main.ip, main.port);
            main.socket.setTcpNoDelay(true);
            main.output = new ObjectOutputStream(main.socket.getOutputStream());
            main.input = new ObjectInputStream(main.socket.getInputStream());
            main.connected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Creating server.");
            main.isplayerOne = true;
            return false;
        } catch (IOException e) {
            System.out.println("Creating server. Error2");
            main.isplayerOne = true;
            return false;
        }
        System.out.println("Connected");
        main.isplayerTwo = true;

        return true;
    }

    static void initializeServer(Main main) {
        try {
            main.serverSocket = new ServerSocket(main.port, 8, InetAddress.getByName(main.ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void InitGame(Main main, GraphicsContext GameBoard) {
        if (main.GameRunning) {

            main.BallXPosition = main.BallXPosition + main.BallXSpeed;
            main.BallYPosition = main.BallYPosition + main.BallYSpeed;
            GameBoard.setStroke(main.Background);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.setStroke(Color.WHITE);
            GameBoard.setTextAlign(TextAlignment.RIGHT);
            GameBoard.strokeText(String.valueOf(main.PlayerOneScore) + "        ", Main.width / 2, 40);
            GameBoard.setTextAlign(TextAlignment.LEFT);
            GameBoard.strokeText("        " + String.valueOf(main.PlayerTwoScore), Main.width / 2, 40);
            if (main.Multiplayer) {
                GameBoard.strokeText("Multiplayer: " + main.Multiplayer + ", Connected: " + main.connected + ", PlayerOnePosition: " + Main.playerOneYPos + ", Player 1: " + main.isplayerOne + ", Player 2:" + main.isplayerTwo, 10, 60);
            }


            GameBoard.setStroke(Color.WHITE);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.fillText(String.valueOf(main.PlayerTwoScore), Main.width / 0.75, Main.height / 4);
            GameBoard.beginPath();
            GameBoard.moveTo(Main.width / 2, 0);
            GameBoard.lineTo(Main.width / 2, Main.height);
            GameBoard.setFill(Color.WHITE);
            GameBoard.stroke();
            GameBoard.setFill(main.EntitiesColor);
            GameBoard.fillRect(main.BallXPosition, main.BallYPosition, Main.BallRadius, Main.BallRadius);
        } else {
            GameBoard.setStroke(Color.RED);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.fillText(Main.startText, Main.width / 2, Main.height / 2);
            GameBoard.setStroke(Color.RED);
            if (!main.Multiplayer) {
                if (!main.PvC) {
                    GameBoard.setStroke(Color.WHITE);
                    GameBoard.setTextAlign(TextAlignment.RIGHT);
                    GameBoard.strokeText("Press 1 to enable PvP.    ", Main.width / 2, Main.height / 2 + 50);
                    GameBoard.setStroke(Color.RED);
                    GameBoard.setTextAlign(TextAlignment.LEFT);
                    GameBoard.strokeText("   Press 2 to enable PvC.", Main.width / 2, Main.height / 2 + 50);
                } else {
                    GameBoard.setStroke(Color.RED);
                    GameBoard.setTextAlign(TextAlignment.RIGHT);
                    GameBoard.strokeText("Press 1 to enable PvP.    ", Main.width / 2, Main.height / 2 + 50);
                    GameBoard.setStroke(Color.WHITE);
                    GameBoard.setTextAlign(TextAlignment.LEFT);
                    GameBoard.strokeText("   Press 2 to enable PvC.", Main.width / 2, Main.height / 2 + 50);
                }
            }
            main.BallXPosition = Main.width / 2;
            main.BallYPosition = Main.height / 2;
            main.BallXSpeed = new Random().nextInt(2 - 1) + 1;
            main.BallYSpeed = new Random().nextInt(2 - 1) + 1;
        }
    }
}
