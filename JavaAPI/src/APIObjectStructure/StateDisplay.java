package APIObjectStructure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import APITool.APITool;
import BomberBOTGameAPI.BomberBOTGameAPI;
import ServerObjectStructure.BitFlag;

public class StateDisplay implements Runnable{
	
	private final int informationArea = 60;
	private final int informationAreaShift = 1;
	private String initInformationArea = "";
	//private int[][] GameMap;
	private String ID;
	
	private TextGraphics Writer;
	private Screen screen;
	
	public StateDisplay(String inputID){
		ID = inputID;
		
		while(initInformationArea.length() < (80 - informationArea)) initInformationArea += " ";
		
		Terminal terminal;
		try {
			terminal = new DefaultTerminalFactory().createTerminal();
			screen = new TerminalScreen(terminal);
	        screen.startScreen();
	        screen.setCursorPosition(null);
			Writer = screen.newTextGraphics();
			screen.startScreen();
			screen.clear();
			
			String line = "";
			for(int i = 0 ; i < 80 ; i++) line += " ";
				
			for(int i = 0 ; i < 24 ; i++){
				Writer.setBackgroundColor(TextColor.ANSI.WHITE);
				Writer.putString(0, i, line);
				Writer.setBackgroundColor(TextColor.ANSI.YELLOW);
				Writer.putString(informationArea, i, line);
			}
			Writer.setForegroundColor(TextColor.ANSI.BLACK);
			Writer.putString(informationArea, 0, "ID: ");
			Writer.putString(informationArea + informationAreaShift, 1, ID);
			
			Writer.putString(informationArea + informationAreaShift, 23, "API v " + BomberBOTGameAPI.APIversion);
			
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    updatePlayerInformation(0, 0, 0, 0, 0);
	    updateState("Init success");
	}
	public void updatePlayerInformation(int inputWins, int inputLosses, int inputDraw, int inputScore, int inputRound){
		Writer.setBackgroundColor(TextColor.ANSI.YELLOW);
		Writer.setForegroundColor(TextColor.ANSI.BLACK);
		
		int line = 2;
		Writer.putString(informationArea, line++, "Wins: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, "" + inputWins);
		
		Writer.putString(informationArea, line++, "Losses: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, "" + inputLosses);
		
		Writer.putString(informationArea, line++, "Game draw: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, "" + inputDraw);
		
		Writer.putString(informationArea, line++, "Score: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, "" + inputScore);
		
		line++;
		Writer.putString(informationArea, line++, "Matched: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, "" + inputRound);
		
	    try {
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateState(String inputState){
		Writer.setBackgroundColor(TextColor.ANSI.YELLOW);
		Writer.setForegroundColor(TextColor.ANSI.BLACK);
		
		int line = 13;
		
		Writer.putString(informationArea, line++, "State: ");
		Writer.putString(informationArea + informationAreaShift, line, initInformationArea);
		Writer.putString(informationArea + informationAreaShift, line++, inputState);
		
	    try {
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void udateGameMap(int inputGameMap[][], int inputPlayerMark){
		
		int basicY = 12 - inputGameMap.length /2;
		int basicX = informationArea/2 - inputGameMap[0].length;
		
		String Wall = null;
		String Path = null;
		String Bomb = null;
		
		String Me = null;
		String Other = null;
		String BombRange = null;
		
		try {
			Wall = new String("▉".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Path = new String("　".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Bomb = new String("◎".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			Me = new String("O".getBytes("UTF-8"), Charset.forName("UTF-8"));
			Other = new String("X".getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			BombRange = new String("○".getBytes("UTF-8"), Charset.forName("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Writer.setBackgroundColor(TextColor.ANSI.BLACK);
		Writer.setForegroundColor(TextColor.ANSI.WHITE);
	    for(int y = 0 ; y < inputGameMap.length ; y++){
	    	
			for(int x = 0 ; x <inputGameMap[0].length ; x++){
				
				String Unit = null;
				
				if(APITool.CompareBitFlag(inputGameMap[y][x], inputPlayerMark)){
					Unit = Me + " ";
				}
				else if((inputGameMap[y][x] & BitFlag.Player_Filter) != 0){
					Unit = Other + " ";
				}
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Bomb)){
					Unit = Bomb + " ";
				}
				else if((inputGameMap[y][x] & BitFlag.BlockType_BombCDFilter) > 0x0){
					Writer.setBackgroundColor(TextColor.ANSI.RED);
					Unit = "  ";
				}
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Path)){
					Writer.setBackgroundColor(TextColor.ANSI.BLACK);
					Unit = Path;
				}
				else if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Wall)){
					Writer.setBackgroundColor(TextColor.ANSI.BLACK);
					Unit = Wall + " ";
				}
				else{
					Unit = "? ";
				}
				
				if(APITool.CompareBitFlag(inputGameMap[y][x], BitFlag.BlockType_Bomb)){
					Writer.setBackgroundColor(TextColor.ANSI.RED);
				}
				else if((inputGameMap[y][x] & BitFlag.BlockType_BombCDFilter) > 0x0){
					Writer.setBackgroundColor(TextColor.ANSI.RED);
				}
				else{
					Writer.setBackgroundColor(TextColor.ANSI.BLACK);
				}
				
				Writer.putString(basicX + (x * 2), basicY + y, Unit);
			}
			
		}

	    try {
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {}

}
