package game.obj;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;//tool that helps resize rotate..
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
public class Ashebari {
    public Ashebari() {
        this.image = new ImageIcon(getClass().getResource("/game/image/waarrior2.jpg")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/game/image/warrior1.jpg")).getImage();
    }

    public static final double PLAYER_SIZE = 64; // pixles cuz small n opptimized
    private double x;
    private double y;
    private final float MAX_SPEED = 1f;
    private float speed = 0f; // stand still
    private float angle = 0f;
    private final Image image;
    private final Image image_speed;
    private boolean speedUp;

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(int width, int height) {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;

// boundaries
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > width - PLAYER_SIZE) x = width - PLAYER_SIZE;
        if (y > height - PLAYER_SIZE) y = height - PLAYER_SIZE;
    }

    public void changeAngle(float angle) {
        if (angle < 0) {
            angle = 359; // sets it as turning 360 from left
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void draw(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform trans = new AffineTransform();
   // placing on a table the toy..char in this case
        trans.rotate(Math.toRadians(angle + 2), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        g2.drawImage(speedUp ? image_speed : image, trans, null);
        g2.setTransform(oldTransform);
    }

    public Area getShape() {
        return new Area(new Rectangle2D.Double(x, y, PLAYER_SIZE, PLAYER_SIZE));
    }
    //rec hit box
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public void speedUp() {
        speedUp = true;
        speed = MAX_SPEED;
    }

    public void speedDown() {
        speedUp = false;
        speed = 0;
    }
}
