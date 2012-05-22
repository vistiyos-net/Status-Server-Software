package net.vistiyos.config;

import java.util.HashMap;
import java.util.Map;

import net.vistiyos.util.Desencriptar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {
	
	private static Map<String,String> configuration = new HashMap<String,String>(); 
	
	private static void loadConfiguration() {
		try{
			Document conf = Desencriptar.desencriptar();
			Element root = conf.getDocumentElement();
			NodeList nodos = root.getChildNodes();
			for( int i = 0; i < nodos.getLength() ; i++ ){
				Node nodo = nodos.item(i);
				if(!nodo.getNodeName().startsWith("#")){
					configuration.put(nodo.getNodeName(), nodo.getTextContent());
				}
			}
		}catch(Exception ex){
			System.err.println("Fallo al cargar configuración");
		}
	}
	
	public static String getValue(String name) throws Exception{
		if(configuration.isEmpty()){
			loadConfiguration();
		}
		return configuration.get(name);
	}

}
