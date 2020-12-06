import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class MyFrame extends JFrame implements Serializable {
    //主函数
    public static void main(String[] args) {
        new MyFrame();
    }
    //声明一个用来存储形状的数组
    public ArrayList<shape> shapes = new ArrayList<>();
    //声明形状预览器
    public ArrayList<shape> shapes_temp = new ArrayList<>();
    //
    public Curve curve_temp = null;
    //声明监听器
    public DrawListener listener = new DrawListener();
    //
    public JMenuBar menuBar = new JMenuBar();
    //
    public Color frontcolor = Color.BLACK;

    public int width;

    public Color backgroundcolor = Color.WHITE;
    //
    public JPanel jp1 = new JPanel(new GridLayout(15,2,10,10));
    //
    public JPanel jp2 = new JPanel(){
        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            for (shape s: shapes) {
                if(s instanceof Fill)
                {
                    if(((Fill) s).drawed == false)
                    {
                        ((Fill) s).drawFirst(g);
                        ((Fill) s).drawed = true;
                    }
                }
                s.draw(g);
            }
            for (shape s: shapes_temp) {
                s.draw(g);
            }
        }
    };

    public MyFrame() {
        //初始化窗体
        this.setTitle("陈雨晗打不过我");
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 700);
        this.setLayout(new FlowLayout(0));
        this.setResizable(false);
        this.add(jp1);
        this.add(jp2);

        //添加菜单栏
        JMenu filemenu = new JMenu("  文件  ");
        JMenu editmenu = new JMenu("  编辑  ");
        JMenu aboutmenu = new JMenu("  关于  ");
        menuBar.add(filemenu);
        menuBar.add(editmenu);
        menuBar.add(aboutmenu);


        //添加“文件”的子菜单
        JMenuItem newMenuItem = new JMenuItem("  新建  ");
        JMenuItem openMenuItem = new JMenuItem("  打开  ");
        JMenuItem saveMenuItem = new JMenuItem("  保存  ");
        JMenuItem exitMenuItem = new JMenuItem("  退出  ");
        JMenuItem addMenuItem = new JMenuItem("  添加  ");
        filemenu.add(newMenuItem);
        filemenu.add(openMenuItem);
        filemenu.addSeparator();
        filemenu.add(addMenuItem);
        filemenu.add(saveMenuItem);
        filemenu.addSeparator();
        filemenu.add(exitMenuItem);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));

        //添加“编辑”的子菜单
        JMenuItem copyMenuItem = new JMenuItem("  复制  ");
        JMenuItem pasteMenuItem = new JMenuItem("  粘贴  ");
        JMenuItem rollbackMenuItem = new JMenuItem("  撤销  ");
        editmenu.add(copyMenuItem);
        editmenu.add(pasteMenuItem);
        editmenu.add(rollbackMenuItem);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
        rollbackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));

        //添加“关于”的子菜单
        JMenuItem aboutMenuItem = new JMenuItem("  关于  ");
        aboutmenu.add(aboutMenuItem);

        //
        newMenuItem.addActionListener(listener);
        openMenuItem.addActionListener(listener);
        addMenuItem.addActionListener(listener);
        saveMenuItem.addActionListener(listener);
        exitMenuItem.addActionListener(listener);
        copyMenuItem.addActionListener(listener);
        pasteMenuItem.addActionListener(listener);
        aboutMenuItem.addActionListener(listener);
        rollbackMenuItem.addActionListener(listener);
        //
        JSlider slider = new JSlider(0, 5);
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
        String[] Buttons = new String[]{"Line", "Ellipse", "Rectangle","Circle","Triangle",
                "Curve","ColorChoose","Fill","Eraser","Select","Move"};

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
        jp2.setBackground(Color.white);
        jp2.addMouseListener(listener);
        jp2.addMouseMotionListener(listener);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }

    public void createNewPanel()
    {
        int option = JOptionPane.showConfirmDialog(null, "未保存的图画将消失，是否继续","提示",JOptionPane.WARNING_MESSAGE);
        switch (option)
        {
            case JOptionPane.YES_OPTION:
                shapes.clear();
                repaint();
        }
    }
    public void savePanel()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "文本文件(*.txt)", "txt");
        chooser.setFileFilter(filter);

        int option = chooser.showSaveDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
            File file = chooser.getSelectedFile();

            String fname = chooser.getName(file);	//从文件名输入框中获取文件名

            //假如用户填写的文件名不带我们制定的后缀名，那么我们给它添上后缀
            if(fname.indexOf(".txt")==-1){
                file=new File(chooser.getCurrentDirectory(),fname+".txt");
                System.out.println("renamed");
                System.out.println(file.getName());
            }
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(this.shapes);
                oos.close();
            } catch (IOException e) {
                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
    }
    public void loadPanel()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "文本文件(*.txt)", "txt");
        chooser.setFileFilter(filter);

        int option = chooser.showSaveDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
            File file = chooser.getSelectedFile();
            try {
                ObjectInputStream ios = new ObjectInputStream(new FileInputStream(file));
                this.shapes = (ArrayList<shape>) ios.readObject();
                ios.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
    }
    public void addPanel()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "文本文件(*.txt)", "txt");
        chooser.setFileFilter(filter);

        int option = chooser.showSaveDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
            File file = chooser.getSelectedFile();
            try {
                ObjectInputStream ios = new ObjectInputStream(new FileInputStream(file));
                ArrayList<shape> temp = (ArrayList<shape>) ios.readObject();
                for (shape s:
                        temp
                     ) {
                    shapes.add(s);
                }
                ios.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
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
                shapes_temp.clear();
                repaint();
            }
            else if("Circle".equals(name)){
                shapes.add(new Circle(p1,p2, frontcolor, width));
                shapes_temp.clear();
                repaint();
            }
            else if("Ellipse".equals(name)){
                shapes.add(new Ellipse(p1,p2, frontcolor, width));
                shapes_temp.clear();
                repaint();
            }
            else if("Curve".equals(name)){
                shapes.add(curve_temp);
                shapes_temp.clear();
                repaint();
            }
            else if("Eraser".equals(name)){
                shapes.add(curve_temp);
                shapes_temp.clear();
                repaint();
            }
            else if("Text".equals(name)){
                JTextField jtx = new JTextField();
                jtx.setFont(new Font("楷体",Font.BOLD,16));    //修改字体样式
            }
            else if("Select".equals(name)) {
                for (shape s :
                        shapes
                ) {
                    s.contains(p1, p2);
                }
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
            else if("  撤销  ".equals(e.getActionCommand()) ){
                if(!shapes.isEmpty()){
                    shapes.remove(shapes.size()-1);
                    repaint();
                }
            }
            else if("  新建  ".equals(e.getActionCommand())){
                createNewPanel();
                repaint();
            }
            else if("  保存  ".equals(e.getActionCommand())){
                System.out.println("保存被点击");
                savePanel();
            }
            else if("  打开  ".equals(e.getActionCommand())){
                System.out.println("打开被点击");
                loadPanel();
                repaint();
            }
            else if("  添加  ".equals(e.getActionCommand())){
                System.out.println("打开被点击");
                addPanel();
                repaint();
            }
            else if("Select".equals(e.getActionCommand())){
                for (shape s:
                        shapes
                     ) {
                    s.endMove();
                }
                name = e.getActionCommand();
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
            Point pa = new Point(e.getXOnScreen(),e.getYOnScreen());
            if("Fill".equals(name)){
                try {
                    shapes.add(new Fill(p1,pa,Color.green));
                } catch (AWTException awtException) {
                    awtException.printStackTrace();
                }
                repaint();
            }else if("Curve".equals(name)){
                curve_temp = new Curve();
            }
            else if("Eraser".equals(name)){
                curve_temp = new Curve();
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
                curve_temp.addLine(new Line(p1, p2, frontcolor, width));
                shapes_temp.add(new Line(p1, p2, frontcolor, width));
                p1.x = p2.x;
                p1.y = p2.y;
                repaint();
            }else if("Rectangle".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                if(!shapes_temp.isEmpty())
                {
                    shapes_temp.clear();
                }
                shapes_temp.add(new Rectangle(p1,p2, frontcolor, width));
                repaint();
            }else if("Circle".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                if(!shapes_temp.isEmpty())
                {
                    shapes_temp.clear();
                }
                shapes_temp.add(new Circle(p1,p2, frontcolor, width)) ;
                repaint();
            }
            else if("Ellipse".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                if(!shapes_temp.isEmpty())
                {
                    shapes_temp.clear();
                }
                shapes_temp.add(new Ellipse(p1,p2, frontcolor, width)) ;
                repaint();
            }
            else if("Eraser".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                curve_temp.addLine(new Line(p1, p2, backgroundcolor, width + 5));
                shapes_temp.add(new Line(p1, p2, backgroundcolor, width + 5));
                p1.x = p2.x;
                p1.y = p2.y;
                repaint();
            }
            else if("Move".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                for (shape s:
                        shapes
                     ) {
                    s.move(p1,p2);
                }
                p1.x = p2.x;
                p1.y = p2.y;
                repaint();
            }
            else if("Line".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                if(!shapes_temp.isEmpty())
                {
                    shapes_temp.clear();
                }
                shapes_temp.add(new Line(p1,p2, frontcolor, width)) ;
                repaint();
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}