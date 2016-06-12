package ai.pathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class RectGraphics extends JPanel{

	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public RectGraphics(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
        
        // background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 2000, 2000);
	}

}
