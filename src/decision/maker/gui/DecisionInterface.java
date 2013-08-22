package decision.maker.gui;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import decision.maker.DecisionMaker;



/**
 * This class displays a GUI whenever an user asks for
 * stand alone Decision tools. It allows to find the best
 * argument within a list of arguments and according to a
 * customisable agent, or the best offer (after setting up
 * another customisable agent).
 * 
 * 
 */
public class DecisionInterface extends JPanel implements WindowListener {
    
	// Decision instance :
	//private DecisionMaker decisionTools = new DecisionMaker(new ArrayList<String>());
	// Initial coordinates of the first window (static) :
	private static int Y = 73;
	private static int X = 139;
	
	private JFrame frame;
	
	// BUILDER :
	public DecisionInterface() {
        
		super(new GridLayout(1,2));
		
		frame = new JFrame("Decision Maker v0.1");
		frame.addWindowListener(this);
		
		// Main panel (DecisionPrompt and Computer) :
		JPanel pan = new JPanel();
		// ToolBar panel :
		JPanel tbLine = new JPanel();
		
		// We use BoxLayout for the both :
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		tbLine.setLayout(new BoxLayout(tbLine,BoxLayout.LINE_AXIS));

        // The tabbed pane of the Main panel :
		JTabbedPane tabbedPane = new JTabbedPane();

        // The Best Offer panel (first tab of the tabbed panel) :
		// First we get an instance of the DecisionPanel :
		//DecisionPanel bestOff = new DecisionPanel(decisionTools);
		DecisionPanel bestOff = new DecisionPanel();
		// And then we extract the panel from it :
		JPanel panelBestOffer = bestOff.getPan();

        // The toolbar :
		JToolBar toolBar = new DecisionToolBar(bestOff);
        toolBar.setLayout(new BoxLayout(toolBar,BoxLayout.LINE_AXIS));
        toolBar.setFloatable(false);
		
        // Adding the Best Offer tab :
        tabbedPane.addTab("Best Decision Finder", panelBestOffer);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        // Adding the toolbar to its panel :
        tbLine.add(toolBar);
        tbLine.add(Box.createHorizontalGlue());
        // Adding the toolbar panel to the main panel : 
        pan.add(tbLine);
        pan.add(tabbedPane);
        // Adding the main panel to the Frame :
        add(pan);

        // If needed, please add a windowListener here :
        // ...
        
        //Uncomment the following line to use scrolling tabs.
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(DecisionInterface decInterf) {
        
    	//Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(false);

        //Create and set up the window.
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        
        DecisionInterface newContentPane = new DecisionInterface();
        newContentPane.setOpaque(true); //content panes must be opaque
        decInterf.frame.getContentPane().add(newContentPane, BorderLayout.CENTER);
        decInterf.frame.setPreferredSize(new Dimension(600,721)); // Gold number :D !
        
        //decInterf.frame.setPreferredSize(new Dimension(700,721)); // Gold number :D !
        
        decInterf.frame.setResizable(false);

        //Display the window.
        decInterf.frame.pack();
        // Locate the window :
        decInterf.frame.setLocation(X,Y);
        // Change the location for the next window :
        Y=Math.min(Y+30,283);
        X=Math.min(X+30,589);
        // Set visible :
        decInterf.frame.setVisible(true);
    }

    public static void main(String[] args) {
    	// Incrementing the windows counter :
    	//MainInterface.createWindow();
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(new DecisionInterface());
            }
        });
    }

    // WINDOW LISTENER METHODS :
    // To change if using a WindowListener :).
    
    public void windowOpened(WindowEvent e) {
    	
    }
    
    public void windowDeactivated(WindowEvent e) {
    	
    }
    
    public void windowActivated(WindowEvent e) {
    	
    }
    
    public void windowDeiconified(WindowEvent e) {
    	
    }
    
    public void windowIconified(WindowEvent e) {
    	
    }
    
    /*public void windowClosing(WindowEvent e) {
    	MainInterface.closeWindow();
    	frame.dispose();
    }*/
    
    public void windowClosed(WindowEvent e) {
    	
    }


	@Override
	public void windowClosing(WindowEvent arg0) {
		//frame.dispose();
		if (JOptionPane.showConfirmDialog(new JPanel(),// so that the position is nice
    			"This command will terminate the Decision Maker.\n"+
    			"All Unsaved work will be lost.\n"+
    			"Do you really want to proceed ?",
    			"Close",
    			JOptionPane.YES_NO_OPTION,
    			JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) 
    	{
    		System.exit(0);
    	}
		
	}

}
