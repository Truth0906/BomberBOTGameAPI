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
import javax.smartcardio.TerminalFactory;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;

import APIObjectStructure.APICenter;
import APITool.APITool;
import ServerObjectStructure.BitFlag;
import ServerObjectStructure.Message;
import ServerTool.ErrorCode;

public class BomberGameBOTAPI {
	
	public static final String version = "1.0.160320";
	
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
			
			BombRange = new String("⿴".getBytes("UTF-8"), Charset.forName("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(Number[1] + Number[2] + System.lineSeparator());
	    for(int[] y : map){
			for(int EachMap : y){
				
				if(APITool.CompareBitFlag(EachMap, BitFlag.PlayerA))			System.out.print(PA);
				else if(APITool.CompareBitFlag(EachMap, BitFlag.PlayerB)) 		System.out.print(PB);
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Bomb))System.out.print(Bomb);
				else if((EachMap & 0xF) >= 0x0) 								System.out.print(BombRange);
//				else if((EachMap & 0xF) == 0x1)	System.out.print(Number[1]);
//				else if((EachMap & 0xF) == 0x2)	System.out.print(Number[2]);
//				else if((EachMap & 0xF) == 0x3)	System.out.print(Number[3]);
//				else if((EachMap & 0xF) == 0x4)	System.out.print(Number[4]);
//				else if((EachMap & 0xF) == 0x5)	System.out.print(Number[5]);
//				else if((EachMap & 0xF) == 0x6)	System.out.print(Number[6]);
//				else if((EachMap & 0xF) == 0x7)	System.out.print(Number[7]);
//				else if((EachMap & 0xF) == 0x8)	System.out.print(Number[8]);
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Path)) 	System.out.print(Path);
				else if(APITool.CompareBitFlag(EachMap, BitFlag.BlockType_Wall)) 	System.out.print(Wall);
				else System.out.print(Wall);
				
			}
			System.out.print(System.lineSeparator());
		}
	    
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
		
		System.out.println("BomberGameBOTAPI v " + BomberGameBOTAPI.version);
		try {
			String Test = new String("◯".getBytes("UTF-8"), Charset.forName("UTF-8"));
			System.out.println(Test);
			Test = new String("○".getBytes("UTF-8"), Charset.forName("UTF-8"));
			System.out.println(Test);
			Test = new String("◇".getBytes("UTF-8"), Charset.forName("UTF-8"));
			System.out.println(Test);
			Test = new String("◆".getBytes("UTF-8"), Charset.forName("UTF-8"));
			System.out.println(Test);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Screen screen = new Screen(null);
        screen.startScreen();

        ScreenWriter writer = new ScreenWriter(screen);
        writer.setForegroundColor(Terminal.Color.DEFAULT);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(10, 1, "Hello World");

        writer.setForegroundColor(Terminal.Color.BLACK);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(11, 2, "Hello World");
        writer.setForegroundColor(Terminal.Color.WHITE);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(12, 3, "Hello World");
        writer.setForegroundColor(Terminal.Color.BLACK);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(13, 4, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.WHITE);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(14, 5, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.DEFAULT);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(15, 6, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.DEFAULT);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(16, 7, "Hello World");

        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(10, 10, "Hello World");
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(11, 11, "Hello World");
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(12, 12, "Hello World");
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.MAGENTA);
        writer.drawString(13, 13, "Hello World");
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(14, 14, "Hello World");
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(15, 15, "Hello World");
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(16, 16, "Hello World");
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.MAGENTA);
        writer.drawString(17, 17, "Hello World");

        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(10, 20, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(11, 21, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(12, 22, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.BLUE);
        writer.setBackgroundColor(Terminal.Color.MAGENTA);
        writer.drawString(13, 23, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.DEFAULT);
        writer.drawString(14, 24, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.WHITE);
        writer.drawString(15, 25, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.GREEN);
        writer.setBackgroundColor(Terminal.Color.BLACK);
        writer.drawString(16, 26, "Hello World", ScreenCharacterStyle.Bold);
        writer.setForegroundColor(Terminal.Color.CYAN);
        writer.setBackgroundColor(Terminal.Color.BLUE);
        writer.drawString(17, 27, "Hello World", ScreenCharacterStyle.Bold);
        screen.refresh();
        
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
        }
        screen.stopScreen();
	}
}
