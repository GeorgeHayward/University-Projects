package simplenim;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;



	public class EasyPuzzleGUI extends JFrame
	  implements ActionListener
	{
   	private static final long serialVersionUID = 1L;
	  int sizeOfPile = 5; //Initial size of cat stack.
	  private JPanel mainView; //This is the container for all other panels.
	  private JPanel LeftPanel; //Left pile of cats.
	  private JPanel MiddlePanel; //Middle pile of cats.
	  private JPanel RightPanel; //Right pile of cats.
	  private JPanel sidePanel; //Print Field Panel + Undo / Redo Buttons holder.
	  private JButton buttonU; //Button to undo last move.
	  private JButton buttonR; //Button to redo a move.
	  private JButton leftButton; //Left button variable.
	  private JButton middleButton; //Middle button variable.
	  private JButton rightButton; //Right button variable
	  public JLabel l; //JLabel?
	  private Image source; //A variable to hold the image of the cat.
	  int width; //Variable holding the width of the image.
	  int height; //Variable holding the height of the image.
	  PanelButtons pButtonsLeft; //Create a panel of buttons which will hold the left stack of buttons.
	  PanelButtons pButtonsMiddle; //Create a panel of buttons which will hold the left stack of buttons.
	  PanelButtons pButtonsRight; //Create a panel of buttons which will hold the left stack of buttons.
	  JTextArea fTextArea = null; //Create the empty text area which will later hold the players turn and messages.
	  String player = "white"; //Always start as white, so therefore make the player name string white by default.
	  static int endLeft; //A variable which will later hold the value of the left pile of buttons.
	  static int endMiddle; //A variable which will later hold the value of the left pile of buttons.
	  static int endRight; //A variable which will later hold the value of the left pile of buttons.

	  //Returns an end value as a string.
	  static String getChoice(char x)//Input either l,m or r to return the proper end choice.
	  {
		if(x == 'l'){
	    return "" + endLeft;
		}
		if(x =='m'){
		return "" + endMiddle;
		}
		if(x == 'r'){
		return "" + endRight;
		}
		else return "No pile choice input"; //No input char.
	  }

	  //GUI Constructor.
	  public EasyPuzzleGUI()
	  {
		//MAIN VIEW
		this.mainView = new JPanel(); //Instantiate the main view window.
		this.mainView.setLayout(new GridLayout(1, 0, 0, 0)); //Sets the layout of the objects in the mainView panel. (int rows, int cols, int hgap, int vgap) 

		//SIDE PANEL
	    this.sidePanel = new JPanel(); //Instantiates the side panel which hold the players turn and undo / redo buttons.

	    //UNDO BUTTON
	    this.buttonU = new JButton("Undo"); //Instantiates the Undo button.
	    this.buttonU.addActionListener(this); //Add an action to the Undo button.
	    this.sidePanel.add(this.buttonU); //Add Undo button to the sidepanel.

	    //REDO BUTTON
	    this.buttonR = new JButton("Redo");//Instantiates the Redo button.
	    this.buttonR.addActionListener(this);//Add an action to the Redo button.
	    this.sidePanel.add(this.buttonR);//Add Redo button to the sidepanel.

	    //Instantiate the arrays of buttons.
	    this.pButtonsLeft = new PanelButtons();
	    this.pButtonsMiddle = new PanelButtons();
	    this.pButtonsRight = new PanelButtons();
	    
	    //Button Panels
	    this.LeftPanel = new JPanel();//Instantiate the left panel.
	    this.MiddlePanel = new JPanel();//Instantiate the middle panel.
	    this.RightPanel = new JPanel();//Instantiate the right panel.

	    //Creates a variable to hold the cat(button) image.
	    ImageIcon localImageIcon = new ImageIcon("cat.jpg", "cat");
	    this.source = localImageIcon.getImage();  //Makes the source variable the image that was imported. (localimageicon)
	    this.width = localImageIcon.getIconWidth(); //Makes the width variable the width of the image that was imported.
	    this.height = localImageIcon.getIconHeight(); //Makes the height variable the height of the image that was imported.
	    
	    //Creates the type of box this container will be.
	    add(Box.createRigidArea(new Dimension(2, 0)), "South");
	    //Makes the left panel centered.
	    add(this.LeftPanel, "Center");
	    add(this.MiddlePanel, "Center");
	    add(this.RightPanel, "Center");
	    
	    //Add Left Pile
	    for (int i = 0; i < this.sizeOfPile + 1; i++) { //Goes through the entire size of the Size of the left pile.
	      if (i == this.sizeOfPile) { //If the loop is at its last item.
	        this.fTextArea = new JTextArea();//Instantiate the text area which will show the games status.
	        this.fTextArea.setEditable(false);
	        this.fTextArea.setText("" + this.player + "to start"); //Show which player to start.
	        this.sidePanel.add(this.fTextArea); //Adds the text area to the side panel.
	      } else {
	        this.leftButton = new JButton(localImageIcon); //Makes the left button be presented with the local image icon. Whilst creating it.
	        this.leftButton.addActionListener(this); //Adds an action listen to the left button.
	        this.LeftPanel.add(this.leftButton, -1); //Adds the left button to the end of the pile in the panel
	        this.pButtonsLeft.putButton(this.leftButton); //Put the left button on the top of the array.
	      }
	    }
	    
	    //Add Middle Pile
	    for (int i = 0; i < this.sizeOfPile; i++) { //Goes through the entire size of the Size of the middle pile.
		        this.middleButton = new JButton(localImageIcon); //Makes the middle button be presented with the local image icon. Whilst creating it.
		        this.middleButton.addActionListener(this); //Adds an action listen to the middle button.
		        this.MiddlePanel.add(this.middleButton, -1); //Adds the middle button to the end of the pile in the panel
		        this.pButtonsMiddle.putButton(this.middleButton); //Put the middle button on the top of the array.
		      }
		    
	    
	    //Add Right Pile
	    for (int i = 0; i < this.sizeOfPile; i++) { //Goes through the entire size of the Size of the right pile.
		        this.rightButton = new JButton(localImageIcon); //Makes the right button be presented with the local image icon. Whilst creating it.
		        this.rightButton.addActionListener(this); //Adds an action listen to the left button.
		        this.RightPanel.add(this.rightButton, -1); //Adds the right button to the end of the pile in the panel
		        this.pButtonsRight.putButton(this.rightButton); //Put the right button on the top of the array.
		    }
	    
	    //Adds all panels to main view.
	    this.mainView.add(this.LeftPanel);
	    this.mainView.add(this.MiddlePanel);
	    this.mainView.add(this.RightPanel);
	    this.mainView.add(this.sidePanel);

	    //Sets the area type 
	    add(Box.createRigidArea(new Dimension(0, 1)), "South");
	    add(this.mainView, "Center");
	    
	    //WINDOW
	    setSize(600, 100 * this.sizeOfPile);//Makes the size of the window.
	    setTitle("Three Pile Nim - Kitty Cat Edition"); //Title at the top of the window
	    setResizable(false); //Make it so the user cannot alter the size of the window.
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(2);
	    setVisible(true); //Make the window visible.
	  }
	  
	  
	  void printView(int leftPile, int middlePile, int rightPile, SimpleNim.GameState.Player paramPlayer) //print the view with the 3 stack sizes and the player whos turn it is.
	  {
	    endLeft = this.pButtonsLeft.pile; //Make end the current size of the left pile.
	    endMiddle = this.pButtonsMiddle.pile; //Make end the current size of the middle pile.
	    endRight = this.pButtonsRight.pile; //Make end the current size of the right pile.
	    JButton localJButton;
	    
	    //PRINT LEFT PILE
	    for (int i = 0; i < leftPile; i++) { //Create buttons that need to be in their spaces on the view.
	      localJButton = this.pButtonsLeft.getButtonAt(i);
	      localJButton.setVisible(true);
	    }

	    for (int i = leftPile; i < this.sizeOfPile; i++) { //Removes all the buttons that are not meant to be there.
	      localJButton = this.pButtonsLeft.getButtonAt(i);
	      localJButton.setVisible(false);
	    }

	    this.pButtonsLeft.setPile(leftPile); //Set the pile size to the inputed int.
	    this.LeftPanel.validate(); //Validates the container.
	    
	    //PRINT MIDDLE PILE
	    for (int i = 0; i < middlePile; i++) { //Create buttons that need to be in their spaces on the view.
	      localJButton = this.pButtonsMiddle.getButtonAt(i);
	      localJButton.setVisible(true);
	    }

	    for (int i = middlePile; i < this.sizeOfPile; i++) { //Removes all the old buttons
	      localJButton = this.pButtonsMiddle.getButtonAt(i);
	      localJButton.setVisible(false);
	    }

	    this.pButtonsMiddle.setPile(middlePile); //Removes all the buttons that are not meant to be there.
	    this.MiddlePanel.validate(); //Validates the container.
	    
	    //PRINT RIGHT PILE!
	    for (int i = 0; i < rightPile; i++) { //Create buttons that need to be in their spaces on the view.
	      localJButton = this.pButtonsRight.getButtonAt(i);
	      localJButton.setVisible(true);
	    }

	    for (int i = rightPile; i < this.sizeOfPile; i++) { //Removes all the buttons that are not meant to be there.
	      localJButton = this.pButtonsRight.getButtonAt(i);
	      localJButton.setVisible(false);
	    }

	    this.pButtonsRight.setPile(rightPile); //Set the pile size to the inputed int.
	    this.RightPanel.validate(); //Validates the container.
	    
	    //PLAYERS TURN
	    if (paramPlayer == SimpleNim.GameState.Player.White) this.player = "White"; else
	      this.player = "Black"; //If player isn't white they are player black.
	    if (leftPile > 0 || rightPile > 0 || middlePile > 0) {
	      this.fTextArea.setText("" + this.player + "'s turn"); //If not at the final position, display who's turn it is.
	    }
	    else
	      this.fTextArea.setText("" + this.player + " is the winner!"); //If all stacks are 0, display the winner.
	  }

	  //Action has been performed class handler.
	  public void actionPerformed(ActionEvent paramActionEvent)
	  {
	    JButton localJButton = (JButton)paramActionEvent.getSource();
	    Dimension localDimension = localJButton.getSize(); //Encapsulated the size of the buttons dimensions in an object, that can be called upon.
	    String str = localJButton.getText(); //Get the text for the button clicked in case the button clicked is Undo or Redo.
    	int JButtonY = localJButton.getY(); //get the y position of the button
	    int j = JButtonY / localDimension.height; //get the height of the button in multiples of buttons. e.g 2 = 2 high.
	    int k = j; //Transfer to new variable.
	    if (str == "Undo") { SimpleNim.GameState.buttonClick(10,'x');} //If button is UNDO
	    if (str == "Redo") { SimpleNim.GameState.buttonClick(11,'x');} //If button is REDO
	    
	    //If the button clicked is within the Left panel (check all components)
	    if (LeftPanel.getComponent(0) == localJButton || LeftPanel.getComponent(1) == localJButton || LeftPanel.getComponent(2) == localJButton ||
	    	LeftPanel.getComponent(3) == localJButton || LeftPanel.getComponent(4) == localJButton){ 
		    endLeft = this.pButtonsLeft.pile; //make end the size of the pile
		    SimpleNim.GameState.buttonClick(endLeft - k, 'l'); //change the game state so that a button is clicked with the correct button input. 
		    												   //Take away k(the number of buttons clicked) from the size of the pile.	
	    }
	    
	  //If the button clicked is within the Middle panel (check all components)
	    if (MiddlePanel.getComponent(0) == localJButton || MiddlePanel.getComponent(1) == localJButton || MiddlePanel.getComponent(2) == localJButton ||
	    	MiddlePanel.getComponent(3) == localJButton || MiddlePanel.getComponent(4) == localJButton){ 
	    	endMiddle = this.pButtonsMiddle.pile;
	    	SimpleNim.GameState.buttonClick(endMiddle - k, 'm');
	    }
	    
	  //If the button clicked is within the Right panel (check all components)
	    if (RightPanel.getComponent(0) == localJButton || RightPanel.getComponent(1) == localJButton || RightPanel.getComponent(2) == localJButton ||
	    	RightPanel.getComponent(3) == localJButton || RightPanel.getComponent(4) == localJButton){
	    	endRight = this.pButtonsRight.pile;
	    	SimpleNim.GameState.buttonClick(endRight - k, 'r');
	    	}
  }

	  //This class is designed to hold and manage the buttons inside the button array.
	  public class PanelButtons
	  {
	    int pile = 0; //Create integer called 'pile'
	    JButton[] buttons = new JButton[6]; //Creates an array of buttons which hold the buttons.}  //Not sure of use.

	    public PanelButtons() { }
	    
	    //Adds a button to its stack.
	    void putButton(JButton paramJButton) { this.buttons[this.pile] = paramJButton; //Puts the inputed button at the top of the stack using the reference int 'pile'.
	      this.pile += 1; } //Increase the value of pile so that the pile is its correct size value.

	    int indexButton(JButton paramJButton)
	    {
	      for (int i = 0; i < this.pile; i++) {
	        if (paramJButton.equals(this.buttons[i])) return i; //Find the index of the inputed button within the array of buttons.
	      }
	      return -1; //Return -1 if the item is not in the stack.
	    }

	    JButton getButtonAt(int paramInt) { //Return the button located at the inputed index.
	      return this.buttons[paramInt]; 
	    }

	    void setPile(int paramInt) { //change the size of the pile with the inputed int.
	      this.pile = paramInt;
	    }
	  }
	}
