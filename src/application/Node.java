package application;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{

	public static class Edge {
		Edge(double length, Node node){
			this.length = length;
			this.node = node;
		}

		public double length;
		public Node node;
		
		public String toString() {
			return "" + node.name;
		}
	}

	@Override
	public int compareTo(Node n) {
		return Double.compare(this.f, n.f);
	}

	private String name;
	private double x;
	private double y;
	private double g; //history from start till now
	private double h; //distance between you and end
	private double f = 0; //g+h
	private Node parent = null;
	public List<Edge> adj;

	//constructor for adding the h value
	Node(String name, double h){
		this.h = h;
		this.name = name;
		this.adj = new ArrayList<>();
	}

	public Node(String cityname, double x, double y) {
		this.name = cityname;
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String cityname) {
		this.name = cityname;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public double calHeuristic(Node target) {		
		return this.getH();
	}

	//for adding the branches/edges of the node
	public void addEdge(double weight, Node node){
		Edge newEdge = new Edge(weight, node);
		adj.add(newEdge);
	}

	public String toString1() {
		return "Node [name=" + name + ", x=" + x + ", y=" + y + ", g=" + g + ", h=" + h + ", f=" + f
				+ ", parent=" + parent + ", adj=" + adj;
	}

	@Override
	public String toString() {
		return "" + name;
	}

}