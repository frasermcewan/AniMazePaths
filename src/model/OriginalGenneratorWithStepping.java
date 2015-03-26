package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import data.PointPair;

public class OriginalGenneratorWithStepping extends AbstractMaze {

	private List<Point> graph;
	private List<List<Point>> graphs;
	
	public OriginalGenneratorWithStepping(int width, int height) {
		super(width, height);
	}

	@Override
	public PointPair step() {
		if(!graphs.isEmpty()) {
			graph = graphs.remove(0);
			return mergeGraph(graph, graphs);
		}
		return null;
	}

	@Override
	public void reset(Point start, Point goal) {
		super.reset(start, goal);
		graphs = new ArrayList<List<Point>>();
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				grid[x][y] = 0;
			}
		}
		for(int x = 1 ; x < width - 1 ; x++) {
			for(int y = 1 ; y < height - 1 ; y++) {
				graph = new LinkedList<Point>();
				graph.add(new Point(x,y));
				graphs.add(graph);
			}
		}
		
		if(start != null) {
			graph = new LinkedList<Point>();
			graph.add(start);
			graphs.add(graph);
		}
		
		if(goal != null) {
			graph = new LinkedList<Point>();
			graph.add(goal);
			graphs.add(graph);
		}
		
		Collections.shuffle(graphs);
	}
	
	private PointPair mergeGraph(List<Point> graph, List<List<Point>> graphs) {
		for(List<Point> graph2 : graphs) {
			PointPair p = joinGraphs(graph, graph2);
			if(p != null) {
				graph2.addAll(graph);
				return p;
			}
		}
		return null;
	}
	
	private PointPair joinGraphs(List<Point> graph, List<Point> graph2) {
//		Collections.shuffle(graph);
//		Collections.shuffle(graph2);
		for(Point p1 : graph) {
			for(Point p2 : graph2) {
				if(connectPoints(p1,p2)) {
					return new PointPair(p1, p2);
				}
			}
		}
		return null;
	}
}
