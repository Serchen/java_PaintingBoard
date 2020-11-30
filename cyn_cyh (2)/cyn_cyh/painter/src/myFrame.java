import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class myFrame extends JFrame {
    //主函数
    public static void main(String[] args) {
        new myFrame();
    }
    //声明一个用来存储形状的数组
    public static ArrayList<shape> shapes = new ArrayList<>();
    //声明监听器
    public DrawListener listener = new DrawListener();
    //
    public static JPanel jp1 = new JPanel(new GridLayout(8,2,10,10));
    //
    public static JPanel jp2 = new JPanel(){
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            int num=0;
            for (shape s: shapes) {
                System.out.println(s.getInfo());
                s.draw(g);
            }
        }
    };

    public myFrame() {
        //初始化窗体
        this.setTitle("陈雨晗打不过我");
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 700);
        this.setLayout(new FlowLayout(0));
        this.setResizable(false);
        this.add(jp1);
        this.add(jp2);

        //定义常量
        String[] Buttons = new String[]{"Line", "Point", "Rectangle","Clear", "Triangle", "Curve","ColorChooser"};

        //初始化面板1
        jp1.setPreferredSize(new Dimension(200, 700));
        jp1.addMouseListener(listener);
        jp1.addMouseMotionListener(listener);
        for (String button : Buttons) {
            JButton jb = new JButton(button);
            jb.addActionListener(listener);
            jp1.add(jb);
        }

        //初始化面板2
        jp2.setPreferredSize(new Dimension(900, 800));
        jp2.setBackground(Color.green);
        jp2.addMouseListener(listener);
        jp2.addMouseMotionListener(listener);

        this.setVisible(true);
    }
    public void paint(Graphics g){
        System.out.println("调用了JFrame的paint");
        super.paint(g);
    }

    public class DrawListener implements MouseListener, MouseMotionListener, ActionListener {
        public String name;
        public Point p1 = new Point();
        public Point p2 = new Point();

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("松开了！");
            p2.x = e.getX();
            p2.y = e.getY();
            if("Line".equals(name)){
                shapes.add(new Line(p1, p2));
                for (shape s:
                        shapes
                ) {
                    System.out.println(s.getInfo());
                }
                repaint();
                System.out.println("shapes的大小为"+shapes.size());
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if("".equals(e.getActionCommand())){
                JButton jb = (JButton) e.getSource();
                System.out.println("点击了空白！");
            }
            else{
                System.out.println("点击了按钮！");
                name = e.getActionCommand();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("按下了！");
            p1.x = e.getX();
            p1.y = e.getY();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
//        if("Curve".equals(name)){
//            p1.x = start_x;
//            p1.y = start_y;
//            p2.x = e.getX();
//            p2.y = e.getY();
//            shape s = new Curve(g, p1, p2);
//            s.draw();
//            myFrame.shapes.add(s);
//        }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}