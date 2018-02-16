package edu.sbcc.cs107;

/**
 * @author Luke Lewis
 * CS 107: Disassembler Project
 *
 * This class is used to model a half-word of an object file. Each half-word must have an address as well as a data
 * value that can be disassembled into mnemonics and optional operands.
 * 
 * Note that the half-word is 16 bits but we are using a Java int which is typically 32 bits. Be sure to take that into
 * account when working with it.
 *
 */
public class Halfword {
	private int address;
	private int data;
	
	/**
	 * Constructor for a halfword.
	 * 
	 * @param address
	 * @param data
	 */
	public Halfword(int _address, int _data) {
		address = _address;
		data = _data;
	}
	
	/** 
	 * toString method.
	 * 
	 * The format for the halfword is a hex value 8 characters wide (address), a single space, and a hex
	 * value four characters wide (data).
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String sAddress = String.format("%08X", address);
		String sData = String.format("%04X", data);
		String sFinal = sAddress + " " + sData;
		return sFinal;
	}

	/**
	 * Get the address of the half-word.
	 * 
	 * @return
	 */
	public int getAddress() {
		return address;
	}
	
	/**
	 * Get the data of the half-word.
	 * 
	 * @return
	 */
	public int getData() {
		return data;
	}

}
