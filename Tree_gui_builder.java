package xml_parser;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Tree_gui_builder extends JComponent{

	Tree_class tree;
	int startx,starty;
	JFrame frame;
	int gape=20;
	int height=30;
	int width=150;
	int level=0;
	JPanel panel=null;
	ArrayList<level_record> level_list;
	ArrayList<JButton> buttons;
	ArrayList<Tree_class> list_tree;
	ArrayList<node_class> node_list;
	ArrayList<tree_gui_coord> list_coord_tree;
	ArrayList<Tree_class> two_nodes;
	tree_gui_coord tree_coord;
	Tree_gui_builder(Tree_class tr,int x,int y,JPanel pl,JFrame frm,ArrayList<node_class> nl,ArrayList<Tree_class> tn){
		tree=tr;
		startx=x;
		starty=y;
		panel=pl;
		frame=frm;
		node_list=nl;
		two_nodes=tn;
	}
	
	public void start(){
		buttons=new ArrayList<JButton>();
		level_list=new ArrayList<level_record>();
		list_tree=new ArrayList<Tree_class>();
		list_coord_tree= new ArrayList<tree_gui_coord>();
		tree_coord=new tree_gui_coord();
		tree_coord.x=startx+0;
		tree_coord.y=starty+0;
		tree_coord.number=tree.number;
		tree_gui_coord dupe_tree_coord=tree_coord;
		Tree_class treedupedisplay=tree;
		list_tree.add(treedupedisplay);
		fill_level_list(list_tree.size(),1);
	/*	for(int u=0;u<level_list.size();u++){
			System.out.println(level_list.get(u).level+ "  "+ level_list.get(u).number_of_child);
		} */
		list_tree.clear();
		treedupedisplay=tree;
		list_tree.add(treedupedisplay);
		list_coord_tree.add(dupe_tree_coord);
		fill_coord(list_tree.size(),0);
		dupe_tree_coord=tree_coord;
		   // System.out.println(dupe_tree_coord.x+"   "+dupe_tree_coord.y);
			//display_coord(dupe_tree_coord,dupe_tree_coord.list.size());
			paint_screen();
	}
	
	
	
	
	private void fill_level_list(int size,int level) {

		int total=0;
		for(int i=0;i<size;i++){
			Tree_class treedd=list_tree.remove(0);
			total=total+treedd.tree_list.size();
			for(int y=0;y<treedd.tree_list.size();y++){
				list_tree.add(treedd.tree_list.get(y));
			}
		}
		if(total!=0){
		level_record rec=new level_record();
		rec.number_of_child=total;
		rec.level=level;
		level_list.add(rec);
		level++;
		fill_level_list(list_tree.size(),level);
		}
		
	}

	private void fill_coord(int size,int level) {
	
			int tt=0;
			for(int i=level+1;i<level_list.size();i++){
				tt=tt+level_list.get(i).number_of_child;
			}
			int gap=0;
			if(tt!=0){
			 gap=(((150)*tt)+((tt-1)*20))/2;
			}else{
				gap=20;
			}
			int yu=list_tree.size();
			for(int i=0;i<yu;i++){
				Tree_class treedd=list_tree.remove(0);
				tree_gui_coord t_g_c=list_coord_tree.remove(0);
				int number_of_child=treedd.tree_list.size();
				//System.out.println(number_of_child);
				int first_child=0;
				first_child=(number_of_child/2)*(gap+width);
				if(number_of_child%2==0 && number_of_child!=0){
					first_child=first_child-(int)((0.5)*gap)-(int)((0.5)*width);
				}
				int x_start=t_g_c.x-(first_child);
				int y_start=t_g_c.y+(level+1)*60;
				for(int y=0;y<number_of_child;y++){
					tree_gui_coord tr=new tree_gui_coord();
					tr.x=x_start;
					tr.y=y_start;
					tr.number=treedd.tree_list.get(y).number;
					t_g_c.list.add(tr);
					list_tree.add(treedd.tree_list.get(y));
					list_coord_tree.add(t_g_c.list.get(y));
					x_start=x_start+width+gap;
				}
			}
			
			if(list_tree.size()!=0){
				level++;
				for(int i=level+1;i<level_list.size();i++){
					tt=tt+level_list.get(i).number_of_child;
				}
				if(tt!=0){
					fill_coord(list_tree.size(),level);
				}
			}
		    
			
			
	}
	
	private void display_coord(tree_gui_coord dupe_tree_coord, int size) {

		for(int i=0;i<size;i++){
             if(dupe_tree_coord.list.get(i).list.size()==0){
            	 System.out.println(dupe_tree_coord.list.get(i).x+"   "+dupe_tree_coord.list.get(i).y);
            	 }else{
            		 System.out.println(dupe_tree_coord.list.get(i).x+"   "+dupe_tree_coord.list.get(i).y);
            	 tree_gui_coord tr=dupe_tree_coord.list.get(i);
            	 display_coord(tr,tr.list.size());
             }
		}
	}

	public void paint_screen(){
		  
		  
		 tree_gui_coord dupe_tree_coord=tree_coord;
		 Tree_class treedupe=tree;
		 final Tree_class trd=treedupe;
		 JButton b = new RoundButton(treedupe.name);
		 b.setBounds(dupe_tree_coord.x, dupe_tree_coord.y, width, height);
		 b.setToolTipText("<html> Values : "+node_list.get(treedupe.number).node_value+" <br> "+"String : "+node_list.get(treedupe.number).node_string+"</br></html>");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(two_nodes.size()==2){
					two_nodes.add(trd);
				}
				if(two_nodes.size()==3){
					interchange();
				}
			}

		});
		 buttons.add(b);
		 panel.add(b);
			display_buttons(dupe_tree_coord,dupe_tree_coord.list.size(),treedupe);
			frame.add(panel,BorderLayout.CENTER);
			  frame.setSize(1200,600);
			  frame.setVisible(true); 
	}

	private void display_buttons(tree_gui_coord dupe_tree_coord, int size,Tree_class treedupe) {

		for(int i=0;i<size;i++){
            if(dupe_tree_coord.list.get(i).list.size()==0){
            	JButton b = new RoundButton(treedupe.tree_list.get(i).name);
       		 b.setBounds(dupe_tree_coord.list.get(i).x, dupe_tree_coord.list.get(i).y, width, height);
       		 b.setToolTipText("<html>Values : "+node_list.get(treedupe.tree_list.get(i).number).node_value+" <br> "+"String : "+node_list.get(treedupe.tree_list.get(i).number).node_string+"</br></html>");
       		final Tree_class trd=treedupe;
       		final Tree_class trd1=treedupe.tree_list.get(i);
       		 b.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				two_nodes.add(trd1);
    				two_nodes.add(trd);
    				if(two_nodes.size()==4){
    					interchange();
    				}
    			}

    		});
       		 buttons.add(b);
       		 panel.add(b);
           	 }else{
           		JButton b = new RoundButton(treedupe.tree_list.get(i).name);
          		 b.setBounds(dupe_tree_coord.list.get(i).x, dupe_tree_coord.list.get(i).y, width, height);
          		 b.setToolTipText("<html>Values : "+node_list.get(treedupe.tree_list.get(i).number).node_value+" <br> "+"String : "+node_list.get(treedupe.tree_list.get(i).number).node_string+"</br></html>");
       		
          		final Tree_class trd=treedupe;
          		final Tree_class trd1=treedupe.tree_list.get(i);
          		 b.addActionListener(new ActionListener(){
       			public void actionPerformed(ActionEvent e){
       				two_nodes.add(trd1);
       				two_nodes.add(trd);
       				if(two_nodes.size()==4){
       					interchange();
       				}
       			}

       		});
          		 buttons.add(b);
       		 panel.add(b);
           	 tree_gui_coord tr=dupe_tree_coord.list.get(i);
           	 Tree_class br=treedupe.tree_list.get(i);
           	 display_buttons(tr,tr.list.size(),br);
            }
		}
		
	}
	

	private void interchange() {

		two_nodes.get(1).tree_list.remove(two_nodes.get(0));
		two_nodes.get(2).tree_list.add(two_nodes.get(0));
		two_nodes.clear();
		panel.removeAll();
		frame.setVisible(false);
		frame.setVisible(true);
		Tree_gui_builder builder_gui=new Tree_gui_builder(tree,0,0,panel,frame,node_list,two_nodes);
		builder_gui.start();
	}
}
