import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarkMew_Parser implements ActionListener{

    private JTabbedPane tabbedPane;
    private JEditorPane webView;


    public MarkMew_Parser(MainFrame frame){

        tabbedPane = frame.getTabbedPane();
        webView = frame.getWebView();
        webView.setContentType("text/html");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MarkMew_Tab tab = (MarkMew_Tab) tabbedPane.getSelectedComponent();
        if (tab == null) {
            webView.setText("");
            return;
        }

        String content = parser(tab.getContent());
        try {
            webView.setText(content);
        }
        catch(RuntimeException e1){
            e1.printStackTrace();
            System.out.println("try-catch");
            return;
        }
    }
}
