package spaghetti.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EnumSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import spaghetti.curve.AttractorAnimator;
import spaghetti.curve.AttractorAnimator.Temperament;

import spaghetti.svg.SVGExporter;

public class CurveFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -3434999019395338309L;
	private CurveCanvas canvas;

	public CurveFrame() {

		//setIgnoreRepaint(true);


		canvas = new CurveCanvas();

		canvas.setIgnoreRepaint(true);

		setLayout(new BorderLayout());
		add(canvas, BorderLayout.WEST);
		add(new JPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.NORTH);
		//add(createTablePanel(), BorderLayout.EAST);


		canvas.setBounds(0, 0, 600, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();

	}


	private JPanel createButtonPanel() {
		JToggleButton playpause = new JToggleButton("Play");

		final JSlider speed = new JSlider(0, 16);
		speed.setSnapToTicks(true);
		speed.setMajorTickSpacing(1);

		final JButton export = new JButton("Export to SVG");

		final JButton convert = new JButton("Render exisiting SVG");

		speed.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setFramesDropped((int) Math.pow(2, speed.getValue()) - 1);

			}});



		playpause.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				canvas.playPause();
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});


		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(CurveFrame.this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       File f = chooser.getSelectedFile();
			       //SVGExporter.CurveToSVG(canvas.getCurve(), f);
			       SVGExporter.CurveToSVG(canvas.getCurve(), f);
			       //SVGExporter.CurveToPNG(canvas.getCurve(), f, canvas.getBounds());
			    }

			}});

		convert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				RenderForm form = new RenderForm();
				form.setVisible(true);

			}
		});

		final JCheckBox order = new JCheckBox("Order");
		final JCheckBox chaos = new JCheckBox("Chaos");
		order.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				EnumSet<AttractorAnimator.Temperament> temp = EnumSet.noneOf(AttractorAnimator.Temperament.class);
				if (order.isSelected()) temp.add(Temperament.Orderly);
				if (chaos.isSelected()) temp.add(Temperament.Chaotic);

				canvas.setTemperament(temp);
			}
		});


		chaos.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				EnumSet<AttractorAnimator.Temperament> temp = EnumSet.noneOf(AttractorAnimator.Temperament.class);
				if (order.isSelected()) temp.add(Temperament.Orderly);
				if (chaos.isSelected()) temp.add(Temperament.Chaotic);

				canvas.setTemperament(temp);
			}
		});




		JPanel panel = new JPanel();

		panel.add(playpause);
		panel.add(speed);
		panel.add(order);
		panel.add(chaos);
		panel.add(export);
		panel.add(convert);

		return panel;
	}


	private JPanel createTablePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final DefaultTableModel model = new DefaultTableModel();
		model.addColumn("X Speed");
		model.addColumn("Y Speed");
		JTable table = new JTable(model);
		table.setFillsViewportHeight(false);

		panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		panel.add(table, BorderLayout.CENTER);

		final JTextField xinput = new JTextField(5);
		final JTextField yinput = new JTextField(5);
		JButton addBtn = new JButton("Add");

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(xinput, BorderLayout.NORTH);
		buttonPanel.add(yinput, BorderLayout.NORTH);
		buttonPanel.add(addBtn, BorderLayout.NORTH);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					float vx = Float.parseFloat(xinput.getText());
					float vy = Float.parseFloat(yinput.getText());
					model.addRow(new Object[] {vx, vy});
				} catch (NumberFormatException e) {
					xinput.setText("");
					yinput.setText("");
				}

			}});

		return panel;
	}

	public CurveCanvas getCanvas() {
		return canvas;
	}

}
