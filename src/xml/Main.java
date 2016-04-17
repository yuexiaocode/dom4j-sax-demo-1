package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

public class Main {
	public static void main(String[] args) throws IOException, DocumentException {
		/*BufferedReader br = new BufferedReader(new FileReader(new File("d:/T_RBJ_DWCBXX_201603.xml")));
		int max = 100;
		int i = 0;
		String line = br.readLine();
		while(line!=null && i++<=max){
			System.out.println(line);
			line = br.readLine();
		}
		br.close();*/
		
		SAXReader reader = new SAXReader();
		ElementHandler handler = new ElementHandler() {
			private long total = 0;
			private Workbook workBook = new HSSFWorkbook();
			private Sheet sheet =workBook.createSheet("Data");
			private long everyFileRows = 50000;
			@Override
			public void onStart(ElementPath arg0) {
				
				arg0.getCurrent().detach();
			}
			
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
						// TODO Auto-generated catch block
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
				
				List<Element> datas = elementPath.getCurrent().elements();
				Row row = sheet.createRow((int) index);
				int j = 0;
				for(Element e : datas){
		        	
					Cell cell = row.createCell(j++);
					cell.setCellValue(e.getText());
					
		        }
				
				total++;
				elementPath.getCurrent().detach();
			}
		};
		reader.addHandler("/MSG/DATABODY/DATA",handler);
		
		reader.addHandler("/MSG/DATABODY",handler);
		
        // 通过read方法读取一个文件 转换成Document对象  
        Document document = reader.read(new File("d:/T_RBJ_DWCBXX_201603.xml"));  
        //Document document = reader.read(new File("d:/1.xml"));  
        //获取根节点元素对象  
        /*Element root = document.getRootElement();
        List<Element> datas = root.selectNodes("//DATABODY/DATA");
        
        System.out.println(datas.size());
        Workbook workBook = new HSSFWorkbook();
        Sheet sheet = workBook.createSheet("娃子");
        int i = 0;
        for(Element e : datas){
        	Row row = sheet.createRow(i++);
    		List<Element> Rowdatas = e.elements();
    		int j = 0;
			for(Element col : Rowdatas){
				 Cell cell = row.createCell(j++);
				 cell.setCellValue(col.getText());
			}
        }
        
        
        OutputStream os = new FileOutputStream("d:/1.xls");
        workBook.write(os);
        os.close()*/;
	}
	
	
	//public static void writeExcel(data)
}
