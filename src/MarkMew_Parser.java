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
        String content = ((MarkMew_Tab)tabbedPane.getSelectedComponent()).getContent();
        webView.setText(content);
    }
}
