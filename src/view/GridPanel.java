package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import data.PointPair;

public class GridPanel extends JPanel {
	
	private boolean showPath     = true;
	private boolean showVisited  = false;
	private boolean showAgenda   = false;
	private boolean drawGrid     = false;
	
	private static final int ALPHA = 255;
	
	private static final Color[] COLOURS = new Color[] {
		new Color(255, 0, 0, ALPHA),
		new Color(0, 255, 0, ALPHA),
		new Color(0, 0, 255, ALPHA),
		new Color(255, 255, 0, ALPHA),
		new Color(0, 255, 255, ALPHA),
		new Color(255, 0, 255, ALPHA)
	};

	private static final long serialVersionUID = 408883625275477255L;
	private static final int UP = 8, RIGHT = 4, DOWN = 2, LEFT = 1;
	private int delay = 0;
	
	private List<List<Point>> paths = new LinkedList<List<Point>>();
	
	private boolean[] showPaths = new boolean[6];
	
	private int[][] grid;
	private final int w, h;
	
	private int direction = 0;
	private int current = 0;
	private int position = 0;
	private int start = 0, end = 0;
	
	public GridPanel(int[][] grid, int anim) {
		this.grid = grid;
		this.h = grid[0].length;
		this.w = grid.length;
		setBackground(Color.BLACK);
		delay = anim;
	}
	
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintGraphics(g);
		g.dispose();
	}
	
	private void paintGraphics(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		double scaleX = (getWidth() / w) / 3.0;
		double scaleY = (getHeight() / h) / 3.0;
		
		for(int x = 0 ; x < w ; x++) {
			for(int y = 0 ; y < h ; y++) {
				int val = grid[x][y];
				g.setColor(Color.DARK_GRAY);
				if(x>0 && x<w-1 && y>0 && y<h-1) {
					g.fillRect((int)(x * scaleX * 3 + scaleX), (int)(y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
				}
				g.setColor(Color.GRAY);
				if(val > 0) {
					g.fillRect((int)(x * scaleX * 3 + scaleX), (int)(y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
					if((val & LEFT) == LEFT) {
						g.fillRect((int)(x * scaleX * 3), (int)(y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
					}
					if((val & RIGHT) == RIGHT) {
						g.fillRect((int)(x * scaleX * 3 + scaleX * 2), (int)(y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
					}
					if((val & DOWN) == DOWN) {
						g.fillRect((int)(x * scaleX * 3 + scaleX), (int)(y * scaleY * 3 + scaleY * 2), (int)scaleX, (int)scaleY);
					}
					if((val & UP) == UP) {
						g.fillRect((int)(x * scaleX * 3 + scaleX), (int)(y * scaleY * 3), (int)scaleX, (int)scaleY);
					}
				} 
			}
		}

		for(int p = 0 ; p < paths.size() ; p++) {
			if(showPaths[p]) {
				List<Point> path = paths.get(p);
				g2.setColor(COLOURS[p]);
				g2.fillRect((int)((p+1) * 3 * scaleX * 3 + scaleX), (int)(scaleY), (int)scaleX, (int)scaleY);
				int i;
				for(i = 0 ; i < path.size()-1 ; i++) {
					Point p1 = path.get(i);
					g2.fillRect((int)(p1.x * scaleX * 3 + scaleX), (int)(p1.y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
					Point p2 = path.get(i+1);
					switch(p1.x - p2.x) {
					case -1: //Right
						g2.fillRect((int)(p1.x * scaleX * 3 + scaleX * 2), (int)(p1.y * scaleY * 3 + scaleY), (int)(scaleX*2), (int)scaleY);
						break;
					case 1: //Left
						g2.fillRect((int)(p1.x * scaleX * 3 - scaleX), (int)(p1.y * scaleY * 3 + scaleY), (int)(scaleX*2), (int)scaleY);
						break;
					case 0: 
						switch(p1.y - p2.y) {
						case -1: //Down
							g2.fillRect((int)(p1.x * scaleX * 3 + scaleX), (int)(p1.y * scaleY * 3 + scaleY * 2), (int)scaleX, (int)(scaleY*2));
							break;
						case 1: //Up
							g2.fillRect((int)(p1.x * scaleX * 3 + scaleX), (int)(p1.y * scaleY * 3 - scaleY), (int)scaleX, (int)(scaleY*2));
							break;
						default :
							System.out.println("V GridPanel.paintComponents() WTF! " + p1 + " " + p2);
						}
						break;
					default:
						System.out.println("H GridPanel.paintComponents() WTF! " + p1 + " " + p2);
					}
				}
				Point p1 = path.get(i);
				g2.fillRect((int)(p1.x * scaleX * 3 + scaleX), (int)(p1.y * scaleY * 3 + scaleY), (int)scaleX, (int)scaleY);
			}
		}
		if(drawGrid) {
			g.setColor(Color.GRAY);
			for(int x = 1 ; x < w ; x++) {
				g.drawLine((int)(x * 3 * scaleX), 0, (int)(x * 3 * scaleX), getHeight());
			}
			for(int y = 1 ; y < h ; y++) {
				g.drawLine(0, (int)(y * 3 * scaleY), getWidth(), (int)(y * 3 * scaleY));
			}
		}
		g2.setColor(Color.GRAY);
		switch(direction) {
		case UP:
			g2.fillRect(position, current, (int) scaleX, start - current);
			break;
		case DOWN:
			g2.fillRect(position, start, (int) scaleX, current - start);
			break;
		case LEFT:
			g2.fillRect(current, position, start - current, (int) scaleY);
			break;
		case RIGHT:
			g2.fillRect(start, position, current - start, (int) scaleY);
			break;
		default:
		}
	}
	
	public void makeJoin(PointPair pair, int[][] grid) {
		double scaleX = (getWidth() / w) / 3.0;
		double scaleY = (getHeight() / h) / 3.0;
		Point p1 = pair.a;
		Point p2 = pair.b;
		switch(p1.x - p2.x) {
		case -1: //Right
			direction = RIGHT;
			start = (int) (p1.x * scaleX * 3 + scaleX);
			end = (int) (start + scaleX * 4);
			current = (int) (start + scaleX);
			position = (int) (p1.y * scaleY * 3 + scaleY);
			while(current < end) {
				repaint();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {}
				current++;
			}
			break;
		case 1: //Left
			direction = LEFT;
			start = (int) (p1.x * scaleX * 3 + scaleY * 2);
			end = (int) (p2.x * scaleX * 3 + scaleY);
			current = (int) (start - scaleX);
			position = (int) (p1.y * scaleY * 3 + scaleY);
			while(current > end) {
				repaint();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {}
				current--;
			}
			break;
		case 0: 
			switch(p1.y - p2.y) {
			case -1: //Down
				direction = DOWN;
				start = (int) (p1.y * scaleY * 3 + scaleY);
				end = (int) (start + scaleY * 4);
				current = (int) (start + scaleY);
				position = (int) (p1.x * scaleX * 3 + scaleX);
				while(current < end) {
					repaint();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {}
					current++;
				}
				break;
			case 1: //Up
				direction = UP;
				start = (int) ((p2.y + 2) * scaleY * 3 - scaleY);
				end = (int) (start - scaleY * 4);
				current = (int) (start - scaleY);
				position = (int) (p1.x * scaleX * 3 + scaleX);
				while(current > end) {
					repaint();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {}
					current--;
				}
				break;
			default :
				System.err.println("makeJoin() WTF! V");
				break;
			}
			break;
		default:
			System.err.println("makeJoin() WTF! H " + pair);
			break;
		}
		this.grid = grid;
		position = 0;
		current = 0;
		start = 0;
	}
	


	public void addSolution(List<Point> path) {
		paths.add(path);
	}

	public void clear() {
		showPaths = new boolean[6];
		paths = new LinkedList<List<Point>>();
		this.grid = new int[(int) w][(int) h];
		this.repaint();
	}

	public void toggleSolution() {
		showPath = !showPath;
		repaint();
	}
	
	public void toggleVisited() {
		showAgenda = !showAgenda;
		showVisited = !showVisited;
		repaint();
	}

	public void togglePath(int i) {
		showPaths[i] = !showPaths[i];
		repaint();
	}
}
