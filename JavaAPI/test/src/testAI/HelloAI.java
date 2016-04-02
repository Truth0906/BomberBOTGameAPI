package testAI;

import java.security.SecureRandom;

import APITool.APITool;
import BomberGameBOTAPI.BomberGameBOTAPI;
import ServerObjectStructure.BitFlag;
import ServerTool.ErrorCode;


public class HelloAI {
	
	public static void main(String[] args) {
		
		String inputID = args[0];
		String inputPW = args[1];
		
		BomberGameBOTAPI testAPI = new BomberGameBOTAPI(inputID, inputPW);
		testAPI.runConsole();
		while(BomberGameBOTAPI.isContinue){
			
			int rtn = testAPI.match();
			if(rtn != 0){
				APITool.showOnScreen("AI", testAPI.getErrorCode());
				APITool.showOnScreen("AI", testAPI.getErrorMessage());
				return;
			}
			
			int PlayerMark = testAPI.getPlayerMark();
			int map[][];
			SecureRandom rand = new SecureRandom();
			int NoMove = -1;
			
			testAPI.showMap();
			
			while(testAPI.getErrorCode() == ErrorCode.Success){
				map = testAPI.getMap();
				
				int Y = 0, X = 0;
				int move = 0;
				
				for(int y = 0 ; y < map.length ; y++){
					for(int x = 0 ; x < map[0].length ; x++){
						if((map[y][x] & PlayerMark) == PlayerMark){
							Y = y;
							X = x;
							break;
						}
					}
				}
				
				int NextMove = -1;
				
				do{
					
					if(args[1].equals("01")) break;
					
					move = rand.nextInt();
					
					if(move < 0) move = move * -1;
					move = move % 4;
					
					if(move == NoMove) continue;
					
					if(move == 0){ //up
						if((Y - 1) < 0) continue;
						if(!APITool.CompareBitFlag(map[Y - 1][X], BitFlag.BlockType_Path)) continue; 
						
						NoMove = 1;
						NextMove = BitFlag.Move_Up;
					}
					else if(move == 1){//down
						if((Y + 1) >= map.length) continue;
						if(!APITool.CompareBitFlag(map[Y + 1][X], BitFlag.BlockType_Path)) continue;
						
						NoMove = 0;
						NextMove = BitFlag.Move_Down;
					}
					else if(move == 2){//right
						if((X + 1) >= map[0].length) continue;
						if(!APITool.CompareBitFlag(map[Y][X + 1], BitFlag.BlockType_Path)) continue;
						
						NoMove = 3;
						NextMove = BitFlag.Move_Right;
					}
					else if(move == 3){//left
						if((X - 1) < 0) continue;
						if(!APITool.CompareBitFlag(map[Y][X - 1], BitFlag.BlockType_Path)) continue;
						
						NoMove = 2;
						NextMove = BitFlag.Move_Left;
					}
					
				}while(NextMove == -1);
				APITool.showOnScreen("AI", "Next move: " + NextMove);
				
				int putBombFlag = 0;
				
				move = rand.nextInt();
				if(move < 0) move = move * -1;
				move = move % 5;
				
				if(move == 1){
					putBombFlag = BitFlag.putBombAfterMove;
				}
				else if(move == 2){
					putBombFlag = BitFlag.putBombBeforeMove;
				}
				if(args[1].equals("01")) testAPI.move(0, 0);
				else testAPI.move(NextMove, putBombFlag);
				
				testAPI.showMap();
				
				if(testAPI.isGameEnd()) break;
			}
			if(testAPI.getErrorCode() == ErrorCode.Success){
				APITool.showOnScreen("AI", testAPI.getGameResult());
			}
			else{
				APITool.showOnScreen("AI", testAPI.getErrorCode());
				APITool.showOnScreen("AI", testAPI.getErrorMessage());
				
			}
		
		}
	}

}
