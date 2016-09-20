import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import EasySwing.BoxLayoutUtils;
import UserContainer.*;

/**
 * Created by Roman on 26.06.2016.
 */
public class DirView implements ActionListener, ListSelectionListener
{
    private  JTextField jtfDir,jtfFName;
    private JLabel jlblDir,jlblFName,jlblBeforList,jlblInfo;
    private JButton jbtnStart, jbtnFind ;
    private JList <String> jlstFNames;
    private JScrollPane jscrlp;
     private DefaultListModel listModel;
    private Queue<String> qFilesNames;
    private File[] filesList;


    DirView()
    {
        filesList=null;
        qFilesNames  = new Queue<>();
        JPanel jpnlLeft = BoxLayoutUtils.createVerticalPanel();

            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(20) );
            jpnlLeft.add( BoxLayoutUtils.createHorizontalStrut(20) );

            jlblDir = new JLabel("Введите путь к исследуемой директории ");
            jpnlLeft.add(jlblDir);

            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(10) );
            jpnlLeft.add( BoxLayoutUtils.createHorizontalStrut(20) );


            jtfDir = new JTextField(10);
            jpnlLeft.add(jtfDir);

            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(50) );

            jlblFName = new JLabel("Введите имя выходного файла");
            jpnlLeft.add(jlblFName);

            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(10) );

            jtfFName = new JTextField(20);
            jpnlLeft.add(jtfFName);

            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(80) );

            jlblInfo = new JLabel("");
            jpnlLeft.add(jlblInfo);
            jpnlLeft.add( BoxLayoutUtils.createVerticalStrut(10) );

        JPanel jpnlRight = BoxLayoutUtils.createVerticalPanel();
            jpnlRight.setAlignmentX(Component.CENTER_ALIGNMENT);

            jpnlRight.add( BoxLayoutUtils.createVerticalStrut(30) );

            jlblBeforList = new JLabel("Cодержимое директории");
            jpnlRight.add(jlblBeforList);
            jpnlRight.add( BoxLayoutUtils.createVerticalStrut(30) );


             //DefaultListModel listModel = new DefaultListModel();
            jlstFNames = new JList<>();
            jlstFNames.setModel(new DefaultListModel());
            jlstFNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       jlstFNames.addListSelectionListener(this);

            jscrlp = new JScrollPane(jlstFNames);
            jscrlp.setPreferredSize(new Dimension(100,90));
            jpnlRight.add(jscrlp);


            jpnlRight.add( BoxLayoutUtils.createVerticalStrut(30) );
        JPanel jpnlButton = new JPanel();
        jpnlButton.setLayout(new FlowLayout());
            jbtnStart = new JButton("Старт");
            jbtnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
            jbtnStart.setAlignmentY(Component.CENTER_ALIGNMENT);

            jbtnStart.addActionListener(this);

        jbtnFind = new JButton("Поиск");
        jbtnFind .setAlignmentX(Component.CENTER_ALIGNMENT);
        jbtnFind .setAlignmentY(Component.CENTER_ALIGNMENT);

        jbtnFind .addActionListener(this);
        jpnlButton.add(jbtnStart);
        jpnlButton.add(jbtnFind);

            jpnlRight.add(jpnlButton);
            jpnlRight.add( BoxLayoutUtils.createVerticalStrut(30) );

    JPanel jpnlHorizontal =  BoxLayoutUtils.createHorizontalPanel();
        jpnlHorizontal.add(BoxLayoutUtils.createHorizontalStrut(20));
        jpnlHorizontal.add(jpnlLeft);
        jpnlHorizontal.add(BoxLayoutUtils.createHorizontalStrut(30));
        jpnlHorizontal.add(jpnlRight);
        jpnlHorizontal.add(BoxLayoutUtils.createHorizontalStrut(30));

        JFrame jfrm= new JFrame ("Dir View");
        jfrm.setSize(560,320);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jfrm.setLayout(new GridLayout(0,1,30,5));
        jfrm.add(jpnlHorizontal);
       // jfrm.add(jpnlLeft);
       // jfrm.add(jpnlRight);

        jfrm.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource()==jbtnFind)
        {
            String pathToDir = jtfDir.getText();
            if(pathToDir.equals(""))
            {
                jlblInfo.setText("Введите путь к директории! ");
                return;
            }
            jlblInfo.setText("");
          //  File fileDir = new File(pathToDir);
            try {
                 filesList = getFilesListFromDir(pathToDir);

                updateListView(filesList);
            }
            catch(GenericExeption exc)
            {
                jlblInfo.setText(exc.toString());
            }
        }
        else if(ae.getSource()==jbtnStart)
        {
            String pathToDir = jtfDir.getText();
            if(pathToDir.equals(""))
            {
                jlblInfo.setText("Введите путь к директории! ");
                return;
            }
            String outFileName=jtfFName.getText();
            if( outFileName.equals(""))
            {
                jlblInfo.setText("Введите имя файла! ");
                return;
            }
            jlblInfo.setText("");
            String pathToFileWrite = pathToDir+System.getProperty("file.separator")+outFileName;
            try {
                filesList = getFilesListFromDir(pathToDir);

                updateListView(filesList);
                SafeWriterFile.clear(pathToFileWrite);
                }
            catch(GenericExeption exc)
            {
                jlblInfo.setText(exc.toString());
            }



            qFilesNames = new Queue<>();
            getNamesToStringQueue(filesList,qFilesNames);

            Runnable taskParseFiles = ()->
            {
                System.out.println(Thread.currentThread().getName());
                while(!qFilesNames.isEmpty())
                {
                    String fNameQueue;
                    try
                    {
                         fNameQueue = qFilesNames.pop();
                        if (outFileName.equals(fNameQueue)) continue;
                        String pathToFileRead  =  pathToDir+System.getProperty("file.separator")+fNameQueue;



                        try
                        {
                           ArrayList<String> strSplitBuf = SplitFiles.run(pathToFileRead);
                            SafeWriterFile.write(strSplitBuf,pathToFileWrite);
                        }
                        catch(GenericExeption ex)
                        {
                            jlblInfo.setText(jlblInfo.getText()+ex.toString());
                        }



                    }
                    catch(QueueEmptyException ex)
                    {
                        jlblInfo.setText(ex.toString());
                    }

                }
            };

            ExecutorService executor = Executors.newFixedThreadPool(5);
            for(int i=0; i<5; i++)
            {
                 executor.execute(taskParseFiles);
            }
            executor.shutdown();
            jlblInfo.setText(jlblInfo.getText()+System.getProperty("line.separator")+'\n'+"Обработка завершена!");

        }


    }


    @Override
    public void valueChanged(ListSelectionEvent lse)
    {
    if(!jlstFNames.isSelectionEmpty())
    {
        String nameFileOut = jlstFNames.getSelectedValue();
        jtfFName.setText(nameFileOut);
    }

    }

    public final File [] getFilesListFromDir(String pathToDir) throws GenericExeption
    {
        File fileDir = new File(pathToDir);

        if(!fileDir.exists()) throw new GenericExeption("Директории не существует!");

        final FileFilter filter = (dir) -> {
            return !dir.isDirectory();
        };

        File[] filesList = fileDir.listFiles(filter

        );
        return filesList;
    }
public void updateListView(File[] filesList)
    {
       int indexFileInList=0;
        DefaultListModel model = (DefaultListModel)jlstFNames.getModel();
        model.clear();
        for(File file:filesList)
        {
            String strFile = file.getName();
            model.add(indexFileInList,strFile);
            indexFileInList++;
        }

    }

    private void getNamesToStringQueue (File[] files, Queue<String> queueFNames)
    {

        for(File file:files)
        {
            queueFNames.pushBack(file.getName());
        }

    }

    public static void main (String...args)
    {


        SwingUtilities.invokeLater(

                new Runnable() {
            @Override
            public void run()
            {
                new DirView();
            }
        }
        );


    }
}
