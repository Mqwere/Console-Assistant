package Core;

import java.util.Date;

import UI.UI;

public class Assistant {
	public static UI UI;
	public static final int AREA_WIDTH 	= 315;
	public static final int LINE_LENGHT = ((AREA_WIDTH/10));
	public static String APP_INFO = 
							 "                INFO:\n"
							+"        Console Assistant App\n"
							+"        A smol console assistant\n"
							+"        Project started 27 may 2019\n"
							+"        Author: Marcin Chrąchol\n";
	
	
	public static void main(String[] args) {
		UI = new UI();
		//showInfo();
	}
	
	private static void showInfo() {
		UI.addToArea(APP_INFO);
		//clientContact.setCaretPosition(clientContact.getDocument().getLength());
	}
	
	@SuppressWarnings("deprecation")
	public static String getDate() {
		Date date = new Date();
		
		String dayAdd = date.getDate()>9 ? "":"0";
		String monthAdd = date.getMonth()<9? "0":"";
		String weekDay;
		switch(date.getDay()) {
			case 1:
				weekDay = "monday";
				break;
			case 2:
				weekDay = "tuesday";
				break;
			case 3:
				weekDay = "wednesday";
				break;
			case 4:
				weekDay = "thursday";
				break;
			case 5:
				weekDay = "friday";
				break;
			case 6:
				weekDay = "saturday";
				break;
			case 0:
				weekDay = "sunday";
				break;
				
			default:
				weekDay = "PARSING_ERROR";
		}
		
		return dayAdd + date.getDate() + "." + monthAdd + (date.getMonth()+1) + "." + (date.getYear()+1900) + ", " + weekDay; 
	}
	
	@SuppressWarnings("deprecation")
	public static String getTime() {
		Date date = new Date();
		String hadd = date.getHours()>9? 	"":"0",
			   madd = date.getMinutes()>9? 	"":"0",
			   sadd = date.getSeconds()>9? 	"":"0";
		
		return hadd+date.getHours()+":"+madd+date.getMinutes()+":"+sadd+date.getSeconds();
	}
	
	public static void error(Object message) {
		String output = new String();
		if(message.getClass() == String.class) {
			String obj = (String)message;
			output = obj;
		}
		else {
			output = message.toString();
		}
		output = output.toUpperCase();
		write("ERROR",output);
	}
	
	public static void print(Object message) {
		if(message.getClass() == String.class) {
			String obj = (String)message;
			UI.addToArea(obj);
		}
		else {
			UI.addToArea(message.toString());
		}
	}
	
	public static void sys(Object input) {
		write("SYSTEM", input);
	}
	
	public static void log(Object input) {
		write("LOG", input);
	}
	
	public static void write(Object input) {
		write("ASSISTANT",input);
	}

	public static void write(String from, Object input) {
		String mess;
		String tab = new String("");
		if(input.getClass()==String.class) 
				mess = (String)input;
		else 	mess = input.toString();
		
		int offset = 4 + from.length();
		if(from.length()>0) {
			UI.addToArea("["+from+"]: ");
		}
		else {
			offset = 1;
			UI.addToArea(" ");
		}
		
		for(int i=0; i<offset;i++) tab+=" ";
		if(mess.length()<=LINE_LENGHT) {
			UI.addToArea(mess);		
		}
		else {
			String[] messes = mess.split("\n");
			
			for(int y=0; y<messes.length;y++) {
				String message = messes[y];
				String[] pieces = message.split(" ");
				int 	 length = pieces.length, temp = 0;
				for(int i = 0; i<length;i++) {
					if(temp + pieces[i].length()>(LINE_LENGHT-offset)) {
						if(pieces[i].length()>(LINE_LENGHT-offset)) {
							if(temp!=0) UI.addToArea(" ");
							for(int x=0; x<pieces[i].length();x++) {
								if(temp%(LINE_LENGHT-offset)==(LINE_LENGHT-offset)-1) {
									UI.addToArea("-"+"\n"+tab);
									temp = 0;
								}
								UI.addToArea(pieces[i].charAt(x));
								temp++;
							}
						}
						else {
							UI.addToArea("\n"+tab+pieces[i]);
							temp = pieces[i].length();
						}
					}
					else {
						if(temp!=0) UI.addToArea(" ");
						UI.addToArea(pieces[i]);
						temp += pieces[i].length()+1;
					}
				}
				UI.addToArea("\n"+tab);
			}
		}
		UI.addToArea("\n");
	}
	
	public static void answer(String input) {
		input = input.toUpperCase();
		String[] content = input.split(" ");
		Command cmd = null;
		for(Command a: Command.values()) {
			if(content[0].contentEquals(a.name())) cmd = a;
		}
		
		switch(cmd) {
		case ABOUT:
		case INFO:
			showInfo();
			break;
		case CLOSE:
		case EXIT:
			System.exit(0);
			break;
		case DATE:
			if(content.length==1) {
				write(getDate());
			}
			break;
		default:
			write("Unknown command.");
			break;
		}
	}
}

