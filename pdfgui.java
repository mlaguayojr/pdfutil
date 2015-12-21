import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class pdfgui {

	protected static JLabel l1;

	public static void main(String[] args) {
		ArrayList<String> files = new ArrayList<String>();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
			JFrame.setDefaultLookAndFeelDecorated(false);
		}

		JFrame f = new JFrame("PDF Merger");
		JPanel p = new JPanel(null);


		JMenuBar mb = new JMenuBar();
		f.setJMenuBar(mb);

		JMenu file = new JMenu("File");
		mb.add(file);

		JMenuItem about = new JMenuItem("About");
		file.add(about);
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(f, "PDF Merger\n(2015) Mario Aguayo (mlaguayojr@gmail.com)");
			}

		});

		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);

		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		l1 = new JLabel("Merge PDFs: "+files.size());
		l1.setBounds(10,10,100,25); //x, y, width, height

		JList pdfs = new JList(files.toArray());

		JButton rem = new JButton("Remove");
		rem.setBounds(320,65, 100, 25);
		rem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				files.remove(pdfs.getSelectedIndex());
				f.update(f.getGraphics());
				f.repaint();
				f.revalidate();
				pdfs.setListData(files.toArray());
			}

		});

		JButton merge = new JButton("Merge All");
		merge.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new FileNameExtensionFilter("PDF Document (*.pdf)","pdf"));
				fc.setDialogTitle("Save Merged PDF to:");
				fc.setCurrentDirectory(new File(System.getProperty("user.home")));
				fc.showSaveDialog(f);
				String file = fc.getSelectedFile().toString();
				if (!file.contains(".pdf")) {
					file+=".pdf";
				}

				try {
					//pdf.exe needs 3 params
					if(files.size()==2){
						Process p = new ProcessBuilder(System.getProperty("user.dir")+"\\pdf.exe",file,files.get(0).toString(),files.get(1).toString()).start();
					}else{
						Process p = new ProcessBuilder(System.getProperty("user.dir")+"\\pdf.exe",file,files.get(0).toString(),files.get(1).toString()).start();
						for(int i = 2; i < files.size(); i++){
							p = new ProcessBuilder(System.getProperty("user.dir")+"\\pdf.exe",file,files.get(i).toString(),"").start();
						}
					}
					
					if(new File(file).exists()){
						JOptionPane.showMessageDialog(f, file+" was saved!");
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		merge.setBounds(320,95, 100, 25);

		JButton add = new JButton("Add File");
		add.setBounds(320,35, 100, 25);
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addFile();

			}

			private void addFile() {
				pdfs.clearSelection();
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Browse For File");
				fc.setCurrentDirectory(new File(System.getProperty("user.home")));
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new FileNameExtensionFilter("PDF Document","pdf"));
				fc.setAcceptAllFileFilterUsed(false);
				fc.showOpenDialog(null);
				if(fc.accept(fc.getSelectedFile())){
					if(files.isEmpty() || !files.contains(fc.getSelectedFile().toString())){
						files.add(fc.getSelectedFile().toString());
					}else{
						JOptionPane.showMessageDialog(null, fc.getSelectedFile().getName()+" is already in the queue!");
					}
				}
				if(files.size()>=2){
					rem.setEnabled(true);
					merge.setEnabled(true);
				}else{
					merge.setEnabled(false);
				}
				pdfs.setListData(files.toArray());
				l1.setText("Merge PDFs: "+files.size());
			}
		});

		pdfs.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList)evt.getSource();
				if (evt.getClickCount() == 2) {
					try {
						Desktop.getDesktop().open(new File(list.getSelectedValue().toString()).getAbsoluteFile());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(f, "Cannot open! Was the file deleted, renamed, or relocated?");
					}
				}
			}
		});

		pdfs.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {

				if(files.isEmpty()){
					rem.setEnabled(false);
					merge.setEnabled(false);
				}else if(files.size()==2){
					merge.setEnabled(true);
				}else{
					merge.setEnabled(false);
				}
				pdfs.setToolTipText("Double click to open "+new File(pdfs.getSelectedValue().toString()).getName().toString());
				rem.setEnabled(true);
			}

		});

		pdfs.setBackground(Color.white);
		pdfs.setAutoscrolls(true);

		JScrollPane s = new JScrollPane(pdfs);
		s.setViewportView(pdfs);
		s.setAutoscrolls(true);
		s.setBounds(10, 35, 300, 300);

		p.add(l1);
		p.add(s);
		p.add(add);
		p.add(rem);
		p.add(merge);

		rem.setEnabled(false);
		merge.setEnabled(false);

		f.add(p);
		f.setVisible(true);
		f.setSize(new Dimension(440, 400)); //w, h
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
	}

}
