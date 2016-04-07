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

public class APIConsoleUI{

	private JFrame frmAaa;
	private JLabel label;
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
	public void setRounds(int inputRound){
		label.setText("Play: " + inputRound + " round" + (inputRound == 0 ? "" : "s"));
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
		frmAaa.setTitle("BomberGameAPI console UI v " + BomberBOTGameAPI.APIversion);
		frmAaa.setBounds(100, 100, 384, 191);
		frmAaa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnStopWhenThe = new JButton("Stop when this match finished");
		btnStopWhenThe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BomberBOTGameAPI.isContinue = false;
				frmAaa.dispose();
			}
		});
		btnStopWhenThe.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblNewLabel = new JLabel("Player: " + inputID);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		label = new JLabel("Play: 0 round");
		label.setFont(new Font("Times New Roman", Font.BOLD, 18));
		GroupLayout groupLayout = new GroupLayout(frmAaa.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStopWhenThe, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnStopWhenThe, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(54, Short.MAX_VALUE))
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
