package sideMenu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class SideMenu extends JPanel {
	
	private JButton[] buttons;	
	
	
	public void pickButton(int buttonNumber){
		this.removeAll();
		for ( int i = 0;i < buttons.length;i++){
			this.add(buttons[i]);
			if (i == buttonNumber){
				this.add(panelMain);
			}//if 
		}//for
		this.validate();
		//this.repaint();
	}//pickButton
	
	
	private void appInit(){
		 buttons = new JButton[]{btn1,btn2,btn3,btn4,btn5,btn6};
	}// appInit
	
	public SideMenu() {
		initialize();
		appInit();
	}//Constructor
	
	private void initialize(){
		setBorder(UIManager.getBorder("InternalFrame.border"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		btn1 = new JButton("Button 1");
		btn1.setMaximumSize(new Dimension(500, 23));
		btn1.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btn1);
		
		btn2 = new JButton("Button 2");
		btn2.setMaximumSize(new Dimension(500, 23));
		btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btn2);
		
		btn3 = new JButton("Button 3");
		btn3.setMaximumSize(new Dimension(500, 23));
		btn3.setAlignmentX(0.5f);
		add(btn3);
		
		panelMain = new JPanel();
		panelMain.setBackground(Color.DARK_GRAY);
		add(panelMain);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[]{0};
		gbl_panelMain.rowHeights = new int[]{0};
		gbl_panelMain.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panelMain.rowWeights = new double[]{Double.MIN_VALUE};
		panelMain.setLayout(gbl_panelMain);
		
		btn4 = new JButton("Button 4");
		btn4.setMaximumSize(new Dimension(500, 23));
		btn4.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btn4);
		
		btn5 = new JButton("Button 5");
		btn5.setMaximumSize(new Dimension(500, 23));
		btn5.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btn5);
		
		btn6 = new JButton("Button 6");
		btn6.setMaximumSize(new Dimension(500, 23));
		btn6.setAlignmentX(0.5f);
		add(btn6);
	}//initialize
	
	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JPanel panelMain;
	private JButton btn4;
	private JButton btn5;
	private JButton btn6;



}//class SideMenu
