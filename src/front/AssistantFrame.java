package front;

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
import javax.swing.JTextField;

import core.Assistant;

public class AssistantFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private Color darkBlue = new Color(0, 43, 60);

	private JMenuBar menuBar = new JMenuBar();
	private JMenu optionsMenu = new JMenu("Opcje");
	private JMenuItem exitMenuItem = new JMenuItem("Zakończ program");

	private AssistantTextArea assistantTextArea;
	private JScrollPane assistantScrollArea;

	private JLabel inputLabel = new JLabel(">:");
	private JTextField inputField = new JTextField();

	public AssistantFrame(int areaWidth) 
	{
		assistantTextArea = new AssistantTextArea( areaWidth / 10 );
		assistantTextArea.setEditable( false );
		assistantTextArea.setFont( new Font( Font.MONOSPACED, Font.PLAIN, 12 ) );
		
		assistantScrollArea = new JScrollPane( assistantTextArea );
		
		inputLabel.setForeground( Color.WHITE );

		exitMenuItem.setCursor( handCursor );
		exitMenuItem.addActionListener( ( e ) -> { System.exit( 0 ); } );
		
		inputField.setFocusTraversalKeysEnabled( false );
		inputField.addKeyListener(
				new KeyListener( )
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
									assistantTextArea.append("<" + Assistant.getTime() + ">: " + message + "\n");
									assistantTextArea.setCaretPosition(assistantTextArea.getDocument().getLength());
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

		menuBar.add( optionsMenu );
		
		optionsMenu.setCursor( handCursor );
		optionsMenu.add( exitMenuItem );

		JPanel panel = new JPanel( );
		panel.setLayout( null );
		panel.setBackground( darkBlue );
		panel.setFocusable( true );
		panel.requestFocus( );
		panel.add( assistantScrollArea );
		panel.add( inputField ); 
		panel.add( inputLabel );
		panel.add( menuBar );

		int randomValue = new Random().nextInt(100);
		
		setTitle( randomValue < 20 ? "BootLog" : "Console Assistant" );
		setSize( areaWidth + 35, 400 );
		setResizable( false );
		setContentPane( panel );
		placeContent( areaWidth );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		setLocationRelativeTo( null );
		setVisible(true);
		
		inputField.setFocusable(true);
		inputField.requestFocus( );
	}
	
	public void setInputEnabled(boolean enabled)
	{
		inputField.setEnabled(enabled);
		if (enabled) inputField.requestFocus();
	}

	public void addToArea(Object message) 
	{
		assistantTextArea.append(message);
	}
	
	public void addToArea(String senderOfMessage, Object message) 
	{
		assistantTextArea.append(senderOfMessage, message);
	}
	
	public void scrollDown() 
	{
        JScrollBar sb = assistantScrollArea.getVerticalScrollBar();
        sb.setValue(sb.getMaximum());
	}

	private void placeContent(int areaWidth) 
	{
		menuBar.setBounds(0, 0, areaWidth+35, 20);
		assistantScrollArea.setBounds(10, 30, areaWidth, 290);
		inputLabel.setBounds(10, 330, 20, 20);
		inputField.setBounds(30, 330, areaWidth-20, 20);
	}

	private void writeToField(String input) 
	{
		this.inputField.setText(input);
	}
	
}
