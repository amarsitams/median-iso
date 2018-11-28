package com.rumango.median.iso.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;

public class Test1 {
	private static ISO93APackager packager93;
	private static ISO87APackager packager87;

	private static ISOMsg unpackMessage(String stringMessage, String isoVersion)
			throws UnsupportedEncodingException, ISOException {
		try {
			ISOMsg isoMsg = new ISOMsg();
			if (isoVersion.equalsIgnoreCase("87")) {
				packager87 = new ISO87APackager();
				isoMsg.setPackager(packager87);
			}
			if (isoVersion.equalsIgnoreCase("93")) {
				packager93 = new ISO93APackager();
				isoMsg.setPackager(packager93);
			}
			isoMsg.unpack(stringMessage.getBytes("US-ASCII"));// "US-ASCII"
			return isoMsg;
		} catch (Exception e) {
			System.out.println("Exception while unpacking " + e);
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

	public static void main(String[] args) throws ISOException, UnsupportedEncodingException {
//		String s = "0200F27A200108E0800000000000040000001011404630001000000000001070000130094304        09430409430401300130404069405005942924A3FBBMOB00002000000000105817test|Kimani|Elizabeth||0008527001       404130010008527001";
//		String s2 = "0200F27A200108E0800000000000040000001011404630001000000000001070000130094304        09430409430401300130404069405005942924A3FBBMOB00002000000000105817test|Kimani|Elizabeth||0008527001       404130010008527001";
//		ISOMsg msg = unpackMessage(s2, "87");
//		logISOMsg(msg, "");

		String s =
				"1200FA3A800108E080000000000004000000061234560000110000000123450000000043111102155116000001181102155116181118110211020812312312232         123     2132           0533122003169876543210123456";
		
		ISOMsg msg = unpackMessage(s, "93");
		logISOMsg(msg, "");
		
		//logISOMsg(unpackMessage(s, "iso93"), "93");
		//logISOMsg(unpackMessage(s, "iso87"), "87");
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
		// String padded = String.format("%12s", "1234");
		// System.out.println(getIso());
//		String s = "1652909800096123010000000000000000000200000000600511201811130950422018111320006006827831714600511000000000000001166005131300600040CAFE EXPRESS>BURLINGTON               US15000404D000000000000000000000000D000000000000000040400404D000000000000000000000000D000000000000000040400404D000000000000000000000000D0000000000000000404N M  16AC1015001106001003SWT";
//		s = "INTB400000000000000001000000000758757 201811151353552018032620006000000404150011900010002751500119000100006203SWT";
//		String s = "18040030010000000000444444201809101129831";
//		 //System.out.println(s.getBytes("US-ASCII"));
//		logISOMsg(unpackMessage(s, "93"), "");
//		//getIso();
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

//		<isomsg direction="incoming">
//		<field id="0" value="1804"/>
//		<field id="11" value="444444"/>
//		<field id="12" value="20180910112931"/>
//		<field id="24" value="831"/>
//		</isomsg>

		msg.setMTI("1804");
		msg.set(11, "444444");
		msg.set(12, "201809101129");
		msg.set(24, "831");

		ISO93APackager p = new ISO93APackager();
		msg.setPackager(p);
		System.out.println(new String(msg.pack()));
		return null;
	}

}
