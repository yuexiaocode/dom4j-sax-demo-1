package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Main2 {
	public static void main(String[] args) throws IOException, DocumentException {
		BufferedReader br = new BufferedReader(new FileReader(new File("d:/T_RBJ_DWCBXX_201603.xml")));
		int max = 1000;
		int i = 1;
		String line = br.readLine();
		int lastClose = 0;
		int lineIndex = 0;
		StringBuffer sb = new StringBuffer();
		while(++lineIndex<=max && line!=null){
			sb.append(line).append("\n");
			
			if(line.trim().equals("</DATA>")){
				lastClose = lineIndex;
				System.out.print(sb);
				sb.delete( 0, sb.length() );
			}
			line = br.readLine();
		}
		br.close();
		System.out.println("  </DATABODY>");
		System.out.println("</MSG>");
		
	}

}
