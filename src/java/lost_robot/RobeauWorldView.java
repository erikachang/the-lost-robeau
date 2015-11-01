package lost_robot;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;

import java.awt.Color;
import java.awt.Graphics;

public class RobeauWorldView extends GridWorldView {

	private static final long serialVersionUID = -7209607988558790810L;
	
	public RobeauWorldView(GridWorldModel model) {
		super(model, "The Lost Robeau", 400);
		this.setSize(800, 225);
		setVisible(true);
		repaint();
	}
	
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		switch(object) {
		case RobeauWorldModel.CIRCLE_T:
			drawTinyCircle(g, x, y);
			break;
		case RobeauWorldModel.CIRCLE_S:
			drawSmallCircle(g, x, y);
			break;
		case RobeauWorldModel.CIRCLE_M:
			drawMediumCircle(g, x, y);
			break;
		case RobeauWorldModel.CIRCLE_L:
			drawLargeCircle(g, x, y);
			break;
		}
	}
	
	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		g.setColor(Color.black);
		super.drawAgent(g, x, y, Color.red, id);
//		this.drawString(g, x, y, defaultFont, Integer.toString(id));
	}
	
	public void drawTinyCircle(Graphics g, int x, int y) {
		int width = (int)(cellSizeW * 0.1875);
		int height = (int)(cellSizeH * 0.1875);
		
		drawCircle(g, x, y, width, height);
	}
	
	public void drawSmallCircle(Graphics g, int x, int y) {
		int width = (int)(cellSizeW * 0.375);
		int height = (int)(cellSizeH * 0.375);
		
		drawCircle(g, x, y, width, height);
	}
	
	public void drawMediumCircle(Graphics g, int x, int y) {		
		int width = (int)(cellSizeW * 0.5625);
		int height = (int)(cellSizeH * 0.5625);
		
		drawCircle(g, x, y, width, height);
	}
	
	public void drawLargeCircle(Graphics g, int x, int y) {
		int width = (int)(cellSizeW * 0.75);
		int height = (int)(cellSizeH * 0.75);
		
		drawCircle(g, x, y, width, height);
	}
	
	public void drawCircle(Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.blue);
		
		int posX = (x*cellSizeW)+((cellSizeW/2)-(width/2));
		int posY = (y*cellSizeH)+((cellSizeW/2)-(height/2));
		
		g.drawOval(posX, posY, width, height);
		g.fillOval(posX, posY, width, height);
	}
}
