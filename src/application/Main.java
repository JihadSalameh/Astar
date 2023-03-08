package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Main extends Application {

	static File file1 = new File("C:\\Users\\Smart\\Desktop\\Java\\AI project1\\src\\application\\places.txt");
	static File file2 = new File("C:\\Users\\Smart\\Desktop\\Java\\AI project1\\src\\application\\roads.txt");
	static Node p = null;
	static ArrayList<Node> places = new ArrayList<>();
	static ArrayList<Node> places1 = new ArrayList<>();
	@FXML
    private Pane paneImg;
	@FXML
    private ImageView imgv;
	@FXML
	private ComboBox<Node> toCombo;
	@FXML
	private ComboBox<Node> fromCombo;
	@FXML
	private Button findBtn;
	@FXML
    private AnchorPane root;
	Line line = new Line();
	
	//adding nodes to the ComboBox
	@FXML
	void locationsCombo(Event event) {
		ObservableList<Node> list = FXCollections.observableArrayList(places);
		toCombo.setItems(list);
		fromCombo.setItems(list);
	}

	//function to start the search for the answer
	@FXML
	void Find(ActionEvent event) {
		if(paneImg.getChildren().size() > 1) {
			paneImg.getChildren().remove(1);
		}
		places1.clear();
		Node end = toCombo.getValue();
		Node start = fromCombo.getValue();
		System.out.println(start.getName() + " -> " + end.getName());

		for(int i = 0; i < places.size(); i++) {
			Double hval = Math.sqrt((Math.pow(end.getX() - places.get(i).getX(), 2)) + (Math.pow(end.getY() - places.get(i).getY(), 2)));
			Node n = new Node(places.get(i).getName(), hval);
			places1.add(n);
		}
		
		try {
			Scanner scanner = new Scanner(file2);

			//reading the second file(edges) and adding the edges
			while (scanner.hasNext()) {
				String x = scanner.nextLine();
				String[] t = new String[3];
				t = x.split(" ");
				
				for(int i = 0; i < places1.size(); i++) {
					if (places1.get(i).getName().compareTo(t[0]) == 0) {
						places1.get(i).addEdge(Double.parseDouble(t[2]), places1.get(findNode(t[1])));
					} else if (places1.get(i).getName().compareTo(t[1]) == 0) {
						places1.get(i).addEdge(Double.parseDouble(t[2]), places1.get(findNode(t[0])));
					}
				}
			}
			
			for(int i = 0; i < places1.size(); i++) {
				System.out.println(places1.get(i).toString1());
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//finding them because they were stored from the places array not places1 and that doesn't have the h value
		end = places1.get(findNode(end.getName()));
		start = places1.get(findNode(start.getName()));
		
		aStar(start, end);
		
		//for getting the path reversed
		ArrayList<Node> path = printPath(end);
        System.out.println("Path: " + path);
        
        ArrayList<Node> draw = new ArrayList<>();
        //for getting the solution nodes from places not places1 because its the array with the x and y values
        for(int i = 0; i < path.size(); i++) {
        	for(int j = 0; j < places.size(); j++) {
        		if(path.get(i).getName().equals(places.get(j).getName())) {
        			draw.add(places.get(j));
        		}
        	}
        }
        
        drawLine(draw);
        draw.clear();
	}
	
	//drawing the line on the map
	public void drawLine(ArrayList<Node> known) {
		for(int i = 0; i < known.size() - 1; i++) {
			double X1 = known.get(i).getX()*791/919;
			double Y1 = known.get(i).getY()*426/501;
			double X2 = known.get(i + 1).getX()*791/919;
			double Y2 = known.get(i + 1).getY()*426/501;
			line = new Line(X1, Y1, X2, Y2);
			line.setStroke(Color.BLACK);
			paneImg.getChildren().add(line);
		}
	}
	
	//finding the index of a specific node
	public int findNode(String name) {
		int index = 0;
		for(int i = 0; i < places1.size(); i++) {
			if(places1.get(i).getName().compareTo(name) == 0)
				index = i;
		}
		return index;
	}
	
	//getting the path and reversing it to the correct way
	public ArrayList<Node> printPath(Node end) {
		ArrayList<Node> path = new ArrayList<Node>();
		for(Node node = end; node != null; node = node.getParent()) {
			path.add(node);
		}
		Collections.reverse(path);
		return path;
	}

	//aStar algorithm
	public static Node aStar(Node start, Node end){
		PriorityQueue<Node> closed = new PriorityQueue<>();
		PriorityQueue<Node> open = new PriorityQueue<>();

		start.setF(start.getG() + start.calHeuristic(end));
		open.add(start);

		while(!open.isEmpty()){
			Node n = open.peek();
			
			if(n == end){
				return n;
			}
			
			for(Node.Edge edge : n.adj){
				Node m = edge.node;
				double totalWeight = n.getG() + edge.length;

				if(!open.contains(m) && !closed.contains(m)){
					m.setParent(n);
					m.setG(totalWeight);
					m.setF(m.getG() + m.calHeuristic(end));
					open.add(m);
				} else {
					if(totalWeight < m.getG()){
						m.setParent(n);
						m.setG(totalWeight);
						m.setF(m.getG() + m.calHeuristic(end));

						if(closed.contains(m)){
							closed.remove(m);
							open.add(m);
						}
					}
				}
			}

			open.remove(n);
			closed.add(n);
		}

		return null;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = (AnchorPane)FXMLLoader.load(getClass().getResource("UI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(file1);

			//reading first file(nodes) and storing it
			while(scanner.hasNext()) {
				String x = scanner.nextLine();
				String[] t1 = new String[3];
				t1 = x.split(" ");
				places.add(new Node(t1[0], Double.parseDouble(t1[1]), Double.parseDouble(t1[2])));
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		launch(args);
	}

}