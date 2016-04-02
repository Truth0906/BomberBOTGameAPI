package APIObjectStructure;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import BomberGameBOTAPI.BomberGameBOTAPI;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class APIConsoleUI{

	private JFrame frmAaa;

	/**
	 * Launch the application.
	 */
	public static void showUI(final String inputID){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					APIConsoleUI window = new APIConsoleUI(inputID);
					window.frmAaa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		frmAaa.setAutoRequestFocus(false);
		frmAaa.setAlwaysOnTop(true);
		frmAaa.setResizable(false);
		frmAaa.setFont(new Font("Times New Roman", Font.ITALIC, 18));
		frmAaa.setTitle("BomberGameAPI console UI v " + BomberGameBOTAPI.APIversion);
		frmAaa.setBounds(100, 100, 384, 145);
		frmAaa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnStopWhenThe = new JButton("Stop when this match finished");
		btnStopWhenThe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BomberGameBOTAPI.isContinue = false;
			}
		});
		btnStopWhenThe.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblNewLabel = new JLabel("Player: " + inputID);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		GroupLayout groupLayout = new GroupLayout(frmAaa.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
						.addComponent(btnStopWhenThe, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnStopWhenThe, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(128, Short.MAX_VALUE))
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
