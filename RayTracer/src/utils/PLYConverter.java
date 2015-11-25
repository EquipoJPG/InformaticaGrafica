package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import data.Vector4;
import objetos.Figura;

public class PLYConverter {

	public static void main(String[] args) {
		String a = "caca pedo pis\n luls";
		String lastWord = a.substring(a.lastIndexOf(" ")+1);
//		Scanner s = new Scanner(a);
//		Scanner p = new Scanner(s.nextLine().trim());
//		
//		String h = "";
//		while(p.hasNext()){
//			p.next();
//		}
		System.out.println(lastWord);
//		Figura f = getFigure("test.ply");
	}

	public static Figura getFigure(String plyFile) {
		boolean plyfile = false;
		ArrayList<Vector4> vertices = new ArrayList<Vector4>();
		int index = 0;
		int vertexNumber = -1;
		int faceNumber = -1;
		int indexX = -1;
		int indexY = -1;
		int indexZ = -1;
		int indexRed = -1;
		int indexGreen = -1;
		int indexBlue = -1;
		try {
			Scanner s = new Scanner(new File(plyFile));
			while (s.hasNextLine()) {
				String line = s.nextLine();
				Scanner lineScanner = new Scanner(line);
				String actual = lineScanner.next();
				
				// Check ply file
				if (!plyfile) {
					if (actual.equals("ply")) {
						// Yes, its a ply file
						plyfile = true;
						System.out.println("Hooray");
					} else {
						s.close();
						lineScanner.close();
						throw new Exception("Thats not a valid ply file");
					}
				} // End of checking ply file
				
				else {
					// Element line
					if (actual.equals("element")) {
						actual = lineScanner.next();
						// Vertices definition
						if (actual.equals("vertex")) {
							actual = lineScanner.next();
							vertexNumber = Integer.parseInt(actual);
						}
						
						// Faces definition
						else if(actual.equals("face")){
							actual = lineScanner.next();
							faceNumber = Integer.parseInt(actual);
						}
						index = 0;
					} // End of element line
					
					// Property line
					else if(actual.equals("property")){
						actual = line.substring(line.lastIndexOf(" ")+1);
						
						//Check x
						if(actual.equals("x")){
							indexX = index;
						}

						//Check y
						else if(actual.equals("y")){
							indexY = index;
						}
						
						//Check z
						else if(actual.equals("z")){
							indexZ = index;
						}
						
						//Check red
						else if(actual.equals("red")){
							indexRed = index;
						}
						
						//Check green
						else if(actual.equals("green")){
							indexRed = index;
						}
						
						//Check blue
						else if(actual.equals("blue")){
							indexRed = index;
						}
						index++;
					}
				}
			}
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
