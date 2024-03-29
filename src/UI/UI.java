package UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class UI extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
	private Color color = new Color(0, 43, 60);

	private Scribe slave = new Scribe(20);

	JMenuBar bar = new JMenuBar();
	JMenu options = new JMenu("Opcje");
	JMenuItem exitItem = new JMenuItem("Zakończ program");

	JTextArea area = new JTextArea();
	public JScrollPane scrollArea = new JScrollPane(area);

	JLabel tLabel = new JLabel(">:");
	public JTextField inputField = new JTextField();

	private void placeConetent() {
		bar.setBounds(0, 0, Assistant.AREA_WIDTH+35, 20);
		scrollArea.setBounds(10, 30, Assistant.AREA_WIDTH, 290);
		tLabel.setBounds(10, 330, 20, 20);
		inputField.setBounds(30, 330, Assistant.AREA_WIDTH-20, 20);
	}

	public UI() {
		options.setCursor(cursor);
		exitItem.setCursor(cursor);

		Random rand = new Random();
		if(rand.nextInt()%100 == 0) 
			setTitle("BootLog");
		else
			setTitle("Console Assistant");
		setSize(Assistant.AREA_WIDTH+35, 400);
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

		area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		panel.add(scrollArea);
		panel.add(inputField); inputField.setFocusTraversalKeysEnabled(false);
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
	public void actionPerformed(ActionEvent eventSource) {
		Object event = eventSource.getSource();
		if (event == exitItem)
			System.exit(0);
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

	@Override
	public void keyPressed(KeyEvent event) {
		int code = event.getKeyCode();

		String message;
		if (code == KeyEvent.VK_ENTER) {
			message = inputField.getText();
			slave.suggestionUsed = true;
			if (message.length() > 0) {
				area.append("<" + Assistant.getTime() + ">: " + message + "\n");
				area.setCaretPosition(area.getDocument().getLength());
				Assistant.answer(message);
				inputField.setText("");
				slave.log(message);
			}
		} else if (code == KeyEvent.VK_UP) {
			try {
				message = slave.upGoThemLogs();

				if (message != null) {
					this.writeToField(message);
				}
			} catch (Exception e) {
				Assistant.error(e.getMessage() + "\n" + slave.getStatus());
			}
		} else if (code == KeyEvent.VK_DOWN) {
			try {
				message = slave.downGoThemLogs();
				if (message != null) {
					this.writeToField(message);
				} else {
					if (!slave.logsWereUsed)
						inputField.setText("");
				}
			} catch (Exception e) {
				Assistant.error(e.getMessage() + "\n" + slave.getStatus());
			}
		} else if (code == KeyEvent.VK_TAB) {
			this.inputField.setText(slave.tabGuess(this.inputField.getText()));
			this.inputField.requestFocus();
		}
		
		
		if(code != KeyEvent.VK_TAB) slave.suggestionUsed = true;
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

}
