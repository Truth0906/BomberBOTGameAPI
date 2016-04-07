package BomberBOTGameAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.SocketFactory;

import APIObjectStructure.APICenter;
import APIObjectStructure.APIConsoleUI;
import APITool.APITool;
import ServerObjectStructure.BitFlag;
import ServerObjectStructure.Message;
import ServerTool.ErrorCode;

public class BomberBOTGameAPI {
	
	public static final String APIversion = "1.0.160404";
	public static boolean isContinue = true;
	
	private BufferedWriter Writer;
	private BufferedReader Reader;
	private Socket Client;
	
	private String ID;
	private String Password;
	
	private APIConsoleUI ConsoleUI; 
	private int Round;
	
	private int map[][];
	private Message LastMessage;
	private ArrayList<String> SortedID;
	private ArrayList<Integer> SortedScore;
	
	private String LogName = "BomberGameBOTAPI";
	
	public BomberBOTGameAPI(String inputID, String inputPW){
	    Writer = null;
	    Reader = null;
	    Client = null;
	    ID = inputID;
	    Password = inputPW;
	    Round = 0;
	    new APICenter();
	}
	
	
	public String echo(String inputString){
	    
	    if (!connect()) {

	    	return null;
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "echo");
	    Msg.setMsg(Message.Message, inputString);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
    
	    return LastMessage.getMsg(Message.Message);
	}
	public int match(){
	    
	    if (!connect()) {
	    	return getErrorCode();
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "match");
	    Msg.setMsg(Message.ID, ID);
	    Msg.setMsg(Message.Password, Password);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
	    
	    if(getErrorCode() == ErrorCode.Success) {
	    	ParseMap();
	    	ConsoleUI.setRounds(Round);
	    	Round++;
	    }
	    
	    return getErrorCode();
	}
	public int move(int inputMove, int putBombFlag){
		
		if (!connect()) {
	    	return getErrorCode();
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "move");
	    Msg.setMsg(Message.ID, ID);
	    Msg.setMsg(Message.Password, Password);
	    Msg.setMsg(Message.BombFlag, putBombFlag);
	    Msg.setMsg(Message.Move, inputMove);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
	    
	    if(getErrorCode() == ErrorCode.Success) ParseMap();
	    
	    return getErrorCode();
	}
	public int query(){
		return query(null);
	}
	public int query(String inputID){
		
		if (!connect()) {
	    	return getErrorCode();
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "query");
	    Msg.setMsg(Message.ID, ID);
	    Msg.setMsg(Message.Password, Password);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
	    
	    if(getErrorCode() != ErrorCode.Success) return getErrorCode(); 
	    
	    String ScoreMapString = LastMessage.getMsg(Message.ScoreMap);
	    
	    SortedID = new ArrayList<String>();
	    SortedScore = new ArrayList<Integer>();
	    
	    HashMap<String, Integer> ScoreMap = APITool.StringToScoreMap(ScoreMapString);
	    
	    for(Entry<String, Integer> EachPlayer : ScoreMap.entrySet()){
	    	SortedID.add(EachPlayer.getKey());
	    	SortedScore.add(EachPlayer.getValue());
	    }
	    
	    for (int i = 1; i < SortedScore.size(); i++) {
	        
	    	int ScoreKey = SortedScore.get(i);
	    	String IDKey = SortedID.get(i);
	        
	        int j = i;
	        while((j > 0) && (SortedScore.get(j - 1) < ScoreKey)) {
	        	SortedScore.set(j, SortedScore.get(j - 1));
	        	SortedID.set(j, SortedID.get(j - 1));
	            j--;
	        }
	        SortedScore.set(j,ScoreKey);
	        SortedID.set(j, IDKey);
	    }
	    for (int i = 0; i < SortedScore.size(); i++) {
	    	
	    	if(inputID != null && inputID.length() != 0){
	    		if(!SortedID.get(i).equals(inputID)) continue;
	    	}
	    	APITool.showOnScreen(LogName, (i + 1) + ". " + SortedID.get(i) + " " + SortedScore.get(i));
	    }
	    
	    return ErrorCode.Success;
	}
	public void showMap(){
		
		
		String Wall = null;
		String Path = null;
		String Bomb = null;
		
		String PA = null;
		String PB = null;
		String BombRange = null;
		
		try {
			Wall = new String("▉".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Path = new String("　".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Bomb = new String("◎".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			PA = new String("★".getBytes("UTF-8"), Charset.forName("UTF-8"));
			PB = new String("☆".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			BombRange = new String("○".getBytes("UTF-8"), Charset.forName("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String Buffer = "";
		for(int i = 0 ; i < 10 ; i++) Buffer += System.lineSeparator();
		Buffer += "You are " + (getPlayerMark() == BitFlag.PlayerA ? PA : PB);
		Buffer += System.lineSeparator();
	    for(int[] y : map){
			for(int EachMap : y){
				
				if(APITool.CompareBitFlag(EachMap, BitFlag.PlayerA))			Buffer += PA;
				else if(APITool.CompareBitFlag(EachMap, BitFlag.PlayerB)) 		Buffer += PB;
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Bomb))Buffer += Bomb;
				else if((EachMap & BitFlag.BlockType_BombCDFilter) > 0x0) 								    Buffer += BombRange;
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Path)) 	Buffer += Path;
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Wall)) 	Buffer += Wall;
				else Buffer += "?";
				
			}
			Buffer += System.lineSeparator();
		}
	    System.out.print(Buffer);
	}
	public int getPlayerMark(){
		return Integer.parseInt(LastMessage.getMsg(Message.PlayerMark));
	}
	public String getErrorMessage(){
		return LastMessage.getMsg(Message.Message);
	}
	public int getErrorCode(){
		return Integer.parseInt(LastMessage.getMsg(Message.ErrorCode));
	}
	public int[][] getMap(){
		return map;
	}
	public boolean isGameEnd(){
		return Boolean.parseBoolean(LastMessage.getMsg(Message.End));
	}
	public String getGameResult(){
		return LastMessage.getMsg(Message.GameResult);
	}
	public void runConsole(){
		final String inputID = ID;
		ConsoleUI = APIConsoleUI.showUI(inputID);
	}
	private void ParseMap(){
		String temp = LastMessage.getMsg(Message.Map);
		if(temp == null){
			APITool.showOnScreen("BomberGameBOTAPI", "Error null map");
			LastMessage.setMsg(Message.ErrorCode, ErrorCode.GenernalError);
			return;
		}
		String tempMap[] = temp.split(" ");
		
		map = new int[ Integer.parseInt(tempMap[0]) ][Integer.parseInt(tempMap[1])];
		    
		int indexTemp = 2;
		  
		for(int y = 0 ; y < map.length ; y++){
			for(int x = 0 ; x < map[0].length ; x++){
					
				map[y][x] = Integer.parseInt(tempMap[indexTemp++]);
				
			}
		}
	} 
	private boolean connect(){
		
		for(int port : APIOptions.PortList){
			
//			ST.showOnScreen(LogName, "Connect to port " + PortList[i]);
		    try{
		    	Client = SocketFactory.getDefault().createSocket();
		    	InetSocketAddress remoteaddr = new InetSocketAddress(APIOptions.ServerIP, port);
		    	Client.connect(remoteaddr, 2000);
		    	Reader = new BufferedReader(new InputStreamReader(Client.getInputStream(), "UTF-8"));
		    	Writer = new BufferedWriter(new OutputStreamWriter(Client.getOutputStream(), "UTF-8"));
		    }
		    catch (IOException e){
		    	APITool.showOnScreen(LogName, "Connect prot " + port + " time out");
		    	continue;
		    }
		    break;
		}
//	    ST.showOnScreen(LogName, "Connect success");
	    return true;
	}
	private boolean sendMsg(Message inputMsg){
		
		inputMsg.setMsg(Message.APIVersion, APIversion);
		
		String Msg = APITool.MessageToString(inputMsg);
		try {
			Writer.write(Msg + "\r\n");
			Writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private Message receiveMsg(){
		Message resultMsg = null;
		
		String receivedString = null;
		try {
			
			receivedString = Reader.readLine();
			resultMsg = APITool.StringToMessage(receivedString);
			
		} catch (IOException e) {
			resultMsg = new Message();
			resultMsg.setMsg(Message.Message, "Server disconnect");
			resultMsg.setMsg(Message.ErrorCode, ErrorCode.ConnectError);
		}
		
		return resultMsg;
	}
	public static void main(String[] args) {
		new APICenter();
		System.out.println("BomberGameBOTAPI v " + BomberBOTGameAPI.APIversion);
		
	}
}
