import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class EngExam {

    File txtSource;
    File picsSource;
    File eqSource;
    File settings;

    String[] filesList = {
            "F1-P1-Okrętowe silniki tłokowe.txt",
            "F1-P2-Siłownie okrętowe.txt",
            "F1-P3-Maszyny i urządzenia okrętowe.txt",
            "F1-P4-Kotły okrętowe.txt",
            "F1-P5-Chłodnictwo, wentylacja i klimatyzacja okrętowa.txt",
            "F1-P6-Termodynamika.txt",
            "F1-P7-Płyny eksploatacyjne.txt",
            "F1-P8-Język angielski.txt",
            "F2-P1-Elektrotechnika i elektronika okrętowa.txt",
            "F2-P2-Automatyka okrętowa.txt",
            "F3-P1-Mechanika i wytrzymałość materiałów.txt",
            "F3-P2-Technologia remontów.txt",
            "F3-P3-Teoria i budowa okrętu.txt",
            "F4-P1-Bezpieczna eksploatacja statku.txt",
            "F4-P2-Ochrona środowiska morskiego.txt",
            "F4-P3-Prawo i ubezpieczenia morskie.txt"
    };
    int[] questionsInFile = new int[filesList.length];
    int[] randomQuestionsNum = {20, 20, 20, 10, 5, 5, 10, 10, 10, 5, 5, 25, 5, 10, 5, 10};
    int time = 175;
    boolean[] range = new boolean[filesList.length];
    ArrayList<Question> questions = new ArrayList<Question>();

    JFrame mainFrame = new JFrame("Egzamin");
    Clock clock = new Clock();

    public EngExam(){
        txtSource = new File("Poziom zarządzania/Filtered");
        picsSource = new File("Poziom zarządzania/Pics");
        eqSource = new File("Poziom zarządzania/Equations");
        settings = new File("Poziom zarządzania/settings.txt");
        init();
        mainFrame.setSize(1200, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        setStartPanel();
        mainFrame.setVisible(true);
    }

    private class Clock extends JLabel implements ActionListener{

        Timer timer = new Timer(1000, this);
        int time = 0;

        Clock(){}

        public void start(){
            time = 0;
            this.setText("00:00:00");
            timer.start();
        }

        public void stop(){
            timer.stop();
        }

        public void actionPerformed(ActionEvent e){
            time += 1;
            int h = time/3600;
            int m = (time%3600)/60;
            int s = time%60;
            String hh;
            if(h < 10) hh = "0" + h + ":";
            else hh = h + ":";
            String mm;
            if(m < 10) mm = "0" + m + ":";
            else mm = m + ":";
            String ss;
            if(s < 10) ss = "0" + s;
            else ss = Integer.toString(s);
            String ts = hh + mm + ss;
            this.setText(ts);
            mainFrame.repaint();
        }
    }

    void init(){
        for(int i = 0; i < filesList.length; i++){
            try{
                Scanner scan = new Scanner(new File(txtSource.getPath() + "/" + filesList[i]));
                int num = 0;
                while(scan.hasNextLine()){
                    scan.nextLine();
                    num += 1;
                }
                questionsInFile[i] = num;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    void saveSettings(){
        try{
            String cont = "";
            for(int i = 0; i < range.length; i++){
                if(range[i]) cont += 1 + " ";
                else cont += 0 + " ";
            }
            cont += System.getProperty("line.separator");
            FileWriter fw = new FileWriter(settings);
            fw.write(cont);
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void setStartPanel(){
        clock.stop();
        mainFrame.getContentPane().removeAll();
        double[][] tab = {{TableLayout.FILL},{100, TableLayout.FILL, 100, TableLayout.FILL, 100, TableLayout.FILL, 100, TableLayout.FILL, 100}};
        mainFrame.getContentPane().setLayout(new TableLayout(tab));
        JButton[] buttons = new JButton[4];
        buttons[0] = new JButton("Test z wybranego zakresu");
        buttons[1] = new JButton("Symulacja egzaminu");
        buttons[2] = new JButton("Szybki test");
        buttons[3] = new JButton("Wyjście");
        buttons[0].setFont(new Font(buttons[0].getFont().getFontName(), Font.BOLD, 50));
        buttons[1].setFont(new Font(buttons[0].getFont().getFontName(), Font.BOLD, 50));
        buttons[2].setFont(new Font(buttons[0].getFont().getFontName(), Font.BOLD, 50));
        buttons[3].setFont(new Font(buttons[0].getFont().getFontName(), Font.BOLD, 24));
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getSource().equals(buttons[0])){
                    setRangePanel();
                }
                else if(e.getSource().equals(buttons[1])){
                    setTest(1);
                }
                else if(e.getSource().equals(buttons[2])){
                    setTest(2);
                }
                else if(e.getSource().equals(buttons[3])){
                    System.exit(0);
                }
            }
        };
        for(int i = 0; i < buttons.length; i++){
            buttons[i].addActionListener(al);
        }
        mainFrame.add(buttons[0], "0 1 c c");
        mainFrame.add(buttons[1], "0 3 c c");
        mainFrame.add(buttons[2], "0 5 c c");
        mainFrame.add(buttons[3], "0 7 c c");
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    void setRangePanel(){
        mainFrame.getContentPane().removeAll();
        JRadioButton[] boxes = new JRadioButton[range.length];
        ActionListener bl = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton b = (JRadioButton)e.getSource();
                for(int i = 0; i < boxes.length; i++){
                    if(!(b.equals(boxes[i]))) boxes[i].setSelected(false);
                }
            }
        };
        for(int i = 0; i < boxes.length; i++){
            boxes[i] = new JRadioButton();
            boxes[i].setPreferredSize(new Dimension(50, 50));
            boxes[i].addActionListener(bl);
        }
        JLabel[] names = new JLabel[filesList.length];
        for(int i = 0; i < names.length; i++){
            names[i] = new JLabel(filesList[i].replaceAll(".txt", ""));
            names[i].setFont(new Font(names[i].getFont().getFontName(), Font.BOLD, 20));
        }
        double[] cols = {10, 50, 10, TableLayout.FILL, 10, 50, 10, TableLayout.FILL};
        int r;
        if(names.length%2 == 0) r = names.length/2;
        else r = 1 + names.length/2;
        double[] rows = new double[2*r + 1];
        for(int i = 0; i < rows.length; i++){
            if(i%2 == 0) rows[i] = 10;
            else rows[i] = 50;
        }
        double[][] tab = {cols, rows};
        JPanel text = new JPanel();
        text.setLayout(new TableLayout(tab));
        for(int i = 0; i < names.length; i++){
            if(i < r){
                String loc = "1 " + (2*i + 1) + " l c";
                text.add(boxes[i], loc);
                loc = "3 " + (2*i + 1) + " l c";
                text.add(names[i], loc);
            }
            else{
                String loc = "5 " + (2*i + 2 - rows.length) + " l c";
                text.add(boxes[i], loc);
                loc = "7 " + (2*i + 2 - rows.length) + " l c";
                text.add(names[i], loc);
            }
        }
        JPanel bP = new JPanel();
        double[][] t = {{100, TableLayout.FILL, 100, TableLayout.FILL, 100}, {100, TableLayout.FILL, 50}};
        bP.setLayout(new TableLayout(t));
        JButton[] b = new JButton[2];
        b[0] = new JButton("Rozpocznij test");
        b[1] = new JButton("Anuluj");
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getSource().equals(b[0])){
                    boolean selected = false;
                    for(int i = 0; i < boxes.length; i++){
                        range[i] = boxes[i].isSelected();
                        if(range[i]) selected = true;
                    }
                    if(selected) setTest(0);
                    else JOptionPane.showMessageDialog(null, "Nic nie wybrano");
                }
                else if(e.getSource().equals(b[1])){
                    setStartPanel();
                }
            }
        };
        for(int i = 0; i < b.length; i++){
            b[i].setFont(new Font(b[i].getFont().getFontName(), Font.BOLD, 30));
            b[i].addActionListener(al);
        }
        bP.add(b[0], "1 1 c c");
        bP.add(b[1], "3 1 c c");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(text, BorderLayout.NORTH);
        mainFrame.add(bP, BorderLayout.SOUTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    void setOptionsPanel(){

    }

    void setFinishPanel(int questionsInTest, int correctAnswers){
        clock.stop();
        mainFrame.getContentPane().removeAll();
        double[][] tab = {{TableLayout.FILL},
                {100, TableLayout.PREFERRED, 40, TableLayout.PREFERRED, 40, TableLayout.PREFERRED, 40, TableLayout.PREFERRED, TableLayout.FILL}};
        mainFrame.setLayout(new TableLayout(tab));
        JLabel l1 = new JLabel("Test zakończony");
        l1.setFont(new Font(l1.getFont().getFontName(), Font.BOLD, 50));
        mainFrame.add(l1, "0 1 c c");
        JLabel l2 = new JLabel("Pytań: " + questionsInTest + " rozwiązanych poprawnie: " + correctAnswers + " w czasie: " + clock.getText());
        l2.setFont(new Font(l2.getFont().getFontName(), Font.PLAIN, 20));
        mainFrame.add(l2, "0 3 c c");
        JLabel l3 = new JLabel("Procentowo: " + 100*correctAnswers/questionsInTest + "%");
        l3.setFont(new Font(l2.getFont().getFontName(), Font.PLAIN, 40));
        mainFrame.add(l3, "0 5 c c");
        if((100*correctAnswers/questionsInTest) < 60){
            JLabel l = new JLabel("Czyli peszek...");
            l.setFont(new Font(l.getFont().getFontName(), Font.BOLD, 50));
            mainFrame.add(l, "0 7 c c");
        }
        else{
            JLabel l = new JLabel("Czyli zdane!");
            l.setFont(new Font(l.getFont().getFontName(), Font.BOLD, 50));
            mainFrame.add(l, "0 7 c c");
        }
        JButton b = new JButton("Zamknij");
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setStartPanel();
            }
        };
        b.addActionListener(al);
        mainFrame.add(b, "0 8 c c");
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    void setTest(int type){
        switch(type){
            case 0:
                setQuestionsByRange();
                break;
            case 1:
                setQuestionsByRandom(1);
                break;
            case 2:
                setQuestionsByRandom(5);
        }
        mainFrame.getContentPane().removeAll();
        mainFrame.setLayout(null);
        ActionListener bl = new ActionListener(){
            int questionNo = 0;
            int correctAnswers = 0;
            boolean selected = false;
            JLabel stats = new JLabel();

            public void actionPerformed(ActionEvent e){
                if(selected){
                    questionNo += 1;
                    if(questionNo > questions.size() - 1){
                        setFinishPanel(questions.size(), correctAnswers);
                    }
                    else{
                        mainFrame.getContentPane().removeAll();
                        stats.setText("pytanie: " + (questionNo + 1) + "/" + questions.size());
                        JPanel p = questionScreen(questions.get(questionNo), this, stats);
                        p.setSize(mainFrame.getSize());
                        mainFrame.add(p);
                        mainFrame.revalidate();
                        mainFrame.repaint();
                        selected = false;
                    }
                }
                else{
                    Question q = questions.get(questionNo);
                    JButton source = (JButton)e.getSource();
                    int opt = 0;
                    Iterator<JButton> it = q.answers.iterator();
                    while(it.hasNext()){
                        if(it.next().equals(source)) break;
                        else opt += 1;
                    }
                    if(opt == q.correctOpt){
                        source.setBackground(Color.GREEN);
                        correctAnswers += 1;
                    }
                    else{
                        source.setBackground(Color.RED);
                        q.answers.get(q.correctOpt).setBackground(Color.GREEN);
                    }
                    selected = true;
                    mainFrame.repaint();
                }
            }
        };
        clock.start();
        JPanel p = questionScreen(questions.get(0), bl, new JLabel("pytanie: 1/" + questions.size()));
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(p, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    void setQuestionsByRange(){
        questions = new ArrayList<Question>();
        for(int i = 0; i < filesList.length; i++){
            if(range[i]){
                String[] cont = loadContent(filesList[i]);
                for(int j = 0; j < cont.length; j++){
                    Question q = new Question(cont[j], filesList[i].replaceAll(".txt", ""), j);
                    questions.add(q);
                }
            }
        }
    }

    void setQuestionsByRandom(int divider){
        questions = new ArrayList<Question>();
        for(int i = 0; i < filesList.length; i++){
            int[] nums = randomList(randomQuestionsNum[i]/divider, questionsInFile[i]);
            try{
                Scanner scan = new Scanner(new File(txtSource.getPath() + "/" + filesList[i]));
                int n = 0;
                String[] txt = new String[questionsInFile[i]];
                while(scan.hasNextLine()){
                    txt[n] = scan.nextLine();
                    n += 1;
                }
                for(int j = 0; j < nums.length; j++){
                    questions.add(new Question(txt[nums[j]], filesList[i].replaceAll(".txt", ""), nums[j]));
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    int[] randomList(int howMany, int total){
        int[] list = new int[howMany];
        boolean[] truthTable = new boolean[total];
        int n = 0;
        Random random = new Random();
        while(n < howMany){
            int r = random.nextInt(total);
            if(!truthTable[r]){
                truthTable[r] = true;
                list[n] = r;
                n += 1;
            }
        }
        return list;
    }

    String[] loadContent(String file){
        ArrayList<String> list = new ArrayList<String>();
        try{
            Scanner scan = new Scanner(new File(txtSource + "/" + file));
            while(scan.hasNextLine()){
                list.add(scan.nextLine());
            }
            scan.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Object[] temp = list.toArray();
        String[] out = new String[temp.length];
        for(int i = 0; i < temp.length; i++){
            out[i] = (String)temp[i];
        }
        return out;
    }

    JPanel questionScreen(Question q, ActionListener bl, JLabel stats){
        JPanel out = new JPanel();
        double[][] tb = {{30, TableLayout.FILL, 30, TableLayout.FILL, 30}, {20, TableLayout.PREFERRED, 20, TableLayout.FILL, 20, TableLayout.PREFERRED, 30}};
        out.setLayout(new TableLayout(tb));
        JLabel question = new JLabel(q.question);
        question.setFont(new Font(question.getFont().getFontName(), Font.BOLD, 16));
        question.setVerticalAlignment(JLabel.TOP);
        out.add(question, "1 1 3 1 c t");
        JPanel buttons = new JPanel();
        double[] cols = {40, TableLayout.FILL, 40};
        double[] rows = new double[2*q.answers.size() + 1];
        for(int i = 0; i < rows.length; i++){
            if(i%2 == 0) rows[i] = 20;
            else rows[i] = TableLayout.PREFERRED;
        }
        double[][] tab = {cols, rows};
        buttons.setLayout(new TableLayout(tab));
        int n = 1;
        Iterator<JButton> iter = q.answers.iterator();
        while(iter.hasNext()){
            String loc = "1 " + n + " l t";
            JButton bt = iter.next();
            bt.addActionListener(bl);
            buttons.add(bt, loc);
            n += 2;
        }
        out.add(buttons, "1 3 c c");
        if(q.img != null){
            JButton pic = new JButton();
            ImageIcon i2 = new ImageIcon();
            float xScale = 1;
            float yScale = 1;
            int xMax = mainFrame.getWidth()/2;
            int yMax = mainFrame.getHeight() - (int)question.getPreferredSize().getHeight();
            if(q.img.getIconWidth() > xMax){
                xScale = (float)xMax/q.img.getIconWidth();
            }
            if(q.img.getIconHeight() > yMax){
                yScale = (float)yMax/q.img.getIconHeight();
            }
            if(xScale < yScale){
                if(xScale < 1)i2.setImage(q.img.getImage().getScaledInstance(xMax, -1, Image.SCALE_DEFAULT));
                else i2.setImage(q.img.getImage());
            }
            else{
                if(yScale < 1) i2.setImage(q.img.getImage().getScaledInstance(-1, yMax, Image.SCALE_DEFAULT));
                else i2.setImage(q.img.getImage());
            }
            pic.setIcon(i2);
            ActionListener al = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JFrame picFull = new JFrame();
                    picFull.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JPanel p = new JPanel(){
                        public void paintComponent(Graphics g){
                            super.paintComponent(g);
                            g.drawImage(q.img.getImage(), 0, 0, this);
                        }
                    };
                    picFull.setSize(q.img.getIconWidth() + 20, q.img.getIconHeight() + 50);
                    picFull.add(p);
                    picFull.setLocationRelativeTo(null);
                    picFull.setVisible(true);
                }
            };
            pic.addActionListener(al);
            out.add(pic, "3 3 c c");
        }
        JButton stop = new JButton("Przerwij");
        ActionListener st = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setStartPanel();
            }
        };
        stop.addActionListener(st);
        out.add(stop, "1 5 c c");
        JPanel util = new JPanel();
        JLabel l = new JLabel("zakres: " + q.subject);
        l.setForeground(Color.BLUE);
        util.add(l);
        util.add(stats);
        util.add(clock);
        out.add(util, "3 5 r c");
        return out;
    }

    private class Question{
        String subject;
        int questionNo;
        String question;
        ArrayList<JButton> answers = new ArrayList<JButton>();
        int correctOpt = -1;
        ImageIcon img;

        Question(String in, String subject, int questionNo){
            this.subject = subject;
            this.questionNo = questionNo;
            Scanner input = new Scanner(in);
            input.useDelimiter(">");
            String q = "";
            if(input.hasNext()){
                q = input.next();
            }
            String a = "";
            if(input.hasNext()){
                a = input.next();
            }
            if(input.hasNext()) correctOpt = Integer.parseInt(input.next());
            if(input.hasNext()){
                String imgPath = picsSource.getPath() + "/" + subject + "/" + input.next();
                img = new ImageIcon(imgPath);
            }
            else img = null;
            input.close();
            Scanner line = new Scanner(q);
            q = "<html>";
            while(line.hasNext()){
                String token = line.next();
                if(!token.contains(".jpg")) q += token + " ";
                else q += getEqPath(token) + " ";
            }
            q += "</html>";
            question = q;
            line.close();
            line = new Scanner(a);
            line.useDelimiter("_n_");
            while(line.hasNext()){
                JButton b = new JButton(line.next());
                answers.add(b);
            }
            line.close();
            Iterator<JButton> iter = answers.iterator();
            while(iter.hasNext()){
                JButton bt = iter.next();
                String txt = bt.getText();
                line = new Scanner(txt);
                txt = "<html>";
                while(line.hasNext()){
                    String token = line.next();
                    if(token.contains(".jpg")){
                        txt += getEqPath(token);
                    }
                    else txt += token + " ";
                }
                txt += "</html>";
                bt.setText(txt);
            }
        }

        String getEqPath(String fileName){
            String path ="<img src=\"file:" + eqSource.getPath() + "/" + subject + "/" + fileName + "\" alt=\"Coś nie pykło...\">";
            return path;
        }
    }

    public static void main(String[] args) {
        new EngExam();
    }

}