import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class shape implements Serializable {
    public Color color = Color.black;
    public int bs = BasicStroke.CAP_BUTT;
    public int state = 0;
    public abstract void draw(Graphics g);
    public shape(Color color, int width){
        this.color = color;
        bs = width;
    }
    public shape(){}
    public abstract void contains(Point P1, Point P2);
    public abstract void move(Point P1,Point P2);
    public void endMove()
    {
        state = 0;
    }
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
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.setColor(color);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    public void contains(Point P1, Point P2) {
        if(Math.min(p1.x,p2.x) >= Math.min(P1.x,P2.x) && Math.min(p1.y,p2.y) >= Math.min(P1.y,P2.y)){
            if(Math.max(p1.x,p2.x) <= Math.max(P1.x,P2.x) && Math.max(p1.y,p2.y) <= Math.max(P1.y,P2.y)){
                state = 1;
            }
        }
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            p1.x = p1.x + P2.x - P1.x;
            p1.y = p1.y + P2.y - P1.y;
            p2.x = p2.x + P2.x - P1.x;
            p2.y = p2.y + P2.y - P1.y;
        }
    }
}

class Curve extends shape{
    public ArrayList<Line> lines = new ArrayList<>();
    @Override
    public void draw(Graphics g) {
        for (Line l:
                lines
        ) {
            l.draw(g);
        }
    }

    @Override
    public void contains(Point P1, Point P2) {
        Point pmin = new Point(Math.min(P1.x,P2.x),Math.min(P1.y,P2.y));
        Point pmax = new Point(Math.max(P1.x,P2.x),Math.max(P1.y,P2.y));
        for (Line l: lines) {
            if(Math.min(l.p1.x,l.p2.x) >= pmin.x && Math.min(l.p1.y,l.p2.y) >= pmin.y &&
                    Math.max(l.p1.x,l.p2.x) <= pmax.x && Math.max(l.p1.y,l.p2.y) <= pmax.y){
                continue;
            }
            else
            {
                state = 0;
                return;
            }
        }
        state = 1;
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1) {
            for (Line l:
                    lines
            ) {
                l.p1.x = l.p1.x + P2.x - P1.x;
                l.p1.y = l.p1.y + P2.y - P1.y;
                l.p2.x = l.p2.x + P2.x - P1.x;
                l.p2.y = l.p2.y + P2.y - P1.y;
            }
        }
    }

    public void addLine(Line l)
    {
        lines.add(l);
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
    public void contains(Point P1, Point P2) {
        if(Math.min(p1.x,p2.x) >= Math.min(P1.x,P2.x) && Math.min(p1.y,p2.y) >= Math.min(P1.y,P2.y)){
            if(Math.max(p1.x,p2.x) <= Math.max(P1.x,P2.x) && Math.max(p1.y,p2.y) <= Math.max(P1.y,P2.y)){
                state = 1;
            }
        }
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            p1.x = p1.x + P2.x - P1.x;
            p1.y = p1.y + P2.y - P1.y;
            p2.x = p2.x + P2.x - P1.x;
            p2.y = p2.y + P2.y - P1.y;
        }
    }
}

class Text extends shape{
    public Point p1 = new Point();
    public Point p2 = new Point();
    public Text(Point P1, Point P2, Color color, int width)
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
        float[] dash1 = {1.0f};
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
        g2d.setColor(color);
        float x1 = (float)p1.x;
        float x2 = (float)p2.x;
        float y1 = (float)p1.y;
        float y2 = (float)p2.y;
        Line2D line1 = new Line2D.Float(x1, y1, x1, y2);
        Line2D line2 = new Line2D.Float(x2, y1, x2, y2);
        Line2D line3 = new Line2D.Float(x1, y1, x2, y1);
        Line2D line4 = new Line2D.Float(x1, y2, x2, y2);
        g2d.draw(line1);
        g2d.draw(line2);
        g2d.draw(line3);
        g2d.draw(line4);
//        int spacex = Math.max(p1.x, p2.x) - Math.min(p1.x, p2.x);
//        int spacey = Math.max(p1.y, p2.y) - Math.min(p1.y, p2.y);
//        int tmpx = Math.min(p1.x, p2.x);
//        int tmpy = Math.min(p1.y, p2.y);
//        while(spacex % 25 == 0){
//            g2d.drawLine(tmpx, p1.y, tmpx+10, p1.y);
//            g2d.drawLine(tmpx, p2.y, tmpx+10, p2.y);
//            tmpx += 25;
//            spacex -= 25;
//        }
//        g2d.drawLine(tmpx, p1.y, Math.max(p1.x, p2.x), p1.y);
//        g2d.drawLine(tmpx, p2.y, Math.max(p1.x, p2.x), p2.y);
//        while(spacey % 25 == 0){
//            g2d.drawLine(p1.x, tmpy, p1.x, tmpy+10);
//            g2d.drawLine(p2.x, tmpy, p2.x, tmpy+10);
//            tmpy += 25;
//            spacey -= 25;
//        }
//        g2d.drawLine(p1.x, tmpy, p1.x, Math.max(p1.y, p2.y));
//        g2d.drawLine(p2.x, tmpy, p2.x, Math.max(p1.y, p2.y));
    }

    @Override
    public void contains(Point P1, Point P2) {
        if(Math.min(p1.x,p2.x) >= Math.min(P1.x,P2.x) && Math.min(p1.y,p2.y) >= Math.min(P1.y,P2.y)){
            if(Math.max(p1.x,p2.x) <= Math.max(P1.x,P2.x) && Math.max(p1.y,p2.y) <= Math.max(P1.y,P2.y)){
                state = 1;
            }
        }
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            p1.x = p1.x + P2.x - P1.x;
            p1.y = p1.y + P2.y - P1.y;
            p2.x = p2.x + P2.x - P1.x;
            p2.y = p2.y + P2.y - P1.y;
        }
    }

}
class Word extends shape{
    public int x;
    public int y;
    public String s;
    public int txtstyle;
    public String stylestring;
    public Font font;
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(bs));
        g2d.drawString(s, x, y);

    }

    public Word(String ss,int xx, int yy, Color color, int width, int style, String styleString){
        super(color, width);
        s = ss;
        x = xx;
        y = yy;
        font = new Font(styleString, style, width);
        txtstyle = style;
        stylestring = styleString;
    }

    @Override
    public void contains(Point P1, Point P2) {

    }

    @Override
    public void move(Point P1, Point P2) {

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
    public void contains(Point P1, Point P2) {
        if(Math.min(p1.x,p2.x) >= Math.min(P1.x,P2.x) && Math.min(p1.y,p2.y) >= Math.min(P1.y,P2.y)){
            if(Math.max(p1.x,p2.x) <= Math.max(P1.x,P2.x) && Math.max(p1.y,p2.y) <= Math.max(P1.y,P2.y)){
                state = 1;
            }
        }
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            p1.x = p1.x + P2.x - P1.x;
            p1.y = p1.y + P2.y - P1.y;
            p2.x = p2.x + P2.x - P1.x;
            p2.y = p2.y + P2.y - P1.y;
        }
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
    public void contains(Point P1, Point P2) {
        if(Math.min(p1.x,p2.x) >= Math.min(P1.x,P2.x) && Math.min(p1.y,p2.y) >= Math.min(P1.y,P2.y)){
            if(Math.max(p1.x,p2.x) <= Math.max(P1.x,P2.x) && Math.max(p1.y,p2.y) <= Math.max(P1.y,P2.y)){
                state = 1;
            }
        }
    }

    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            p1.x = p1.x + P2.x - P1.x;
            p1.y = p1.y + P2.y - P1.y;
            p2.x = p2.x + P2.x - P1.x;
            p2.y = p2.y + P2.y - P1.y;
        }
    }

}

class Fill extends shape{
    public boolean drawed = false;
    public Point p1 = new Point();
    public Point pa = new Point();
    public Color color = Color.black;
    public Color formercolor = Color.black;
    private Robot robot = new Robot();
    public ArrayList<Point> points = new ArrayList<>();

    public Fill(Point P1, Point Pa,Color c) throws AWTException {
        p1.x = P1.x;
        p1.y = P1.y;
        pa.x = Pa.x;
        pa.y = Pa.y;
        color = c;
        formercolor = robot.getPixelColor(pa.x, pa.y);
//        System.out.println("鼠标点击位置坐标为("+p1.x+","+p1.y+")");
//        System.out.println("鼠标点击位置的像素为"+formercolor.toString());
    }
    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        for (Point p:
                points
        ) {
            g.drawLine(p.x,p.y,p.x,p.y);
        }
    }
    public void drawFirst(Graphics g)
    {
        points.add(p1);
        Color mycolor1 = robot.getPixelColor(pa.x-1,pa.y);
        Color mycolor2 = robot.getPixelColor(pa.x,pa.y-1);
        Color mycolor3 = robot.getPixelColor(pa.x+1,pa.y);
        Color mycolor4 = robot.getPixelColor(pa.x,pa.y+1);
        Point p_1 = new Point(p1.x-1, p1.y);
        Point p_2 = new Point(p1.x, p1.y-1);
        Point p_3 = new Point(p1.x+1, p1.y);
        Point p_4 = new Point(p1.x, p1.y+1);
        if(mycolor1.equals(formercolor) && !points.contains(p_1))
        {
            points.add(p_1);
            drawRecursion(g, p1.x - 1, p1.y, pa.x - 1, pa.y, 1);
        }
        if(mycolor2.equals(formercolor) && !points.contains(p_2))
        {
            points.add(p_2);
            drawRecursion(g, p1.x, p1.y - 1,pa.x, pa.y - 1,2);
        }
        if(mycolor3.equals(formercolor) && !points.contains(p_3))
        {
            points.add(p_3);
            drawRecursion(g, p1.x + 1, p1.y,pa.x + 1, pa.y,3);
        }
        if(mycolor4.equals(formercolor) && !points.contains(p_4))
        {
            points.add(p_4);
            drawRecursion(g, p1.x, p1.y + 1,pa.x, pa.y + 1,4);
        }
    }

    @Override
    public void contains(Point P1, Point P2) {
        Point pmin = new Point(Math.min(P1.x,P2.x),Math.min(P1.y,P2.y));
        Point pmax = new Point(Math.max(P1.x,P2.x),Math.max(P1.y,P2.y));
        for (Point p:
                points
        ) {
            if(p.x >= pmin.x && p.y >= pmin.y && p.x <= pmax.x && p.y <= pmax.y){
                continue;
            }
            else {
                return;
            }
        }
        state = 1;
    }
    @Override
    public void move(Point P1, Point P2) {
        if(state == 1){
            for (Point p:
                    points
            ){
                p.x = p.x + P2.x - P1.x;
                p.y = p.y + P2.y - P1.y;
            }
        }
    }
    public void drawRecursion(Graphics g,int x, int y, int xa, int ya, int direction)
    {
        Color mycolor1 = robot.getPixelColor(xa-1,ya);
        Color mycolor2 = robot.getPixelColor(xa,ya-1);
        Color mycolor3 = robot.getPixelColor(xa+1,ya);
        Color mycolor4 = robot.getPixelColor(xa,ya+1);
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
                    drawRecursion(g, x - 1, y,xa-1, ya,1);
                }
                if(mycolor2.equals(formercolor) && !points.contains(p_2))
                {
                    points.add(p_2);
                    drawRecursion(g, x, y - 1,xa,ya - 1,2);
                }
                if(mycolor4.equals(formercolor) && !points.contains(p_4))
                {
                    points.add(p_4);
                    drawRecursion(g, x, y + 1, xa, ya + 1,4);
                }
            }
            case 2 -> {
                System.out.println("进入了drawRecursion,向上扩展，当前点：("+x+","+y+")");
                if(mycolor1.equals(formercolor) && !points.contains(p_1))
                {
                    points.add(p_1);
                    drawRecursion(g, x - 1, y,xa-1, ya,1);
                }
                if(mycolor2.equals(formercolor) && !points.contains(p_2))
                {
                    points.add(p_2);
                    drawRecursion(g, x, y - 1,xa,ya - 1,2);
                }
                if(mycolor3.equals(formercolor) && !points.contains(p_3))
                {
                    points.add(p_3);
                    drawRecursion(g, x + 1, y,xa + 1,ya,3);
                }
            }
            case 3 -> {
                System.out.println("进入了drawRecursion,向右扩展，当前点：("+x+","+y+")");
                if(mycolor2.equals(formercolor) && !points.contains(p_2))
                {
                    points.add(p_2);
                    drawRecursion(g, x, y - 1,xa,ya - 1,2);
                }
                if(mycolor3.equals(formercolor) && !points.contains(p_3))
                {
                    points.add(p_3);
                    drawRecursion(g, x + 1, y,xa + 1,ya,3);
                }
                if(mycolor4.equals(formercolor) && !points.contains(p_4))
                {
                    points.add(p_4);
                    drawRecursion(g, x, y + 1,xa, ya + 1,4);
                }
            }
            case 4 -> {
                System.out.println("进入了drawRecursion,向下扩展，当前点：("+x+","+y+")");
                if(mycolor1.equals(formercolor) && !points.contains(p_1))
                {
                    points.add(p_1);
                    drawRecursion(g, x - 1, y,xa-1, ya,1);
                }
                if(mycolor3.equals(formercolor) && !points.contains(p_3))
                {
                    points.add(p_3);
                    drawRecursion(g, x + 1, y,xa + 1,ya,3);
                }
                if(mycolor4.equals(formercolor) && !points.contains(p_4))
                {
                    points.add(p_4);
                    drawRecursion(g, x, y + 1,xa, ya + 1,4);
                }
            }
        }
    }

}