package game.component;
import game.obj.Bullet;
import game.obj.Ashebari;
import game.obj.ItalianSoilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PanelGame extends JComponent {
    private Graphics2D g2;
    private BufferedImage image;
    private int width;
    private int height;
    private Thread thread;
    public boolean start = true;
    private boolean gameOver = false;
    private boolean showIntro = true;
    private Key key;
    private int shotTime; // in b/n delay
    private int score = 0;

    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;
    private Ashebari ashebari;
    private List<Bullet> bullets; // fast iteration, track n store
    private List<ItalianSoilder> italianSoilders;

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            // gets blank canvas
        g2 = image.createGraphics(); // paintbrush

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//graphics less pix
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);// img less blurry

        thread = new Thread(() -> {
            while (start) {
                try {
                    long startTime = System.nanoTime(); // starts stop watch
                    drawBackground();
                    drawGame();
                    if (!showIntro && !gameOver) {
                        updateGame();
                    }
                    render();
                    long time = System.nanoTime() - startTime; // chekcs timer
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
                        sleep(sleep); //sleeps for calculated time
                    }
                } catch (Throwable t) {

                }
            }
        });

        initObjectGame();
        initKeyboard();
        initBullets();
        thread.start();
    }

    private void initObjectGame() {
        ashebari = new Ashebari();
        ashebari.changeLocation(150, 150);
        italianSoilders = new ArrayList<>(); //waiting list
        new Thread(() -> {  // spawns sold
            while (start) {
                try {
                    if (!showIntro && !gameOver) {
                        addRocket();
                    }
                    sleep(3000);//3 sec
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

    private void addRocket() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;

        ItalianSoilder italianSoilder = new ItalianSoilder();
        italianSoilder.changeLocation(0, locationY);
        italianSoilder.changeAngle(0);
        italianSoilders.add(italianSoilder);

        ItalianSoilder italianSoilder2 = new ItalianSoilder();
        italianSoilder2.changeLocation(width, locationY);
        italianSoilder2.changeAngle(180);  //left
        italianSoilders.add(italianSoilder2);
    }

    private void initKeyboard() {
        key = new Key();
        requestFocus();// window recieves input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) key.setKey_left(true);
                else if (e.getKeyCode() == KeyEvent.VK_D) key.setKey_right(true);
                else if (e.getKeyCode() == KeyEvent.VK_SPACE) key.setKey_space(true);
                else if (e.getKeyCode() == KeyEvent.VK_J) key.setKey_J(true);
                else if (e.getKeyCode() == KeyEvent.VK_K) key.setKey_K(true);
                else if (e.getKeyCode() == KeyEvent.VK_ENTER && showIntro) showIntro = false;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) key.setKey_left(false);
                else if (e.getKeyCode() == KeyEvent.VK_D) key.setKey_right(false);
                else if (e.getKeyCode() == KeyEvent.VK_SPACE) key.setKey_space(false);
                else if (e.getKeyCode() == KeyEvent.VK_J) key.setKey_J(false);
                else if (e.getKeyCode() == KeyEvent.VK_K) key.setKey_K(false);
            }
        });

        new Thread(() -> { // actual / speed of turn
            float s = 0.5f;
            while (start) {
                try {
                    if (!showIntro && !gameOver) {
                        float angle = ashebari.getAngle();
                        if (key.isKey_left()) angle -= s;
                        if (key.isKey_right()) angle += s;

                        if (key.isKey_J() || key.isKey_K()) {
                            if (shotTime == 0) {
                                bullets.add(0, new Bullet(ashebari.getX(), ashebari.getY(), ashebari.getAngle(),
                                        key.isKey_J() ? 5 : 20, 3f));
                            }
                            shotTime++;
                            if (shotTime == 30) shotTime = 0;
                        }

                        if (key.isKey_space()) ashebari.speedUp();
                        else ashebari.speedDown();

                        ashebari.update(width, height);
                        ashebari.changeAngle(angle);
                    }

                    for (int i = italianSoilders.size() - 1; i >= 0; i--) {
                        ItalianSoilder italianSoilder = italianSoilders.get(i);
                        if (italianSoilder != null) {
                            italianSoilder.update();
                            if (!italianSoilder.check(width, height)) {
                                italianSoilders.remove(i);
                            }
                        }
                    }

                    sleep(5);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

    private void initBullets() {
        bullets = new ArrayList<>();
        new Thread(() -> {
            while (start) {
                try {
                    if (!showIntro && !gameOver) {
                        for (int i = bullets.size() - 1; i >= 0; i--) {
                            Bullet bullet = bullets.get(i);
                            if (bullet != null) {
                                bullet.update();
                                checkBullets(bullet);
                                if (!bullet.check(width, height)) {
                                    bullets.remove(i);
                                }
                            }
                        }
                    }
                    sleep(1);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

    private void checkBullets(Bullet bullet) {
        for (int i = italianSoilders.size() - 1; i >= 0; i--) {
            ItalianSoilder italianSoilder = italianSoilders.get(i);
            if (italianSoilder != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(italianSoilder.getShape());
                if (!area.isEmpty()) {
                    italianSoilders.remove(i);
                    bullets.remove(bullet);
                    score += 10;
                }
            }
        }
    }

    private void checkCollisions() {
        Area playerArea = ashebari.getShape();
        for (int i = italianSoilders.size() - 1; i >= 0; i--) {
            ItalianSoilder italianSoilder = italianSoilders.get(i);
            if (italianSoilder != null) {
                Area rocketArea = italianSoilder.getShape();
                rocketArea.intersect(playerArea);
                if (!rocketArea.isEmpty()) {
                    gameOver = true;
                    break;
                }
            }
        }
    }

    private void updateGame() {
        if (!showIntro && !gameOver) {
            checkCollisions();
        }
    }

    private void drawBackground() {
        g2.setColor(new Color(0, 128, 0));
        g2.fillRect(0, 0, width, height);
    }

    private void drawGame() {
        if (showIntro) {
            g2.setColor(new Color(20, 20, 20));
            g2.fillRect(0, 0, width, height);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String title = "Defeat Italian Soldiers!";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, width / 2 - titleWidth / 2, height / 2 - 80);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String[] helpText = {
                    "Controls:",
                    "A - Move Left",
                    "D - Move Right",
                    "SPACE - Sprint",
                    "J - Shoot small bullet",
                    "K - Shoot big bullet",
                    "",
                    "Press ENTER to start"
            };
            int y = height / 2 - 20;
            for (String line : helpText) {
                int lineWidth = g2.getFontMetrics().stringWidth(line);
                g2.drawString(line, width / 2 - lineWidth / 2, y);
                y += 30;
            }
            return;
        }

        if (gameOver) {
            g2.setColor(new Color(255, 0, 0, 150));
            g2.fillRect(0, 0, width, height);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            String text = "GAME OVER";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, width / 2 - textWidth / 2, height / 2);
            return;
        }

        ashebari.draw(g2);
        for (Bullet bullet : bullets) {
            if (bullet != null) bullet.draw(g2);
        }
        for (ItalianSoilder italianSoilder : italianSoilders) {
            if (italianSoilder != null) italianSoilder.draw(g2);
        }
//. title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + score, 10, 25);
    }

    private void render() {
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
