package com.nikhiltyagi.nikhil.voicecommands;

/**
 * Created by nikhil on 12/3/16.
 */
public class GameClass {
    private int board[][];
    private int playerTurn;

    public GameClass(){
        board = new int[][]{
            {0,0,0},
            {0,0,0},
            {0,0,0}
        };
        playerTurn = 1;
    }

    public boolean playerMove(int x,int y){
        if(board[x][y]==0){
            board[x][y]=playerTurn;
            playerTurn = (playerTurn == 1) ? 2 : 1;
            return true;
        }
        return false;
    }

    public int returnGameStatus(){
        boolean draw = true;
        for(int i=0;i<3;i++)
            for (int j=0;j<3;j++)
                if(board[i][j]==0)
                    draw = false;

        if(draw) return -1;

        for(int i=0;i<3;i++){
            if(board[i][0]==board[i][1]&& board[i][0]==board[i][2]&&board[i][0]!=0)
                return board[i][0];
            if(board[0][i]==board[1][i]&& board[0][i]==board[2][i]&&board[0][i]!=0)
                return board[0][i];
        }
        if(board[0][0]==board[1][1]&& board[0][0]==board[2][2]&&board[0][0]!=0)
            return board[0][0];
        if(board[0][2]==board[1][1]&& board[0][2]==board[2][0]&&board[0][2]!=0)
            return board[0][2];
        return 0;
    }

    public char[][] getBoardConfig(){
        char[][] boardValue = new char[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                switch (board[i][j]){
                    case 1: boardValue[i][j]='X';break;
                    case 2: boardValue[i][j]='O';break;
                    default: boardValue[i][j]=' ';break;
                }
            }
        }
        return boardValue;
    }

    public String whichPlayer(){
        return (playerTurn==1)?"X":"O";
    }

    @Override
    public String toString() {
        String str = "";
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++) {
                str += board[i][j] + " ";
            }
        }
        return str;
    }
}
