package xml_parser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

public class Main {
	static Main main_ins=null;

	File selected_file=null;
	FileWriter fr;
	BufferedWriter br;
	FileWriter fr1;
	BufferedWriter br1;
	   JFrame frame=null;
	  JPanel panel=null;
	   JScrollPane pane=null;
	   JTextArea textarea=null;
	   Container contentpane=null;
	   JButton start_button,tree_button,displaygui,displayfinalgui,save=null;
	   ArrayList<node_class> list=null;
	   Parser xml_parser=null;
	   Tree_class tree=null;
	   ArrayList<Tree_class> two_nodes;
	   int list_count=0;
	   int startx=0,starty=0;
	   JTree jtree;
   public static void main(String[] args){
	  main_ins=new Main();
	  main_ins.start();
	 
   }


   public void start(){
	   frame=new JFrame();
	   panel=new JPanel();
	   textarea=new JTextArea(25,41);
	   textarea.setEditable(false);
	   start_button=new JButton();
	   panel.add(textarea);
	   pane=new JScrollPane(panel);
	   contentpane=frame.getContentPane();
	   contentpane.setLayout(new BorderLayout());
	   contentpane.add(pane,BorderLayout.CENTER);
	   start_button.setText("Choose File");
	   contentpane.add(start_button,BorderLayout.SOUTH);
	   textarea.setText("Choose an XML file to start");
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setSize(500, 500);
	   frame.show();
	   
	   start_button.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e){
				  JFileChooser chooser=new JFileChooser();
				  FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
				  chooser.setDialogTitle("Open XML file");
				  chooser.setFileFilter(xmlfilter);
		            chooser.setFileSelectionMode( JFileChooser.OPEN_DIALOG);
		            chooser.showOpenDialog( null );
				  selected_file=chooser.getSelectedFile();
				  textarea.setText("Parsing your XML....\n Please wait...");
				  xml_parser=new Parser(selected_file,main_ins);
				  xml_parser.parse();
				  if(xml_parser.EOF){
				  textarea.append("\n hello end ");
				  preparing_tree();
				  }
			  }

			
		  });
   }
   private void preparing_tree() {

	   list=new ArrayList<node_class>();
	   list=xml_parser.nodelist;
	   textarea.setText("");
	   for(int u=0;u<list.size();u++){
			node_class nd=list.get(u);
			textarea.append(nd.node_name +"\n");
			textarea.append("     String :"+nd.node_string +"\n");
			textarea.append("     Value :"+nd.node_value + "\n");
			textarea.append("     no_of_nodes :"+String.valueOf(nd.no_of_nodes)+"\n");
		}
	   tree_button=new JButton("Make Tree");
	   contentpane.remove(start_button);
	   frame.revalidate();
		contentpane.add(tree_button,BorderLayout.SOUTH);
		frame.revalidate();
		
		tree_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				make_n_show_tree();
				
			}

		});
	}
   

	public void make_n_show_tree() {

		tree=new Tree_class();
		Tree_class treedupe=tree;
		treedupe.name=list.get(list_count).node_name;
		treedupe.number=list_count;
	    complete_the_tree(treedupe,list.get(list_count).no_of_nodes);
	    textarea.setText("");
	    Tree_class treedupedisplay=tree;
	    textarea.append(treedupedisplay.name+"\n");
		display_tree(treedupedisplay,treedupedisplay.tree_list.size());
		displaygui=new JButton("Display JTree");
		 contentpane.remove(tree_button);
		   frame.revalidate();
			contentpane.add(displaygui,BorderLayout.SOUTH);
			frame.revalidate();
			
			displaygui.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					display_gui();
					
				}
				
			});
	}
	


	private void display_tree(Tree_class treedupedisplay, int size) {

		for(int i=0;i<size;i++){
             if(treedupedisplay.tree_list.get(i).tree_list.size()==0){
            	 textarea.append(treedupedisplay.tree_list.get(i).name+"  "+treedupedisplay.tree_list.get(i).number+" "+i+"\n");
             }else{
            	 textarea.append(treedupedisplay.tree_list.get(i).name+"  "+treedupedisplay.tree_list.get(i).number+"  "+i+"\n");
            	 Tree_class tr=treedupedisplay.tree_list.get(i);
            	 display_tree(tr,tr.tree_list.size());
             }
		}
	}


	private void complete_the_tree(Tree_class treedupe,int no_of_nodes) {

		for(int i=0;i<no_of_nodes;i++){
			list_count++;
			if(list.get(list_count).no_of_nodes==0){
				Tree_class tr=new Tree_class();
				tr.name=list.get(list_count).node_name;
				tr.number=list_count;
				treedupe.tree_list.add(tr);
			}else{
				Tree_class tr=new Tree_class();
				tr.name=list.get(list_count).node_name;
				tr.number=list_count;
				treedupe.tree_list.add(tr);
				complete_the_tree(tr,list.get(list_count).no_of_nodes);
			}
		}
	}

	


	private void display_gui() {

		textarea.append("hai");
		contentpane.remove(displaygui);
		frame.revalidate();
		panel.remove(textarea);
		frame.revalidate();
	//	Tree_gui_builder tr_gui_bld=new Tree_gui_builder(tree,0,0);
		pane.remove(panel);
		contentpane.remove(pane);
	//	contentpane.add(tr_gui_bld);
		frame.revalidate();
		Tree_class treedupedisplay=tree;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(treedupedisplay.name+"  ( "+list.get(treedupedisplay.number).node_value+" , "+list.get(treedupedisplay.number).node_string+" )");
		jtree_show(treedupedisplay,treedupedisplay.tree_list.size(),root);
		jtree=new JTree(root);
		contentpane.setLayout(new BorderLayout());
		contentpane.add(jtree,BorderLayout.CENTER);
		displayfinalgui=new JButton("Proceed to GUI");
		contentpane.add(displayfinalgui,BorderLayout.SOUTH);
		frame.revalidate();
		displayfinalgui.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				InteractiveGUI();
			}

			
		});
	}


	private void jtree_show(Tree_class treedupedisplay,int size,DefaultMutableTreeNode root) {

		for(int i=0;i<size;i++){
            if(treedupedisplay.tree_list.get(i).tree_list.size()==0){
            	DefaultMutableTreeNode rt=new DefaultMutableTreeNode(treedupedisplay.tree_list.get(i).name+"  ( "+list.get(treedupedisplay.tree_list.get(i).number).node_value+" , "+list.get(treedupedisplay.tree_list.get(i).number).node_string+" )");
            	root.add(rt);
            }else{
            	DefaultMutableTreeNode rt=new DefaultMutableTreeNode(treedupedisplay.tree_list.get(i).name+"  ( "+list.get(treedupedisplay.tree_list.get(i).number).node_value+" , "+list.get(treedupedisplay.tree_list.get(i).number).node_string+" )");
            	root.add(rt);
           	 Tree_class tr=treedupedisplay.tree_list.get(i);
           	 jtree_show(tr,tr.tree_list.size(),rt);
            }
		}
	}
	
	
	
	public void InteractiveGUI() {
		contentpane.remove(jtree);
		contentpane.remove(displayfinalgui);
		frame.remove(contentpane);
		save = new JButton("Save");
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				 Tree_class treedupedisplay1=tree;
				    textarea.append(treedupedisplay1.name+"\n");
					print_display_tree(treedupedisplay1,treedupedisplay1.tree_list.size());
				try{
					 fr=new FileWriter(selected_file);
					 br=new BufferedWriter(fr);
					 Tree_class treedupedisplay=tree;

					 String name=treedupedisplay.name;

					    textarea.append(treedupedisplay.name+"\n");
					    br.append("<?xml version=\"1.0\"?>");
					    br.newLine();
					    br.flush();
					    br.append("<"+treedupedisplay.name+" "+list.get(treedupedisplay.number).node_value+">  "+list.get(treedupedisplay.number).node_string);
						save_to_file(treedupedisplay,treedupedisplay.tree_list.size());

						br.flush();
						fr.close();
					/*	FileReader frw=new FileReader(selected_file);
						BufferedReader brw=new BufferedReader(frw);

						String hai="";
						String hg="";
						while((hg=brw.readLine())!=null){
							hai=hai+hg+"\n";
						}
						fr=new FileWriter(selected_file);
						 br=new BufferedWriter(fr);
						 hai=hai+"</"+name+">";
						 System.out.println(hai); */
						//br.write(hai);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

			
		});
		frame.add(save,BorderLayout.SOUTH);
		frame.revalidate();
		frame.setVisible(false);
		frame.setVisible(true);
		panel=new JPanel();
		  panel.setLayout(null);
		  two_nodes=new ArrayList<Tree_class>();
		Tree_gui_builder builder_gui=new Tree_gui_builder(tree,startx,starty,panel,frame,list,two_nodes);
		builder_gui.start();
		panel.addMouseListener(new MouseAdapter(){
			int x=0;
			int y=0;
			public void mousePressed(MouseEvent e){
				x=e.getX();
				y=e.getY();
        }
			public void mouseReleased(MouseEvent e){
				x=e.getX()-x;
				y=e.getY()-y;
				startx=startx+x;
				starty=starty+y;
				panel.removeAll();
				frame.setVisible(false);
				frame.setVisible(true);
				Tree_gui_builder builder_gui=new Tree_gui_builder(tree,startx,starty,panel,frame,list,two_nodes);
				builder_gui.start();
	        }
		});
	}
	
	
	private void save_to_file(Tree_class treedupedisplay, int size) {


		for(int i=0;i<size;i++){
			try{
            if(treedupedisplay.tree_list.get(i).tree_list.size()==0){
           	  br.newLine();
			    br.flush();
			    br.append("<"+treedupedisplay.tree_list.get(i).name+" "+list.get(treedupedisplay.tree_list.get(i).number).node_value+">  "+list.get(treedupedisplay.tree_list.get(i).number).node_string);
			    br.flush();
				br.append("</"+treedupedisplay.tree_list.get(i).name+">");
            }else{
           	 br.newLine();
			    br.flush();
			    br.append("<"+treedupedisplay.tree_list.get(i).name+" "+list.get(treedupedisplay.tree_list.get(i).number).node_value+">  "+list.get(treedupedisplay.tree_list.get(i).number).node_string);
				 Tree_class tr=treedupedisplay.tree_list.get(i);
           	 save_to_file(tr,tr.tree_list.size());
            }
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	

	private void print_display_tree(Tree_class treedupedisplay,
			int size) {
		for(int i=0;i<size;i++){
            if(treedupedisplay.tree_list.get(i).tree_list.size()==0){
           	 System.out.print(treedupedisplay.tree_list.get(i).name+"  "+treedupedisplay.tree_list.get(i).number+" "+i+"\n");
            }else{
           	 System.out.print(treedupedisplay.tree_list.get(i).name+"  "+treedupedisplay.tree_list.get(i).number+"  "+i+"\n");
           	 Tree_class tr=treedupedisplay.tree_list.get(i);
           	 display_tree(tr,tr.tree_list.size());
            }
		}
		
	}

}
