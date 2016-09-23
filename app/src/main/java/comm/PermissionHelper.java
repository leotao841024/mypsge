package comm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bean.Food;
import bean.FoodCal;
import bean.Permin;
import android.content.Context;

public class PermissionHelper {
	private String filenm = "permiss.xml";
	private InputStream inputstream;

	public PermissionHelper(Context context) {
		try {
			inputstream = context.getResources().getAssets().open(filenm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Permin> getPermin() {
		List<Permin> permins=new ArrayList<Permin>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 取得DocumentBuilderFactory实例
			DocumentBuilder builder = factory.newDocumentBuilder(); // 从factory获取DocumentBuilder实例
			Document doc = builder.parse(inputstream); // 解析输入流 得到Document实例
			Element root = doc.getDocumentElement();
			NodeList list = root.getElementsByTagName("level");
			 for(int i=0;i<list.getLength();i++){
		        	Node item =list.item(i);
		        	String level = item.getAttributes().getNamedItem("id").getNodeValue();
		        	NodeList nodelist= item.getChildNodes(); 
		        	for(int j=0;j<nodelist.getLength();j++){
		        		Node citem=nodelist.item(j);
		        		if(citem.getAttributes()!=null){
		        			Permin per=new Permin();
		        			per.setLevel(level);
		        			per.setDesc(citem.getAttributes().getNamedItem("desc1").getNodeValue()); 
		        			per.setName(citem.getAttributes().getNamedItem("name").getNodeValue()); 
		            	    permins.add(per);
		        		}
		        	}
		 }
		} catch (Exception ex) {
			
		}
		return permins;
	}
}
