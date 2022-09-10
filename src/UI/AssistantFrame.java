package UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Core.Assistant;
import Support.Scribe;

public class AssistantFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	private Color color = new Color(0, 43, 60);

	private JMenuBar bar = new JMenuBar();
	private JMenu options = new JMenu("Opcje");
	private JMenuItem exitItem = new JMenuItem("Zakończ program");

	private JTextArea area = new JTextArea();
	private JScrollPane scrollArea = new JScrollPane(area);

	private JLabel tLabel = new JLabel(">:");
	private JTextField inputField = new JTextField();

	private void placeContent() {
		bar.setBounds(0, 0, Assistant.AREA_WIDTH+35, 20);
		scrollArea.setBounds(10, 30, Assistant.AREA_WIDTH, 290);
		tLabel.setBounds(10, 330, 20, 20);
		inputField.setBounds(30, 330, Assistant.AREA_WIDTH-20, 20);
	}

	public AssistantFrame() 
	{
		options.setCursor(cursor);
		exitItem.setCursor(cursor);

		int randomValue = new Random().nextInt(100);
		setTitle(randomValue < 20 ? "BootLog" : "Console Assistant");
		
		setSize(Assistant.AREA_WIDTH+35, 400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		area.setEditable(false);
		tLabel.setForeground(Color.WHITE);

		exitItem.addActionListener( (e) -> { System.exit(0);} );

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(color);
		panel.setFocusable(true);
		panel.requestFocus();
		inputField.addKeyListener(
				new KeyListener()
				{
					@Override
					public void keyPressed(KeyEvent event) {
						int code = event.getKeyCode();

						if(code != KeyEvent.VK_TAB) Assistant.scribe.suggestionUsed = true;
						
						switch(code)
						{
							case KeyEvent.VK_ENTER:
								String message = inputField.getText();
								if (message.length() > 0) 
								{
									area.append("<" + Assistant.getTime() + ">: " + message + "\n");
									area.setCaretPosition(area.getDocument().getLength());
									Assistant.answer(message);
									inputField.setText("");
									Assistant.scribe.log(message);
								}
								break;

							case KeyEvent.VK_UP:
								Assistant.executeWrapped(
									() -> {
										String log = Assistant.scribe.upGoThemLogs();

										if (log != null) 
										{
											writeToField(log);
										}
									} 	
								);
								break;

							case KeyEvent.VK_DOWN:
								Assistant.executeWrapped(
									() -> {
										String log = Assistant.scribe.downGoThemLogs();
										if (log != null) 
										{
											writeToField(log);
										} 
										else 
										{
											if (Assistant.scribe.logsWereUsed)
												inputField.setText("");
										}
									} 	
								);
								break;
								
							case KeyEvent.VK_TAB:
								inputField.setText(Assistant.scribe.tabGuess(inputField.getText()));
								inputField.requestFocus();
								break;
						}
					}

					@Override
					public void keyReleased(KeyEvent e){}

					@Override
					public void keyTyped(KeyEvent e){}
					
				}
		);

		area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		panel.add(scrollArea);
		panel.add(inputField); inputField.setFocusTraversalKeysEnabled(false);
		panel.add(tLabel);
		//panel.add(exitItem);
		panel.add(bar);
		bar.add(options);
		options.add(exitItem);

		setContentPane(panel);
		placeContent();
		setVisible(true);
		inputField.requestFocus();
	}
	
	public void setInputEnabled(boolean enabled)
	{
		inputField.setEnabled(enabled);
		if (enabled) inputField.requestFocus();
	}

	public void addToArea(String input) {
		area.append(input);
	}

	public void addToArea(char input) {
		String temp = ("" + input);
		area.append(temp);
	}

	private void writeToField(String input) {
		this.inputField.setText(input);
	}
	
	public void scrollDown() {
        JScrollBar sb = scrollArea.getVerticalScrollBar();
        sb.setValue(sb.getMaximum());
	}

}
