package FlappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

	public static FlappyBird flappyBird;

	public final int WIDTH = 800, HIGHT = 800;

	public Renderer renderer;

	public Rectangle bird;

	public ArrayList<Rectangle> columns;

	public int ticks, yMotion, score;

	public Random rand;

	public boolean gameOver, started;

	public FlappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setSize(WIDTH, HIGHT);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setVisible(true);

		bird = new Rectangle(WIDTH / 2 - 10, HIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HIGHT - height - space));
		}
	}

	public void paintColum(Graphics g, Rectangle colum) {
		g.setColor(Color.green.darker());
		g.fillRect(colum.x, colum.y, colum.width, colum.height);
	}

	public void jump() {
		if (gameOver) {

			bird = new Rectangle(WIDTH / 2 - 10, HIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started) {
			started = true;
		} else if (!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int speed = 10;

		ticks++;
		
		if (!gameOver) {
			renderer.repaint();
		}
		if (started) {
			for (int i = 0; i < columns.size(); i++) {

				Rectangle column = columns.get(i);
				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++) {

				Rectangle column = columns.get(i);

				if (column.x + column.width < 0) {
					columns.remove(column);

					if (column.y == 0) {
						addColumn(false);
					}
				}

				// if (column.intersects(bird)) {
				// gameOver = true;
				//
				// bird.x = column.x - bird.width;
				// }
			}

			bird.y += yMotion;

			for (Rectangle column : columns) {
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10
						&& bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
					score++;
				}

				if (column.intersects(bird)) {
					gameOver = true;

					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;

					} else {
						if (column.y != 0) {
							bird.y = column.y - bird.height;
						} else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}
				}
			}
			if (bird.y > HIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}

			if (bird.y + yMotion >= HIGHT - 120) {
				bird.y = HIGHT - 120 - bird.height;
				gameOver = true;
			}

		}
		
	}

	public void paint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HIGHT - 120, WIDTH, 20);

		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		for (Rectangle column : columns) {
			paintColum(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (!started) {
			g.drawString("Click to start!", 75, HIGHT / 2 - 50);
		}

		if (gameOver) {
			g.drawString("Game Over!", 105, HIGHT / 2 - 50);
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args) {
		flappyBird = new FlappyBird();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
