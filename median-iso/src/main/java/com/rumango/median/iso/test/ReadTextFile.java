package com.rumango.median.iso.test;

import java.io.InputStream;
import java.util.Scanner;

import com.rumango.median.iso.entity.IsoFiledDetails;

public class ReadTextFile {
	public static void main(String[] args) {
		new ReadTextFile().main();
	}

	public void main() {
		IsoFiledDetails fd = new IsoFiledDetails();
		Scanner input = null;
		try {
			// ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			// InputStream inputstream = classLoader.getResourceAsStream("Fields_data.txt");
			InputStream inputstream = this.getClass().getResourceAsStream("Fields_data.txt");
			input = new Scanner(inputstream);
			input.useDelimiter(";");
			while (input.hasNextLine()) {
				while (input.hasNext()) {
					System.out.println(input.next());
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			input.close();
		}
	}

//	try {
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		// InputStream inputstream = classLoader.getResourceAsStream("Fields_data.txt");
//		InputStream inputstream = this.getClass().getResourceAsStream("basic.xml");
//		Scanner input = new Scanner(inputstream);
//		while (input.hasNextLine()) {
//			String line = input.nextLine();
//			System.out.println(line);
//		}
//		input.close();
//	} catch (Exception ex) {
//		ex.printStackTrace();
//	}

}
