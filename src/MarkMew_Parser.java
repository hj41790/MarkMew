import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarkMew_Parser implements ActionListener{

    private JScrollPane scrollPane;
    private JTabbedPane tabbedPane;
    private JEditorPane webView;

    private HTMLEditorKit   editorKit;
    private StyleSheet      styleSheet;

    private double  offset = 0.9;

    private String content;

    public MarkMew_Parser(MainFrame frame){

        scrollPane = frame.getScrollPane();
        tabbedPane = frame.getTabbedPane();
        webView = frame.getWebView();
        webView.setContentType("text/html");

        settingCSS(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        MarkMew_Tab tab = (MarkMew_Tab) tabbedPane.getSelectedComponent();
        if (tab == null) {
            webView.setText("");
            return;
        }
        if(!tab.isUpdate()) return;

        content = parser(tab.getContent());
        try {

            Point p = scrollPane.getViewport().getViewPosition();
            webView.setText(content);

            // maintain vertical scroll position
            SwingUtilities.invokeLater(new Thread(){
                @Override
                public void run() {
                    super.run();
                    scrollPane.getViewport().setViewPosition(p);
                }
            });

        }
        catch(RuntimeException e1){
            e1.printStackTrace();
            return;
        }
        finally {
            tab.setUpdate(false);
        }

    }

    private String parser(String content){
        String result = "" + content;




        return result;
    }

    public void settingCSS(boolean size){
        if(size) offset += 0.1;
        else offset -= 0.1;

        styleSheet = new StyleSheet();
        styleSheet.addRule("p {font-size:"+(22*offset)+"; font-family:\"맑은 고딕\",sans-serif}");
        styleSheet.addRule("h1 {font-size:"+(50*offset)+"; font-weight:bold}");
        styleSheet.addRule("h2 {font-size:"+(45*offset)+"; font-weight:bold}");
        styleSheet.addRule("h3 {font-size:"+(40*offset)+"; font-weight:bold}");
        styleSheet.addRule("h4 {font-size:"+(35*offset)+"; font-weight:bold}");
        styleSheet.addRule("h5 {font-size:"+(30*offset)+"; font-weight:bold}");
        styleSheet.addRule("h6 {font-size:"+(25*offset)+"; font-weight:bold}");
        styleSheet.addRule("div {height: +"+(10*offset)+"px; font-size:0px; background:#DEDEDE; padding:"+(2*offset)+"; margin-top:"+(15*offset)+"; margin-bottom:"+(15*offset)+"}");

        editorKit = new HTMLEditorKit();
        editorKit.setStyleSheet(styleSheet);

        webView.setEditorKit(editorKit);
        webView.setText(content);
    }
}
