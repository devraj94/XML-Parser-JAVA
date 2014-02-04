package xml_parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class Parser {
 File selected_file=null;
 static JTextArea textarea=null;
 Main main_ins=null;
 static FileReader fr=null;
	static BufferedReader br=null;
	static String line;
	static String buff="";
	static ArrayList<node_class> nodelist;
	static ArrayList<String> dupe_node_list,dupe_list;
	static int linecount=0;
	static node_class noder=null;
	static boolean EOF=false;
	Parser(File file,Main mm){
		selected_file=file;
		main_ins=mm;
	}
	
	public void parse(){
		textarea=main_ins.textarea;
		  nodelist=new ArrayList<node_class>();
		  dupe_node_list=new ArrayList<String>();
		  dupe_list=new ArrayList<String>();
		  try{
			   fr=new FileReader(selected_file);
			   br=new BufferedReader(fr);
			   line=br.readLine();
			   linecount++;
			   buff=line;
			  if(line.startsWith("<?xml ")){
				  buff=buff.replaceAll("\\s+", "");
				  if(buff.startsWith("<?xmlversion=\"1.0\"")){
					  buff=buff.replaceAll(",","");
					  if(buff.endsWith("?>")){
						 find_node();
					  }else{
						  textarea.setText("Error Line 1 : expected ?> as end tag");
					  }
				  }else{
					  textarea.setText("Error Line 1 : at Version expected version=\"1.0\"");
				  }
			  }else{
				  textarea.setText("Error Line 1 Column 1 : XML starts with <?xml version=\"1.0\" ?>tag");
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	
	}
	
	private static void find_node() {
		 try {
			if((line=br.readLine())!=null){
				linecount++;
				  buff=line.replaceAll("\\s+", ",");
				  while(buff.compareTo(",")==0){
					  line=br.readLine();
					  linecount++;
					  buff=line.replaceAll("\\s+", ",");
				  }
				     next_node(0);
			}
			
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private static void next_node(int i) {
		  String node="";
		  String value="";
		  String info="";
		  if(buff.charAt(i)==44){
			  i++;
		  }
		  if(buff.charAt(i)==60){
			  i++;
			  if(buff.charAt(i)!=44){
				  while(buff.charAt(i)!=44 && buff.charAt(i)!=62){
					  node=node+String.valueOf(buff.charAt(i));
					  i++;
				  }
			     if(buff.charAt(i)==62 || buff.charAt(i)==44){
			   	  while(i<buff.length() && buff.charAt(i)==44){
			   		  i++;
			   	  }
			   	 if(dupe_node_list.size()>0 && node.compareTo(dupe_node_list.get(dupe_node_list.size()-1))==0){
			   		 textarea.append("Error Line "+linecount+": Duplicate node not allowed");
			   	 }else{
			   		 noder=new node_class();
				   	  noder.node_name=node;
					  nodelist.add(noder);
					  add_child_to_dupe_list();
					  dupe_node_list.add(node);
					  dupe_list.add(node);

					  int flag_equal=0;
					  int flag_quote=0;
					  int extras=0;
					  int end_tag=0;
					  int endtag_line=0;
					  int buff_char=buff.charAt(i);
			if(buff_char==47){
				end_tag=1;
				i++;
				 if(buff.length()<=i){
						skip_line();
						if(end_tag==1){
							endtag_line=1;
						}
						i=0;
					  }
					  buff_char=buff.charAt(i);
			}else{
				while(buff_char!=62){//multiple att values check
					   flag_equal=0;
					   flag_quote=0;
					   extras=0;
					   end_tag=0;
					   endtag_line=0;
					   buff_char=buff.charAt(i);
					  while(buff_char!=62 && buff_char!=44){
						  if(buff_char!=44 && buff_char!=47){
						  value=value+String.valueOf(buff.charAt(i));
						  }
						  if(buff_char==61 && flag_quote==0 && flag_equal==0){
							  flag_equal=1;
						  }else if(buff_char==61 && (flag_quote!=1) && flag_equal==1){
							  textarea.setText("Error Line "+linecount+" : found extra =");
						  }
						  if(buff_char==34 && flag_equal==1 && flag_quote<2){
							  flag_quote++;
						  }else if(buff_char==34 && flag_quote==2){
							  textarea.setText("Error Line "+linecount+" : found extra \"");
						  }
						  i++;
						  
						  if(buff.length()<=i){
							skip_line();
							if(end_tag==1){
								endtag_line=1;
							}
							i=0;
						  }
						  buff_char=buff.charAt(i);
						  if(flag_equal==1 && flag_quote==2){
							  if((buff_char!=44 && buff_char!=47)|| (end_tag==1 && buff_char==44)){
							     extras++;
							  }else if(buff_char==47 && buff_char!=44){
								  end_tag++;
							  }
						  }
					  }
					  
					  if(buff_char==44){
						  i++;
						  if(buff.length()<=i){
								skip_line();
								if(end_tag==1){
									endtag_line=1;
								}
								i=0;
							  }
							  buff_char=buff.charAt(i);
						  if((flag_equal==1 && flag_quote==2) && buff_char!=62){
								value=value+" ";
								 
							  }
					  }
					 
				}
			}
					  if((flag_equal==1 && flag_quote==2) ||(flag_equal==0 && flag_quote==0 && value.compareTo("")==0)){
						if(flag_equal==0 && flag_quote==0 && end_tag==0){
							 i++;
							  string_checker(i);
						}else if(flag_equal==0 && flag_quote==0 && end_tag==1){
							dupe_node_list.remove(dupe_node_list.size()-1);
							  dupe_list.set(dupe_list.size()-1, "");
							  i++;
							  while(i<buff.length() && buff.charAt(i)==44){
						   		  i++;
						   	  }
							  next_node_start(i);
						}else{
							 if(buff.charAt(i)==62  && extras==1 && end_tag==0){
								  noder=nodelist.get(nodelist.size()-1);
								  noder.node_value=value;
								  nodelist.set(nodelist.size()-1, noder);
								  i++;
								  while(i<buff.length() && buff.charAt(i)==44){
							   		  i++;
							   	  }
								  string_checker(i);
							  }else if(buff.charAt(i)==62  && extras==1 && end_tag==1 && endtag_line==0){
								  noder=nodelist.get(nodelist.size()-1);
								  noder.node_value=value;
								  noder.node_string="";
								  dupe_node_list.remove(dupe_node_list.size()-1);
								  dupe_list.set(dupe_list.size()-1, "");
								  nodelist.set(nodelist.size()-1, noder);
								  i++;
								  while(i<buff.length() && buff.charAt(i)==44){
							   		  i++;
							   	  }
								  next_node_start(i);
							  } else{
								  textarea.setText("Error Line "+linecount+" : Expected \">\" after value for the node");
							  }
						}
						 
					  }else{
						  textarea.setText("Error Line "+linecount+" : Expected \" for Att value");
					  }
			   	 }
			     }else{
			    	 textarea.setText("Error Line "+linecount+": node not found");
			     }
			  }else{
				  textarea.setText("Error Line "+linecount+" : Start Tag <xxxx> expected");
			  }
		  }else{
			  textarea.setText("Error Line "+linecount+" : Start Tag <---> expected");
		  }
		
	}
	private static void add_child_to_dupe_list() {

		for(int u=dupe_node_list.size()-1;u>=0;u--){
			int y=0;
			String str=dupe_node_list.get(u);
			for(int k=dupe_list.size()-1;k>=0;k--){
				if(dupe_list.get(k).compareTo(str)==0){
					y=k;
					break;
				}
			}
			if(str.compareTo("")!=0){
				if(nodelist.get(y).node_name.compareTo(str)==0){
					nodelist.get(y).no_of_nodes++;
					break;
				}
			}
			
		}
		
	}
	private static void next_node_start(int i) {

		if(buff.length()<=i){
			skip_line();
			i=0;
		}
		if(!EOF){
		next_node(i);
		}else{
				//Display_lists();
		}
		
	}
	private static void string_checker(int i) {

		if(buff.length()<=i){
			skip_line();
			i=0;
		}
		String str="";
		while(buff.charAt(i)!=60){
			str=str+String.valueOf(buff.charAt(i));
			i++;
			if(buff.length()<=i){
				skip_line();
				i=0;
			}
		}
		noder=nodelist.get(nodelist.size()-1);
		noder.node_string=str;
		nodelist.set(nodelist.size()-1, noder);
		node_checker(i);
	}


	private static void string_checker_closetag(int i) {

		if(buff.length()<=i){
			skip_line();
			i=0;
		}
		if(!EOF){
			String str="";
			while(buff.charAt(i)!=60){
				str=str+String.valueOf(buff.charAt(i));
				i++;
				if(buff.length()<=i){
					skip_line();
					i=0;
				}
			}
			if(str.compareTo("")!=0){
				String tr=dupe_node_list.get(dupe_node_list.size()-1);
				for(int u=0;u<nodelist.size();u++){
					if(nodelist.get(u).node_name.compareTo(tr)==0){
						noder=nodelist.get(u);
						noder.node_string=noder.node_string+" "+str;
						nodelist.set(nodelist.size()-1, noder);
					}
				}
			}
			node_checker(i);
		}else{
			//Display_lists();
		}

	}


	private static void node_checker(int i) {
	     i++;
		if(buff.charAt(i)==47){
			i++;
			got_end_of_node(i);
		}else if(buff.charAt(i)==44){
			textarea.setText("Error at Line "+linecount+": node decleration");
		}else{
			i--;
			if(!EOF){
			next_node(i);
			}else{
				//Display_lists();
			}
		}
	}
	private static void got_end_of_node(int i) {
		String node="";
		int error=0;
		if(buff.charAt(i)!=44){
			while(buff.charAt(i)!=62){
				if(buff.charAt(i)!=44 && buff.charAt(i)!=60){
					node=node+String.valueOf(buff.charAt(i));
					i++;
				}
				if(buff.length()<=i){
					skip_line();
					i=0;
				}
				int flager=0;
				while(buff.charAt(i)==44){
					i++;
					flager=3;
					if(buff.length()<=i){
						skip_line();
						i=0;
					}
				}
				
				if((EOF || buff.charAt(i)!=62) && flager!=0){
					textarea.setText("Error Line "+linecount+" : expected closing tag >");
					if(EOF){
						EOF=false;
					}
					error=1;
					break;
				}else if((buff.charAt(i)==60)){
					textarea.setText("Error Line "+linecount+" : expected closing tag >");
					error=1;
					break;
				}
				
				if(buff.charAt(i)==62){
					if(node.compareTo(dupe_node_list.get(dupe_node_list.size()-1))==0){
						  dupe_node_list.remove(dupe_node_list.size()-1);
						  dupe_list.set(dupe_list.size()-1, "");
						  i++;
						  while(i<buff.length() && buff.charAt(i)==44){
					   		  i++;
					   	  }
						  string_checker_closetag(i);
						  break;
					}else{
						textarea.setText("Error Line "+linecount+": Bad closing tag");
						break;
					}
				}
			}
		}else{
			textarea.setText("Error Line "+linecount+" : expected closing tag");
		}
		
	}
	private static void Display_lists() {
		textarea.setText(" ");
		for(int u=0;u<nodelist.size();u++){
			node_class nd=nodelist.get(u);
			textarea.append(nd.node_name +"\n");
			textarea.append(nd.node_string +"\n");
			textarea.append(nd.node_value + "\n");
			textarea.append(String.valueOf(nd.no_of_nodes)+"\n");
		}
	}

	private static void skip_line() {
		try {
			if((line=br.readLine())!=null){
				linecount++;
				  buff=line.replaceAll("\\s+", ",");
				  while(buff.compareTo(",")==0 || buff.compareTo("")==0){
					  line=br.readLine();
					  linecount++;
					  buff=line.replaceAll("\\s+", ",");
				  }
				 
			}else{
				EOF=true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
