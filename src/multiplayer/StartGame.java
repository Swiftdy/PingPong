package multiplayer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartGame {
    static void DrawGame(Main main, GraphicsContext GameBoard) {

        //System.out.println("Multiplayer: " + main.Multiplayer + ", Connected: " + main.connected + ", PlayerOnePosition: " + Main.playerOneYPos + ", Player 1: " + main.isplayerOne + ", Player 2:" + main.isplayerTwo);

        if(main.Multiplayer & !main.GameRunning & !main.connected) {
            if (!misc.connect(main) & !main.GameRunning) misc.initializeServer(main);
            System.out.println("Multiplayer");
            if(!main.connected) {
                System.out.println("WAITING");
                misc.listenForServerRequest(main);
            }else {
                System.out.println("RUNNING!");
            }
        }

        if(!main.connected & main.Multiplayer) {
            misc.listenForServerRequest(main);
        }

        if(main.errors >= 10) {
            main.NoConnectionWithPartner = true;
        }

        GameBoard.setFill(Color.BLACK);
        GameBoard.fillRect(0, 0, Main.width, Main.height);
        GameBoard.setFill(Color.WHITE);
        GameBoard.setFont(Font.font(20));
        misc.InitGame(main, GameBoard);
        if (main.playerOneturn && main.BallYPosition >= Main.playerOneYPos && main.BallYPosition <= Main.playerOneYPos + Main.GoalKeeper_Height && main.BallXPosition - Main.BallRadius <= main.playerOneXPos + Main.GoalKeeper_Width / 2) {
            main.playerTwoturn = true;
            main.playerOneturn = false;
            main.touches++;
            if ((Main.playerOneYPos + Main.GoalKeeper_Height / 2) >= main.BallYPosition & !main.isplayerTwo) {
                System.out.println("Player 1: " + (Main.playerOneYPos + Main.GoalKeeper_Height / 2) + " Ball Y: " + main.BallYPosition);
                main.BallYSpeed = -5 + (int) (Math.random() * ((0 - (-3)) + 1));
                System.out.println(main.BallYPosition);
            } else if ((Main.playerOneYPos + Main.GoalKeeper_Height / 2) <= main.BallYPosition & !main.isplayerTwo) {
                System.out.println("Player 1: " + (Main.playerOneYPos + Main.GoalKeeper_Height / 2) + " Ball Y: " + main.BallYPosition);
                main.BallYSpeed = 5 + (int) (Math.random() * (3 - (0) + 1));
                System.out.println(main.BallYPosition);
            }
            main.BallXSpeed -= 0.9;
            main.BallXSpeed *= -1;
        }
        if (main.playerTwoturn && main.BallYPosition >= Main.playerTwoYPos && main.BallYPosition <= Main.playerTwoYPos + Main.GoalKeeper_Height && main.BallXPosition + Main.BallRadius >= main.playerTwoXPos) {
            main.playerTwoturn = false;
            main.playerOneturn = true;
            main.touches++;
            if ((Main.playerTwoYPos + Main.GoalKeeper_Height / 2) >= main.BallYPosition & !main.isplayerTwo) {
                System.out.println("Player 2: " + (Main.playerTwoYPos + Main.GoalKeeper_Height / 2) + " Ball Y: " + main.BallYPosition);
                main.BallYSpeed = -5 + (int) (Math.random() * ((0 - (-3)) + 1));
                System.out.println(main.BallYPosition);
            } else if ((Main.playerTwoYPos + Main.GoalKeeper_Height / 2) <= main.BallYPosition & !main.isplayerTwo) {
                System.out.println("Player 2: " + (Main.playerTwoYPos + Main.GoalKeeper_Height / 2) + " Ball Y: " + main.BallYPosition);
                main.BallYSpeed = 5 + (int) (Math.random() * (3 - (0) + 1));
                System.out.println(main.BallYPosition);
            }
            main.BallXSpeed += 0.9;
            main.BallXSpeed *= -1;
        }

        if(main.BallYPosition > Main.height || main.BallYPosition < 0) {
            main.BallYSpeed *=-1;
        }

        if(main.BallXPosition < main.playerOneXPos - Main.GoalKeeper_Width) {
            main.PlayerTwoScore += 1;
            main.touches = 0;
            if (main.Multiplayer & main.isplayerOne) {
                Main.startText = "Player Two Won!\nClick enter to start next round!";
            } else if (main.Multiplayer & main.isplayerTwo) {
                Main.startText = "Player Two Won!\nWaiting on game to start!";
            } else {
                Main.startText = "Player Two Won!\nClick enter to start next round!";
            }
            main.playerOneturn = true;
            main.playerTwoturn = true;
            main.GameRunning = false;
        }
        if(main.BallXPosition > main.playerTwoXPos + Main.GoalKeeper_Width) {
            main.PlayerOneScore++;
            main.touches = 0;
            if (main.Multiplayer & main.isplayerOne) {
                Main.startText = "Player One Won!\nClick enter to start next round!";
            } else if (main.Multiplayer & main.isplayerTwo) {
                Main.startText = "Player One Won!\nWaiting on game to start!";
            } else {
                Main.startText = "Player One Won!\nClick enter to start next round!";
            }
            main.playerOneturn = true;
            main.playerTwoturn = true;
            main.GameRunning = false;
        }
        if(main.PvC && main.playerTwoturn) {
            if (main.BallYPosition >= Main.playerTwoYPos + Main.GoalKeeper_Height / 2) {
                Main.playerTwoYPos += main.MoveSpeed - 3;
            }
            if (main.BallYPosition <= Main.playerTwoYPos + Main.GoalKeeper_Height / 2) {
                Main.playerTwoYPos -= main.MoveSpeed - 3;
            }
        }

        /* Skappar Goal keepers! */
        GameBoard.setFill(Color.GHOSTWHITE);
        GameBoard.fillRect(main.playerTwoXPos, Main.playerTwoYPos, Main.GoalKeeper_Width, Main.GoalKeeper_Height);
        GameBoard.setFill(Color.BLUE);
        GameBoard.fillRect(main.playerOneXPos, Main.playerOneYPos, Main.GoalKeeper_Width, Main.GoalKeeper_Height);
        GameBoard.setFill(Color.WHITE);
    }

}
