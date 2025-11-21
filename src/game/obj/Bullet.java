package game.obj;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Bullet {
    private double x;
    private double y;
    private final Shape shape;  // locks variable (refrence) not contents.(final)
    private final Color color= new Color (255,255,255);
    private final float angle;
    private double size;  // diameter
    private float speed =1f;

     public Bullet(double x, double y, float angle, double size, float speed){
         this.size=size;
         x+= Ashebari.PLAYER_SIZE/2 -(size/2); // if not this bullet spawn top left
         y+= Ashebari.PLAYER_SIZE/2 -(size/2);// vertical
         this.x=x;
         this.y=y;
         this.angle=angle;
         this.speed=speed;
         shape= new Ellipse2D.Double(0,0,size,size);

     }
     public void update() {   //bullet movement
         x+=Math.cos(Math.toRadians(angle)) * speed;// based on angle if 0 x go y unchanged if 90y go xno change
         y+=Math.sin(Math.toRadians(angle)) * speed;
     }
     public  boolean check(int width, int height){  // boundaries
         if(x<= -size || y<-size || x > width || y> height) {
             return false;
         }else {
             return true;
         }



     }
     public void draw(Graphics2D g2){

         AffineTransform oldTransformldTransform= g2.getTransform(); // moves to new spot..drawing analogy
         g2.setColor(color);
         g2.translate(x,y);// moves shape pos to x,y( bullets pos instead of top left
         g2.fill(shape);
         g2.setTransform(oldTransformldTransform);
         // undoes tranlsate ..ensure no affection
     }
      public  Shape getShape(){ // collision
         return  new Area( new Ellipse2D.Double(x,y, size,size));
//copy of actual bullet
      }
     public double getX(){
         return x;
     }
    public double getY(){
        return y;
    }
    public double getSize(){
        return size;
    }
    public double getCenterX(){
        return x+size/2;
    }
    public double getCenterY(){
        return y+size/2;
    }
}

