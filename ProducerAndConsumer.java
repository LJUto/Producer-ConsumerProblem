import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class ProducerAndConsumer {
    private static final int BUFFER_SIZE = 4;
    
    private JLabel label1_1;
    private JLabel label1_2;
    private JLabel label1_3;
    private JLabel label1_4;
    private JLabel label2_1;
    private JLabel label2_2;
    private JButton button1;
    private JButton button2;
    private JLabel text1;
    private JLabel text2;
    private JLabel text3;
    private JLabel text4;
    private DrawCircle1 circle1;
    private DrawCircle2 circle2;
    private DrawLine1 line1;
    private DrawLine2 line2;
    private DrawLine3 line3;
    private DrawLine4 line4;
    private JLabel buf0;
    private JLabel buf1;
    private JLabel buf2;
    private JLabel buf3;
    private JLabel buffer0;
    private JLabel buffer1;
    private JLabel buffer2;
    private JLabel buffer3;
    private Semaphore mutexP;
    private Semaphore mutexC;
    private Semaphore nrfull;
    private Semaphore nrempty;
    private int in;
    private int out;
    private String[] buffer;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProducerAndConsumer().createAndShowGUI());
    }
    
    private void createAndShowGUI() {
    	JFrame frame = new JFrame("원형 다중 버퍼를 이용한 생산자-소비자 문제");
    	frame.setLayout(null);
    	frame.getContentPane().setBackground(Color.WHITE);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initialize();
        
        label1_1 = new JLabel("mutexP : -");
        label1_1.setBounds(12, 328, 72, 15);
        
        label1_2 = new JLabel("mutexC : -");
        label1_2.setBounds(294, 328, 72, 15);
        
        label1_3 = new JLabel("nrfull : -");
        label1_3.setBounds(12, 370, 72, 15);
        
        label1_4 = new JLabel("nrempty : -");
        label1_4.setBounds(294, 370, 72, 15);
        
        label2_1 = new JLabel("in : -");
        label2_1.setBounds(124, 262, 72, 40);
        
        label2_2 = new JLabel("out : -");
        label2_2.setBounds(294, 262, 100, 40);
        
        button1 = new JButton("생산자 프로세스");
        button1.setBackground(Color.GRAY);
        button1.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		startProducers();
        	}
        });
        button1.setBounds(330, 102, 127, 23);
        
        button2 = new JButton("소비자 프로세스");
        button2.setBackground(Color.GRAY);
        button2.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		startConsumers();
        	}
        });
        button2.setBounds(330, 168, 127, 23);
        
		text1 = new JLabel("NULL");
		text1.setBounds(79, 328, 193, 15);
		frame.add(text1);
		
		text2 = new JLabel("NULL");
		text2.setBounds(368, 328, 193, 15);
		frame.add(text2);
	
		text3 = new JLabel("NULL");
		text3.setBounds(79, 370, 193, 15);
		frame.add(text3);
		
		text4 = new JLabel("NULL");
		text4.setBounds(368, 370, 193, 15);
		frame.add(text4);
		
		circle1 = new DrawCircle1();
		circle1.setBounds(125, 125, 50, 50);
		
		circle2 = new DrawCircle2();
		circle2.setBounds(50, 50, 200,200);
			
		line1 = new DrawLine1();
    	line1.setBackground(Color.BLACK);
		line1.setBounds(150, 50, 2, 75);
		
		line2 = new DrawLine2();
    	line2.setBackground(Color.BLACK);
		line2.setBounds(175, 150, 75, 3);
		
		line3 = new DrawLine3();
    	line3.setBackground(Color.BLACK);
		line3.setBounds(150, 175, 2, 75);
		
		line4 = new DrawLine4();
    	line4.setBackground(Color.BLACK);
		line4.setBounds(50, 150, 75, 3);
		
		buf0 = new JLabel("NULL");
		buf0.setBounds(180, 100, 193, 15);
		
		buf1 = new JLabel("NULL");
		buf1.setBounds(180, 185, 193, 15);
		
		buf2 = new JLabel("NULL");
		buf2.setBounds(100, 100, 193, 15);
		
		buf3 = new JLabel("NULL");
		buf3.setBounds(100, 185, 193, 15);
		
		buffer0 = new JLabel("buffer[0]");
		buffer0.setBounds(225, 70, 193, 15);
		
		buffer1 = new JLabel("buffer[1]");
		buffer1.setBounds(225, 220, 193, 15);
		
		buffer2 = new JLabel("buffer[2]");
		buffer2.setBounds(25, 220, 193, 15);
		
		buffer3 = new JLabel("buffer[3]");
		buffer3.setBounds(25, 70, 193, 15);
		
        frame.add(label1_1);
        frame.add(label1_2);
        frame.add(label1_3);
        frame.add(label1_4);
        frame.add(label2_1);
        frame.add(label2_2);
        frame.add(button1);
        frame.add(button2);
        frame.add(circle1);
        frame.add(circle2);
        frame.add(line1);
        frame.add(line2);
        frame.add(line3);
        frame.add(line4);
        frame.add(buf0);
        frame.add(buf1);
        frame.add(buf2);
        frame.add(buf3);
        frame.add(buffer0);
        frame.add(buffer1);
        frame.add(buffer2);
        frame.add(buffer3);
        
        frame.pack();
    	frame.setSize(550, 450);
        frame.setVisible(true);
        
    }
    
    private class DrawCircle1 extends JPanel{
    	@Override
    	public void paint(Graphics g) {
    		g.drawOval(0,0, 50, 50);
    	}
    }
    
    private class DrawCircle2 extends JPanel{
    	@Override
    	public void paint(Graphics g) {
    		g.drawOval(0, 0, 200, 200);
    	}
    }
    
    private class DrawLine1 extends JPanel{
    	@Override
    	public void paintComponent(Graphics g) {
    	    super.paintComponent(g);
    	    Graphics2D g2 = (Graphics2D) g;

    	    g2.draw(new Line2D.Double(150, 50, 150, 125));
    	}
    }
    
    private class DrawLine2 extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.draw(new Line2D.Double(175, 150, 250, 150));
        }
    }

    
    private class DrawLine3 extends JPanel{
    	@Override
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.draw(new Line2D.Double(250, 150, 150, 175));
    	}
    }
    
    private class DrawLine4 extends JPanel{
    	@Override
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.draw(new Line2D.Double(150, 50, 125, 150));
    	}
    }
    
    private void initialize() {
    	mutexP = new Semaphore(1);
    	mutexC = new Semaphore(1);
    	nrfull = new Semaphore(0);
    	nrempty = new Semaphore(BUFFER_SIZE);
    	in = 0;
    	out = 0;
    	buffer = new String[BUFFER_SIZE];
    	for (int i = 0; i < BUFFER_SIZE; i++) {
    		buffer[i] = null;
    	}
    }
    
    private void startProducers() {
        new Thread(new Producer()).start();
    }
    
    
    private void startConsumers() {
    	new Thread(new Consumer()).start();
    }
    
    private void updateGUI() {
     	SwingUtilities.invokeLater(new Runnable() {
    		@Override
    		public void run() {
    			label2_1.setText("in: " + in);
    			label2_2.setText("out: " + out);
    			label1_1.setText("mutexP: " + mutexP.availablePermits());
    			label1_2.setText("mutexC: " + mutexC.availablePermits());
    			label1_3.setText("nrfull: " + nrfull.availablePermits());
    			label1_4.setText("nrempty: " + nrempty.availablePermits());
    			text1.setText("대기중인 프로세스의 수: " + mutexP.getQueueLength());
    			text2.setText("대기중인 프로세스의 수: " + mutexC.getQueueLength());
    			text3.setText("대기중인 프로세스의 수: " + nrfull.getQueueLength());
    			text4.setText("대기중인 프로세스의 수: " + nrempty.getQueueLength());
    			buf0.setText("" + buffer[0]);
    			buf1.setText("" + buffer[1]);
    			buf2.setText("" + buffer[3]);
    			buf3.setText("" + buffer[2]);
    		}
    	});
    }
    
    private class Producer implements Runnable {
        @Override
        public void run() {
                try {
                	updateGUI();

                	mutexP.acquire();
                    updateGUI();

                	nrempty.acquire();
                	updateGUI();
                    
                    Random random1 = new Random();
                    
                    String item = ""+ random1.nextInt()%100;
                    buffer[in] = item;
                    in = (in + 1) % BUFFER_SIZE;

                    updateGUI();
                    
                    nrfull.release();
                    updateGUI();

                    mutexP.release();
                    updateGUI();

                    Thread.sleep((long) (Math.random() * 2000));
                    updateGUI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    private class Consumer implements Runnable {
        @Override
        public void run() {
                try {
                	updateGUI();

                    mutexC.acquire();
                    updateGUI();

                    nrfull.acquire();
                    updateGUI();
                    
                    String item = buffer[out];
                    buffer[out] = null;
                    out = (out + 1) % BUFFER_SIZE;

                    updateGUI();
                    
                    nrempty.release();
                    updateGUI();
;
                    mutexC.release();
                    updateGUI();

                    Thread.sleep((long) (Math.random() * 2000));
                    updateGUI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
