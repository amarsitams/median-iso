package com.rumango.median.iso.test;
//package com.rumango.median.iso.util;
//
//import java.io.FileWriter;
//import java.io.Writer;
//import java.util.List;
//
//import com.rumango.median.iso.entity.IsoFiledDetails;
//
//public class ReadFromCsv {
//	IsoFiledDetails details;
//
//	
//	public static void main(String[] args) {
//		
//	}
//	
//	public void process() {
//		List<IsoFiledDetails> beans = new CsvToBeanBuilder(FileReader("yourfile.csv")).withType(IsoFiledDetails.class).build().parse();
//
//	     // List<MyBean> beans comes from somewhere earlier in your code.
//	     Writer writer = new FileWriter("yourfile.csv");
//	     StatefulBeanToCsv beanToCsv = StatefulBeanToCsvBuilder(writer).build();
//	     beanToCsv.write(beans);
//	     writer.close();
//	}
//}
