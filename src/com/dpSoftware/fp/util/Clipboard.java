package com.dpSoftware.fp.util;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Clipboard {

	public static String getText() {
		try {
			return Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setClipboardText(String text) {
		StringSelection strSel = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(strSel, null);
	}
	
}
