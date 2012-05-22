package net.vistiyos.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDF {
	
	private static Font catFont = new Font( Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	
	public static void generarInforme(ArrayList<ArrayList<Object>> arrayList){
		getDayOfTheWeek();
		Calendar calendar = Calendar.getInstance();
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, 
					new FileOutputStream(System.getProperty("user.dir")+
							"/informes/Informe"
							+calendar.get(Calendar.YEAR)
							+(1+calendar.get(Calendar.MONTH))
							+calendar.get(Calendar.DAY_OF_MONTH)+".pdf"
							));
			document.open();
			addMetaData(document);
			addTitlePage(document);
			addContent(document,arrayList);
			document.close();
			try{
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+
													"/informes/Informe"
													+calendar.get(Calendar.YEAR)
													+(1+calendar.get(Calendar.MONTH))
													+calendar.get(Calendar.DAY_OF_MONTH)+".pdf"));
			}catch(IOException ex){
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addMetaData(Document document) {
		document.addTitle("Informe PDF");
		document.addAuthor("Escrubasoft Inc.");
	}
	
	private static String getDayOfTheWeek(){
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		String day = "";
		switch(weekday){
			case 1:
				day = "Domingo";
				break;
			case 2:
				day = "Lunes";
				break;
			case 3:
				day = "Martes";
				break;
			case 4:
				day = "Miércoles";
				break;
			case 5:
				day = "Jueves";
				break;
			case 6:
				day = "Viernes";
				break;
			case 7:
				day = "Sábado";
				break;
		}
		return day;
	}

	private static void addTitlePage(Document document) throws DocumentException {
		Calendar calendar = Calendar.getInstance();
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 1);
		preface.add(new Paragraph("Turno XXXX, "+getDayOfTheWeek()+" "+calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR), catFont));
		addEmptyLine(preface, 1);
		document.add(preface);
	}

	private static void addContent(Document document,ArrayList<ArrayList<Object>> arrayList) throws DocumentException {
		PdfPTable table = new PdfPTable(5);
		// t.setBorderColor(BaseColor.GRAY);
		// t.setPadding(4);
		// t.setSpacing(4);
		// t.setBorderWidth(1);
		PdfPCell c1 = new PdfPCell(new Phrase("Nombre"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Salario"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Barra"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Encargado"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		c1 = new PdfPCell(new Phrase("Observa."));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);
		Iterator<ArrayList<Object>> it = arrayList.iterator();
		while(it.hasNext()){
			ArrayList<Object> array = it.next();
			for(int i = 0 ; i < array.size() ; i++){
				if(array.get(i) != null){
					if(array.get(i).getClass().toString().contains("Boolean")){
						boolean encargado = (boolean) array.get(i);
						if(encargado){
							table.addCell("Si");
						}
						else{
							table.addCell("No");
						}
					}
					else{
						table.addCell(String.valueOf(array.get(i)));
					}
				}
				else{
					table.addCell("");
				}
			}
		}
		document.add(table);		
	}
		
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

}