import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class shape {
    public Color color = Color.black;
    public int bs = BasicStroke.CAP_BUTT;
    public abstract void draw(Graphics g);
    public abstract String getInfo();
    public shape(Color color, int width){
        this.color = color;
        bs = width;
    }
    public shape(){}
}

class Line extends shape {
    public Point p1 = new Point();
    public Point p2 = new Point();

    public Line(Point P1, Point P2, Color color, int width)
    {
        super(color, width);
        p1.x = P1.x;
        p1.y = P1.y;
        p2.x = P2.x;
        p2.y = P2.y;
    }

    @Override
    public String getInfo()
    {
        return "x1="+p1.x+",y1="+p1.y+".x2="+p2.x+",y2="+p2.y;
    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.setColor(color);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
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

class Rectangle extends  shape{
    public Point p1 = new Point();
    public Point p2 = new Point();

    public Rectangle(Point P1, Point P2, Color color, int width)
    {
        super(color, width);
        p1.x = P1.x;
        p1.y = P1.y;
        p2.x = P2.x;
        p2.y = P2.y;
    }

    @Override
    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.setColor(color);
        g.drawRect(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }

    @Override
    public String getInfo()
    {
        return null;
    }
}

class Circle extends  shape{
    public Point p1 = new Point();
    public Point p2 = new Point();

    public Circle(Point P1, Point P2, Color color, int width)
    {
        super(color, width);
        Point center = new Point();
        center.x = (P1.x + P2.x)/2;
        center.y = (P1.y + P2.y)/2;
        double r = (Math.sqrt(Math.pow(P1.x - P2.x,2) + Math.pow(P1.y - P2.y,2)))/2;
        p1.x = center.x - (int)r;
        p1.y = center.y - (int)r;
        p2.x = center.x + (int)r;
        p2.y = center.y + (int)r;
    }

    @Override
    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.setColor(color);
        g2d.drawArc(p1.x, p1.y, p2.x - p1.x,p2.y - p1.y,0, 360);
    }

    @Override
    public String getInfo()
    {
        return null;
    }
}

class Ellipse extends  shape{
    public Point p1 = new Point();
    public Point p2 = new Point();

    public Ellipse(Point P1, Point P2, Color color, int width)
    {
        super(color, width);
        p1.x = P1.x;
        p1.y = P1.y;
        p2.x = P2.x;
        p2.y = P2.y;
    }

    @Override
    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.setColor(color);
        g2d.drawArc(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y),0, 360);
    }

    @Override
    public String getInfo()
    {
        return null;
    }
}

class Fill extends shape{
    public Point p1 = new Point();
    public Color color = Color.black;
    public Color formercolor = Color.black;
    private Robot robot = new Robot();
    public ArrayList<Point> points = new ArrayList<>();

    public Fill(Point P1, Color c) throws AWTException {
        p1.x = P1.x;
        p1.y = P1.y;
        color = c;
        formercolor = robot.getPixelColor(p1.x, p1.y);
        System.out.println(formercolor.toString());
    }

    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        g.drawLine(p1.x, p1.y, p1.x, p1.y);
        points.add(p1);
        Color mycolor1 = robot.getPixelColor(p1.x-1,p1.y);
        Color mycolor2 = robot.getPixelColor(p1.x,p1.y-1);
        Color mycolor3 = robot.getPixelColor(p1.x+1,p1.y);
        Color mycolor4 = robot.getPixelColor(p1.x,p1.y+1);
        System.out.println(mycolor1.toString());
        System.out.println(mycolor2.toString());
        System.out.println(mycolor3.toString());
        System.out.println(mycolor4.toString());
//        Point p_1 = new Point(p1.x-1, p1.y);
//        Point p_2 = new Point(p1.x, p1.y-1);
//        Point p_3 = new Point(p1.x+1, p1.y);
//        Point p_4 = new Point(p1.x, p1.y+1);
//        if(mycolor1.equals(formercolor) && !points.contains(p_1))
//        {
//            System.out.println("in direction 11");
//            points.add(p_1);
//            drawRecursion(g, p1.x - 1, p1.y,1);
//        }
//        if(mycolor2.equals(formercolor) && !points.contains(p_2))
//        {
//            System.out.println("in direction 22");
//            points.add(p_2);
//            drawRecursion(g, p1.x, p1.y - 1,2);
//        }
//        if(mycolor3.equals(formercolor) && !points.contains(p_3))
//        {
//            System.out.println("in direction 33");
//            points.add(p_3);
//            drawRecursion(g, p1.x + 1, p1.y,3);
//        }
//        if(mycolor4.equals(formercolor) && !points.contains(p_4))
//        {
//            System.out.println("in direction 44");
//            points.add(p_4);
//            drawRecursion(g, p1.x, p1.y + 1,4);
//        }
//        System.out.println("递归结束");
//        for (Point p:
//                points
//             ) {
//            g.drawLine(p.x,p.y,p.x,p.y);
//        }
    }

    public void drawRecursion(Graphics g,int x, int y, int direction)
    {
        Color mycolor1 = robot.getPixelColor(x-1,y);
        Color mycolor2 = robot.getPixelColor(x,y-1);
        Color mycolor3 = robot.getPixelColor(x+1,y);
        Color mycolor4 = robot.getPixelColor(x,y+1);
        Point p_1 = new Point(x-1, y);
        Point p_2 = new Point(x, y-1);
        Point p_3 = new Point(x+1, y);
        Point p_4 = new Point(x, y+1);
            switch (direction) {
                case 1 -> {
                    System.out.println("进入了drawRecursion,向左扩展，当前点：("+x+","+y+")");
                    if(mycolor1.equals(formercolor) && !points.contains(p_1))
                    {
                        points.add(p_1);
                        drawRecursion(g, x - 1, y,1);
                    }
                    if(mycolor2.equals(formercolor) && !points.contains(p_2))
                    {
                        points.add(p_2);
                        drawRecursion(g, x, y - 1,2);
                    }
                    if(mycolor4.equals(formercolor) && !points.contains(p_4))
                    {
                        points.add(p_4);
                        drawRecursion(g, x, y + 1,4);
                    }
                }
                case 2 -> {
                    System.out.println("进入了drawRecursion,向上扩展，当前点：("+x+","+y+")");
                    if(mycolor1.equals(formercolor) && !points.contains(p_1))
                    {
                        points.add(p_1);
                        drawRecursion(g, x - 1, y,1);
                    }
                    if(mycolor2.equals(formercolor) && !points.contains(p_2))
                    {
                        points.add(p_2);
                        drawRecursion(g, x, y - 1,2);
                    }
                    if(mycolor3.equals(formercolor) && !points.contains(p_3))
                    {
                        points.add(p_3);
                        drawRecursion(g, x + 1, y,3);
                    }
                }
                case 3 -> {
                    System.out.println("进入了drawRecursion,向右扩展，当前点：("+x+","+y+")");
                    if(mycolor2.equals(formercolor) && !points.contains(p_2))
                    {
                        points.add(p_2);
                        drawRecursion(g, x, y - 1,2);
                    }
                    if(mycolor3.equals(formercolor) && !points.contains(p_3))
                    {
                        points.add(p_3);
                        drawRecursion(g, x + 1, y,3);
                    }
                    if(mycolor4.equals(formercolor) && !points.contains(p_4))
                    {
                        points.add(p_4);
                        drawRecursion(g, x, y + 1,4);
                    }
                }
                case 4 -> {
                    System.out.println("进入了drawRecursion,向下扩展，当前点：("+x+","+y+")");
                    if(mycolor1.equals(formercolor) && !points.contains(p_1))
                    {
                        points.add(p_1);
                        drawRecursion(g, x - 1, y,1);
                    }
                    if(mycolor3.equals(formercolor) && !points.contains(p_3))
                    {
                        points.add(p_3);
                        drawRecursion(g, x + 1, y,3);
                    }
                    if(mycolor4.equals(formercolor) && !points.contains(p_4))
                    {
                        points.add(p_4);
                        drawRecursion(g, x, y + 1,4);
                    }
                }
            }
    }
    @Override
    public String getInfo()
    {
        return null;
    }
}