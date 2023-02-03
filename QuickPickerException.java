package edu.cuny.csi.csc330.lab6;

public class QuickPickerException extends Exception {
	public QuickPickerException(String fileName) {
		super("Can't find: " + fileName + " Unknown Game - Can't locate specified game.");
	}
}
