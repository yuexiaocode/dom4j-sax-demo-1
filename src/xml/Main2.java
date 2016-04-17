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
		int max = 100;
		int i = 0;
		String line = br.readLine();
		while(line!=null && i++<=max){
			System.out.println(line);
			line = br.readLine();
		}
		br.close();

	}

}
