package APITool;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import BomberBOTGameAPI.APIOptions;
import ServerObjectStructure.Block;
import ServerObjectStructure.Message;
import ServerObjectStructure.Player;


public class APITool {
	
	public static String getTime(){
    	SimpleDateFormat nowdate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	nowdate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    	String result ="["+nowdate.format(new java.util.Date())+"]";
    	return result;
    }
	public static void showOnScreen(String inputLogName, int inputMsg){
		showOnScreen(inputLogName, inputMsg + "");
	}
	public static synchronized void showOnScreen(String inputLogName, String inputMsg){
		//String style = System.lineSeparator();
		String style = " ";
		
		System.out.println(APITool.getTime() + "[" + inputLogName + "]" + style + inputMsg);
	}
	private static String ByteToHex(byte[] input){
		
		
		String result = new String(Hex.encodeHex(input)); 
		
		return result.toUpperCase();
		
	}
	public static String StringToHex(String input){
		
		try {
			return ByteToHex(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		return null;
	}
	public static String HexToString(String input){
		
		if(input == null) return null;
		
		String result = null;
		
		try {
			result =  new String(Hex.decodeHex(input.toCharArray()), "UTF-8");
		} catch (DecoderException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String SHA256(String inputData){
		
		MessageDigest sha = null;
		
		try{
			sha = MessageDigest.getInstance("SHA-256");  
			sha.update(inputData.getBytes("UTF-8"));  
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		String result = ByteToHex(sha.digest()); 
		
		return result;
		
	}
	
	public static APIOptions StringToOption(String inputPlayerString){
		GsonBuilder gsonBuilder  = new GsonBuilder();
		gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
		
		Gson gson = gsonBuilder.create();
		
		APIOptions result = gson.fromJson(inputPlayerString, APIOptions.class);
				
		return result;
	}
	public static String OptionToString(APIOptions inputOption){
		
		GsonBuilder gsonBuilder  = new GsonBuilder();
		gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
		
		Gson gson = gsonBuilder.create();
		
		String result = gson.toJson(inputOption);
		
		return result;	
	}
	
	public static Player StringToPlayer(String inputPlayerString){
		Gson gson = new Gson();
		
		Player result = gson.fromJson(inputPlayerString, Player.class);
		
		return result;
	}
	public static String PlayerToString(Player inputPlayer){
		Gson gson = new Gson();
		
		String result = gson.toJson(inputPlayer);

		return result;		
	}
	public static Message StringToMessage(String inputMsgString){
		Gson gson = new Gson();
		
		Message result = gson.fromJson("{\"MsgMap\":" + inputMsgString + "}", Message.class);
		
		return result;
	}
	
	public static String MessageToString(Message inputMsg){
		Gson gson = new Gson();
		
		String result = gson.toJson(inputMsg);
		result = result.substring(10, result.length() -1);
		return result;		
	}
	
	public static String MapToString(Block [][] inputMap){
		String result = "";
		
		result += inputMap.length + " " + inputMap[0].length;
		
		for(int y = 0 ; y < inputMap.length ; y++){
			for(int x = 0 ; x < inputMap[0].length ; x++){
				
				result += " " + inputMap[y][x];
				
			}
		}
		
		return result;
	}
	public static boolean CompareBitFlag(int input, int inputFlag){
		return (input & inputFlag) == inputFlag;
	}
	public static int abs(int input){
		if(input < 0) return -1 * input;
		return input;
	}
	public static HashMap<String, Integer> StringToScoreMap(String inputString){
		Gson gson = new Gson();
		
		Type ScoreMapType = new TypeToken<HashMap<String, Integer>>(){}.getType();
		HashMap<String, Integer> result = gson.fromJson(inputString, ScoreMapType);
		
		return result;
	}
}
