import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MidpointLineAlgorithm extends JFrame {
	private List<Point> points = new ArrayList<>();
	private Point startPoint;
	private Point endPoint;

	public MidpointLineAlgorithm() {
		setTitle("Midpoint Line Program");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

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
		translateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dx = Integer.parseInt(JOptionPane.showInputDialog("translation x:"));
				int dy = Integer.parseInt(JOptionPane.showInputDialog("translation y:"));
				translate(dx, dy);
			}
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
			lineDraw(g2d, startPoint, endPoint);
		}
	}

	private void lineDraw(Graphics2D g2d, Point startPoint, Point endPoint) {
		int x0 = (int) startPoint.getX();
		int y0 = (int) startPoint.getY();
		int x1 = (int) endPoint.getX();
		int y1 = (int) endPoint.getY();

		int dx = x1 - x0;
		int dy = y1 - y0;
		int d = 2 * dy - dx;
		int x = x0;
		int y = y0;
		writePixel(g2d, x, y);

		int orderX, orderY;

		// 오른쪽부터 할지 왼쪽부터 할지 결정
		if (dx >= 0) { // x1 >= x0
			orderX = 1;
		} else { // x1 < x0
			orderX = -1;
			dx = -dx;
		}

		if (dy >= 0) { // y1 >= y0
			orderY = 1;
		} else { // y1 < y0
			orderY = -1;
			dy = -dy;
		}

		if (dx > dy) { // 1보다 작을때
			int deltaA = 2 * (dy - dx);
			int deltaB = 2 * dy;
			while (x != x1) {
				x += orderX;
				if (d <= 0) {
					d += deltaB;
				} else {
					y += orderY;
					d += deltaA;
				}
				writePixel(g2d, x, y);
			}
		} else { // 1보다 클 때
			int deltaA = 2 * (dx - dy);
			int deltaB = 2 * dx;
			while (y != y1) {
				y += orderY;
				if (d <= 0) {
					d += deltaB;
				} else {
					x += orderX;
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