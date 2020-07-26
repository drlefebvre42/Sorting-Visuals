import java.awt.Color;
import java.awt.event.ItemListener;
import java.util.Random;
import java.awt.Graphics;

import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;

public class VisualSort{
    //frame
    private JFrame jf;
    //Variables
    private int len = 50;
    private int off = 0;
    private int curlAlg = 0;
    private int spd = 15;
    private int compare = 0;
    private int acc = 0;
    //Graph
    private final int SIZE = 600;
    private int current = -1;
    private int check = -1;
    private int width = SIZE/len;
    private int type = 0;
    //Arrays
    private int[] list;
    private String[] types = {"Bar Graph", "Dot Plot"};
    private String[] algorithms = {"QuickSort","BubbleSort","BogoSort","SelectionSort"};

    private String[] algInfo = {"Best Case:O(N log N)\nAverage Case:O(N log N)\nWorst Case:O(N^2)\n",
    "Best Case:O(N)\nAverage Case:O(N^2)\nWorst Case:O(N^2)\n",
    "Best Case:Terrible..",
    "Best Case:O(1)\nAverage Case:O(N)\nWorst Case:O(N)\n"};

    //Booleans
    private boolean sorting = false;
    private boolean shuffled = true;
    
    //Utility objects
    Random r = new Random();
    
    //Panels
    JPanel tools = new JPanel();
    GraphCanvas canvas;

    //Labels
    JLabel delayL = new JLabel("Delay: ");
    JLabel msL = new JLabel( spd + " ms");
    JLabel sizeL = new JLabel("Size: ");
    JLabel lenL = new JLabel(len + "");
    JLabel compareL = new JLabel("Comparisons : " + compare);
    JLabel accessL = new JLabel("Array Accesses" + acc);
    JLabel algorithmL = new JLabel("Algorithms");
    JLabel typeL = new JLabel("Graph Types");

    //DropDown boxes
    JComboBox alg = new JComboBox(algorithms);
    JComboBox graph = new JComboBox(types);
    JTextArea information = new JTextArea(algInfo[curlAlg]);

    //Buttons

    JButton sort = new JButton("Sort");
    JButton shuffle = new JButton("Shuffle");
    JButton credit = new JButton("Credit");
    
    //Sliders
    JSlider size = new JSlider(JSlider.HORIZONTAL,1,6,1);
    JSlider speed = new JSlider(JSlider.HORIZONTAL,0,100,spd);

    //Border Styles
    Border lowerdetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    //Constructor 
    public VisualSort(){
        shuffleList();//creates list
        initialize();//initializes the gui

    }
    public void createList(){
        list = new int[len];
        for(int i =0; i <len; i++){
            list[i] = i+1;
        }
    }
    public void shuffleList(){
        createList();
        for(int a = 0; a<500; a++){//shuffles 500 times
            for(int i = 0; i < len; i++){//access each element of the list
                int rand = r.nextInt(len);//pick a random num from 0-len
                int temp = list[i];//sets temp int to current element
                list[i] = list[rand];//swaps the current index with a random index
                list[rand] = temp;//sets the random index to the temp
                
            }
        }
        sorting = false;
        shuffled = true;
    }

    public void initialize(){
        //setting the frames up
        jf = new JFrame();
        jf.setSize(800,635); //window size
        jf.setTitle("VisualSort");

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(null);

        //settting up the toolbar
        tools.setLayout(null);
        tools.setBounds(5,10,180,590);
        tools.setBorder(BorderFactory.createTitledBorder(lowerdetched,"Controls"));

        //setting up algorithm label
        algorithmL.setHorizontalAlignment(JLabel.CENTER);
        algorithmL.setBounds(40,20,100,25);
        jf.add(algorithmL);

        //set up dropdown
        alg.setBounds(30,45,120,25);
        jf.add(alg);

        //set up graph Type Label
        typeL.setHorizontalAlignment(JLabel.CENTER);
        typeL.setBounds(40,80,100,25);
        jf.add(typeL);

        //set up graph type dropdown
        graph.setBounds(30,105,120,25);
        jf.add(graph);

        //sett up the sort button
        sort.setBounds(40,150,100,25);
        jf.add(sort);
        
        //set up the shuggle button
        shuffle.setBounds(40,190,100,25);
        shuffle.setVisible(true);
        jf.add(shuffle);
        

        //set up the delay label
        delayL.setHorizontalAlignment(JLabel.LEFT);
        delayL.setBounds(10,230,50,25);
        jf.add(delayL);

        //set up the ms label
        msL.setHorizontalAlignment(JLabel.LEFT);
        msL.setBounds(135,230,50,25);
        jf.add(msL);

        //set up the speed slider
        speed.setMajorTickSpacing(5);
        speed.setBounds(55,230,75,25);
        speed.setPaintTicks(false);
        jf.add(speed);
    
        //set up size Label
        sizeL.setHorizontalAlignment(JLabel.LEFT);
        sizeL.setBounds(10,275,50,25);
        jf.add(sizeL);

        //set up size Slider
        size.setMajorTickSpacing(50);
        size.setBounds(55,275,75,25);
        size.setPaintTicks(false);
        jf.add(size);

        //set up the comparisons label
        compareL.setHorizontalAlignment(JLabel.LEFT);
        compareL.setBounds(10,320,200,25);
        jf.add(compareL);

        //sett up array access Label
        accessL.setHorizontalAlignment(JLabel.LEFT);
        accessL.setBounds(10,360,200,25);
        jf.add(accessL);
        
        //set up info area
        information.setBounds(10,400,160,125);
        information.setEditable(false);
        jf.add(information);

        //set up credit button
        credit.setBounds(40,540,100,25);
        jf.add(credit);

        //set up canvas for graph
        canvas = new GraphCanvas();
        canvas.setBounds(190,0,SIZE,SIZE);
        canvas.setBorder(BorderFactory.createLineBorder(Color.black));
        jf.getContentPane().add(tools);
        jf.getContentPane().add(canvas);
        jf.setVisible(true);

        //add ActionListeners

        alg.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                curlAlg = alg.getSelectedIndex();
                information.setText(algInfo[curlAlg]);
            }
            
        });

        graph.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                type = graph.getSelectedIndex();
                shuffleList();
                reset();
                Update();
            }
        });

        sort.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(shuffled){
                    sorting = true;
                    compare = 0;
                    acc = 0;

                }
            }
        });

        shuffle.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shuffleList();
                reset();

            }
        });
        
        speed.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent arg0){
                spd = (int)speed.getValue();
                msL.setText(spd+ " ms");

            }
        });

        size.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                int val = size.getValue();
                if(size.getValue() == 5){
                    val = 4;
                }
                len = val * 50;
                lenL.setText(len+"");
                if(list.length != len){
                    shuffleList();
                }
                reset();
            }
        });

        credit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(jf, " By: Dawson Lefebvre\n", "Credits", JOptionPane.PLAIN_MESSAGE);


            }
        });
        sorting();

    }
    //Sorting state

    public void sorting(){
        if(sorting){
            try{
                switch(curlAlg){
                    case 0:
                        quickSort(0, len-1);
                        break;
                    case 1:
                        bubbleSort();
                        break;
                    case 2:
                        bogoSort();
                    case 3:
                        selectionSort();
                    default:
                        bubbleSort();
                        break;

                }
            }catch(IndexOutOfBoundsException e){}
        }
        reset();
        pause();
    }
//pause state
    public void pause(){
        int i = 0;
        while(!sorting){
            i++;
            if(!sorting){
                i++;
                if(i > 100){
                    i = 0;

                }
                try{
                    Thread.sleep(1);
                }catch(Exception e){}
            }
            
        }
        sorting();
    }
    //reset some variables
    public void reset(){
        sorting = false;
        current = -1;
        check = -1;
        off = 0;
        Update();

    }

    //Delay Method
    public void delay(){
        try{
            Thread.sleep(spd);
        }catch(Exception e){}
    }
    //updates the graph and labels
    public void Update(){
        width = SIZE/len;
        canvas.repaint();
        compareL.setText("Comparisons :" + compare);
        accessL.setText("Array Accesses:" + acc);

    }
    //sub class to control the graph
    class GraphCanvas extends JPanel{
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public GraphCanvas() {
            setBackground(Color.black);

        }
        //paints the graph.
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for(int i = 0;i < len; i++){//runs through each elemts of the list
                int HEIGHT = list[i]*width;
                if(type == 0){//bar graphtype

                    g.setColor(Color.white);//default color
                    
                    if(current > -1 && i == current){
                        g.setColor(Color.green);//color of current

                    }
                    
                    if(check > -1 && i == check){
                    
                    g.setColor(Color.red);//color of the checking

                }
                //draws the bar and the blackoutline
                g.fillRect(i*width, SIZE-HEIGHT,width,HEIGHT);
                g.setColor(Color.black);
                g.drawRect(i*width, SIZE-HEIGHT, width, HEIGHT);
            }
                else if(type == 1){
                    g.setColor(Color.white);//default  color
                    if(current > -1 && i == current){
                        g.setColor(Color.green);
                    }
                    if(check > -1 && i == check){
                        g.setColor(Color.red);//color of checking

                    }
                    //draws dot
                    g.fillOval(i*width, SIZE-HEIGHT, width, width);
                }
            }
        }
    }

        //sorting methods & main methods

    //bubblesort
    public void bubbleSort(){
        int c = 1;
        boolean noSwaps = false;
        while(!noSwaps && sorting){
            current = len-c;
            noSwaps = true;
            for(int i = 0; i < len-c; i++){
                if(!sorting){
                    break;
                }
                if(list[i+1] < list[i]){
                    noSwaps = false;
                    swap(i,i+1);
                }
                check = i+1;
                compare ++;
                acc+=2;
                Update();
                delay();

            }
            c++;

        }
    }

    //bogosort
    public void bogoSort(){
        while(!checkSorted() && sorting){
            for(int i =0; i < len; i++){
                if(!sorting){
                    break;
                }
                current = i;
                int rand = r.nextInt(len);
                check = rand;
                int temp = list[i];
                acc++;
                list[i] = list[rand];
                acc += 2;
                list[rand] = temp; 
                acc++;
                Update();
                delay();

            }

        }

    }

    //selection sort
    public void selectionSort(){
        int c = 0;
        while(c < len && sorting){
            int sm = c;
            current = c;
            for(int i = c+1; i < len; i++){
                if(!sorting){
                    break;
                }
                if(list[i] < list[sm]){
                    sm = i;
                }
                check = i;
                compare++;
                Update();
                delay();
            }
            if(c != sm){
                swap(c,sm);
            }    c++;
        }
    }
    //quick sort
    public void quickSort(int low, int hi){
        if(!sorting){
            return;
        }
        current = hi;
        if(low < hi){
            int p = partition(low,hi);
            quickSort(low, p-1);
            quickSort(p+1, hi);

        }

    }
//checksorted for the bogosort
    public boolean checkSorted(){
        for(int i = 0; i < len-1; i++){
            if(list[i] > list[i+1]){
                acc+=2;
                return false;
            }

        }return true;

    }

    public int partition(int lo, int hi){
        int pivot = list[hi]; 
        acc++;
        int i = lo -1;
        for(int j = lo; j < hi; j++){
            check = j;
            if(!sorting){
                break;
            }
            if(list[j] < pivot){
                i++;
                swap(i,j);

            }
            compare++;
            acc++;
            Update();
            delay();
        }
        swap(i+1,hi);
        Update();
        delay();
        return i+1;
}

    public void swap(int i1, int i2){
        int temp = list[i1]; acc++;
        list[i1] = list[i2]; acc += 2;
        list[i2] = temp; acc++;
    }

    

    
    public static void main(String[] args) {
        new VisualSort();
       
        
    }
}