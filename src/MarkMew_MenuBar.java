import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarkMew_MenuBar extends JMenuBar {

    private MainFrame frame;

    MarkMew_MenuBar(MainFrame frame){
        super();

        this.frame = frame;

        // File Menu
        menu_File = new JMenu("File");
        this.add(menu_File);

        item_New = new JMenuItem("New");
        item_Open = new JMenuItem("Open");
        item_Save = new JMenuItem("Save");
        item_Save_As = new JMenuItem("Save As");
        item_Close = new JMenuItem("Close");
        item_Exit = new JMenuItem("Exit");

        item_Export_html = new JMenuItem("Export As HTML");

        menu_File.add(item_New);
        menu_File.add(item_Open);
        menu_File.add(item_Close);
        menu_File.addSeparator();
        menu_File.add(item_Save);
        menu_File.add(item_Save_As);
        menu_File.addSeparator();
        menu_File.add(item_Export_html);
        menu_File.addSeparator();
        menu_File.add(item_Exit);

        // Edit Menu
        menu_Edit = new JMenu("Edit");
        this.add(menu_Edit);

        item_Undo = new JMenuItem("Undo");
        item_Redo = new JMenuItem("Redo");
        item_Cut = new JMenuItem("Cut");
        item_Copy = new JMenuItem("Copy");
        item_Paste = new JMenuItem("Paste");
        item_Delete = new JMenuItem("Delete");
        item_Find = new JMenuItem("Find");
        item_Select_All = new JMenuItem("Select All");

        menu_Edit.add(item_Undo);
        menu_Edit.add(item_Redo);
        menu_Edit.addSeparator();
        menu_Edit.add(item_Cut);
        menu_Edit.add(item_Copy);
        menu_Edit.add(item_Paste);
        menu_Edit.add(item_Delete);
        menu_Edit.addSeparator();
        menu_Edit.add(item_Find);
        menu_Edit.add(item_Select_All);

        setComponentListener();
    }

    private void setComponentListener(){

        JTabbedPane tabbedPane = frame.getTabbedPane();
        JLabel      stateLabel = frame.getStateLabel();

        String[] filter_desc = new String[]{"Markdown File (*.md)", "Text File (*.txt)"};
        String[] filter_ext  = new String[]{"md", "txt"};

        item_New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedComponent(new MarkMew_Tab(tabbedPane, "Untitled"));
                stateLabel.setText("New tab is added");
            }
        });

        item_Close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MarkMew_Tab tab = (MarkMew_Tab) tabbedPane.getSelectedComponent();

                if ((tab.hasFile() && !tab.getIsSaved()) || (!tab.hasFile() && tab.hasContent())) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Content has modified, save changes?",
                            "Save Changed?",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {

                        if(tab.hasFile()) {
                            tab.saveFile(null);
                            tabbedPane.remove(tabbedPane.getSelectedIndex());

                            // update indexing
                            int count = tabbedPane.getTabCount();
                            for(int i=0; i<count; i++){
                                ((MarkMew_Tab)tabbedPane.getComponentAt(i)).setIndex(i);
                            }

                            stateLabel.setText("Selected tab is removed");
                            return;
                        }

                        String[] filter_desc = new String[]{"Markdown File (*.md)", "Text File (*.txt)"};
                        String[] filter_ext = new String[]{"md", "txt"};

                        JFileChooser saveDialog = new JFileChooser();
                        saveDialog.setAcceptAllFileFilterUsed(false);
                        for (int i = 0; i < filter_desc.length; i++)
                            saveDialog.addChoosableFileFilter(new FileNameExtensionFilter(filter_desc[i], filter_ext[i]));

                        if (saveDialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                            if (saveDialog.getSelectedFile().exists()) {
                                int confirm = JOptionPane.showConfirmDialog(null,
                                        "Do you want to overwrite the file?",
                                        "File Duplication",
                                        JOptionPane.YES_NO_OPTION);

                                if (confirm != JOptionPane.YES_OPTION) return;
                            }

                            // get file path
                            String path = saveDialog.getSelectedFile().getAbsolutePath();
                            String extension = ((FileNameExtensionFilter) saveDialog.getFileFilter()).getExtensions()[0];
                            if (!path.endsWith(extension))
                                path += "." + extension;

                            tab.saveFile(path);
                        }
                    }

                }

                // remove tab
                tabbedPane.remove(tabbedPane.getSelectedIndex());

                // update indexing
                int count = tabbedPane.getTabCount();
                for(int i=0; i<count; i++){
                    ((MarkMew_Tab)tabbedPane.getComponentAt(i)).setIndex(i);
                }

                stateLabel.setText("Selected tab is removed");
            }
        });

        item_Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ask whether save or not about all tabs

                for(int i=0; i<tabbedPane.getTabCount(); i++){
                    MarkMew_Tab tab = (MarkMew_Tab) tabbedPane.getComponentAt(0);
                    tabbedPane.setSelectedIndex(0);

                    if ((tab.hasFile() && !tab.getIsSaved()) || (!tab.hasFile() && tab.hasContent())) {
                        int result = JOptionPane.showConfirmDialog(null,
                                tab.getTitle() + " has modified, save changes?",
                                "Save Changed?",
                                JOptionPane.YES_NO_OPTION);

                        if (result == JOptionPane.YES_OPTION) {

                            if(tab.hasFile()) {
                                tab.saveFile(null);
                                tabbedPane.remove(0);
                                return;
                            }

                            String[] filter_desc = new String[]{"Markdown File (*.md)", "Text File (*.txt)"};
                            String[] filter_ext = new String[]{"md", "txt"};

                            JFileChooser saveDialog = new JFileChooser();
                            saveDialog.setAcceptAllFileFilterUsed(false);
                            for (int j = 0; j < filter_desc.length; j++)
                                saveDialog.addChoosableFileFilter(new FileNameExtensionFilter(filter_desc[j], filter_ext[j]));

                            if (saveDialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                                if (saveDialog.getSelectedFile().exists()) {
                                    int confirm = JOptionPane.showConfirmDialog(null,
                                            "Do you want to overwrite the file?",
                                            "File Duplication",
                                            JOptionPane.YES_NO_OPTION);

                                    if (confirm != JOptionPane.YES_OPTION) return;
                                }

                                // get file path
                                String path = saveDialog.getSelectedFile().getAbsolutePath();
                                String extension = ((FileNameExtensionFilter) saveDialog.getFileFilter()).getExtensions()[0];
                                if (!path.endsWith(extension))
                                    path += "." + extension;

                                tab.saveFile(path);
                            }
                        }

                    }

                    tabbedPane.remove(0);
                }

                System.exit(0);
            }
        });

        item_Open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int count = 0;

                JFileChooser openDialog = new JFileChooser();
                openDialog.setMultiSelectionEnabled(true);
                openDialog.setAcceptAllFileFilterUsed(false);
                for(int i=0; i<filter_desc.length; i++)
                    openDialog.addChoosableFileFilter(new FileNameExtensionFilter(filter_desc[i], filter_ext[i]));

                if(openDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){

                    File[] files = openDialog.getSelectedFiles();
                    for(int i=0; i<files.length; i++){

                        // is the file already opened?
                        boolean duplication = false;
                        for(int j=0; j<tabbedPane.getTabCount(); j++){
                            if(((MarkMew_Tab)tabbedPane.getComponentAt(j)).compareFile(files[i])) {
                                tabbedPane.setSelectedIndex(j);
                                duplication = true;
                                break;
                            }
                        }
                        if(duplication) continue;

                        MarkMew_Tab tab = new MarkMew_Tab(tabbedPane, files[i].getName());
                        tab.openFile(files[i]);
                        tabbedPane.setSelectedComponent(tab);

                        count++;
                    }

                    stateLabel.setText(count+" file(s) opened and " + (files.length-count) + " file(s) already opened on MarkMew");

                }
            }
        });

        item_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MarkMew_Tab current = (MarkMew_Tab)tabbedPane.getSelectedComponent();
                if(current.hasFile()) {
                    current.saveFile(null);
                    return;
                }

                JFileChooser saveDialog = new JFileChooser();
                saveDialog.setAcceptAllFileFilterUsed(false);
                for(int i=0; i<filter_desc.length; i++)
                    saveDialog.addChoosableFileFilter(new FileNameExtensionFilter(filter_desc[i], filter_ext[i]));

                if(saveDialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){

                    if(saveDialog.getSelectedFile().exists()){
                        int result = JOptionPane.showConfirmDialog(null,
                                "Do you want to overwrite the file?",
                                "File Duplication",
                                JOptionPane.YES_NO_OPTION);

                        if(result != JOptionPane.YES_OPTION) return;
                    }

                    // get file path
                    String path = saveDialog.getSelectedFile().getAbsolutePath();
                    String extension = ((FileNameExtensionFilter)saveDialog.getFileFilter()).getExtensions()[0];
                    if(!path.endsWith(extension))
                        path += "." + extension;

                    ((MarkMew_Tab)tabbedPane.getSelectedComponent()).saveFile(path);
                    stateLabel.setText("Saved successfully on " + path);
                }
            }
        });

        item_Save_As.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveDialog = new JFileChooser();
                saveDialog.setAcceptAllFileFilterUsed(false);
                for(int i=0; i<filter_desc.length; i++)
                    saveDialog.addChoosableFileFilter(new FileNameExtensionFilter(filter_desc[i], filter_ext[i]));

                if(saveDialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {

                    if (saveDialog.getSelectedFile().exists()) {
                        int result = JOptionPane.showConfirmDialog(null,
                                "Do you want to overwrite the file?",
                                "File Duplication",
                                JOptionPane.YES_NO_OPTION);

                        if (result != JOptionPane.YES_OPTION) return;
                    }

                    // get file path
                    String path = saveDialog.getSelectedFile().getAbsolutePath();
                    String extension = ((FileNameExtensionFilter) saveDialog.getFileFilter()).getExtensions()[0];
                    if (!path.endsWith(extension))
                        path += "." + extension;

                    ((MarkMew_Tab) tabbedPane.getSelectedComponent()).saveFile(path);

                    stateLabel.setText("Saved successfully on" + path);
                }
            }
        });

        item_Export_html.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MarkMew_Tab currentTab = (MarkMew_Tab)tabbedPane.getSelectedComponent();
                if(currentTab==null) return;


                JFileChooser saveDialog = new JFileChooser();
                saveDialog.setAcceptAllFileFilterUsed(false);
                saveDialog.addChoosableFileFilter(new FileNameExtensionFilter("HTML File (*.html)", "html"));

                if(saveDialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {

                    if (saveDialog.getSelectedFile().exists()) {
                        int result = JOptionPane.showConfirmDialog(null,
                                "Do you want to overwrite the file?",
                                "File Duplication",
                                JOptionPane.YES_NO_OPTION);

                        if (result != JOptionPane.YES_OPTION) return;
                    }

                    // get file path
                    String path = saveDialog.getSelectedFile().getAbsolutePath();
                    String extension = ((FileNameExtensionFilter) saveDialog.getFileFilter()).getExtensions()[0];
                    if (!path.endsWith(extension))
                        path += "." + extension;

                    String content = frame.getParser().parser(currentTab.getContent());
                    try {
                        File file = new File(path);
                        FileWriter fw = new FileWriter(file);
                        fw.write(content);
                        fw.close();
                    }
                    catch (IOException e1){
                        e1.printStackTrace();
                    }

                    stateLabel.setText("Saved successfully on " + path);
                }
            }
        });
    }


    private JMenu menu_File;
    private JMenu menu_Edit;

    private JMenuItem item_New;
    private JMenuItem item_Open;
    private JMenuItem item_Save;
    private JMenuItem item_Save_As;
    private JMenuItem item_Close;
    private JMenuItem item_Export_html;
    private JMenuItem item_Exit;

    private JMenuItem item_Undo;
    private JMenuItem item_Redo;
    private JMenuItem item_Cut;
    private JMenuItem item_Copy;
    private JMenuItem item_Paste;
    private JMenuItem item_Delete;
    private JMenuItem item_Find;
    private JMenuItem item_Select_All;

}
