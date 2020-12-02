import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    //
    public static shape shapes_temp = null;
    //声明监听器
    public DrawListener listener = new DrawListener();
    //
    public Color frontcolor = Color.BLUE;

    public int width;

    public Color backgroundcolor;
    //
    public static JPanel jp1 = new JPanel(new GridLayout(15,2,10,10));
    //
    public static JPanel jp2 = new JPanel(){
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            int num=0;
            for (shape s: shapes) {
//                System.out.println(s.getInfo());
                s.draw(g);
            }
            if(shapes_temp != null)
            shapes_temp.draw(g);
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

        JSlider slider = new JSlider(0, 2);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        jp1.add(slider);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                width = slider.getValue();
            }
        });
        //定义常量
        String[] Buttons = new String[]{"Line", "Ellipse", "Rectangle","Circle", "ColorChoose", "Triangle", "Curve","Fill"};

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
                shapes.add(new Line(p1, p2, frontcolor, width));
                repaint();
                System.out.println("shapes的大小为"+shapes.size());
            }
            else if("Rectangle".equals(name)){
                shapes.add(new Rectangle(p1,p2, frontcolor, width));
                shapes_temp = null;
                repaint();
            }
            else if("Circle".equals(name)){
                shapes.add(new Circle(p1,p2, frontcolor, width));
                shapes_temp = null;
                repaint();
            }
            else if("Ellipse".equals(name)){
                shapes.add(new Ellipse(p1,p2, frontcolor, width));
                shapes_temp = null;
                repaint();
            }
            else if("Text".equals(name)){
                JTextField jtx = new JTextField();
                jtx.setFont(new Font("楷体",Font.BOLD,16));    //修改字体样式
            }

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if("".equals(e.getActionCommand())){
                JButton jb = (JButton) e.getSource();
                System.out.println("点击了空白！");
            }
            else if("ColorChoose".equals(e.getActionCommand())){
                frontcolor = JColorChooser.showDialog(jp2, "colorchooser", Color.gray);

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
            if("Fill".equals(name)){
                try {
                    shapes_temp = new Fill(p1,Color.black);
                } catch (AWTException awtException) {
                    awtException.printStackTrace();
                }
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            if("Curve".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                shapes.add(new Line(p1, p2, frontcolor, width));
                p1.x = p2.x;
                p1.y = p2.y;
                repaint();
            }else if("Rectangle".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                shapes_temp = new Rectangle(p1,p2, frontcolor, width);
                repaint();
            }else if("Circle".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                shapes_temp = new Circle(p1,p2, frontcolor, width);
                repaint();
            }
            else if("Ellipse".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                shapes_temp = new Ellipse(p1,p2, frontcolor, width);
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}