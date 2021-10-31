package spaghetti.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import spaghetti.svg.SVGExporter;
import spaghetti.svg.SVGInfo;

public class RenderForm extends JFrame {

	private static final long serialVersionUID = -7221258526048835679L;

	private File svgFile;
	private Rectangle originalDimensions;

	public RenderForm() {
		JPanel panel = new JPanel();
		this.add(panel);

		JButton chooseFile = new JButton("Choose file...");
		panel.add(chooseFile);

		JLabel widthL = new JLabel("width (inches):");
		panel.add(widthL);
		final JTextField widthField = new JTextField(6);
		panel.add(widthField);

		JLabel heightL = new JLabel("height (inches):");
		panel.add(heightL);
		final JTextField heightField = new JTextField(6);
		panel.add(heightField);

		JLabel dpiL = new JLabel("pixels per inch:");
		panel.add(dpiL);
		final JTextField dpiField = new JTextField(6);
		panel.add(dpiField);


		final JButton render = new JButton("Render");
		panel.add(render);
		render.setEnabled(false);

		chooseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(RenderForm.this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       svgFile = chooser.getSelectedFile();
			       originalDimensions = SVGInfo.getDimensions(svgFile);
			       render.setEnabled(true);
			    }

			}
		});

		render.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File pngFile = new File(svgFile.getPath() + ".svg");
					int widthInches = Integer.parseInt(widthField.getText());
					int heightInches = Integer.parseInt(heightField.getText());
					int dpi = Integer.parseInt(dpiField.getText());

					SVGExporter.SVGtoPNG(svgFile, pngFile, widthInches * dpi, heightInches * dpi);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}

			}
		});


		this.pack();



	}

}
