package ctd.test.word;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ctd.util.xml.XMLHelper;

public class WordMLMaker {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode("XML");
		resolver.setSuffix(".xml");
		resolver.setPrefix("ctd/test/word/");
		resolver.setCharacterEncoding("utf-8");
		resolver.setCacheTTLMs(10000l);
		
		TemplateEngine templateEngine = new TemplateEngine(); 
		templateEngine.setTemplateResolver(resolver);
		
		Document doc = XMLHelper.getDocument("C:/Users/Sean/Documents/templateDefine.xml");
		
		Context ctx = new Context();
		ctx.setVariable("doc", doc);
		
		
		
		
		byte[] data = FileUtils.readFileToByteArray(new File("C:/Users/Sean/Pictures/piechart.png"));
		String imageData1 = Base64.encode(data);
		NativeBase64Image img = new NativeBase64Image();
		img.setId("image1");
		img.setFilename("image1.jpg");
		img.setBase64(imageData1);
		
		List<NativeBase64Image> imageList = new ArrayList<NativeBase64Image>();
		imageList.add(img);
		ctx.setVariable("images", imageList);
		
		String result = templateEngine.process("template1", ctx);	
		FileUtils.write(new File("d:/word2.doc"), result,"UTF-8");
		
	}

}
