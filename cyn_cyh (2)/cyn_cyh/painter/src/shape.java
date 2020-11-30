import java.awt.*;

public abstract class shape {
    public Color color = Color.black;
    public abstract void draw(Graphics g);
    public abstract String getInfo();
}

class Line extends shape {
    public Point p1 = new Point();
    public Point p2 = new Point();

    public String getInfo()
    {
        return "x1="+p1.x+",y1="+p1.y+".x2="+p2.x+",y2="+p2.y;
    }
    public Line( Point P1, Point P2)
    {
        p1.x = P1.x;
        p1.y = P1.y;
        p2.x = P2.x;
        p2.y = P2.y;
    }
    public void draw(Graphics g){
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
}

//class Curve extends shape{
//    public Curve(Point P1, Point P2){
//        super(P1, P2);
//    }
//    public void draw(Graphics g){
//        g.drawLine(p1.x, p1.y, p2.x, p2.y);
//    }
//}

//class Rectangle extends  shape{
//    public Rectangle(Point P1, Point P2){
//        super( P1, P2);
//    }
//    public void draw(Graphics g){
//        g.drawRect(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
//    }
//}
