import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;

public class MarkMew_Tab extends JPanel{
    private JTextPane textEditor;
    private JPanel mainPanel;

    private JTabbedPane parent;
    private String title;
    private int index;

    private boolean isSaved;

    private File file;
    private FileReader fileReader;
    private BufferedReader bufferedReader;

    private Tab_Panel tabPanel;

    MarkMew_Tab(JTabbedPane _parent, String _title){

        super();

        parent = _parent;
        title = _title;

        isSaved = false;
        file = null;

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        parent.add(title, this);
        index = parent.indexOfComponent(this);

        tabPanel = new Tab_Panel(parent, this, index);

        textEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                unsaveFile();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                unsaveFile();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                unsaveFile();
            }
        });
    }

    public void openFile(File _file){
        file = _file;

        try{
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            String text = "";
            while((line = bufferedReader.readLine())!=null)
                text += line + "\n";

            textEditor.setText(text);
            tabPanel.saved();
            isSaved = true;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public void saveFile(String path){

        try {

            if(path==null){
                // save at file variable
                FileWriter fw = new FileWriter(file);
                fw.write(textEditor.getText());
                fw.close();
            }
            else{
                // save at path variable
                file = new File(path);
                FileWriter fw = new FileWriter(file);
                fw.write(textEditor.getText());
                fw.close();

                // change title (for save as)
                title = file.getName();
                tabPanel.setTitle(title);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        isSaved = true;
        tabPanel.saved();
    }

    public void unsaveFile(){
        if(!isSaved) return;

        isSaved = false;
        tabPanel.unsaved();
    }

    public boolean hasFile(){
        if(file == null) return false;
        return true;
    }

    public boolean hasContent(){
        if(textEditor.getText().equals("")) return false;
        return true;
    }

    public boolean compareFile(File f){
        if(file==null) return false;
        return file.equals(f);
    }

    public boolean getIsSaved(){
        return isSaved;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public String getContent(){
        return textEditor.getText();
    }
}
