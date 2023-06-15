import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchEvent implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("clicked");
        System.out.println(e);

    }
}
