package edu.sbcc.cs107;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Luke Lewis CS 107: Disassembler Project
 *
 *         This code implements working with a Hex file. The hex file format is
 *         documented at http://www.keil.com/support/docs/1584/
 */
public class HexFile {
	/**
	 * This is where you load the hex file. By making it an ArrayList you can easily
	 * traverse it in order.
	 */
	private ArrayList<String> hexFile = new ArrayList<String>();
	private int address;
	private int recordType;
	private int recordNdx;
	private int lineLoc;

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */
	public HexFile(String hexFileName) throws FileNotFoundException {
		Scanner s = new Scanner(new File("./" + hexFileName));
		while (s.hasNextLine()) {
			hexFile.add(s.nextLine());
		}
		s.close();
		address = getAddressOfRecord(hexFile.get(0));
		lineLoc = 9; // start of data on each record of the hex file
		recordNdx = 0;
	}

	/**
	 * Pulls the length of the data bytes from an individual record.
	 * 
	 * This extracts the length of the data byte field from an individual hex
	 * record. This is referred to as LL->Record Length in the documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return record length.
	 */
	public int getDataBytesOfRecord(String record) {
		int first = Character.getNumericValue(record.charAt(1));
		int second = Character.getNumericValue(record.charAt(2));
		int bytenum = (16 * first) + (second); //parseint gave me some issues here, so had to do it this way
		return bytenum;
	}

	/**
	 * Get the starting address of the data bytes.
	 * 
	 * Extracts the starting address for the data. This tells you where the data
	 * bytes start and are referred to as AAAA->Address in the documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return Starting address of where the data bytes go.
	 */
	public int getAddressOfRecord(String record) {
		String addressString = record.substring(3, 7);
		int addressInt = Integer.parseInt(addressString, 16);
		return addressInt;
	}

	/**
	 * Gets the record type.
	 * 
	 * The record type tells you what the record can do and determines what happens
	 * to the data in the data field. This is referred to as DD->Data in the
	 * documentation.
	 * 
	 * @param Hex
	 *            file record (one line).
	 * @return Record type.
	 */
	public int getRecordType(String record) {
		String typeString = record.substring(7, 9);
		int typeInt = Integer.valueOf(typeString);
		return typeInt;
	}

	/**
	 * Returns the next halfword data byte.
	 * 
	 * This function will extract the next halfword from the Hex file. By repeatedly
	 * calling this function it will look like we are getting a series of halfwords.
	 * Behind the scenes we must parse the HEX file so that we are extracting the
	 * data from the data files as well as indicating the correct address. This
	 * requires us to handle the various record types. Some record types can effect
	 * the address only. These need to be processed and skipped. Only data from
	 * recordType 0 will result in something returned. When finished processing null
	 * is returned.
	 * 
	 * @return Next halfword.
	 */
	public Halfword getNextHalfword() {
		String record = hexFile.get(recordNdx);
		String raw_data = null; 
		recordType = getRecordType(record);
		while (recordNdx < hexFile.size()) {//will break out of loop once recordNdx is at end
			if(recordType == 4) recordNdx++;//skip this recordType
			record = hexFile.get(recordNdx);
			if (record.length() > lineLoc + 4) { //there is still room on the record for another halfword
				raw_data = record.substring(lineLoc, lineLoc + 4);
			    lineLoc += 4;
			    break; //break out of our while loop. recordNdx and lineLoc is preserved so that we can keep track of where we are in our hexfile
			}
			else {
				recordNdx++;
				lineLoc = 9; //data always starts at index 9
				recordType = getRecordType(record);
			}
		}
		if (recordType == 1) {//EOF
			return null;
		}
		address += 2; 
		String data = raw_data.substring(2) + raw_data.substring(0,2); //flips our halfword from little endian to normal
		return new Halfword(address - 2, Integer.parseInt(data, 16));
	}
}
