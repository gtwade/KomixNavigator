import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class testClass {

	//Fields
	private ImageIcon ii;
	private JLabel imageLabel;
	private JScrollPane scrollPane;

	public testClass() {
		//Create the JFrame to display on start
		JFrame jfrm = new JFrame();
		jfrm.setPreferredSize(new Dimension(500,500));

		//create and link the fields
		ii = new ImageIcon("someFile.gif");
		imageLabel = new JLabel(ii);
		scrollPane = new JScrollPane(imageLabel);

		//set size for the scrollPane
		scrollPane.setPreferredSize(new Dimension(400,400));

		//add scrollPane to the JFrame and make the JFrame visible
		jfrm.add(scrollPane);
		jfrm.setVisible(true);
	}

	public void changeImage(String newImageFileName) {
		//This will change the image.  We can call this function and the image shown by the scroll
		//pane will change after the function returns.
		ii = new ImageIcon(newImageFileName);
		return;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	testClass tc = new testClass();
            }
        });
	}
}

