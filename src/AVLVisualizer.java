import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.IdentityHashMap;
import java.util.Map;

/** Run with Maven: mvn javafx:run */
public class AVLVisualizer extends Application {
    private AVLTree<Integer> tree = AVLTree.minimumNodeTree(5);
    private final Pane drawing = new Pane();
    private final Label status = new Label();
    private final Label message = new Label();
    private final TextField heightInput = new TextField("5");
    private final TextField valueInput = new TextField();
    private final Map<BST.TreeNode<Integer>, Double> positions = new IdentityHashMap<>();
    private int rank;

    @Override
    public void start(Stage stage) {
        heightInput.setPrefColumnCount(3);
        valueInput.setPrefColumnCount(6);
        valueInput.setPromptText("Integer key");
        Button generate = new Button("Build minimum AVL");
        Button insert = new Button("Insert");
        Button delete = new Button("Delete");
        Button clear = new Button("Clear");
        generate.setOnAction(event -> generate());
        heightInput.setOnAction(event -> generate());
        insert.setOnAction(event -> edit(true));
        valueInput.setOnAction(event -> edit(true));
        delete.setOnAction(event -> edit(false));
        clear.setOnAction(event -> { tree.clear(); message.setText(""); redraw(); });
        FlowPane controls = new FlowPane(10, 10, new Label("Height (0-12):"),
                heightInput, generate, new Separator(), valueInput, insert, delete, clear);
        Label explanation = new Label("N(0) = 0, N(1) = 1, N(h) = 1 + N(h-1) + N(h-2).  "
                + "Leaf height = 1. Small labels show balance factor (left height minus right height).");
        explanation.setWrapText(true);
        VBox header = new VBox(10, controls, status, explanation, message);
        header.setPadding(new Insets(16));
        drawing.setStyle("-fx-background-color: #f5f7fb;");
        ScrollPane scroll = new ScrollPane(drawing);
        scroll.setPannable(true);
        BorderPane layout = new BorderPane(scroll);
        layout.setTop(header);
        stage.setScene(new Scene(layout, 1100, 740));
        stage.setTitle("AVL Tree: Minimum Nodes by Height");
        redraw();
        stage.show();
    }

    private void generate() {
        try {
            int h = Integer.parseInt(heightInput.getText().trim());
            if (h < 0 || h > 12) throw new IllegalArgumentException("Enter a height from 0 through 12.");
            tree = AVLTree.minimumNodeTree(h);
            message.setText("Minimum-node tree generated. Drag the canvas or use the scrollbars.");
            redraw();
        } catch (IllegalArgumentException ex) {
            message.setText("Enter a whole-number height from 0 through 12.");
        }
    }

    private void edit(boolean inserting) {
        try {
            int value = Integer.parseInt(valueInput.getText().trim());
            if (inserting && tree.getSize() >= 1000 && !tree.search(value)) {
                message.setText("Display limit: 1,000 nodes. Clear or delete nodes first.");
                return;
            }
            boolean changed = inserting ? tree.insert(value) : tree.delete(value);
            message.setText(changed ? (inserting ? "Inserted " : "Deleted ") + value
                    : (inserting ? "Duplicate key: " : "Key not found: ") + value);
            redraw();
        } catch (NumberFormatException ex) {
            message.setText("Enter an integer key.");
        }
    }

    private void redraw() {
        drawing.getChildren().clear();
        positions.clear();
        rank = 0;
        assignPositions(tree.getRoot());
        drawing.setPrefSize(Math.max(1050, tree.getSize() * 76 + 80),
                Math.max(460, tree.getHeight() * 100 + 60));
        drawEdges(tree.getRoot(), 0);
        drawNodes(tree.getRoot(), 0);
        int h = tree.getHeight();
        status.setText("Height: " + h + "     Nodes: " + tree.getSize()
                + "     Minimum at this height: " + AVLTree.MinNode(h)
                + (tree.getSize() == AVLTree.MinNode(h) ? "     Minimum achieved" : ""));
        if (tree.isEmpty()) {
            Text empty = new Text(40, 60, "Empty tree, height 0, nodes 0");
            drawing.getChildren().add(empty);
        }
    }

    private void assignPositions(BST.TreeNode<Integer> node) {
        if (node == null) return;
        assignPositions(node.left);
        positions.put(node, 52.0 + rank++ * 76);
        assignPositions(node.right);
    }

    private void drawEdges(BST.TreeNode<Integer> node, int depth) {
        if (node == null) return;
        double x = positions.get(node), y = 48 + depth * 100;
        if (node.left != null) edge(x, y, positions.get(node.left), y + 100);
        if (node.right != null) edge(x, y, positions.get(node.right), y + 100);
        drawEdges(node.left, depth + 1);
        drawEdges(node.right, depth + 1);
    }

    private void edge(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.web("#8c9cb1"));
        line.setStrokeWidth(2);
        drawing.getChildren().add(line);
    }

    private void drawNodes(BST.TreeNode<Integer> node, int depth) {
        if (node == null) return;
        double x = positions.get(node), y = 48 + depth * 100;
        Circle circle = new Circle(x, y, 26, Color.web("#2259a8"));
        Text label = new Text(node.element.toString());
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-size: 12px;");
        // Scale long int keys so they still fit within the node.
        double width = label.getLayoutBounds().getWidth();
        if (width > 44) {
            label.setStyle("-fx-font-size: " + (12 * 44 / width) + "px;");
        }
        label.setX(x - label.getLayoutBounds().getWidth() / 2);
        label.setY(y + 4);
        Text balance = new Text(x - 16, y + 44, "BF " + tree.balanceFactor(node));
        balance.setFill(Color.web("#46556b"));
        drawing.getChildren().addAll(circle, label, balance);
        drawNodes(node.left, depth + 1);
        drawNodes(node.right, depth + 1);
    }

    public static void main(String[] args) { launch(args); }
}
