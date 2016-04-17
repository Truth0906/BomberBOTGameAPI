package APIObjectStructure;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import BomberBOTGameAPI.BomberBOTGameAPI;
import javax.swing.JTextArea;

public class APIConsoleUI{

	private JFrame frmAaa;
	/**
	 * Launch the application.
	 */
	public static APIConsoleUI showUI(final String inputID){
		final APIConsoleUI window = new APIConsoleUI(inputID);;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frmAaa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return window;
	}

	/**
	 * Create the application.
	 */
	public APIConsoleUI(String inputID) {
		initialize(inputID);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String inputID) {
		frmAaa = new JFrame();
		frmAaa.setResizable(false);
		frmAaa.setAutoRequestFocus(false);
		frmAaa.setAlwaysOnTop(true);
		frmAaa.setFont(new Font("Times New Roman", Font.ITALIC, 18));
		frmAaa.setTitle("BomberGameAPI console UI v " + BomberBOTGameAPI.APIversion);
		frmAaa.setBounds(100, 100, 352, 312);
		frmAaa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnStopWhenThe = new JButton("Stop when this match finished");
		btnStopWhenThe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BomberBOTGameAPI.isContinue = false;
				frmAaa.dispose();
			}
		});
		btnStopWhenThe.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JTextArea txtrPlayer = new JTextArea();
		txtrPlayer.setFont(new Font("Times New Roman", Font.BOLD, 18));
		txtrPlayer.setText("Player: " + inputID);
		txtrPlayer.setLineWrap(true);
		txtrPlayer.setEditable(false);
		GroupLayout groupLayout = new GroupLayout(frmAaa.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnStopWhenThe, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
						.addComponent(txtrPlayer, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(txtrPlayer, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnStopWhenThe, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
					.addContainerGap())
		);
		frmAaa.getContentPane().setLayout(groupLayout);
		setWindowLocation(frmAaa);
	}
	private void setWindowLocation(JFrame inputFrame){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)(screenSize.getWidth()/2);
		int screenHeight = (int)(screenSize.getHeight()/2);
		inputFrame.setLocation(screenWidth - (inputFrame.getWidth()/2), screenHeight - (inputFrame.getHeight()/2));
		inputFrame.setFocusable(true);
	}
}
