package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

public class Main {
	public static void main(String[] args) throws IOException, DocumentException {
		
		SAXReader reader = new SAXReader();
		ElementHandler handler = new ElementHandler() {
			private long total = 0;
			private Workbook workBook = new HSSFWorkbook();
			private Sheet sheet =workBook.createSheet("Data");
			private long everyFileRows = 65536;
			@Override
			public void onStart(ElementPath elementPath) {
				
				elementPath.getCurrent().detach();
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void onEnd(ElementPath elementPath) {
				
				long index = total%everyFileRows;
				if(elementPath.getPath().equals("/MSG/DATABODY")){
					
					try {
						OutputStream os = new FileOutputStream("d:/export/"+
									(int)Math.ceil(total/(everyFileRows*1.0))
							+".xls");
						workBook.write(os);
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					return;
				}
				
				
				if(index==0 && total>0){
					try {
						OutputStream os = new FileOutputStream("d:/export/"+total/everyFileRows+".xls");
						workBook.write(os);
						os.close();
						System.gc();
						workBook = new HSSFWorkbook();
						sheet = workBook.createSheet("Data");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Element current = elementPath.getCurrent();
				List<Element> datas =current.elements();
				Row row = sheet.createRow((int) index);
				int j = 0;
				for(Element e : datas){
		        	
					Cell cell = row.createCell(j++);
					cell.setCellValue(e.getText());
					
		        }
				
				total++;
				current.detach();
			}
		};
		reader.addHandler("/MSG/DATABODY/DATA",handler);
		
		reader.addHandler("/MSG/DATABODY",handler);
		
       
        reader.read(new File("d:/T_RBJ_DWCBXX_201603.xml"));  
        //reader.read(new File("d:/1.xml"));  
       
	}

}
