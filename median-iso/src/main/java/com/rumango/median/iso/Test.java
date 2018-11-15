package com.rumango.median.iso;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;

public class Test {

	public static ISOMsg unpackMessage(String stringMessage, String isoVersion) {
		try {
			ISOMsg isoMsg = new ISOMsg();
			if (isoVersion.equalsIgnoreCase("87")) {
				ISO87APackager packager = new ISO87APackager();
				isoMsg.setPackager(packager);
			} else if (isoVersion.equalsIgnoreCase("93")) {
				ISO93APackager packager = new ISO93APackager();
				isoMsg.setPackager(packager);
			}
			isoMsg.unpack(stringMessage.getBytes("US-ASCII"));
			return isoMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
		try {
			System.out.println("-----------------" + stringMessage + "-----------------------");
			System.out.println("  MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					System.out.println(i + " " + " : " + msg.getString(i));
				}
			}
		} catch (ISOException e) {
			System.out.println("Exception occured" + e.getMessage());
		}
	}

	public static void main(String[] args) throws ISOException {
//		String s = "0200F27A200108E0800000000000040000001011404630001000000000001070000130094304        09430409430401300130404069405005942924A3FBBMOB00002000000000105817test|Kimani|Elizabeth||0008527001       404130010008527001";
//		String s2= "0200F27A200108E0800000000000040000001011404630001000000000001070000130094304        09430409430401300130404069405005942924A3FBBMOB00002000000000105817test|Kimani|Elizabeth||0008527001       404130010008527001";
//		ISOMsg msg = unpackMessage(s, "93");
//		logISOMsg(msg, "");
//		
//		UUID uuid = UUID.randomUUID();
//		System.out.println("uuid :" + uuid);
		// System.out.println(new Timestamp(System.currentTimeMillis()));
//		String date = LocalDate.now().toString();
//		StringBuilder sb=new StringBuilder(date);
//		//sb.de
//		System.out.println(" date ::" + date);
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YY-MM-DD");
		// LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

		// System.out.println("dateTime :" + dateTime);

		String padded = String.format("%12s", "1234");

		System.out.println(getIso());
	}

	public static String getIso() throws ISOException {
//		MTI : 1200
//#		Field-2 : INTB
//		Field-3 : 400000
//#		Field-4 : 0000000000035000
//		Field-11 : 622279
//#		Field-12 : 20180830123435
//#		Field-17 : 20180830
//		Field-24 : 200
//		Field-32 : 000000
//		Field-49 : 840
//		Field-102 : 001190001000381
//		Field-103 : 001190001000062
//		Field-123 : SWT
//		Field-124 : ITB
//		Field-125 : 
		ISOMsg msg = new ISOMsg();
//		msg.setMTI("1200");
//		msg.set(2, "INTB1234567890123");
//		msg.set(3, "400000");
//		msg.set(4, "000000035000");
//		msg.set(11, "622279");
//		msg.set(12, "201808301234");
//		msg.set(17, "0830");
//		msg.set(24, "200");
//		msg.set(32, "000000");
//		msg.set(49, "840");
//		msg.set(102, "001190001000381");
//		msg.set(103, "001190001000062");
//		msg.set(123, "SWT");
//		msg.set(124, "ITB");
//		msg.setPackager(new ISO93APackager());
//		System.out.println(new String(msg.pack()));

//		Funds Transfer:(MTI 1200)
//		Field2=2547XXXXXXX   - MTI
//		Field3=400000
//#		Field4=0000000000005500   - Amount
//#		Field11=000000972060    - stan
//#		Field12=20150317115213   -datetime
//#		Field17=20150317      - date
//		Field24=200            
//		Field32=00057
//		Field49=404
//		Field102=001XXXXXXXXXXXX  debit account
//		Field103=003XXXXXXXXXXXX   credit account
//		Field123=ITAX
//		Field126=FT - Narration

		msg = new ISOMsg();
		msg.setMTI("1200");
		msg.set(2, "2547XXXXXXX");
		msg.set(3, "400000");
		msg.set(4, "000000005500");
		msg.set(11, "972060");
		msg.set(12, "201503171152");
		msg.set(17, "0317");
		msg.set(24, "200");
		msg.set(32, "00057");
		msg.set(49, "404");
		msg.set(102, "001XXXXXXXXXXXX");
		msg.set(103, "003XXXXXXXXXXXX");
		msg.set(123, "ITAX");
		msg.set(126, "FT - Narration");
		ISO93APackager p=new ISO93APackager();
		//msg.get
		msg.setPackager(p);
		System.out.println(new String(msg.pack()));
		return null;
	}
}
