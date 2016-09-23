package manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList; 
import bean.DataBaseStr;

public class SqlVersionManager {
	private InputStream fileStream;

	public SqlVersionManager(InputStream fileStream) {
		this.fileStream = fileStream;
	}
	
	List<DataBaseStr> getSqlSource() {
		try {
			List<DataBaseStr> sqls = new ArrayList<DataBaseStr>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileStream); 
			Element root = doc.getDocumentElement();
			NodeList list = root.getElementsByTagName("version");
			for (int i = 0; i < list.getLength(); i++) {
				DataBaseStr sqldata = new DataBaseStr();
				Node item = list.item(i);
				String level = item.getAttributes().getNamedItem("level").getNodeValue();
				sqldata.setVersion(Integer.parseInt(level));
				NodeList nodelist = item.getChildNodes();
				List<String> mlist = new ArrayList<String>();
				for (int j = 0; j < nodelist.getLength(); j++) {
					Node citem = nodelist.item(j);
					if (citem.getAttributes() != null) {
						mlist.add(citem.getAttributes().getNamedItem("desc").getNodeValue());
					}
				}
				sqldata.setMlist(mlist);
				sqls.add(sqldata);
			}
			return sqls;
		} catch (Exception ex) {
			return null;
		}
	}
	
	public List<String> getSqlsByVersion(int pversion){
		List<DataBaseStr> datas= getSqlSource();
		List<String> sqls=new ArrayList<String>();
		for(DataBaseStr item:datas){
			if(item.getVersion()>pversion){
				sqls.addAll(item.getMlist());
			}
		}
		return sqls;
	}
	
}
