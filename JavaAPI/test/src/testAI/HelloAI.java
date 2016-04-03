package testAI;

import java.security.SecureRandom;

import APITool.APITool;
import BomberGameBOTAPI.BomberGameBOTAPI;
import ServerObjectStructure.BitFlag;
import ServerTool.ErrorCode;


public class HelloAI {
	private BomberGameBOTAPI API;
	private int [][] map;
	private int Y, X;
	private int NextMove;
	private boolean isOnBombRange;
	
	public HelloAI(BomberGameBOTAPI inputAPI){
		API = inputAPI;
		isOnBombRange = false;
	}
	public int NextMove(){
		
		map = API.getMap(); //Get the game map from server
		UpdateAIStatus(); //Update the flags of AI, like isOnBombRange
		if(isOnBombRange){
			
			//If player is in bomb range
			//Player will find the nearest safe place and try to move to the nearest safe place
			
			findShortesSafePlace();
		}
		else{
			
			//If player is NOT in bomb range
			//Let's take a random move!!!
			
			randomlyMove();
		}
		return NextMove;
	}
	public int putBombFlag(){
		
		//If player is in bomb range
		//DO NOT put bomb
		
		if(isOnBombRange) return 0;
		
		//Randomly put bomb or not
		
		int result = 0;
		int R = new SecureRandom().nextInt(5);
		
		if(R == 0){
			result = BitFlag.putBombBeforeMove;
		}
//		else if(R == 1){
//			result = BitFlag.putBombBeforeMove;
//		}
		return result;
	}
	public static void main(String[] args) {
		
		String inputID = null;
		String inputPW = null;
		
		if(args.length == 0){
			inputID = "HelloAI" + APITool.SHA256(APITool.getTime());
			inputPW = inputID;
		}
		else{
			inputID = args[0];
			inputPW = args[1];
		}
		
		BomberGameBOTAPI API = new BomberGameBOTAPI(inputID, inputPW);
		
		HelloAI AI = new HelloAI(API);
		
		API.runConsole();
		
		while(BomberGameBOTAPI.isContinue){
			
			int rtn = API.match();
			if(rtn != 0){
				APITool.showOnScreen("AI", API.getErrorCode());
				APITool.showOnScreen("AI", API.getErrorMessage());
				break;
			}
			do{
				
				int NextMove = BitFlag.Move_Wait;
				int PutBombFlag = 0;
				
				NextMove = AI.NextMove();
				PutBombFlag = AI.putBombFlag();
				
				API.move(NextMove, PutBombFlag);
				
				if(API.getErrorCode() != ErrorCode.Success) break;
				
				API.showMap();
				
			}while(!API.isGameEnd());
			
			if(API.getErrorCode() != ErrorCode.Success){
				APITool.showOnScreen("API", API.getErrorCode());
			}
			APITool.showOnScreen("API", API.getGameResult());
		}
	}
	private void findShortesSafePlace(){
		int SafeMap[][] = new int[map.length][map[0].length];
		int StartMap[][] = new int[map.length][map[0].length];
		for(int y = 0 ; y < map.length ; y++){
			for(int x = 0 ; x < map[0].length ; x++){
				SafeMap[y][x] = Integer.MAX_VALUE;
				StartMap[y][x] = Integer.MAX_VALUE;
			}
		}
		
		SafeMap[Y][X] = 0;
		
		if((Y - 1) >= 0 && APITool.CompareBitFlag(map[Y - 1][X], BitFlag.BlockType_Path)){ 
			findShortesSafePlaceRecursive(BitFlag.Move_Up, SafeMap, StartMap, Y - 1, X, 1);
		}

		if((Y + 1) < map.length && APITool.CompareBitFlag(map[Y + 1][X], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(BitFlag.Move_Down, SafeMap, StartMap, Y + 1, X, 1);
		}

		if((X + 1) < map[0].length && APITool.CompareBitFlag(map[Y][X + 1], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(BitFlag.Move_Right, SafeMap, StartMap, Y, X + 1, 1);
		}

		if((X - 1) >= 0 && APITool.CompareBitFlag(map[Y][X - 1], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(BitFlag.Move_Left, SafeMap, StartMap, Y, X - 1, 1);
		}
		
		int min = Integer.MAX_VALUE;
		for(int y = 0 ; y < map.length ; y++){
			for(int x = 0 ; x < map[0].length ; x++){
				if(SafeMap[y][x] < min && ((map[y][x] & BitFlag.BlockType_BombCDFilter) == 0)){
					min = SafeMap[y][x];
					NextMove = StartMap[y][x];
				}
			}
		}
	}
	private void findShortesSafePlaceRecursive(int StartMove, int SafeMap[][], int StartMap[][], int inputY, int inputX, int PathLength){
		
		if(SafeMap[inputY][inputX] <= PathLength) return;
		else{
			SafeMap[inputY][inputX] = PathLength;
			StartMap[inputY][inputX] = StartMove;
		}
		
		if((inputY - 1) >= 0 && APITool.CompareBitFlag(map[inputY - 1][inputX], BitFlag.BlockType_Path)){ 
			findShortesSafePlaceRecursive(StartMove, SafeMap, StartMap, inputY - 1, inputX, PathLength + 1);
		}

		if((inputY + 1) < map.length && APITool.CompareBitFlag(map[inputY + 1][inputX], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(StartMove, SafeMap, StartMap, inputY + 1, inputX, PathLength + 1);
		}

		if((inputX + 1) < map[0].length && APITool.CompareBitFlag(map[inputY][inputX + 1], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(StartMove, SafeMap, StartMap, inputY, inputX + 1, PathLength + 1);
		}

		if((inputX - 1) >= 0 && APITool.CompareBitFlag(map[inputY][inputX - 1], BitFlag.BlockType_Path)){
			findShortesSafePlaceRecursive(StartMove, SafeMap, StartMap, inputY, inputX - 1, PathLength + 1);
		}
	}
	private void UpdateAIStatus(){
		
		int PlayerMark = API.getPlayerMark();
		
		//Update Location start
		for(int y = 0 ; y < map.length ; y++){
			for(int x = 0 ; x < map[0].length ; x++){
				if((map[y][x] & PlayerMark) == PlayerMark){
					Y = y;
					X = x;
					break;
				}
			}
		}
		//Update Location end
		
		isOnBombRange = (map[Y][X] & BitFlag.Move_Filter) > 0; 
		
	}
	private void randomlyMove(){
		
		NextMove = BitFlag.Move_Wait;
		int R = new SecureRandom().nextInt(4);
		
		for(int i = R ; i < R + 16 ; i++){
			if(i % 4 == 0){ //up
				if((Y - 1) < 0) continue;
				if(!APITool.CompareBitFlag(map[Y - 1][X], BitFlag.BlockType_Path)) continue; 
				
				NextMove = BitFlag.Move_Up;
			}
			else if(i % 4 == 1){//down
				if((Y + 1) >= map.length) continue;
				if(!APITool.CompareBitFlag(map[Y + 1][X], BitFlag.BlockType_Path)) continue;
				
				NextMove = BitFlag.Move_Down;
			}
			else if(i % 4 == 2){//right
				if((X + 1) >= map[0].length) continue;
				if(!APITool.CompareBitFlag(map[Y][X + 1], BitFlag.BlockType_Path)) continue;
				
				NextMove = BitFlag.Move_Right;
			}
			else if(i % 4 == 3){//left
				if((X - 1) < 0) continue;
				if(!APITool.CompareBitFlag(map[Y][X - 1], BitFlag.BlockType_Path)) continue;
				
				NextMove = BitFlag.Move_Left;
			}
		}
	}
}
