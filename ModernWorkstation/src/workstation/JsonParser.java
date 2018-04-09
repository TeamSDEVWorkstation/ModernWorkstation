package workstation;
/*
 * Author: Curtis Warren
 * Description: This is a demo for the project SDEV Workstation (Modern Workstation)
 * to show how files should be saved in json form and how to work with the data for use in the program.
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonParser {
	
	ArrayList<Notes> notesArray = new ArrayList<Notes>();
        List<List<Notes>> noteHold = new ArrayList<List<Notes>>();
            
	
	public void save (ArrayList<Notes> notesList) {
		
		FileWriter writer = null;
		try {

			File notesFile = new File("src/workstation/Notes.json");
			writer = new FileWriter(notesFile);
			// [ starts a list in JSON/JavaScript
			writer.write("[\n");
			for (Notes note: notesList) {

				//  The note.toString() is written in JavaScript object notation i.e.  { "Title":"Store Data Here", \n"Category":"Store Category"}
				writer.write(note.toString());
				if (note.equals(notesList.get(notesList.size() - 1))) {

				} else {
				writer.write(",\n");
				}

			}
			writer.write("\n]");

			writer.flush();
			//  Calls the private parse(), to change the saved JSON back to a usable form.
			parse(notesFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//  Closes file writer.
			if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}

	}

	private String parse(File json) {
		
        StringBuilder builder = new StringBuilder();
		StringBuilder contentBuilder = new StringBuilder();

		Scanner scanner = null;
		FileReader reader = null;
		try {
			reader = new FileReader(json);
			
			int data = reader.read();
			while (data != -1) {
				
				builder.append((char)data);
				data = reader.read();
			}
			
			String dataString = builder.toString();
			
			scanner = new Scanner (dataString);
			String dataLine = null;
                        String titleHolder = null;
			
			/*
			 * Here we create notes and add them to the array list, this 
			 * array list will list out all of the notes to the user in a browse section of the gui.
			 */
                        notesArray.clear();
                        noteHold.clear();
			while(scanner.hasNextLine()){
											
			    dataLine = scanner.nextLine();

				if (dataLine.matches("[\\s\\S]*[\"Title\":][\\s\\S]*")) {
					
					Notes note = new Notes();
					int startOfTitle = dataLine.indexOf(":\"") + 2;
					int endOfTitle = dataLine.lastIndexOf("\"");

					//  Sets new title.
					note.setTitle(dataLine.substring(startOfTitle, endOfTitle));
					System.out.println(dataLine.substring(startOfTitle, endOfTitle));
					
					dataLine = scanner.nextLine();
					
					
					if(dataLine.matches("[\\s\\S]*[\"Category\":][\\s\\S]*")) {
						
						int startOfCategory= dataLine.indexOf(":\"") + 2;
						int endOfCategory = dataLine.lastIndexOf("\"");
						
						//  Sets new category.
						note.setCategory(dataLine.substring(startOfCategory, endOfCategory));
						titleHolder = dataLine.substring(startOfTitle, endOfTitle);
						System.out.println(dataLine.substring(startOfCategory, endOfCategory));
						
						dataLine = scanner.nextLine();

						if(dataLine.matches("[\\s\\S]*[\"Content\":][\\s\\S]*")) {
					
							int startOfContent = dataLine.indexOf(":\"") + 2;
							int endOfContent = dataLine.lastIndexOf("\"");
							
							contentBuilder.append(dataLine);

							while (!contentBuilder.toString().endsWith("\"")) {
								
								if(!scanner.hasNextLine()) {
									break;
								}
								dataLine = scanner.nextLine();
								contentBuilder.append("\n" + dataLine);

							}
							
							String contentString = contentBuilder.toString();
							
							contentBuilder.delete(0, contentBuilder.length());
							
							//  Sets new category.
							note.setContent(contentString.substring("\"Content\":\"".length(), contentString.length() - 1));
							System.out.println(contentString.substring("\"Content\":\"".length(), contentString.length() - 1));
notesArray.add(note);
                                                        //start my code
                                                        //noteHold.clear();
                                                        
                                                        if(noteHold.isEmpty())
                                                        {
                                                            noteHold.add(new ArrayList<>());
                                                            
                                                        }
                                                        
                                                        for(int x = 0; x < notesArray.size()+1; x++)
                                                        {
                                                            if(noteHold.get(x).isEmpty())
                                                            {
                                                                noteHold.add(new ArrayList<>());
                                                                noteHold.get(x).add(note);
                                                                if(x == 0)
                                                                {
                                                                    x++;
                                                                }
                                                                break;
                                                            }
                                                            else if(noteHold.get(x).get(0).getTitle().equalsIgnoreCase(note.getTitle()))
                                                            {
                                                                noteHold.get(x).add(note);
                                                                break;
                                                            }
                                                            
                                                        }
                                                        
                                                       /* for(List<Notes> current : noteHold)
                                                        {
                                                            Notes p = new Notes();
                                                           
                                                            if(current.isEmpty())
                                                            {
                                                                current.add(note);
                                                                
                                                            }
                                                            else if(current.get(0).getTitle().equals(note.getTitle()))
                                                            {
                                                                current.add(note);
                                                                
                                                                
                                                            }
                                                            else
                                                            {
                                                                //noteHold.add(new ArrayList<>());
                                                                noteHold.get(noteHold.size() - 1).add(note);
                                                                break;
                                                            }
                                                            
                                                        }*/
                                                        
                                                        //end of my code
							
						
						}
						
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (scanner != null) {
				scanner.close();
			}
		}
		
		return "null";
		
	}
	
	public static void main(String[] args) {
		
		JsonParser parser = new JsonParser();
		
		ArrayList<Notes> notesList = new ArrayList<Notes>();
		
		//  Simulates adding notes to the array, via the gui.
		notesList.add(new Notes());
		notesList.add(new Notes());
		notesList.add(new Notes());

		parser.save(notesList);
		
	}

}

