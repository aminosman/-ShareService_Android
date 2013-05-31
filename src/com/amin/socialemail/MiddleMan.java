package com.amin.socialemail;

import java.util.List;

public class MiddleMan {
	private static MainActivity parent;
	public static List<String> titles;
	public static List<String> links;
	public static MyAdapter adapter;
	public static int index = 0;
	public static int position = 0;
	private static Server server;
	
	public MiddleMan(MainActivity parent, MyAdapter adapter) {
		this.adapter=adapter;
    	MiddleMan.parent = parent;
    	server = new Server(parent);
    	parent.memory();
		server.xml_ajax();
		parent.memory();
    	this.titles = server.titles;
    	this.links = server.links;
	}
	public static void update(){
		feedMe();
	}

	public static void feedMe() {
		parent.memory();

		Debug.out("Titles Size: "+titles.size()+" links seize: "+links.size());

		if(titles.size()<=index+11 || links.size()<=index+11){
			Debug.out("Fetching new batch of xml");
			index=0;
			
				server.xml_ajax();
		}
		else{
		for(int i =0; i<10;i++){
		//	Debug.out("Dispence Cards "+index+server.links.get(0));

			if((links.get(index).length()>5)&&(links.get(index).substring(links.get(index).length()-4,links.get(index).length()-3).equals(".") && links.get(index).contains("imgur"))){
				if(links.get(index).substring(links.get(index).length()-3,links.get(index).length()).equals("gif")){
					Card newCard = (Card) new WebCard(links.get(index) , titles.get(index), index, position, MiddleMan.parent);
					adapter.updateCards(newCard);
					//   Debug.out("Title: "+titles.get(index)+" at "+index);
					  //  Debug.out("Link: "+links.get(index)+" at "+index);
				}
				else{

					Card newCard = new PictureCard(links.get(index) , titles.get(index), index, position, MiddleMan.parent);
					adapter.updateCards(newCard);
					Debug.out("Title: "+titles.get(index)+" at "+index);
					Debug.out("Link: "+links.get(index)+" at "+index);

				}
				position++;

			}

		index++;
		}
		}
	   }
	
}
