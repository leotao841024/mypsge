package comm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.renrenstep.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.Food;
import bean.FoodCal;

public class FoodXmlHandler { 
	private static List<FoodCal> foods;
	public static  List<FoodCal> getFoods(Context context){
		if(foods==null){
			try
			{
				InputStream inputstream = context.getResources().getAssets().open("foods.xml");
				foods=parse(inputstream);
			}
			catch(Exception ex){
			}
		}
		return foods;
	}
	static List<FoodCal> parse(InputStream is) throws Exception {  
	        List<FoodCal> foods = new ArrayList<FoodCal>();  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
	        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
	        Document doc = builder.parse(is);   //解析输入流 得到Document实例  
	        Element root = doc.getDocumentElement();
	        NodeList list= root.getElementsByTagName("level"); 
	        for(int i=0;i<list.getLength();i++){
	        	FoodCal fcal=new FoodCal(); 
	        	Node item =list.item(i);
	        	String level = item.getAttributes().getNamedItem("id").getNodeValue();
	        	fcal.setLeval(level);
	        	NodeList nodelist= item.getChildNodes();
	        	List<Food> foodlist=new ArrayList<Food>();
	        	for(int j=0;j<nodelist.getLength();j++){
	        		Node citem=nodelist.item(j);
	        		if(citem.getAttributes()!=null){
	        		Food food=new Food();
	            	food.setId(Integer.parseInt(citem.getAttributes().getNamedItem("id").getNodeValue()));
	            	food.setName(citem.getAttributes().getNamedItem("name").getNodeValue());
	            	food.setUnit(citem.getAttributes().getNamedItem("unit").getNodeValue());
	            	food.setPicname(citem.getAttributes().getNamedItem("picnm").getNodeValue());
	            	foodlist.add(food);
	        		}
	        	}
	        	fcal.setFoods(foodlist);
	        	foods.add(fcal);
	        }
	    return foods;  
	}  




}
