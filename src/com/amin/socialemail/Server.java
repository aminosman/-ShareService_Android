package com.amin.socialemail;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;

public class Server {
	private List<String> urls;
	private MainActivity main;
	public List<String> titles = new ArrayList<String>();
	public List<String> links = new ArrayList<String>();
	public Server(List<String> urls){
		this.urls = urls;
		
	}
	public Server(){
		
	}
	public Server(MainActivity main){
		this.main = main;
	}
	public void xml_ajax(){  
		titles.clear();
		links.clear();
		AjaxCallback<XmlDom> cb = new AjaxCallback<XmlDom>();        
        String url = "https://api.imgur.com/3/gallery.xml";
		//Strig url = 
		cb.url(url).type(XmlDom.class).weakHandler(this, "picasaCb");
		cb.header("Authorization", "Client-ID 1bc97289e6a0f6b");
		Debug.out("Before");
		main.memory();

        MainActivity.aq.ajax(cb);      
		Debug.out("XML");

}

public void picasaCb(String url, XmlDom xml, AjaxStatus status){
	if(xml==null){
	Debug.out("XML OVER "+url);
	}
        List<XmlDom> entries = xml.tags("item");               
     
        for(XmlDom entry: entries){
        //	Debug.out(entry.text("link"));

                titles.add(entry.text("title"));
                links.add(entry.text("link"));
        }
        status.invalidate();
        xml=null;
        MiddleMan.feedMe();
		Debug.out("After");
		
		main.memory();         
        
}

}
