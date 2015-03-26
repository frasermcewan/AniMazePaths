package controller;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import model.EllersGenerator;
import model.FirstEscapeDrunkWalk;
import model.FrostyMazeGenerator;
import model.LastEscapeDrunkWalk;
import model.Maze;
import model.OriginalGenneratorWithStepping;
import model.RandomEscapeDrunkWalk;
import solver.Solver;
import solver.StupidSearch;
import view.GridPanel;
import data.PointPair;

public class PGMaze {
	
	private final Solver[] solvers = new Solver[] {
		new StupidSearch(),
		new StupidSearch(),
		new StupidSearch(),
		new StupidSearch()
	};
	
	private static final int TUNNELS = 200;
	
	private final Random rng = new Random();
	private int width;
	private int height;
	private int gen;
	private int anim;
	private boolean animate;
	private GridPanel gridPanel;
	private Maze grid;
	private Point start;
	private Point goal;
	
	private boolean threadLock = false;

	public PGMaze(String[] args) {
		
		// DEFAULTS
		width = 40;
		height = 40;
		gen = 4;
		anim = 0; // set to -1 for no animation
		
		start = new Point(0, height/2);
		goal = new Point(width-1, height/2);

		
		if (args.length > 0) {
			try {
				switch(args.length) {
				case 1:
					width = Integer.parseInt(args[0]);
					height = width;
					break;
				case 2:
					width = Integer.parseInt(args[0]);
					height = width;
					gen = Integer.parseInt(args[1]);
					break;
				case 3:
					width = Integer.parseInt(args[0]);
					height = width;
					gen = Integer.parseInt(args[1]);
					anim = Integer.parseInt(args[2]);
					System.out.println(width + " x " + height + " gen " + gen + " anim " + anim);
					break;
				default:
					System.err.println("You're avin giaraffe, intcha?");
					System.exit(0);
				}
			} catch (Exception e) {
				System.err.println("You're avin giaraffe, intcha?");
				System.exit(0);
			}
		}
		
		animate = anim >= 0;
		switch(gen) {
		case 0:
			grid = new OriginalGenneratorWithStepping(width, height);
			break;
		case 1:
			grid = new FrostyMazeGenerator(width, height);
			break;
		case 2:
			grid = new LastEscapeDrunkWalk(width, height);
			break;
		case 3:
			grid = new FirstEscapeDrunkWalk(width, height);
			break;
		case 4:
			grid = new RandomEscapeDrunkWalk(width, height);
			break;
		case 5:
			grid = new EllersGenerator(width, height);
			break;
		default:
			grid = new FrostyMazeGenerator(width, height);
		}
		
		final JFrame frame = new JFrame("PGMaze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gridPanel = new GridPanel(grid.getGrid(), anim);
		gridPanel.setPreferredSize(new Dimension(600, 600));
		frame.getContentPane().add(gridPanel);
		frame.setVisible(true);
		frame.pack();
		frame.setResizable(true);
		
		grid.reset(start, goal);
		
		KeyListener kl = new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			public void keyReleased(KeyEvent arg0) {}
			public void keyPressed(KeyEvent arg0) {
				if(threadLock) return;
				int key = arg0.getKeyCode();
				if(key == 83) {
					gridPanel.toggleSolution();
				} else if(key == 86) {
					gridPanel.toggleVisited();
				} else if(key >= 49 && key <= 54) {
					gridPanel.togglePath(key-49);
				}
			}
		};
		
		MouseListener ml = new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				if(threadLock) return;
				Thread t = new Thread(new Runnable() {
					public void run() {
						grid.reset(start, goal);
						gridPanel.clear();
						frame.pack();
						generate();
					}
				});
				t.start();
			}
		};
		
		gridPanel.addMouseListener(ml);
		frame.addKeyListener(kl);
		
		generate();
	}

	public static void main(String[] args) {
		
		new PGMaze(args);

	}

	private synchronized void generate() {
		threadLock = true;
		long t = System.currentTimeMillis();
		PointPair pair = grid.step();
		if(animate && pair != null)
			gridPanel.makeJoin(pair, grid.getGrid());
		while(pair != null) {
			pair = grid.step();
			if(animate && pair != null)
				gridPanel.makeJoin(pair, grid.getGrid());
		}
		
		for(int i = 0 ; i < TUNNELS ; i++) {
			int x = rng.nextInt(width-3) + 1;
			int y = rng.nextInt(height-3) + 1;
			
			Point p1 = new Point(x,y);
			Point p2 = rng.nextBoolean() ? new Point(x+1,y) : new Point(x,y+1);
			
			if(!grid.forceJoin(p1,p2)) {
				i--;
			}
			
			gridPanel.makeJoin(new PointPair(p1, p2), grid.getGrid());
		}
		System.out.println("Generated in " + (System.currentTimeMillis() - t) + "ms.");
		
		List<Point> solution = null;
		for(Solver solver : solvers) {
			System.out.print(solver.getClass().getSimpleName());
			t = System.currentTimeMillis();
			solution = solver.solve(grid.getGrid(), start, goal);
			System.out.println(" solved the maze in " + (System.currentTimeMillis()-t) + "ms with a path length of " + solution.size());
			gridPanel.addSolution(solution);
		}
		System.out.println("*********************************************************************\n");
		gridPanel.setGrid(grid.getGrid());
		gridPanel.repaint();
		threadLock = false;
	}

}
