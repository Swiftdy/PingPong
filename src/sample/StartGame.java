package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class StartGame {
    static void IntilizeGame(Main main, GraphicsContext GameBoard) {
        GameBoard.setFill(Color.BLACK);
        GameBoard.fillRect(0, 0, Main.width, Main.height);
        GameBoard.setFill(Color.WHITE);
        GameBoard.setFont(Font.font(20));
        InitGame(main, GameBoard);
        if (main.playerOneturn && main.BallYPosition >= main.playerOneYPos && main.BallYPosition <= main.playerOneYPos + Main.GoalKeeper_Height && main.BallXPosition - Main.BallRadius <= main.playerOneXPos + Main.GoalKeeper_Width/2) {
            main.playerTwoturn = true;
            main.playerOneturn = false;
            main.touches++;
            main.BallXSpeed -= 0.9;
            main.BallYSpeed = -5 + (int) (Math.random() * ((5 - (-5)) + 1));
            main.BallXSpeed *= -1;
        }
        if (main.playerTwoturn && main.BallYPosition >= main.playerTwoYPos && main.BallYPosition <= main.playerTwoYPos +  Main.GoalKeeper_Height && main.BallXPosition + Main.BallRadius >= main.playerTwoXPos) {
            main.playerTwoturn = false;
            main.playerOneturn = true;
            main.touches++;
            main.BallXSpeed += 0.9;
            main.BallYSpeed = -5 + (int) (Math.random() * ((5 - (-5)) + 1));
            main.BallXSpeed *= -1;
        }

        if(main.BallYPosition > Main.height || main.BallYPosition < 0) {
            main.BallYSpeed *=-1;
        }

        if(main.BallXPosition < main.playerOneXPos - Main.GoalKeeper_Width) {
            main.PlayerTwoScore += 1;
            main.touches = 0;
            Main.startText = "Player Two Won!\nClick enter to start next round!";
            main.playerOneturn = true;
            main.playerTwoturn = true;
            main.GameRunning = false;
        }
        if(main.BallXPosition > main.playerTwoXPos + Main.GoalKeeper_Width) {
            main.PlayerOneScore++;
            main.touches = 0;
            Main.startText = "Player One Won!\nClick enter to start next round!";
            main.playerOneturn = true;
            main.playerTwoturn = true;
            main.GameRunning = false;
        }
        if(main.touches > 3 & main.touches < 6) {
            main.EntitiesColor = Color.BLUE;
        } else if(main.touches > 5) {
            main.EntitiesColor = Color.GRAY;
        }else{
            main.EntitiesColor = Color.WHITE;
        }



        if(main.PvC && main.playerTwoturn) {
            if(main.BallYPosition >= main.playerTwoYPos + Main.GoalKeeper_Height/2) {
                main.playerTwoYPos += main.MoveSpeed - 3 ;
            }
            if (main.BallYPosition <= main.playerTwoYPos + Main.GoalKeeper_Height/2 ) {
                main.playerTwoYPos -= main.MoveSpeed - 3;
            }
        }
        /* Skappar Goal keepers! */
        GameBoard.setFill(Color.GHOSTWHITE);
        GameBoard.fillRect(main.playerTwoXPos, main.playerTwoYPos, Main.GoalKeeper_Width, Main.GoalKeeper_Height);
        GameBoard.setFill(Color.BLUE);
        GameBoard.fillRect(main.playerOneXPos, main.playerOneYPos, Main.GoalKeeper_Width, Main.GoalKeeper_Height);
        GameBoard.setFill(Color.WHITE);
    }

    private static void InitGame(Main main, GraphicsContext GameBoard) {
        if(main.GameRunning) {

            main.BallXPosition = main.BallXPosition + main.BallXSpeed;
            main.BallYPosition = main.BallYPosition + main.BallYSpeed;
            GameBoard.setStroke(main.Background);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.setStroke(Color.WHITE);
            GameBoard.setTextAlign(TextAlignment.RIGHT);
            GameBoard.strokeText(String.valueOf(main.PlayerOneScore) + "        ",  Main.width / 2, 40);
            GameBoard.setTextAlign(TextAlignment.LEFT);
            GameBoard.strokeText("        " + String.valueOf(main.PlayerTwoScore),  Main.width / 2, 40);


            GameBoard.setStroke(Color.WHITE);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.fillText(String.valueOf(main.PlayerTwoScore), Main.width/0.75, Main.height /4);
            GameBoard.beginPath();
            GameBoard.moveTo(Main.width/2, 0);
            GameBoard.lineTo(Main.width/2, Main.height);
            GameBoard.setFill(Color.WHITE);
            GameBoard.stroke();
            GameBoard.setFill(main.EntitiesColor);
            GameBoard.fillRect(main.BallXPosition, main.BallYPosition, Main.BallRadius, Main.BallRadius);
        } else {
            GameBoard.setStroke(Color.RED);
            GameBoard.setTextAlign(TextAlignment.CENTER);
            GameBoard.fillText(Main.startText, Main.width / 2, Main.height / 2);
            GameBoard.setStroke(Color.RED);
            if(!main.PvC) {
                GameBoard.setStroke(Color.WHITE);
                GameBoard.setTextAlign(TextAlignment.RIGHT);
                GameBoard.strokeText("Press 1 to enable PvP.    ",  Main.width / 2, Main.height / 2 + 50);
                GameBoard.setStroke(Color.RED);
                GameBoard.setTextAlign(TextAlignment.LEFT);
                GameBoard.strokeText("   Press 2 to enable PvC.",  Main.width / 2, Main.height / 2 + 50);
            }else {
                GameBoard.setStroke(Color.RED);
                GameBoard.setTextAlign(TextAlignment.RIGHT);
                GameBoard.strokeText("Press 1 to enable PvP.    ",  Main.width / 2, Main.height / 2 + 50);
                GameBoard.setStroke(Color.WHITE);
                GameBoard.setTextAlign(TextAlignment.LEFT);
                GameBoard.strokeText("   Press 2 to enable PvC.",  Main.width / 2, Main.height / 2 + 50);
            }
            main.BallXPosition = Main.width / 2;
            main.BallYPosition = Main.height / 2;
            main.BallXSpeed = new Random().nextInt(2-1) + 1;
            main.BallYSpeed = new Random().nextInt(2-1) + 1;
        }
    }
}
