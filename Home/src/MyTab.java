import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class MyTab extends Tab{
    private String name;
    private HBox hbox;

    public MyTab(String text, Node content, String name, HBox hbox) {
        super(text, content);
        this.name = name;
        this.hbox = hbox;
    }
}
