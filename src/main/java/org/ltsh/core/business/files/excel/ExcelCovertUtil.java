package org.ltsh.core.business.files.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;

import org.ltsh.core.business.exp.BusinessException;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.core.util.StringUtil;

/**
 * @author 
 * @date 2018年12月28日
 */
public class ExcelCovertUtil {
	/**
	 * 没一个sheet转化为一个单独的html
	 * @param excelFilePath
	 * @param htmlFilePath
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws ExcelConvertException 
	 */
	public static List<String> convertExcel2Html(String excelFilePath,String htmlFilePath) throws BusinessException {
		File excelFile = new File(excelFilePath);
		File htmlFile = new File(htmlFilePath);
		InputStream is = null;
		List<String> list = new ArrayList<String>();
		try {
			if (excelFile.exists()) {
				if (!htmlFile.exists()) {
					htmlFile.mkdirs();
				}
				String _sheetName = "南粤,村镇";
				String sheetName[] = _sheetName.split(",");
				
				boolean isHave[] = new boolean[sheetName.length];
				for (int i = 0; i < sheetName.length; i++) {
					//复制两份
					String fileName = StringUtil.UUID();
					String clonePath = htmlFilePath + File.separator + fileName + ".xls";
					list.add(clonePath);
					FileUtil.copyFile(excelFilePath, clonePath);
					is = new FileInputStream(new File(clonePath));
					HSSFWorkbook workBook = new HSSFWorkbook(is);
					workBook.removeSheetAt(i == 0 ? 1 : 0);
					//先设置False
					isHave[i] = false;
					
					for (int j = 0; j < workBook.getNumberOfSheets(); j++) {
						HSSFSheet sheet = workBook.getSheetAt(j);
						String name = sheet.getSheetName();
						if(sheetName[i].equals(name)) {
							isHave[i] = true;
						}
					}
					if(!isHave[i]) {
						BusinessException.err("excel中没有【"+sheetName[i]+"】！");
					}
					//转为HTML
					for (int j = 0; j < workBook.getNumberOfSheets(); j++) {
						HSSFSheet sheet = workBook.getSheetAt(j);
						if(sheetName[i].equals(sheet.getSheetName())) {
							converter(workBook, sheet, new File(htmlFilePath+File.separator + fileName + ".html"));
						}
						
					}
				}
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			BusinessException.err(e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 将HSSFWorkbook转化为html
	 * @param workBook
	 * @param sheet
	 * @param htmlFile
	 * @throws Exception
	 */
	private static void converter(HSSFWorkbook workBook,HSSFSheet sheet,File htmlFile) throws BusinessException{
		OutputStream out = null;
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			Document docTotal = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			ExcelToHtmlConverter converter = new ExcelToHtmlConverter(docTotal);
			converter.setOutputColumnHeaders(false);
			converter.setOutputRowNumbers(false);
//			converter.processWorkbook(workBook);
			converter.processWorkbook(workBook);
			Transformer serializer = TransformerFactory.newInstance()
					.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(new DOMSource(converter.getDocument()),
					new StreamResult(writer));

			out = new FileOutputStream(htmlFile);
			out.write(writer.toString().getBytes("utf-8"));
			out.flush();
			out.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException.err("EXCEL转换为HTML出错！");
		} finally {
			
			try {
				if (out != null) {
					out.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		List<String> list = ExcelCovertUtil.convertExcel2Html("E:\\upload\\tmp\\年终结算.xls", "E:\\upload\\tmp");
		System.out.println(list);
	}
}
