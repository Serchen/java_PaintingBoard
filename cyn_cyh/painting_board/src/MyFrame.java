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
    //声明菜单栏
    public JMenuBar menuBar = new JMenuBar();
    //画笔颜色
    public Color frontcolor = Color.BLACK;

    public int width;
    //设定文字风格
    public int style;
    //画笔
    public Graphics2D g2d;
    //设置文字风格
    public String textstyle;
    //添加单项选择
    public static JRadioButton jrbn1 = new JRadioButton("背景颜色");
    public static JRadioButton jrbn2 = new JRadioButton("画笔颜色");
    public static ButtonGroup btngroup = new ButtonGroup();

    //选择线条粗细的选择框
    public class myDialog extends JDialog{
        public myDialog(){
            this.setBounds(200, 200, 200, 200);
            setTitle("线条粗细选择");
            JSlider slider = new JSlider(0, 5);
            slider.setMinorTickSpacing(1);
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);
            this.add(slider);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    width = slider.getValue();
                }
            });
        }
    }

    public myDialog mg = new myDialog();
    //文本框
    public static JTextField jtf = new JTextField();
    //背景颜色
    public Color backgroundcolor = Color.WHITE;
    //加载按钮的画布1
    public JPanel jp1 = new JPanel(new GridLayout(15,2,10,10));
    //用来绘制图形的画布2
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
        this.setTitle("画图板");
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 700);
        this.setLayout(new FlowLayout(0));
        this.setResizable(false);
        this.add(jp1);
        this.add(jp2);

        btngroup.add(jrbn1);
        btngroup.add(jrbn2);
        jrbn1.setSelected(true);
        jp1.add(jrbn1);
        jp1.add(jrbn2);
        //添加菜单栏
        JMenu filemenu = new JMenu("  文件  ");
        JMenu editmenu = new JMenu("  编辑  ");
        JMenu aboutmenu = new JMenu("  关于  ");
        JMenu textmenu = new JMenu("  线条  ");
        JMenu fontmenu = new JMenu("  文字格式  ");
        JMenu fontstylemenu = new JMenu("  文字风格  ");
        menuBar.add(fontstylemenu);
        menuBar.add(fontmenu);
        menuBar.add(textmenu);
        menuBar.add(filemenu);
        menuBar.add(editmenu);
        menuBar.add(aboutmenu);

        //添加“线条”的子菜单
        JMenuItem sizeMenuItem = new JMenuItem("  粗细  ");
        textmenu.add(sizeMenuItem);

        JMenuItem plainMenuItem = new JMenuItem("  普通字体  ");
        JMenuItem boldMenuItem = new JMenuItem("  加粗字体  ");
        JMenuItem italicMenuItem = new JMenuItem("  斜体字体  ");
        JMenuItem complexMenuItem = new JMenuItem("  粗斜体字体  ");
        fontmenu.add(plainMenuItem);
        fontmenu.add(boldMenuItem);
        fontmenu.add(italicMenuItem);
        fontmenu.add(complexMenuItem);

        JMenuItem songtiMenuItem = new JMenuItem("  宋体  ");
        JMenuItem heitiMenuItem = new JMenuItem("  黑体  ");
        JMenuItem kaitiMenuItem = new JMenuItem("  楷体  ");
        fontstylemenu.add(songtiMenuItem);
        fontstylemenu.add(kaitiMenuItem);
        fontstylemenu.add(heitiMenuItem);
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
        sizeMenuItem.addActionListener(listener);
        plainMenuItem.addActionListener(listener);
        boldMenuItem.addActionListener(listener);
        italicMenuItem.addActionListener(listener);
        complexMenuItem.addActionListener(listener);
        songtiMenuItem.addActionListener(listener);
        kaitiMenuItem.addActionListener(listener);
        heitiMenuItem.addActionListener(listener);

        //
        jp2.add(jtf);
        jtf.setVisible(false);
        jtf.addKeyListener(listener);

        //定义常量
        String[] Buttons = new String[]{"Line", "Ellipse", "Text", "Rectangle","Circle","Triangle",
                "Curve","ColorChoose","Fill","Eraser","Select","Move"};

        //初始化面板1
        jp1.setPreferredSize(new Dimension(90, 600));
        jp1.addMouseListener(listener);
        jp1.addMouseMotionListener(listener);
//        ImageIcon iconText = new ImageIcon("C:\\Download\\painting_board\\画图工具-文本.png");
//        Image imgtext = iconText.getImage();
//        imgtext = imgtext.getScaledInstance(60, 60);
//        JButton jbText = new JButton();
//        jbText.setIcon(iconText);
//        jp1.add(jbText);
        for (String button : Buttons) {
            JButton jb = new JButton(button);
            jb.addActionListener(listener);
            jp1.add(jb);
        }

        //初始化面板2
        jp2.setPreferredSize(new Dimension(1000, 600));
        jp2.setBackground(backgroundcolor);
        jp2.addMouseListener(listener);
        jp2.addMouseMotionListener(listener);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }
    //创建新的画布
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
    //保存方法
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
    //加载画布方法
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
    //添加画布的方法
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
    //事件监听器，继承了键盘事件鼠标事件等
    public class DrawListener implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
        public String name;
        public Point p1 = new Point();
        public Point p2 = new Point();
        //鼠标松开事件
        @Override
        public void mouseReleased(MouseEvent e) {
            //jtf.setFocusable(false);
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
                shapes.add(new Text(p1, p2, frontcolor, width));
                shapes_temp.clear();
                repaint();
                int wid = Math.max(p1.x, p2.x) - Math.min(p1.x, p2.x);
                int len = Math.max(p1.y, p2.y) - Math.min(p1.y, p2.y);
                jtf.setBounds(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), wid, len);
                jtf.setVisible(true);
                jtf.requestFocus();
            }
            else if("Select".equals(name)) {
                for (shape s :
                        shapes
                ) {
                    s.contains(p1, p2);
                }
            }

        }
        //鼠标点击事件
        @Override
        public void actionPerformed(ActionEvent e) {
            //jtf.setFocusable(false);
            if("".equals(e.getActionCommand())){
                JButton jb = (JButton) e.getSource();
                System.out.println("点击了空白！");
            }
            else if("ColorChoose".equals(e.getActionCommand())){
                if(jrbn1.isSelected()){
                    backgroundcolor = JColorChooser.showDialog(jp2, "colorchooser", Color.gray);
                    jp2.setBackground(backgroundcolor);
                }
                else if(jrbn2.isSelected()){
                    frontcolor = JColorChooser.showDialog(jp2, "colorchooser", Color.gray);
                }
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
            else if("  粗细  ".equals(e.getActionCommand())){
                System.out.println("粗细被点击了");
                mg.setVisible(true);
            }
            else if("  关于  ".equals(e.getActionCommand())){
                JOptionPane.showMessageDialog(null,"作者：陈雨晗 陈奕宁\n" +
                        "画板使用说明：\n"+"1、菜单栏可以进行修改文字粗细，风格以及格式\n"
                + "2、撤销操作对应键盘ctrl + Z\n" + "3、图形复制对应键盘ctrl + C\n" +
                "4、画布保存对应键盘ctrl + s\n" + "5、图片粘贴对应键盘ctrl + v\n" +
                "5、左边的单选按钮可以选择控制背景颜色和画笔颜色\n" +
                "6、左边的按钮分别为颜色选择器，画图形和自由线等以及选择、填充、移动和橡皮等功能","说明",1);
            }
            else if("  普通字体  ".equals(e.getActionCommand())){
                style = Font.PLAIN;
                System.out.println("现在字体是普通字体");
            }
            else if("  加粗字体  ".equals(e.getActionCommand())){
                style = Font.BOLD;
                System.out.println("现在字体是加粗字体");
            }
            else if("  斜体字体  ".equals(e.getActionCommand())){
                style = Font.ITALIC;
                System.out.println("现在字体是斜体字体");
            }
            else if("  粗斜体字体  ".equals(e.getActionCommand())){
                style = Font.BOLD + Font.ITALIC;
                System.out.println("现在字体是粗斜体字体");
            }
            else if("  黑体  ".equals(e.getActionCommand())){
                textstyle = "黑体";
            }
            else if("  宋体  ".equals(e.getActionCommand())){
                textstyle = "宋体";
            }
            else if("  楷体  ".equals(e.getActionCommand())){
                textstyle = "楷体";
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
        //
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        //鼠标按下事件
        @Override
        public void mousePressed(MouseEvent e) {
            //jtf.setFocusable(false);
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
        //鼠标拖动事件
        @Override
        public void mouseDragged(MouseEvent e)
        {
            //jtf.setFocusable(false);
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
            else if("Text".equals(name)){
                p2.x = e.getX();
                p2.y = e.getY();
                if(!shapes_temp.isEmpty())
                {
                    shapes_temp.clear();
                }
                shapes_temp.add(new Text(p1,p2, frontcolor, width));
                repaint();
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }
        //键盘事件
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getSource() == jtf){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String txt = jtf.getText();
                    jtf.setText("");
                    jtf.setVisible(false);
                    g2d = (Graphics2D) jp2.getGraphics();
                    g2d.setStroke(new BasicStroke(width));
                    g2d.setColor(frontcolor);
                    g2d.setFont(jp2.getFont());
                    g2d.drawString(txt, jtf.getLocation().x, jtf.getLocation().y+jtf.getHeight()/2);
                    Word wd = new Word(txt, jtf.getLocation().x, jtf.getLocation().y+jtf.getHeight()/2, frontcolor, width, style, textstyle);
                    shapes.add(wd);
                    System.out.println(txt);
                    shapes_temp.clear();
                    if(!shapes.isEmpty()){
                        shapes.remove(shapes.size()-2);
                    }
                    repaint();
                    jp2.requestFocus();
                }
            }
        }
    }
}