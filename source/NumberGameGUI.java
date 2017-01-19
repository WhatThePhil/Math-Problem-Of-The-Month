/*
 * Phillip Rognerud (G00924402)
 * Problem of the Month (February)
 * College of San Mateo
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Collections;
import java.util.ArrayList;


public class NumberGameGUI extends JFrame{

	protected JButton[] numberBtn;
	protected JLabel scoreU, scoreC, turnStatus;
	protected boolean turn;
	protected int counter, speed;
	protected int factorNumber = 1;
	
	ArrayList<Integer> indicesOfHighlight = new ArrayList<>();
	
	private NumberGame game;
	private Timer timer;
	
	public NumberGameGUI(){
		
		super("Factor Me!");
		setSize(600, 600);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		setResizable(false);
		numberBtn = new JButton[30];
		turn = true;
		counter = 0;
		speed = 1000;
		
		game = new NumberGame();
		timer = new Timer(speed, new TimerListener());
		
		JPanel controlPanel = new JPanel(new GridLayout(1,3));
		scoreC = new JLabel("AI (RED): " + game.getCScore());
		scoreU = new JLabel("User (BLUE): " + game.getUScore());
		
		if(turn){
			turnStatus = new JLabel("User turn!");
		}else{
			turnStatus = new JLabel("AI turn!");
		}
		
		scoreC.setFont(new Font("Monaco", Font.BOLD, 14));
		scoreU.setFont(new Font("Monaco", Font.BOLD, 14));
		turnStatus.setFont(new Font("Monaco", Font.BOLD, 18));
		turnStatus.setForeground(Color.BLUE);
		
		scoreC.setHorizontalAlignment(JLabel.CENTER);
		turnStatus.setHorizontalAlignment(JLabel.CENTER);
		scoreU.setHorizontalAlignment(JLabel.CENTER);
		
		controlPanel.add(scoreC);
		controlPanel.add(turnStatus);
		controlPanel.add(scoreU);
		controlPanel.setPreferredSize(new Dimension(50, 50));
		
		JPanel gamePanel = new JPanel(new GridLayout(5, 6));
		
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		contentPane.add(gamePanel, BorderLayout.CENTER);
		
		for(int i = 0; i < 30; i ++){
			numberBtn[i] = new JButton(String.valueOf(i+1));
			numberBtn[i].addActionListener(new GameListener());
			numberBtn[i].setBackground(Color.GREEN);
			numberBtn[i].setFont(new Font("ARIAL", Font.BOLD, 20));
			gamePanel.add(numberBtn[i]);
			
			//try making an actionlistener and moving out of constructor...
			numberBtn[i].addMouseListener(new MouseAdapter(){
				
				public void mouseEntered(MouseEvent event) {
					JButton button = (JButton) event.getSource();
					String btnText = button.getText();
					int number = Integer.parseInt(btnText);
					
					if(button.isEnabled() && turn == true){
						button.setBackground(Color.BLUE);
						factorsHighlight(number);
					}
				}

				public void mouseExited(MouseEvent event) {
					JButton button = (JButton) event.getSource();
					if(button.isEnabled()){
						button.setBackground(Color.GREEN);
						for(int i = 0; i < indicesOfHighlight.size(); i++){
							if(numberBtn[indicesOfHighlight.get(i)].isEnabled()){
								numberBtn[indicesOfHighlight.get(i)].setBackground(Color.GREEN);
							}
						}
						indicesOfHighlight.clear();
					}
				
				}
			});
			
		}
		
		//timer.start();
		setVisible(true);
	}
	
	/*public void findTotalOptions(){
		int numberOfSequences = 0;
		int numberOfMoves = 0;
		
		ArrayList<ArrayList<Integer>> availableMoves = new ArrayList<ArrayList<Integer>>(30);
		
		for(int i = 0; i < 30; i ++){
			if(numberBtn[i].isEnabled()){
				availableMoves<>(i).add(i+1);
			}
		}
		
		for(int i = 0; i < availableMoves.size(); i++){
			String btnText = numberBtn[i].getText();
			int number = Integer.parseInt(btnText);
			
			
		}
		
	}*/
	
	public void factorsHighlight(int number){
		for(int i = 0; i < 30; i++){
			while(factorNumber < number){
				if((number % factorNumber == 0) && (numberBtn[factorNumber-1].isEnabled())){
					if(turn){
						numberBtn[factorNumber-1].setBackground(Color.RED);
					}else{
						numberBtn[factorNumber-1].setBackground(Color.BLUE);
					}
					indicesOfHighlight.add(factorNumber-1);
				}
				factorNumber++;
			}
			factorNumber = 1;
		}
	}

	private class GameListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			JButton button = (JButton) event.getSource();
			
			String btnText = button.getText();
			int number = Integer.parseInt(btnText);

			findFactors(number, 1);
		}
	}
	
	public void findFactors(int number, int factorNumber){
		if(turn){
			game.setUScore(number);
			scoreU.setText("User (BLUE): " + game.getUScore());
			numberBtn[number-1].setBackground(Color.BLUE);
			numberBtn[number-1].setEnabled(false);
			counter++;
		}else{
			game.setCScore(number);
			scoreC.setText("AI (RED): " + game.getCScore());
			numberBtn[number-1].setBackground(Color.RED);
			numberBtn[number-1].setEnabled(false);
			counter++;
		}
			
		while(factorNumber < number){
			if(number % factorNumber == 0){
				if(turn){
					if(numberBtn[factorNumber-1].isEnabled()){
						numberBtn[factorNumber-1].setBackground(Color.RED);
						game.setCScore(factorNumber);
						scoreC.setText("AI (RED): " + game.getCScore());
						counter++;
					}
				}else{
					if(numberBtn[factorNumber-1].isEnabled()){
						game.setUScore(factorNumber);
						scoreU.setText("User (BLUE): " + game.getUScore());
						numberBtn[factorNumber-1].setBackground(Color.BLUE);
						counter++;
					}
				}
					
				numberBtn[factorNumber-1].setEnabled(false);
			}
				
			factorNumber++;
		}
		
		factorNumber = 1;
		turn ^= true;
			
			if(counter == 30){
				timer.stop();
				if(game.getCScore() > game.getUScore()){
					turnStatus.setText("AI WINS!");
					turnStatus.setForeground(Color.RED);

				}else{
					turnStatus.setText("USER WINS!");
					turnStatus.setForeground(Color.BLUE);
				}
			}else{
				if(!turn){
					turnStatus.setText("AI turn!");
					turnStatus.setForeground(Color.RED);
					timer.start();
				}else{
					turnStatus.setText("User turn!");
					turnStatus.setForeground(Color.BLUE);
					timer.stop();
				}
			}
		
	}
	
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			makeMove();
		}
	}
	
	private void makeMove(){
		ArrayList<Integer> availableMoves = new ArrayList<>();
		ArrayList<Integer> differenceOfScores = new ArrayList<>();
		
		int factor = 1;
		int calculation = 0;
		int index = 0;
		
		for(int i = 0; i < 30; i ++){
			if(numberBtn[i].isEnabled()){
				availableMoves.add(i+1);
			}
		}
		
		
		
		for(int i = 0; i < availableMoves.size(); i++){
			while(factor < availableMoves.get(i)){
				if((availableMoves.get(i) % factor == 0) && (numberBtn[factor-1].isEnabled())){
					calculation += factor;
				}
				factor++;
			}
			differenceOfScores.add( (game.getCScore() + availableMoves.get(i)) - (game.getUScore() + calculation));
			calculation = 0;
			factor = 1;
		}

		
		for (int i = 0; i < differenceOfScores.size(); i++) {
			if(differenceOfScores.get(i) == Collections.max(differenceOfScores)) {
				index = i;
			}
		}
		
		if(availableMoves.get(index) == 13){
			findFactors(26, 1);
		}else{
			findFactors(availableMoves.get(index), 1);
		}
	}
	
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NumberGameGUI frame = new NumberGameGUI();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
	}
}