package workstation;

/*
 * Author: Curtis Warren
 * Editor(s): Adam Levandowski (Functionality Tester) 
 * Description: This version has provided major changes to the application, the design has been changed to more accurately
 * reflect the UI design first introduced.
 * Version 3.1.1
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import workstation.Notes_Interface.CustomNotesComponent;


public class Notes_Interface extends JFrame {

	ArrayList<Notes> listOfNotes = new ArrayList<Notes>();
	private JButton saveBt;
	private JButton setCategoryBt;
	private JButton setTitleBt;
    	private String results;
    	private String catResults;
	Notes newNote = new Notes();

	private static JTextArea notesArea;
  	private static JTextField titleArea;
  	private static JTextField categoryArea;
  	private static JLabel titleLabel;
  	private static JLabel categoryLabel;
	//  The main panel for page two of the application.
	private JPanel browsingNotesPanel;
	private JPanel homePanel;
	private JPanel notesCreationPanel;
	private JPanel browsePane;
	private JPanel remindersPanel ;
	private JPanel recentNotesPanel ;

	private static Font notesFont;
	private static File outputFile;
	private static int currentlyEditedNoteIndex;
	private JButton browseButton;
	private static Font labelFont;


	static {

		currentlyEditedNoteIndex = -1;
		notesArea = new JTextArea();
		notesFont = new Font("Arial", 20, 20);
		labelFont = new Font("Arial", 15, 15);

	}

	public Notes_Interface ( ) {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 400);
		this.setResizable(false);

		createHomeScreen();

		//createBrowsingNotesScreen();

	}
	public JPanel populateRecentNotes(JPanel newRecentNotesPanel, int previewSize) {

		JsonParser recentParser = new JsonParser();

		int row = 0;
		int col = 0;
		Boolean alternateColor = true;
		if (recentParser.retrieveNotesArray() == null) {

		} else {

			listOfNotes = recentParser.retrieveNotesArray();

			for (Notes note : listOfNotes) {

				if (listOfNotes.size() - listOfNotes.indexOf(note) <= previewSize) {
					String contentPreview;

					if (note.getContent().length() < 14) {

						contentPreview = note.getContent();

					} else {

						contentPreview = note.getContent().substring(0, 10) + "...";

					}

					CustomNotesComponent customComponent = new CustomNotesComponent("Title: " + note.getTitle(), "Content: " + contentPreview, alternateColor);

					customComponent.setID(listOfNotes.indexOf(note));

					customComponent.edit.addActionListener(e -> {

						Notes editableNote = listOfNotes.get(customComponent.getID());

						currentlyEditedNoteIndex = customComponent.getID();

						createBrowsingNotesScreen();

						notesArea.setText(editableNote.getContent());

                         // Added methods to populate title and category fields
                         titleArea.setText(editableNote.getTitle());

                         categoryArea.setText(editableNote.getCategory());

						//  Resets after data for newNote.
						newNote.setContent("");
						newNote.setCategory("");
						newNote.setTitle("");

					});

					newRecentNotesPanel.add(customComponent, row, 0);

					System.out.print("[" + note.getContent() + "]");

					alternateColor = !alternateColor;
					row++;
				}
			}
		}

		return newRecentNotesPanel;

	}
	public JPanel resetBrowsePane(JPanel newBrowsePane) {

		JsonParser browseParser = new JsonParser();

		int row = 0;
		int col = 0;
		Boolean alternateColor = true;
		if (browseParser.retrieveNotesArray() == null) {

		} else {

			listOfNotes = browseParser.retrieveNotesArray();

			for (Notes note : listOfNotes) {

				String contentPreview;

				if (note.getContent().length() < 14) {

					contentPreview = note.getContent();

				} else {

					contentPreview = note.getContent().substring(0, 10) + "...";

				}

				CustomNotesComponent customComponent = new CustomNotesComponent("Title: " + note.getTitle(), "Content: " + contentPreview, alternateColor);

				customComponent.setID(listOfNotes.indexOf(note));

				customComponent.edit.addActionListener(e -> {

					Notes editableNote = listOfNotes.get(customComponent.getID());

					currentlyEditedNoteIndex = customComponent.getID();

					notesArea.setText(editableNote.getContent());

					// Added methods to populate title and category fields
                    titleArea.setText(editableNote.getTitle());

                    categoryArea.setText(editableNote.getCategory());

					//  Resets after data for newNote.
					newNote.setContent("");
					newNote.setCategory("");
					newNote.setTitle("");

				});

				newBrowsePane.add(customComponent, row, 0);

				System.out.print("[" + note.getContent() + "]");

				alternateColor = !alternateColor;
				row++;
			}
		}

		return newBrowsePane;

	}
	public void createBrowsingNotesScreen () {

		if (homePanel != null) {
			this.remove(homePanel);
		}

		JsonParser parser = new JsonParser();
		//  If notes have been saved to Notes.json we preserve them.
		if (parser.retrieveNotesArray() != null) {

			listOfNotes = parser.retrieveNotesArray();

		}

		GridBagConstraints constraints = new GridBagConstraints();

		Insets insets = new Insets(0,0,0,0);

		constraints.insets = insets;


		/* Start of home and browse button section */

		JPanel homeBrowseBtPanel = new JPanel(new GridBagLayout());

		JButton homeBt = new JButton("Home");

		homeBt.addActionListener( e -> {

			createHomeScreen();

		});

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		homeBrowseBtPanel.add(homeBt, constraints);

		JButton browseBt = new JButton("Browse");

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		homeBrowseBtPanel.add(browseBt, constraints);


		/* End of home and browse button section*/

		/* Start  of the search section */

		JPanel searchPanel = new JPanel(new GridBagLayout());

		JLabel noteTitle =  new JLabel ("Notes");
		noteTitle.setFont(labelFont);

		constraints.insets.right = 0;
		constraints.insets.left = 0;
		constraints.insets.bottom = 0;
		constraints.insets.top = 0;

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		searchPanel.add(noteTitle, constraints);

		JTextField searchField = new JTextField (25);
		searchField.setText("Enter Search Terms");
		searchField.setPreferredSize(new Dimension(20, 20));

		searchField.addMouseListener( new MouseAdapter () {

			@Override
			public void mouseClicked(MouseEvent e) {

				searchField.setText("");

			}

		});

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		searchPanel.add(searchField, constraints);

		JButton goBt = new JButton("Go");

		constraints.gridx = 5;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 0.25;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		searchPanel.add(goBt, constraints);
                
                goBt.addActionListener( e -> {
                    JsonParser recentParser = new JsonParser();
                    searchNotes sn = new searchNotes();
                    List<Notes> findKey;
                    List<Notes> findTitle;
                    File notesFile = new File("src/workstation/Notes.json");
                    recentParser.parse(notesFile);
                    findKey = sn.searchKeyword(recentParser.getList(), searchField.getText());
                    findTitle = sn.searchTitle(recentParser.getList(), searchField.getText());
                    if(findKey.size() > 0)
                    {
                    
                        JOptionPane.showMessageDialog(null, findKey.toString().replaceAll("\\{","").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", ""), "Keyword Matches:", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                    if(findTitle.size() > 0)
                    {
                        JOptionPane.showMessageDialog(null, findTitle.toString().replaceAll("\\{","").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", ""), "Title Matches:", JOptionPane.INFORMATION_MESSAGE);
                    }

		});

		/* End of the search section */

		/* Start of browse section */

		browsePane = new JPanel();

		browsePane.setLayout(new GridLayout(0, 1));

		browsePane = resetBrowsePane(browsePane);

		JScrollPane scrollBrowsePane = new JScrollPane(browsePane);
		scrollBrowsePane.setPreferredSize(new Dimension(200, 200));


		/* End of browse section */

		/* Start of notes creation section */
		notesCreationPanel = new JPanel(new GridBagLayout());

		/*------------------------------------------------*
                * Added Title and Category labels and text fields *
                *-------------------------------------------------*/

    titleLabel =  new JLabel("Title");
		titleLabel.setFont(labelFont);

    categoryLabel =  new JLabel("Category");
		categoryLabel.setFont(labelFont);

    titleArea = new JTextField(25);
		titleArea.setPreferredSize(new Dimension(20, 20));

    categoryArea = new JTextField(25);
    categoryArea.setPreferredSize(new Dimension(20, 20));

    constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		constraints.gridheight = 1;
		constraints.weighty = 2;

		constraints.fill = GridBagConstraints.HORIZONTAL;

		notesCreationPanel.add(titleLabel, constraints);

    constraints.gridy = 1;

    notesCreationPanel.add(titleArea, constraints);

    constraints.gridy = 2;

    notesCreationPanel.add(categoryLabel, constraints);

    constraints.gridy = 3;

    notesCreationPanel.add(categoryArea, constraints);

		notesArea = new JTextArea(10, 10);
		notesArea.setPreferredSize(new Dimension(10, 10));

		notesArea.setFont(notesFont);

		JScrollPane notesScrollPane = new JScrollPane(notesArea);
		notesScrollPane.setPreferredSize(new Dimension(200, 200));

		constraints.gridy = 4; // changed from 1 to 4
		constraints.gridheight = 5;

		constraints.fill = GridBagConstraints.HORIZONTAL;

		notesCreationPanel.add(notesScrollPane, constraints);

		constraints.weighty = 1;


		JButton editBt = new JButton ("Edit");

		constraints.gridx = 0; // changed from 2 to 0
		constraints.gridy = 9; // changed from 5 to 9
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;


		notesCreationPanel.add(editBt, constraints);

		editBt.addActionListener( e -> {

			if (currentlyEditedNoteIndex != -1) {

				newNote.setContent(notesArea.getText());

				// added set methods for added title and category areas
				newNote.setTitle(titleArea.getText());

				newNote.setCategory(categoryArea.getText());

				//  Edits ArrayList
				listOfNotes.remove(currentlyEditedNoteIndex);
				listOfNotes.add(currentlyEditedNoteIndex, newNote);
				//  Saves changes to the Notes.json file.
				parser.save(listOfNotes);
				//  removes the contents of the browsePane.
				browsePane.removeAll();
				//  Updates the browsePane with the updated ArrayList i.e. listOfNotes
				browsePane = resetBrowsePane(browsePane);
				//  Repaints browsePane and sets it to to be visible.
				browsePane.setVisible(false);
				browsePane.repaint();
				browsePane.setVisible(true);


			}


		});

		JButton deleteBt = new JButton ("Delete");

		constraints.gridx = 1; // changed from 3 to 1
		constraints.gridy = 9; // changed from 5 to 9
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		notesCreationPanel.add(deleteBt, constraints);

		deleteBt.addActionListener( e -> {

			if (currentlyEditedNoteIndex != -1) {

				listOfNotes.remove(currentlyEditedNoteIndex);

				parser.save(listOfNotes);

				browsePane.removeAll();

				browsePane = resetBrowsePane(browsePane);

				browsePane.setVisible(false);
				browsePane.repaint();
				browsePane.setVisible(true);

				currentlyEditedNoteIndex = -1;

			}


		});

		/* End of notes creation section */

		/* Start of the add note section */

		JPanel addNotePanel = new JPanel(new GridBagLayout());

		JButton addNoteBt = new JButton("Add Note");
		addNoteBt.setFont(notesFont);

		addNoteBt.addActionListener( e -> {

			//  Prevents unintentional editing of notes.
			currentlyEditedNoteIndex = -1;

			newNote.setContent(notesArea.getText());

      // added titls and category methods

      newNote.setTitle(titleArea.getText());

    	newNote.setCategory(categoryArea.getText());

			//  Adds to the list.
			listOfNotes.add(newNote);

			parser.save(listOfNotes);

			browsePane.removeAll();

			browsePane = resetBrowsePane(browsePane);

			browsePane.setVisible(false);
			browsePane.repaint();
			browsePane.setVisible(true);


		});

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;


		addNotePanel.add(addNoteBt, constraints);


		/* End of the add note section */


		browsingNotesPanel = new JPanel (new GridBagLayout());

		constraints.insets = new Insets(0, 5, 0, 0);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;


		browsingNotesPanel.add(homeBrowseBtPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		browsingNotesPanel.add(searchPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		browsingNotesPanel.add(scrollBrowsePane, constraints);

		constraints.insets = new Insets(0, 20, 0, 0);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;


		browsingNotesPanel.add(notesCreationPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;


		browsingNotesPanel.add(addNotePanel, constraints);


		this.add(browsingNotesPanel);

		this.repaint();
		this.setVisible(true);

	}
	public void createHomeScreen () {

		if (browsingNotesPanel != null) {
			this.remove(browsingNotesPanel);
		}

		homePanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();

		Insets insets = new Insets(0,0,0,0);

		constraints.insets = insets;

		/* Start of home and browse button section */

		JPanel homeBrowseBtPanel = new JPanel(new GridBagLayout());

		JButton homeBt = new JButton("Home");

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		homeBrowseBtPanel.add(homeBt, constraints);

		JButton browseBt = new JButton("Browse");

		browseBt.addActionListener( e -> {

			createBrowsingNotesScreen();

		});

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		homeBrowseBtPanel.add(browseBt, constraints);


		/* End of home and browse button section*/

		/* Start of recent notes section*/

		JPanel recentNotesSectionPanel = new JPanel(new GridBagLayout());

		JLabel recentNotesLabel = new JLabel("Recent Notes");
		recentNotesLabel.setFont(labelFont);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		recentNotesSectionPanel.add(recentNotesLabel, constraints);

		recentNotesPanel = new JPanel(new GridLayout(0, 1));

		recentNotesPanel = populateRecentNotes(recentNotesPanel, 10);

		JScrollPane recentNotesScroll = new JScrollPane(recentNotesPanel);
		recentNotesScroll.setPreferredSize(new Dimension(200, 200));

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		recentNotesSectionPanel.add(recentNotesScroll, constraints);


		/* End of recent notes section*/

		/* Start of reminders section*/

		JPanel remindersSectionPanel = new JPanel(new GridBagLayout());

		JLabel remindersLabel = new JLabel("Reminders");
		remindersLabel.setFont(labelFont);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		remindersSectionPanel.add(remindersLabel, constraints);

		remindersPanel = new JPanel(new GridLayout(0, 1));

		JScrollPane remindersScroll = new JScrollPane(remindersPanel);
		remindersScroll.setPreferredSize(new Dimension(100, 200));

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		remindersSectionPanel.add(remindersScroll, constraints);


		/* End of reminders section*/

		/* Start of the add note section */

		JPanel addNotePanel = new JPanel(new GridBagLayout());

		JButton addNoteBt = new JButton("Add Note");
		addNoteBt.setFont(notesFont);

		addNoteBt.addActionListener( e -> {

			createBrowsingNotesScreen();

		});

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;

		addNotePanel.add(addNoteBt, constraints);

		/* End of the add note section */

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		homePanel.add(homeBrowseBtPanel, constraints);

		constraints.insets = new Insets(5, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		homePanel.add(recentNotesSectionPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		homePanel.add(remindersSectionPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		homePanel.add(addNotePanel, constraints);

		this.add(homePanel);

		this.repaint();
		this.setVisible(true);


	}
	public void editNotesScreen () {

	}
	public void createNotesScreen () {

	}

	public void createOtherScreen () {

	}

	public void browseScreen () {


		//this.add(scrollBrowsePane);

		//  Cleanly repaints the new screen.
		this.repaint();
		this.setVisible(true);

	}

	public class CustomNotesComponent extends JPanel {

		JLabel noteTitle;
		JLabel noteContent;
		JButton edit;
		int ID;

		CustomNotesComponent(String newTitle, String newContent, Boolean alternate) {

			GridBagConstraints compConstraints = new GridBagConstraints();

			Insets compInsets = new Insets(0,0,0,0);

			compConstraints.insets = compInsets;

			noteTitle = new JLabel(newTitle);
			noteContent = new JLabel(newContent);
			edit = new JButton ("edit");

			this.setLayout(new GridBagLayout());

			compConstraints.gridx = 0;
			compConstraints.gridy = 0;
			compConstraints.weightx = 1;
			compConstraints.weighty = 1;
			compConstraints.gridwidth = 3;
			compConstraints.gridheight = 1;
			compConstraints.fill = GridBagConstraints.HORIZONTAL;


			this.add(noteTitle, compConstraints);

			compConstraints.gridx = 0;
			compConstraints.gridy = 1;
			compConstraints.weightx = 1;
			compConstraints.weighty = 1;
			compConstraints.gridwidth = 2;
			compConstraints.gridheight = 1;
			compConstraints.fill = GridBagConstraints.HORIZONTAL;

			this.add(noteContent, compConstraints);

			compConstraints.gridx = 2;
			compConstraints.gridy = 1;
			compConstraints.weightx = .2;
			compConstraints.weighty = .2;
			compConstraints.gridwidth = 1;
			compConstraints.gridheight = 1;
			compConstraints.fill = GridBagConstraints.HORIZONTAL;

			this.add(edit, compConstraints);

			//  Alternates color of background.
			if (alternate) {
				this.setBackground(new Color(.5f, .7f, .75f));
			} else {
				this.setBackground(new Color(.5f, .5f, .7f));
			}

		}

		void setID (int id) {

			this.ID =  id;

		}

		int getID () {

			return this.ID;

		}

	}

	public static void main (String [] args) {

			Notes_Interface notesScreen = new Notes_Interface();

			notesScreen.setVisible(true);

	}

}