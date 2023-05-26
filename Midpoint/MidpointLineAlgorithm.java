import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MidpointLineAlgorithm extends JFrame {
	private List<Point> points;
	private Point startPoint;
	private Point endPoint;

	public MidpointLineAlgorithm() {
		setTitle("Midpoint Line Program");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		points = new ArrayList<>();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (startPoint == null) {
					startPoint = e.getPoint();
				} else {
					endPoint = e.getPoint();
					points.add(startPoint);
					points.add(endPoint);
					startPoint = null;
					endPoint = null;
					repaint();
				}
			}
		});

		JPanel buttonPanel = new JPanel();
		JButton translateButton = new JButton("Translation");
		translateButton.addActionListener(e -> {
			int dx = Integer.parseInt(JOptionPane.showInputDialog("translation x:"));
			int dy = Integer.parseInt(JOptionPane.showInputDialog("translation y:"));
			translate(dx, dy);
		});
		buttonPanel.add(translateButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void translate(int dx, int dy) {
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			point.setLocation(point.getX() + dx, point.getY() - dy);
		}
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));

		for (int i = 0; i < points.size() - 1; i += 2) {
			Point startPoint = points.get(i);
			Point endPoint = points.get(i + 1);
			drawLine(g2d, startPoint, endPoint);
		}
	}

	private void drawLine(Graphics2D g2d, Point startPoint, Point endPoint) {
	    int x0 = (int) startPoint.getX();
	    int y0 = (int) startPoint.getY();
	    int x1 = (int) endPoint.getX();
	    int y1 = (int) endPoint.getY();

	    int dx = x1 - x0;
	    int dy = y1 - y0;
	    int stepX, stepY;

	    if (dx >= 0) {
	        stepX = 1;
	    } else {
	        stepX = -1;
	        dx = -dx;
	    }

	    if (dy >= 0) {
	        stepY = 1;
	    } else {
	        stepY = -1;
	        dy = -dy;
	    }

	    int d = 2 * dy - dx;
	    int x = x0;
	    int y = y0;

	    writePixel(g2d, x, y);

	    if (dx > dy) {
	        int deltaA = 2 * (dy - dx);
	        int deltaB = 2 * dy;
	        while (x != x1) {
	            x += stepX;
	            if (d < 0) {
	                d += deltaB;
	            } else {
	                y += stepY;
	                d += deltaA;
	            }
	            writePixel(g2d, x, y);
	        }
	    } else {
	        int deltaA = 2 * (dx - dy);
	        int deltaB = 2 * dx;
	        while (y != y1) {
	            y += stepY;
	            if (d < 0) {
	                d += deltaB;
	            } else {
	                x += stepX;
	                d += deltaA;
	            }
	            writePixel(g2d, x, y);
	        }
	    }
	}

	private void writePixel(Graphics2D g2d, int x, int y) {
	    g2d.drawLine(x, y, x, y);
	}

	public static void main(String[] args) {
		MidpointLineAlgorithm program = new MidpointLineAlgorithm();
		program.setVisible(true);
	}
}