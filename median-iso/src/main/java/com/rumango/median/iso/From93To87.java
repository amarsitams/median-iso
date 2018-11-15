package com.rumango.median.iso;

import java.time.Year;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;

public class From93To87 {

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
		System.out.println("-----------------" + stringMessage + "-----------------------");
		// System.out.println(" MTI : " + msg.getMTI() + msg.getString(0));
		for (int i = 0; i <= msg.getMaxField(); i++) {
			if (msg.hasField(i)) {
				System.out.println(i + " " + " : " + msg.getString(i));
			}
		}
	}

	private static String packMessage(ISOMsg isoMessage, String version) {
		try {
			if (version.equalsIgnoreCase("87")) {
				ISO87APackager packager = new ISO87APackager();
				isoMessage.setPackager(packager);
			} else {
				ISO93APackager packager = new ISO93APackager();
				isoMessage.setPackager(packager);
			}
			byte[] binaryImage = isoMessage.pack();
			return new String(binaryImage);
		} catch (ISOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws ISOException {

//		String iso93 = "1200FA3A800108E080000000000004000000061234560000110000000123450000000043111102155116000001181102155116181118110211020812312312232         123     2132           0533122003169876543210123456";
//		ISOMsg msg = unpackMessage(iso93, "93");
//		logISOMsg(msg, "---------93-----------");
//		ISOMsg msg87 = iso93TO87(msg);
//		logISOMsg(msg87, "---------87-----------");
//		System.out.println(packMessage(msg87, "87"));
//		
//		
//		ISOMsg msg93 = iso87TO93(msg87);
//		logISOMsg(msg93, "---------93-----------");
//		System.out.println(packMessage(msg93, "93"));

//		String iso87 = "0200F23A801F08A08010000000000400000014940400502010010100000000000020001024154319000001154319102410241024 00000000 00000000 00000000 0000000006940400768365278912CAN00001test                                    84001620182018201820180850201001";
		String iso87 = "0200F23A801F08A08010000000000400000014940400502010010100000000000020001024154319000001154319102410241024 00000000 00000000 00000000 0000000006940400768365278912CAN00001test                                    84001620182018201820180850201001";
		ISOMsg msg2 = unpackMessage(iso87, "87");
		System.out.println(new String(msg2.pack()));
		logISOMsg(msg2, "---------87-----------");
//		msg93 = iso87TO93(msg2);
//		logISOMsg(msg93, "---------93-----------");
//		System.out.println(packMessage(msg93, "93"));

//		StringBuilder builderTemp;
//		String strTemp = "1200";
//		builderTemp = new StringBuilder(strTemp);
//		builderTemp.setCharAt(0, '0');// ("0", 0, 1);//replace(1, 1, strTemp).toString();
//		System.out.println(builderTemp);

	}

	public static ISOMsg iso93TO87(ISOMsg isoMessage) {
		int len;
		StringBuilder builderTemp;
		String strTemp, iso12 = "";
		for (int i = 0; i <= isoMessage.getMaxField(); i++) {
			if (isoMessage.hasField(i)) {
				switch (i) {
				// Replace MTI
				case 0:
					strTemp = isoMessage.getString(i);
					builderTemp = new StringBuilder(strTemp);
					builderTemp.setCharAt(0, '0');
					isoMessage.set(0, builderTemp.toString());
					break;
				// 012 new IFA_NUMERIC ( 6, "TIME, LOCAL TRANSACTION"),
				case 12:
					strTemp = isoMessage.getString(i);
					iso12 = strTemp;
					isoMessage.set(12, isoMessage.getString(i).substring(6));
					break;
				// 013 new IFA_NUMERIC ( 4, "DATE, LOCAL TRANSACTION"),
				case 13:
					isoMessage.set(13, iso12.substring(2, 6));
					break;
				// 015 new IFA_NUMERIC ( 4, "DATE, SETTLEMENT"),
				case 15:
					strTemp = isoMessage.getString(i);
					isoMessage.set(15, strTemp.substring(2));
					break;
				// 022 new IFA_NUMERIC ( 3, "POINT OF SERVICE ENTRY MODE"),
				case 22:
					strTemp = isoMessage.getString(i);
					isoMessage.set(22, strTemp.substring(2));
					break;
				// 025 new IFA_NUMERIC ( 2, "POINT OF SERVICE CONDITION CODE")
				case 25:
					strTemp = isoMessage.getString(i);
					isoMessage.set(25, strTemp.substring(2));
					break;
				// 026 new IFA_NUMERIC ( 2, "POINT OF SERVICE PIN CAPTURE CODE")
				case 26:
					strTemp = isoMessage.getString(i);
					isoMessage.set(26, strTemp.substring(2));
					break;
				// 028 new IFA_AMOUNT ( 9, "AMOUNT, TRANSACTION FEE")
				case 28:
					isoMessage.unset(i);
					break;
				// 029 new IFA_AMOUNT ( 9, "AMOUNT, SETTLEMENT FEE")
				case 29:
					isoMessage.unset(i);
					break;
				// 030 new IFA_AMOUNT ( 9, "AMOUNT, TRANSACTION PROCESSING FEE")
				case 30:
					isoMessage.unset(i);
					break;
				// 031 new IFA_AMOUNT ( 9, "AMOUNT, SETTLEMENT PROCESSING FEE")
				case 31:
					isoMessage.unset(i);
					break;
				// 039 new IF_CHAR ( 2, "RESPONSE CODE")
				case 39:
					strTemp = isoMessage.getString(i);
					isoMessage.set(39, strTemp.substring(1));
					break;
				// 043 new IF_CHAR ( 40, "CARD ACCEPTOR NAME/LOCATION")
				case 43:
					strTemp = isoMessage.getString(i);
					len = strTemp.length();
					if (len > 40) {
						isoMessage.set(43, strTemp.substring(0, 39));
					} else
						isoMessage.set(43, strTemp);
					break;
				// 044 new IFA_LLCHAR ( 25, "ADITIONAL RESPONSE DATA")
				case 44:
					strTemp = isoMessage.getString(i);
					len = strTemp.length();
					if (len > 25) {
						isoMessage.set(44, strTemp.substring(0, 24));
					} else
						isoMessage.set(44, strTemp);
					break;

				case 46:
					isoMessage.unset(i);
//					strTemp = isoMessage.getString(i);
//					len = strTemp.length();
//					if (len > 25) {
//						isoMessage.set(44, strTemp.substring(0, 24));
//					} else
//						isoMessage.set(44, strTemp);
					break;

				case 53:
					isoMessage.unset(i);
					break;
				case 55:
					isoMessage.unset(i);
					break;

				case 56:
					strTemp = isoMessage.getString(i);
					isoMessage.set(90, strTemp.substring(1));
					break;

				case 57:
					isoMessage.unset(i);
					break;

				case 58:
					isoMessage.unset(i);
					break;

				case 65:
					isoMessage.unset(i);
					break;

				case 66:
					isoMessage.unset(i);
					break;

				// 071 new IFA_NUMERIC ( 4, "MESSAGE NUMBER")
				case 71:
					strTemp = isoMessage.getString(i);
					isoMessage.set(71, strTemp.substring(0, 4));
					break;

				case 72:
					isoMessage.unset(i);
					break;

				case 82:
					isoMessage.unset(i);
					break;

				case 83:
					isoMessage.unset(i);
					break;

				case 84:
					isoMessage.unset(i);
					break;

				case 85:
					isoMessage.unset(i);
					break;

				case 90:
					isoMessage.unset(i);
					break;

				case 91:
					isoMessage.unset(i);
					break;

				case 92:
					strTemp = isoMessage.getString(i);
					isoMessage.set(19, strTemp);
					isoMessage.unset(i);
					break;
				case 93:
					isoMessage.unset(i);
					break;

				case 94:
					isoMessage.unset(i);
					break;

				case 95:
					isoMessage.unset(i);
					break;

				case 96:
					isoMessage.unset(i);
					break;
				case 105:
					isoMessage.unset(i);
					break;

				case 106:
					isoMessage.unset(i);
					break;

				case 107:
					isoMessage.unset(i);
					break;

				case 108:
					isoMessage.unset(i);
					break;

				case 109:
					isoMessage.unset(i);
					break;

				case 110:
					isoMessage.unset(i);
					break;

				default:
					break;
				}
			}
		}
		return isoMessage;
	}

	public static ISOMsg iso87TO93(ISOMsg isoMessage) {
		int year = Year.now().getValue() % 100;
		StringBuilder builderTemp;
		String strTemp, iso12 = "";
		for (int i = 0; i <= isoMessage.getMaxField(); i++) {
			if (isoMessage.hasField(i)) {
				switch (i) {
				// Replace MTI
				case 0:
					strTemp = isoMessage.getString(i);
					builderTemp = new StringBuilder(strTemp);
					builderTemp.setCharAt(0, '1');
					isoMessage.set(0, builderTemp.toString());
					break;
				// 012 new IFA_NUMERIC ( 12, "TIME, LOCAL TRANSACTION")
				case 12:
					builderTemp = new StringBuilder();
					builderTemp.append(year).append(isoMessage.getString(13)).append(isoMessage.getString(i));
					iso12 = builderTemp.toString();
					isoMessage.set(12, iso12);
					break;
				// 013 new IFA_NUMERIC ( 4, "DATE, LOCAL TRANSACTION")
				case 13:
					isoMessage.set(13, iso12.substring(0, 4));
					break;
				// 015 new IFA_NUMERIC ( 6, "DATE, SETTLEMENT")
				case 15:
					isoMessage.set(15, iso12.substring(0, 6));
					break;
				// 022 new IFA_NUMERIC ( 12, "POINT OF SERVICE ENTRY MODE")
				case 22:
					isoMessage.set(22, String.format("%12s", isoMessage.getString(i)));
					break;
				// 025 new IFA_NUMERIC ( 2, "POINT OF SERVICE CONDITION CODE")
				case 25:
					isoMessage.unset(i);
//					strTemp = isoMessage.getString(i);
//					isoMessage.set(25, strTemp.substring(2));
					break;
				// 026 new IFA_NUMERIC ( 2, "POINT OF SERVICE PIN CAPTURE CODE")
				case 26:
					isoMessage.unset(i);
//					strTemp = isoMessage.getString(i);
//					isoMessage.set(26, strTemp.substring(2));
					break;
				// 028 new IFA_AMOUNT ( 9, "AMOUNT, TRANSACTION FEE")
				case 28:
					isoMessage.set(i, isoMessage.getString(i).substring(0, 6));
					// isoMessage.unset(i);
					break;
				// 029 new IFA_AMOUNT ( 9, "AMOUNT, SETTLEMENT FEE")
				case 29:
					isoMessage.unset(i);
					break;
				// 030 new IFA_AMOUNT ( 9, "AMOUNT, TRANSACTION PROCESSING FEE")
				case 30:
					isoMessage.unset(i);
					break;
				// 031 new IFA_AMOUNT ( 9, "AMOUNT, SETTLEMENT PROCESSING FEE")
				case 31:
					isoMessage.unset(i);
					break;
				// 039 new IF_CHAR ( 2, "RESPONSE CODE")
				case 39:
					builderTemp = new StringBuilder();
					builderTemp.append(0).append(isoMessage.getString(i));
					isoMessage.set(39, builderTemp.toString());
					break;
				// 043 new IF_CHAR ( 40, "CARD ACCEPTOR NAME/LOCATION")
				case 43:
					isoMessage.set(i, String.format("%-99s", isoMessage.getString(i)));
					break;
				// 044 new IFA_LLCHAR ( 25, "ADITIONAL RESPONSE DATA")
				case 44:
					isoMessage.set(i, String.format("%99s", isoMessage.getString(i)));
					break;

				case 46:
					isoMessage.unset(i);
//					strTemp = isoMessage.getString(i);
//					len = strTemp.length();
//					if (len > 25) {
//						isoMessage.set(44, strTemp.substring(0, 24));
//					} else
//						isoMessage.set(44, strTemp);
					break;

				case 53:
					isoMessage.unset(i);
					break;
				case 55:
					isoMessage.unset(i);
					break;

				case 56:
					strTemp = isoMessage.getString(i);
					isoMessage.set(i, String.format("%35s", isoMessage.getString(90)));
					break;

				case 57:
					isoMessage.unset(i);
					break;

				case 58:
					isoMessage.unset(i);
					break;

				case 65:
					isoMessage.unset(i);
					break;

				case 66:
					isoMessage.unset(i);
					break;

				// 071 new IFA_NUMERIC ( 4, "MESSAGE NUMBER")
				case 71:
					strTemp = isoMessage.getString(i);
					isoMessage.set(i, String.format("%8s", isoMessage.getString(i)));
					break;

				case 72:
					isoMessage.unset(i);
					break;

				case 82:
					isoMessage.unset(i);
					break;

				case 83:
					isoMessage.unset(i);
					break;

				case 84:
					isoMessage.unset(i);
					break;

				case 85:
					isoMessage.unset(i);
					break;

				case 90:
					isoMessage.unset(i);
					break;

				case 91:
					isoMessage.unset(i);
					break;

				case 92:
					isoMessage.set(i, isoMessage.getString(19));
					break;
				case 93:
					isoMessage.unset(i);
					break;

				case 94:
					isoMessage.unset(i);
					break;

				case 95:
					isoMessage.unset(i);
					break;

				case 96:
					isoMessage.unset(i);
					break;
				case 105:
					isoMessage.unset(i);
					break;

				case 106:
					isoMessage.unset(i);
					break;

				case 107:
					isoMessage.unset(i);
					break;

				case 108:
					isoMessage.unset(i);
					break;

				case 109:
					isoMessage.unset(i);
					break;

				case 110:
					isoMessage.unset(i);
					break;

				default:
					break;
				}
			}
		}
		return isoMessage;
	}

}
