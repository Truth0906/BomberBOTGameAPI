package BomberGameBOTAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import javax.net.SocketFactory;

import APIObjectStructure.APICenter;
import APITool.APITool;
import ServerObjectStructure.BitFlag;
import ServerObjectStructure.Message;
import ServerTool.ErrorCode;

public class BomberGameBOTAPI {
	
	public static final String APIversion = "1.0.160402";
	
	private BufferedWriter Writer;
	private BufferedReader Reader;
	private Socket Client;

	private int map[][];
	private Message LastMessage;
	
	private String LogName = "TestAPI";
	
	public BomberGameBOTAPI(){
	    Writer = null;
	    Reader = null;
	    Client = null;
	    new APICenter();
	}
	
	
	public String echo(String inputString){
	    
	    if (!connect()) {

	    	return null;
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg("FunctionName", "echo");
	    Msg.setMsg("Message", inputString);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
    
	    return LastMessage.getMsg("Message");
	}
	public int match(String inputID, String inputPW){
	    
	    if (!connect()) {
	    	return getErrorCode();
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "match");
	    Msg.setMsg(Message.ID, inputID);
	    Msg.setMsg(Message.Password, inputPW);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
	    
	    if(getErrorCode() == ErrorCode.Success) ParseMap();
	    
	    return getErrorCode();
	}
	public int move(String inputID, String inputPW, int inputMove, int putBombFlag){
		
		if (!connect()) {
	    	return getErrorCode();
	    }
	    
	    Message Msg = new Message();
	    
	    Msg.setMsg(Message.FunctionName, "move");
	    Msg.setMsg(Message.ID, inputID);
	    Msg.setMsg(Message.Password, inputPW);
	    Msg.setMsg(Message.BombFlag, putBombFlag);
	    Msg.setMsg(Message.Move, inputMove);
	    
	    sendMsg(Msg);
	    LastMessage = receiveMsg();
	    
	    if(getErrorCode() == ErrorCode.Success) ParseMap();
	    
	    return getErrorCode();
	}
	public void showMap(){
		
		
		String Wall = null;
		String Path = null;
		String Bomb = null;
		
		String PA = null;
		String PB = null;//★☆
		String Number[] = new String[10];
		String BombRange = null;
		
		try {
			Wall = new String("▉".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Path = new String("　".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Bomb = new String("◎".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			PA = new String("★".getBytes("UTF-8"), Charset.forName("UTF-8"));
			PB = new String("☆".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
//			Number[1] = new String("１".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[2] = new String("２".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[3] = new String("３".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[4] = new String("４".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[5] = new String("５".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[6] = new String("６".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[7] = new String("７".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[8] = new String("８".getBytes("UTF-8"), Charset.forName("UTF-8"));
//			Number[9] = new String("９".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
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
				else if((EachMap & 0xF) > 0x0) 								    Buffer += BombRange;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultMsg;
	}
	public static void main(String[] args) {
		
		System.out.println("BomberGameBOTAPI v " + BomberGameBOTAPI.APIversion);
		
	}
}
