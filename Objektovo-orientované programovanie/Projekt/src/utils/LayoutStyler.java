package utils;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
* Trieda <code>LayoutStyler</code> sluzi na stylizaciu okien.
*/

public class LayoutStyler {
	

	public static void SetLayout(Login l, Node node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.minHeight(height);
		node.minWidth(width);
		Login.pane.getChildren().add(node);	
		}
	

	public static void SetLayout(Pane p, Node node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.minHeight(height);
		node.minWidth(width);
		p.getChildren().add(node);	
		}
	public static void SetLayout(Pane p, ListView<String> node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.setMinHeight(height);
		node.setMinWidth(width);
		node.setMaxHeight(height);
		node.setMaxWidth(width);
		p.getChildren().add(node);	
		}
	public static void SetLayout(Pane p, TextArea node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.setMinHeight(height);
		node.setMinWidth(width);
		node.setMaxHeight(height);
		node.setMaxWidth(width);
		p.getChildren().add(node);	
		}
	
	public static void SetLayout(Login l, TextField node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.setMinHeight(height);
		node.setMinWidth(width);
		node.setMaxHeight(height);
		node.setMaxWidth(width);
		Login.pane.getChildren().add(node);	
		}
	public static void SetLayout(Login l, PasswordField node, int pozx, int pozy, int width, int height, boolean visible ) {
		node.setLayoutX(pozx);
		node.setLayoutY(pozy);
		node.setVisible(visible);
		node.setMinHeight(height);
		node.setMinWidth(width);
		node.setMaxHeight(height);
		node.setMaxWidth(width);
		Login.pane.getChildren().add(node);	
		}
	
}
