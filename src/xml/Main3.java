package xml;

import java.io.File;

import org.dom4j.DocumentException;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import xml.sax.handler.AnnotationElementHandler;

public class Main3 {
	public static void main(String[] args) throws DocumentException {
		SAXReader reader = new SAXReader();
		
	
		AnnotationElementHandler.addMultiPathHandle(reader, new AnnotationElementHandler(){
			
			@SuppressWarnings("unused")
			@Handler(value="/MSG/DATABODY/DATA",type=Type.START_AND_END)
			public void onBodyStart(ElementPath elementPath){
				System.out.println("onBody0..." + elementPath.getCurrent().elements().size());
			}
			@SuppressWarnings("unused")
			@Handler(value="/MSG/DATABODY/DATA",type=Type.START_AND_END,order=-1)
			public void onBodyStart1(ElementPath elementPath){
				System.out.println("onBody1..." + elementPath.getCurrent().elements().size());
			}
			@Override
			public boolean isDetach(ElementPath elementPath) {
				return true;
			}
			
		}, "/MSG/DATABODY","/MSG/DATABODY/DATA");
		

		// reader.read(new File("d:/T_RBJ_DWCBXX_201603.xml"));
		reader.read(new File("d:/1.xml"));
	}
}
