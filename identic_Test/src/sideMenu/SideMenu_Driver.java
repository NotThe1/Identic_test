package sideMenu;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;

import identic.ManageLists;

public class SideMenu_Driver {

	// SideMenu sm = new SideMenu();
	private DefaultListModel<String> targetModel = new DefaultListModel<>();
	private ArrayList<String> targetSuffixes = new ArrayList<>();
	private ArrayDeque<Path> subjects = new ArrayDeque<Path>();
	private ArrayDeque<Path> rejects = new ArrayDeque<Path>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SideMenu_Driver window = new SideMenu_Driver();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	private void doSourceDirectory() {
		JFileChooser fc = new JFileChooser(lblStatus.getText());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Select Starting Directory");
		fc.setApproveButtonText("Select");

		if (fc.showOpenDialog(frmTemplate) == JFileChooser.APPROVE_OPTION) {
			lblStatus.setText(fc.getSelectedFile().getAbsolutePath());
		} // if
	}// doSourceDirectory

	/* Standard Stuff */

	private void doBtnOne() {
		subjects.clear();
		rejects.clear();
		Path p = Paths.get("C:\\Users\\admin\\AppData\\Local\\Identic\\Test.typeList");
		try {
			targetSuffixes = (ArrayList<String>) Files.readAllLines(p);
		} catch (IOException e) {
			e.printStackTrace();
		} // try targetModel
		targetModel.removeAllElements();
		for (String line : targetSuffixes) {
			targetModel.addElement(line);
		} // for
		
		txtLog.setText("");
		MyWalker myWalker = new MyWalker();
		Path start = Paths.get(lblStatus.getText());
		try {
			Files.walkFileTree(start, myWalker);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
		txtLog.setCaretPosition(0);


	}// doBtnOne

	private void doBtnTwo() {
		subjects.clear();
		rejects.clear();
		Path p = Paths.get("C:\\Users\\admin\\AppData\\Local\\Identic\\Test.typeList");
		try {
			targetSuffixes = (ArrayList<String>) Files.readAllLines(p);
		} catch (IOException e) {
			e.printStackTrace();
		} // try targetModel
		
		Path startPath= Paths.get(lblStatus.getText());
		IdentifySubjects identifySubjects = new IdentifySubjects(subjects,rejects,startPath,targetSuffixes);
		Thread threadIdentify = new Thread(identifySubjects);
		threadIdentify.start();
		
		MakeFileKey showSubjects = new MakeFileKey(threadIdentify,subjects,txtLog);
		Thread threadShow = new Thread(showSubjects);
		threadShow.start();
		
		ShowRejects showRejects =new ShowRejects(threadIdentify,rejects,txtLog);
		Thread threadRejects = new Thread(showRejects);
		threadRejects.start();
		
		try{
			threadIdentify.join();
			threadShow.join();
			threadRejects.join();
		}catch (InterruptedException e){
			e.printStackTrace();
		}//try
		
//		System.err.printf("subject count = %d%n",subjects.size());
//		Path p1 = rejects.remove();
//		txtLog.append(String.format("!!!! --- %s%n%n",p1.toString()));
//		System.err.printf(" reject count = %d%n",rejects.size());
		

	}// doBtnTwo

	private void doBtnThree() {
		ManageLists ml = new ManageLists();
		 ml.showDialog();
		ml = null;
	}// doBtnThree

	private void doBtnFour() {
		AppLogger appLogger = AppLogger.getInstance();
		appLogger.setDoc(txtApplicationLogger.getStyledDocument());
		
		appLogger.addInfo("My Info Message1","info message2");
		appLogger.addNL();
		appLogger.addWarning("My Warning Message");
		appLogger.addNL(5);
		appLogger.addError("My Error Message");
		appLogger.addSpecial("My Special Message");
	}// doBtnFour

	// ---------------------------------------------------------

	private void doFileNew() {

	}// doFileNew

	private void doFileOpen() {
		doSourceDirectory();
	}// doFileOpen

	private void doFileSave() {

	}// doFileSave

	private void doFileSaveAs() {

	}// doFileSaveAs

	private void doFilePrint() {

	}// doFilePrint

	private void doFileExit() {
		appClose();
		System.exit(0);
	}// doFileExit

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(SideMenu_Driver.class)
				.node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs.put("SourceDirectory", lblStatus.getText());

		myPrefs = null;

	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(SideMenu_Driver.class)
				.node(this.getClass().getSimpleName());
		frmTemplate.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		txtLog.append(String.format("myPrefs.absolutePath() - %s%n", myPrefs.absolutePath()));
		lblStatus.setText(myPrefs.get("SourceDirectory", "NOT_SET"));

		myPrefs = null;

		// GridBagConstraints gbc_sideMenu = new GridBagConstraints();
		// gbc_sideMenu.fill = GridBagConstraints.BOTH;
		// gbc_sideMenu.insets = new Insets(0, 0, 0, 5);
		// gbc_sideMenu.gridx = 0;
		// gbc_sideMenu.gridy = 0;
		// panelLeft.add(sm,gbc_sideMenu);
		//
		// buttons = new JButton[] { btnFindDups, btnDisplayResults, btnFindDuplicatesByName, btnCopyMoveRemove,
		// btnFileTypes };

	}// appInit

	public SideMenu_Driver() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplate = new JFrame();
		frmTemplate.setTitle("SideMenu_Driver");
		frmTemplate.setBounds(100, 100, 450, 300);
		frmTemplate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTemplate.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmTemplate.getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmTemplate.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnOne = new JButton("Button 1");
		btnOne.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.insets = new Insets(0, 0, 0, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 0;
		panel.add(btnOne, gbc_btnOne);
		btnOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doBtnOne();
			}
		});
		btnOne.setMaximumSize(new Dimension(0, 0));
		btnOne.setPreferredSize(new Dimension(100, 20));

		btnTwo = new JButton("Button 2");
		btnTwo.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.insets = new Insets(0, 0, 0, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 0;
		panel.add(btnTwo, gbc_btnTwo);
		btnTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnTwo();
			}
		});
		btnTwo.setPreferredSize(new Dimension(100, 20));
		btnTwo.setMaximumSize(new Dimension(0, 0));

		btnThree = new JButton("Button 3");
		btnThree.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.insets = new Insets(0, 0, 0, 5);
		gbc_btnThree.gridx = 2;
		gbc_btnThree.gridy = 0;
		panel.add(btnThree, gbc_btnThree);
		btnThree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnThree();
			}
		});
		btnThree.setPreferredSize(new Dimension(100, 20));
		btnThree.setMaximumSize(new Dimension(0, 0));

		btnFour = new JButton("Button 4");
		btnFour.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.anchor = GridBagConstraints.NORTH;
		gbc_btnFour.gridx = 3;
		gbc_btnFour.gridy = 0;
		panel.add(btnFour, gbc_btnFour);
		btnFour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnFour();
			}
		});
		btnFour.setPreferredSize(new Dimension(100, 20));
		btnFour.setMaximumSize(new Dimension(0, 0));

		splitPane1 = new JSplitPane();
		GridBagConstraints gbc_splitPane1 = new GridBagConstraints();
		gbc_splitPane1.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane1.fill = GridBagConstraints.BOTH;
		gbc_splitPane1.gridx = 0;
		gbc_splitPane1.gridy = 1;
		frmTemplate.getContentPane().add(splitPane1, gbc_splitPane1);

		panelLeft = new JPanel();
		splitPane1.setLeftComponent(panelLeft);
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));

		btnFindDups = new JButton("Find Duplicates");
		btnFindDups.setMaximumSize(new Dimension(5000, 23));
		panelLeft.add(btnFindDups);

		btnFindDuplicatesByName = new JButton("Find Duplicates By Name");
		btnFindDuplicatesByName.setMaximumSize(new Dimension(5000, 23));
		panelLeft.add(btnFindDuplicatesByName);

		btnDisplayResults = new JButton("Display Results");
		btnDisplayResults.setMaximumSize(new Dimension(5000, 23));
		panelLeft.add(btnDisplayResults);

		btnCopyMoveRemove = new JButton("Copy Move Remove");
		btnCopyMoveRemove.setMaximumSize(new Dimension(5000, 23));
		panelLeft.add(btnCopyMoveRemove);

		btnFileTypes = new JButton("File Types");
		btnFileTypes.setMaximumSize(new Dimension(5000, 23));
		panelLeft.add(btnFileTypes);

		panelSideMenu = new JPanel();
		panelSideMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelSideMenu.setLayout(new CardLayout(0, 0));
		panelLeft.add(panelSideMenu);

		panel_1 = new JPanel();
		panelSideMenu.add(panel_1, "name_662700243475896");
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		lblNewLabel = new JLabel("label 1");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTH;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);

		panel_2 = new JPanel();
		panelSideMenu.add(panel_2, "name_662709728950278");
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		lblLabel = new JLabel("label 2");
		lblLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 0;
		panel_2.add(lblLabel, gbc_lblLabel);

		panel_3 = new JPanel();
		panelSideMenu.add(panel_3, "name_662727033045683");
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 0, 0 };
		gbl_panel_3.rowHeights = new int[] { 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		lblLabel_1 = new JLabel("label 3");
		lblLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblLabel_1 = new GridBagConstraints();
		gbc_lblLabel_1.gridx = 0;
		gbc_lblLabel_1.gridy = 0;
		panel_3.add(lblLabel_1, gbc_lblLabel_1);

		panel_4 = new JPanel();
		panelSideMenu.add(panel_4, "name_662730520649195");
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 0, 0 };
		gbl_panel_4.rowHeights = new int[] { 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		lblLabel_2 = new JLabel("label 4");
		lblLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblLabel_2 = new GridBagConstraints();
		gbc_lblLabel_2.gridx = 0;
		gbc_lblLabel_2.gridy = 0;
		panel_4.add(lblLabel_2, gbc_lblLabel_2);

		panel_5 = new JPanel();
		panelSideMenu.add(panel_5, "name_662733834070285");
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] { 0, 0 };
		gbl_panel_5.rowHeights = new int[] { 0, 0 };
		gbl_panel_5.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_5.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_5.setLayout(gbl_panel_5);

		lblLabel_3 = new JLabel("label 5");
		lblLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblLabel_3 = new GridBagConstraints();
		gbc_lblLabel_3.gridx = 0;
		gbc_lblLabel_3.gridy = 0;
		panel_5.add(lblLabel_3, gbc_lblLabel_3);

		JPanel panelRight = new JPanel();
		splitPane1.setRightComponent(panelRight);
		GridBagLayout gbl_panelRight = new GridBagLayout();
		gbl_panelRight.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelRight.rowHeights = new int[] { 0, 0 };
		gbl_panelRight.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panelRight.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelRight.setLayout(gbl_panelRight);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelRight.add(scrollPane, gbc_scrollPane);

		txtLog = new JTextArea();
		txtLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() > 1) {
					((JTextComponent) arg0.getComponent()).setText("");
					txtLog.setText("");
				} // if
			}// mouseClicked
		});
		scrollPane.setViewportView(txtLog);

		lblLog = new JLabel("New label");
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblLog);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panelRight.add(scrollPane_1, gbc_scrollPane_1);
		
		lblNewLabel_1 = new JLabel("Logger");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblNewLabel_1);
		
		txtApplicationLogger = new JTextPane();
		scrollPane_1.setViewportView(txtApplicationLogger);
		splitPane1.setDividerLocation(250);

		JPanel panelStatus = new JPanel();
		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 2;
		frmTemplate.getContentPane().add(panelStatus, gbc_panelStatus);

		lblStatus = new JLabel("New label");
		panelStatus.add(lblStatus);

		JMenuBar menuBar = new JMenuBar();
		frmTemplate.setJMenuBar(menuBar);

		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);

		JMenuItem mnuFileNew = new JMenuItem("New");
		mnuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileNew();
			}
		});
		mnuFile.add(mnuFileNew);

		JMenuItem mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileOpen();
			}
		});
		mnuFile.add(mnuFileOpen);

		JSeparator separator = new JSeparator();
		mnuFile.add(separator);

		JMenuItem mnuFileSave = new JMenuItem("Save...");
		mnuFileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSave();
			}
		});
		mnuFile.add(mnuFileSave);

		JMenuItem mnuFileSaveAs = new JMenuItem("Save As...");
		mnuFileSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSaveAs();
			}
		});
		mnuFile.add(mnuFileSaveAs);

		JSeparator separator_2 = new JSeparator();
		mnuFile.add(separator_2);

		JMenuItem mnuFilePrint = new JMenuItem("Print...");
		mnuFilePrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFilePrint();
			}
		});
		mnuFile.add(mnuFilePrint);

		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);

		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileExit();
			}
		});
		mnuFile.add(mnuFileExit);

	}// initialize

	class MyWalker implements FileVisitor<Path> {

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			txtLog.append("                    - " + dir.toString() + System.lineSeparator());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String fileName = file.getFileName().toString();
			int partsCount;
			String indicator = null;
			String part = null;
			String[] parts = fileName.split("\\.");
			partsCount = parts.length;
			if (partsCount > 1) {
				part = parts[partsCount-1].toUpperCase();
				if ( targetModel.contains(part)){
					subjects.add(file);
					indicator = "++ ";
				}else{
					rejects.add(file);
					indicator = "---- ";
				}
				txtLog.append(indicator + part + " -> ");
				txtLog.append(fileName + System.lineSeparator());
				// txtLog.append(suffix + " -> ");
				// txtLog.append(file.toString()+ System.lineSeparator());
			} // if
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			// TODO Auto-generated method stub
			return FileVisitResult.CONTINUE;
		}

	}//

	private JFrame frmTemplate;
	private JButton btnOne;
	private JButton btnTwo;
	private JButton btnThree;
	private JButton btnFour;
	private JSplitPane splitPane1;
	private JTextArea txtLog;
	private JLabel lblLog;
	private JPanel panelLeft;
	private JButton btnFindDups;
	private JButton btnDisplayResults;
	private JButton btnFindDuplicatesByName;
	private JButton btnCopyMoveRemove;
	private JButton btnFileTypes;
	private JPanel panelSideMenu;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JLabel lblNewLabel;
	private JLabel lblLabel;
	private JLabel lblLabel_1;
	private JLabel lblLabel_2;
	private JLabel lblLabel_3;
	private JLabel lblStatus;
	private JScrollPane scrollPane_1;
	private JLabel lblNewLabel_1;
	private JTextPane txtApplicationLogger;

}// class GUItemplate