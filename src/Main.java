import java.util.*;
import javax.swing.*;
import java.awt.*;
public class Main {

	public static void main(String[] args) {
		
		
		//Front end of the program
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		Pathfinder p = new Pathfinder(20);
		JPanel panel = new JPanel();
		JButton genB = new JButton("Generate Maze");
		JButton dfsB = new JButton("DFS Solve");
		JButton bfsB = new JButton("BFS Solve (Shortest Path)");
		JTextField text = new JTextField("Enter maze size");
		panel.add(bfsB);
		panel.add(genB);
		panel.add(dfsB);
		panel.add(text);
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//p.setBackground(Color.WHITE);
		frame.add(p, BorderLayout.CENTER);
		frame.add(panel, BorderLayout.NORTH);
		//frame.setSize(600, 600);
		frame.setVisible(true);
		bfsB.setEnabled(false);
		dfsB.setEnabled(false);
		
		
		genB.addActionListener(e -> {
			p.reset();
			 genB.setEnabled(false); bfsB.setEnabled(false); dfsB.setEnabled(false); text.setEnabled(false);
			 String str = text.getText();
			//Catches errors when mismatching types
			 try {
				 if(Integer.parseInt(str) >= 10)
					 p.changeSize(Integer.parseInt(str));
				 else {
					 p.changeSize(10);
					 text.setText("10");
				 }
			 }
			 catch(Exception e1) {
				 text.setText("Enter a number");
				 JOptionPane.showConfirmDialog(frame,
						    "Enter Number! Creating Default Maze",
						    "NOTICE",
						    JOptionPane.PLAIN_MESSAGE);
					 
			 }
			    new Thread(() -> {
			        p.makeBoard(); 
			        SwingUtilities.invokeLater(() -> {
			            genB.setEnabled(true); bfsB.setEnabled(true); dfsB.setEnabled(true); text.setEnabled(true);
			        });
			    }).start();
			
		});
		
		
		bfsB.addActionListener(e -> {
			p.clearColor();
		    genB.setEnabled(false); bfsB.setEnabled(false); dfsB.setEnabled(false); text.setEnabled(false);
		    new Thread(() -> {
		        p.findShortest();  
		        SwingUtilities.invokeLater(() -> {
		            genB.setEnabled(true); bfsB.setEnabled(true); dfsB.setEnabled(true); text.setEnabled(true);
		        });
		    }).start();
		});
		
		dfsB.addActionListener(e -> {
			p.clearColor();
		    genB.setEnabled(false); bfsB.setEnabled(false); dfsB.setEnabled(false); text.setEnabled(false);
		    new Thread(() -> {
		        p.dfsPath();  
		        SwingUtilities.invokeLater(() -> {
		            genB.setEnabled(true); bfsB.setEnabled(true); dfsB.setEnabled(true); text.setEnabled(true);
		        });
		    }).start();
		});
		
		
	
	
	}

}
