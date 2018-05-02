package multiplayer;

import java.io.IOException;
import java.net.SocketException;

import static multiplayer.Main.*;

public class controls {
    public static void start() {
        new Thread(() -> {
            while (true) {
                if (Main.PlayOneUP && Main.GameRunning) {
                    if (playerOneYPos > 0) {
                        playerOneYPos = playerOneYPos - MoveSpeed;
                    }
                }
                if (Main.PlayOneDown && Main.GameRunning) {
                    if (playerOneYPos + GoalKeeper_Height < Main.height) {
                        playerOneYPos = playerOneYPos + Main.MoveSpeed;
                    }
                }
                if (!Main.PvC) {
                    if (Main.PlayTwoUP && Main.GameRunning) {
                        if (playerTwoYPos > 0) {
                            playerTwoYPos = playerTwoYPos - Main.MoveSpeed;
                        }
                    }
                    if (PlayTwoDown && Main.GameRunning) {
                        if (playerTwoYPos + GoalKeeper_Height < height) {
                            playerTwoYPos = playerTwoYPos + Main.MoveSpeed;
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
        new Thread(() -> {
            while (true) {
                if (Main.Multiplayer & Main.connected) {
                    if (Main.isplayerOne) {
                        Object[] location = {playerOneYPos, Main.BallYPosition, Main.BallXPosition, Main.BallXSpeed, Main.BallYSpeed, GameRunning};
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
                    if (isplayerTwo) {
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
                        } catch (SocketException e) {
                            Multiplayer = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
