package Assistant;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

///////////////////////////////////////////////////////////////////////////	ASSISTANT

public class Assistant {
	public static UI UI;
	private static JTextArea clientContact;
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
		clientContact = UI.area;
		showInfo();
	}
	
	private static void showInfo() {
		clientContact.append(APP_INFO);
		//clientContact.setCaretPosition(clientContact.getDocument().getLength());
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
		switch(content[0]) {
			case "ABOUT":
			case "INFO":
				showInfo();
				break;
				
			case "EXIT":
			case "CLOSE":
				System.exit(0);
				break;
				
			case "OESUS":
			case "KWI":
				write(input.toLowerCase());
				break;
				
			case "DATE":
				if(content.length==1) {
					write(new Date().toString());
				}
				break;
			case "TIME":
				if(content.length==1) {
					Date date = new Date();
					write(date.getHours()+":"+date.getMinutes()+":"+date.getSeconds());
				}
				break;
				
			default:
				write("Unknown command.");
				break;
		}
	}
}

///////////////////////////////////////////////////////////////////////////	UI

class UI  extends JFrame implements ActionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	private Color color = new Color(0, 43, 60);
	
	private Scribe slave = new Scribe(20);
		
	JMenuBar 	bar 		= new JMenuBar();
	JMenu 		options 	= new JMenu("Opcje");
	JMenuItem 	exitItem 	= new JMenuItem("Zakończ program");
		
	JTextArea 	area		= new JTextArea();
	JScrollPane scrollArea 	= new JScrollPane(area);
		
	JLabel 		tLabel 		= new JLabel(">:");
	JTextField 	inputField 	= new JTextField();
		
	private void placeConetent(){
		bar.setBounds(0, 0, 340, 20);
		scrollArea.setBounds(10, 30, 315, 290);
		tLabel.setBounds(10, 330, 20, 20);
		inputField.setBounds(30, 330, 295, 20);
	}
		
	public UI() {
		options.setCursor(cursor);
		exitItem.setCursor(cursor);
			
		setTitle("Console Assistant");
		setSize(350,400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		area.setEditable(false);
		tLabel.setForeground(Color.WHITE);
			
		exitItem.addActionListener(this);
			
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(color);
		panel.setFocusable(true);
		panel.requestFocus();
		inputField.addKeyListener(this);

		area.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));
		panel.add(scrollArea);
		panel.add(inputField);
		panel.add(tLabel);
		panel.add(exitItem);
		panel.add(bar);
		bar.add(options);
		options.add(exitItem);
			
		setContentPane(panel);
		placeConetent();
		setVisible(true);
		inputField.requestFocus();
	}

		@Override
	public void actionPerformed(ActionEvent eventSource) 
	{
		Object event = eventSource.getSource();
		if(event == exitItem) System.exit(0);
	}
		
	public void addToArea(String input) {
		area.append(input);
	}
	
	public void addToArea(char input) {
		String temp = (""+input);
		area.append(temp);
	}
	
	private void writeToField(String input) {
		this.inputField.setText(input);
	}
	
	@Override
	public void keyPressed(KeyEvent event){
			int code = event.getKeyCode();
			
			String message;
			if(code == KeyEvent.VK_ENTER){
				message = inputField.getText();
				if(message.length()>0) {
					area.append(">: " + message + "\n");
					area.setCaretPosition(area.getDocument().getLength());
					Assistant.answer(message);
					inputField.setText("");
					slave.log(message);
				}
			}
			else 
			if(code == KeyEvent.VK_UP) {
				try {
					message = slave.upGoThemLogs();
	
					if(message != null) {
						this.writeToField(message);
					}
				}
				catch(Exception e) {
					Assistant.error(e.getMessage() + "\n" + slave.getStatus());
				}
			}
			else 
			if(code == KeyEvent.VK_DOWN) {
				try {
					message = slave.downGoThemLogs();
					if(message != null) {
						this.writeToField(message);
					}
					else {
						if(!slave.logsWereUsed) inputField.setText("");
					}
				}
				catch(Exception e) {
					Assistant.error(e.getMessage() + "\n" + slave.getStatus());
				}
			}
		}

		@Override
	public void keyReleased(KeyEvent event)	{}

		@Override
	public void keyTyped(KeyEvent event) {}

}

///////////////////////////////////////////////////////////////////////////	SCRIBE

class Scribe {
	private ArrayList<String> logs = new ArrayList<String>();
	private int maxLogCap;
	private int currentLogId;
	public boolean logsWereUsed = false;
	
	public String getStatus() {
		String status = "Scribe Status:"
					+	"\n  maxLogCap:    " + Integer.toString(this.maxLogCap)
					+	"\n  currentLogId: " + Integer.toString(this.currentLogId)
					+   "\n  logsWereUsed: " + Boolean.toString(this.logsWereUsed)
					+	"\n  logs.size():  " + Integer.toString(this.logs.size());
		
		return status;
	}
	
	public Scribe(int capacity) {
		this.maxLogCap = capacity;
		this.currentLogId = 0;
		this.logsWereUsed = false;
	}
	
	private void checkSize() {
		if(this.logs.size() >= this.maxLogCap) {
			ArrayList<String> temp = new ArrayList<String>();
			for(int i=1;i<this.logs.size();i++) {
				temp.add(this.logs.get(i));
			}
			logs.clear();
			logs = temp;
		}
	}
	
	public String upGoThemLogs() {
		if(!this.logsWereUsed) {
			this.logsWereUsed = true;
			if(this.logs.size()>0) return logs.get(this.currentLogId);
			else {
				return null;
			}
		}
		else 	{
			if(--this.currentLogId >= 0) {
				return logs.get(this.currentLogId);
			}
			else {
				this.currentLogId++;
				return null;
			}
		}
	}
	
	public String downGoThemLogs() {
		if(this.logsWereUsed) {
			if (++this.currentLogId < this.logs.size())	return logs.get(this.currentLogId);
			else {
				this.currentLogId--;
				this.logsWereUsed = false;
			}
		}
		return null;
	}
	
	public void log(String input) {
		this.logsWereUsed = false;
		if(this.logs.size() > 0) {
			if(input.equals(logs.get(this.currentLogId))) {
				logs.remove(this.currentLogId);
			}
		}
		this.checkSize();
		this.logs.add(input);
		this.currentLogId = this.logs.size()-1;
	}
}

