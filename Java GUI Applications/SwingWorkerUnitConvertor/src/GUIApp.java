/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * GUI Class
 * Build main frame
 *
 */



// abstract window toolkit (awt)
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Cursor;

// awt events
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

// swing
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

// swing event
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

// swing tree
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

//Functionality of area
import area.CircleDialog;
import area.EllipseDialog;
import area.RectangleDialog;
import area.TriangleDialog;

//Functionality of astronomical distance
import astronomical.AstronomiDialog;
import astronomical.LightyearDialog;
import astronomical.ParsecDialog;

//Functionality of miscellaneous
import miscellaneous.FuelDialog;
import miscellaneous.WeightDialog;
import miscellaneous.ZodiacDialog;

//Functionality of number base
import numberBase.BinaryDialog;
import numberBase.DecimalDialog;
import numberBase.HexaDialog;
import numberBase.OctalDialog;

//Functionality of word count
import wordCount.DiffDialog;
import wordCount.LetterDialog;
import wordCount.TotalDialog;

public class GUIApp {

    private JFrame frame;
    
    private JPanel panel;
    private JScrollPane scrollPane;
    
    private JDesktopPane desktop;
    private JSplitPane splitPane;
    
    private JPanel labelPanel;
    private JLabel statusLabel;
    
    private JTree tree;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenuItem exitItem;
    private JMenuItem aboutItem;

    // constructor
    public GUIApp() {
        initComponents();
        statusLabel.setText("Initialization complete.");
    }

    private void exitActionPerformed() {
        frame.dispose();
    }

    private void aboutActionPerformed() {
        JOptionPane.showMessageDialog(null, "Thanks for using my app!");
    }

    //performance functions when clicking
    private void treeClicked() {
        
        // get the last selected tree node
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        tree.getLastSelectedPathComponent();

        // if the node is a leaf (no children, keep going)
        if (node != null && node.isLeaf()) {
            
            statusLabel.setText(node.toString() + " clicked.");
            
            if(node.toString().equals("Circle")) {
                CircleDialog cird = CircleDialog.getInstance();
                if(!cird.isVisible()) { 
                    cird.setVisible(true);
                    desktop.add(cird);               
                }                       
            }
            
            else if(node.toString().equals("Ellipse")) {
                EllipseDialog elld = EllipseDialog.getInstance();
                if(!elld.isVisible()) { 
                    elld.setVisible(true);           
                    desktop.add(elld);
                } 
            }
            
            else if(node.toString().equals("Rectangle")) {
                RectangleDialog recd = RectangleDialog.getInstance();
                if(!recd.isVisible()) { 
                    recd.setVisible(true);           
                    desktop.add(recd);
                } 
            }
            
            else if(node.toString().equals("Triangle")) {
                TriangleDialog trid = TriangleDialog.getInstance();
                if(!trid.isVisible()) { 
                    trid.setVisible(true);           
                    desktop.add(trid);
                } 
            }
            
            else if(node.toString().equals("Decimal")) {
                DecimalDialog decd = DecimalDialog.getInstance();
                if(!decd.isVisible()) { 
                    decd.setVisible(true);           
                    desktop.add(decd);
                } 
            }
 
            else if(node.toString().equals("Binary")) {
                BinaryDialog bind = BinaryDialog.getInstance();
                if(!bind.isVisible()) { 
                    bind.setVisible(true);           
                    desktop.add(bind);
                } 
            }
            
            else if(node.toString().equals("Octal")) {
                OctalDialog octd = OctalDialog.getInstance();
                if(!octd.isVisible()) { 
                    octd.setVisible(true);           
                    desktop.add(octd);
                } 
            }
            
            else if(node.toString().equals("Hexadecimal")) {
                HexaDialog hexd = HexaDialog.getInstance();
                if(!hexd.isVisible()) { 
                    hexd.setVisible(true);           
                    desktop.add(hexd);
                } 
            }
            
            else if(node.toString().equals("Lightyear")) {
                LightyearDialog ligd = LightyearDialog.getInstance();
                if(!ligd.isVisible()) { 
                    ligd.setVisible(true);           
                    desktop.add(ligd);
                } 
            }
            
            else if(node.toString().equals("Astronomical Unit")) {
                AstronomiDialog astd = AstronomiDialog.getInstance();
                if(!astd.isVisible()) { 
                    astd.setVisible(true);           
                    desktop.add(astd);
                } 
            }
            
            else if(node.toString().equals("Parsec")) {
                ParsecDialog pard = ParsecDialog.getInstance();
                if(!pard.isVisible()) { 
                    pard.setVisible(true);           
                    desktop.add(pard);
                } 
            }
            
            else if(node.toString().equals("Different Words")) {
                DiffDialog difd = DiffDialog.getInstance(frame);
                if(!difd.isVisible()) { 
                    difd.setVisible(true);           
                    desktop.add(difd);
                } 
            }
            
            else if(node.toString().equals("Total Words")) {
                TotalDialog totd = TotalDialog.getInstance(frame);
                if(!totd.isVisible()) { 
                    totd.setVisible(true);           
                    desktop.add(totd);
                } 
            }
            
            else if(node.toString().equals("Different Start Letters")) {
                LetterDialog letd = LetterDialog.getInstance(frame);
                if(!letd.isVisible()) { 
                    letd.setVisible(true);           
                    desktop.add(letd);
                } 
            }
            
            else if(node.toString().equals("Fuel Economy")) {
                FuelDialog fued = FuelDialog.getInstance();
                if(!fued.isVisible()) { 
                    fued.setVisible(true);           
                    desktop.add(fued);
                } 
            }
            
            else if(node.toString().equals("Chinese Zodiac")) {
                ZodiacDialog zodd = ZodiacDialog.getInstance();
                if(!zodd.isVisible()) { 
                    zodd.setVisible(true);           
                    desktop.add(zodd);
                } 
            }
            
            else if(node.toString().equals("Weight On Other Planets")) {
                WeightDialog weid = WeightDialog.getInstance();
                if(!weid.isVisible()) { 
                    weid.setVisible(true);           
                    desktop.add(weid);
                } 
            }

        } // end if isLeaf

    } // end treeClicked

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //build desktop
    private void buildDesktop() {

        desktop = new JDesktopPane()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                ImageIcon icon = new ImageIcon("images/csun_logo.png");
                Image image = icon.getImage();

                int x=0, y=0;
                double imageWidth = image.getWidth(null);
                double imageHeight = image.getHeight(null);
                double screenWidth = getWidth();
                double screenHeight = getHeight();

                if(screenWidth != 0) {
                    x = (int)screenWidth  / 2 - (int)imageWidth  / 2;
                }
                
                if(screenHeight != 0) {
                    y = (int)screenHeight / 2 - (int)imageHeight / 2;
                }
                
                g.drawImage(image, x, y, this);
            }  
        };

    } // end buildDesktop
    
    //build tree
    private void buildTree() {

        // Create data for the tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tools");

        DefaultMutableTreeNode area = new DefaultMutableTreeNode("Areas");
        
        DefaultMutableTreeNode circle = new DefaultMutableTreeNode("Circle");
        DefaultMutableTreeNode ellipse = new DefaultMutableTreeNode("Ellipse");
        DefaultMutableTreeNode rectangle = new DefaultMutableTreeNode("Rectangle");
        DefaultMutableTreeNode triangle = new DefaultMutableTreeNode("Triangle");
        
        area.add(circle);
        area.add(ellipse);
        area.add(rectangle);
        area.add(triangle);
        
        DefaultMutableTreeNode base = new DefaultMutableTreeNode("Number Base");
        
        DefaultMutableTreeNode dec = new DefaultMutableTreeNode("Decimal");
        DefaultMutableTreeNode bin = new DefaultMutableTreeNode("Binary");
        DefaultMutableTreeNode oct = new DefaultMutableTreeNode("Octal");
        DefaultMutableTreeNode hex = new DefaultMutableTreeNode("Hexadecimal");
        
        base.add(dec);
        base.add(bin);
        base.add(oct);
        base.add(hex);
        
        DefaultMutableTreeNode astr = new DefaultMutableTreeNode("Astronomical Distance");
        
        DefaultMutableTreeNode lytokm = new DefaultMutableTreeNode("Lightyear");
        DefaultMutableTreeNode autokm = new DefaultMutableTreeNode("Astronomical Unit");
        DefaultMutableTreeNode partokm= new DefaultMutableTreeNode("Parsec");
        
        astr.add(lytokm);
        astr.add(autokm);
        astr.add(partokm);
        
        DefaultMutableTreeNode misc = new DefaultMutableTreeNode("Miscellaneous");
        
        DefaultMutableTreeNode fuel = new DefaultMutableTreeNode("Fuel Economy");
        DefaultMutableTreeNode czod = new DefaultMutableTreeNode("Chinese Zodiac");
        DefaultMutableTreeNode weig = new DefaultMutableTreeNode("Weight On Other Planets");

        misc.add(fuel);
        misc.add(czod);
        misc.add(weig);
        
        DefaultMutableTreeNode wcou = new DefaultMutableTreeNode("Word Count");
        
        DefaultMutableTreeNode total = new DefaultMutableTreeNode("Total Words");
        DefaultMutableTreeNode diff = new DefaultMutableTreeNode("Different Words");
        DefaultMutableTreeNode letter = new DefaultMutableTreeNode("Different Start Letters");
        
        wcou.add(total);
        wcou.add(diff);
        wcou.add(letter);
        
        root.add(area);
        root.add(base);
        root.add(astr);
        root.add(misc);
        root.add(wcou);

        // create a new tree
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);

    } // buildTree

    private void addTreeListeners() {

        tree.addMouseMotionListener(
        new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                ((JTree)e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseMoved(MouseEvent e) {              
                TreePath pathForLocation = tree.getPathForLocation(e.getX(), e.getY());
                if(pathForLocation != null) {
                    Object lastPathComponent = pathForLocation.getLastPathComponent();
                    if(lastPathComponent instanceof DefaultMutableTreeNode) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)lastPathComponent;
                        if(node.isLeaf()) {
                            ((JTree)e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                        else {
                            ((JTree)e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("clicked!");               
            }                       
        }
        ); 

        tree.addMouseListener(
        new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    treeClicked();
                }          
            }                       
        }
        );
    } // addTreeListeners

    private void buildMenu() {

        // build menu
        menuBar = new JMenuBar();      
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        exitItem = new JMenuItem("Exit");
        aboutItem = new JMenuItem("About");
        fileMenu.add(exitItem);
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

    } // end buildMenu

    private void addMenuListeners() {

        // add listener for exit menu item
        exitItem.addActionListener(
        new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitActionPerformed();
            }
        }
        );
        
        // add listener for about menu item
        aboutItem.addActionListener(
        new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutActionPerformed();
            }
        }
        );

    } // end addMenuListeners

    private void buildPanel() {

        panel = new JPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        scrollPane = new JScrollPane();
        labelPanel = new JPanel();      
        statusLabel = new JLabel();

        scrollPane.getViewport().add(tree);

        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setMinimumSize(new Dimension(0,18));
        statusLabel.setPreferredSize(new Dimension(0,18));
        
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        splitPane.setContinuousLayout(true);           
        splitPane.add(desktop, JSplitPane.RIGHT);
        splitPane.add(scrollPane, JSplitPane.LEFT);     

        panel.setLayout(new BorderLayout());
        panel.add(splitPane, BorderLayout.CENTER);

        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(statusLabel, BorderLayout.CENTER);

    } // end buildPanel

    private void buildFrame() {

        // create a new frame and show it
        frame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("My GUI Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());      
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/csun.gif"));
        // add label panel
        frame.getContentPane().add(labelPanel, BorderLayout.SOUTH);
        // add main panel
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        // add menu bar
        frame.setJMenuBar(menuBar);
        frame.setSize(1280,800);
        frame.setVisible(true);

    }

    // create a GUI and make it visible to user
    private void initComponents() {
        setLookAndFeel();               
        buildDesktop();        
        buildTree();
        addTreeListeners();        
        buildMenu();             
        addMenuListeners();
        buildPanel();
        buildFrame();        
    } // end initComponents

} // end MyFirstGUIApp
