import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Path2D;
import static java.lang.Math.*;

public class Board extends JFrame{
    private static final long serialVersionUID = 1L;
    
    // CONSTRUCTOR
    public Board(boolean isSequential, int length, int recursiveNumber) {
        super("Sierpinski Triangle Project by Suleyman Golbol!!"); // Title.
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setEnabled(true);
        scrollPane.setVisible(true);
        scrollPane.setBackground(Color.GREEN);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        PanelDrawer boardPanel = new PanelDrawer(isSequential, length, recursiveNumber, this);
        scrollPane.setViewportView(boardPanel);
        
    }
    
}   //End of BoardCreator class.

class PanelDrawer extends JPanel{
    private static final long serialVersionUID = 1L;
    private boolean isSequential = true;
    private boolean fitViewPort = true;
    private int length = 120;
    private int numberOfTimes = 5;
    
    private JFrame frame;
    private JButton zoomInButton  = new JButton("Zoom In");
    private JButton zoomOutButton = new JButton("Zoom Out");

    private double scale = 1.0;
    double width = (double)this.getWidth();
    double height = (double)this.getHeight();


    public PanelDrawer(boolean isSequential, int length, int numberOfTimes, JFrame frame) {
        this.isSequential = isSequential;
        this.length = length;
        this.numberOfTimes = numberOfTimes; //recursive number of times.
        this.frame = frame;
        
        zoomInButton.addActionListener(new ActionListener(){  //Load button's action listener if clicked
            @Override
            public void actionPerformed(ActionEvent act){
                scale *= 1.1;
                repaint();
            }
        } );

        zoomOutButton.addActionListener(new ActionListener(){  //Save button's action listener if clicked
            @Override
            public void actionPerformed(ActionEvent act){
                scale /= 1.1;
                repaint();
            }
        } );

        //Adding zoom buttons to frame.
        frame.add(zoomInButton, BorderLayout.NORTH);
        frame.add(zoomOutButton, BorderLayout.SOUTH);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(5000, 3000);
    }

    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic);
        //Creating first point locations for a equilateral triangle 
        Point p1 = new Point(70.0 - length/2.0, 70.0 + length*sin(PI/3));
        Point p2 = new Point(70.0 + length/2.0, 70.0 + length*sin(PI/3));
        Point p3 = new Point(70.0, 70.0);

        //Creating graphic
        Graphics2D g2d = (Graphics2D) graphic.create();

        //Scaling screen.
        g2d.translate(width/2.0, height/2.0);
        g2d.scale(scale, scale);
        g2d.translate(-width/2.0, -height/2.0);        

        if(fitViewPort == true && width < (p2.getX() + length*(numberOfTimes-1)+10.0) ){ //if width smaller than total length of triangles
            scale = frame.getWidth() / ((p2.getX() + (length+20)*(numberOfTimes-1))*1.0); //fitting to the screen
            fitViewPort = false;
            g2d.translate(width/2.0, height/2.0);       //move every point +width/2.0 and +height/2.0
            g2d.scale(scale, scale);
            g2d.translate(-width/2.0, -height/2.0);     //then backmove every point -width/2.0 and -height/2.0
        }

        //Creating temporary points to paint it black.
        Point p1Temp = new Point(p1.getX(), p1.getY());
        Point p2Temp = new Point(p2.getX(), p2.getY());
        Point p3Temp = new Point(p3.getX(), p3.getY());

        for(int i=0; i<numberOfTimes; i++){
            //Firstly painting all triangles to black.
            blackFiller(p1Temp, p2Temp, p3Temp, g2d);                
            p1Temp.setX( p1Temp.getX() + length+10 );
            p2Temp.setX( p2Temp.getX() + length+10 );
            p3Temp.setX( p3Temp.getX() + length+10 );
        }
        
        //Drawing white triangle
        Triangle whiteTriangle = new Triangle(p1, p2, p3, g2d, numberOfTimes, length, isSequential);
        g2d.draw( whiteTriangle );
        
    }
    
    //This is for firstly painting all triangles to black.
    public void blackFiller(Point p1, Point p2, Point p3, Graphics2D g2d){
        double[] arrayX = { p1.getX(), p2.getX(), p3.getX() };
        double[] arrayY = { p1.getY(), p2.getY(), p3.getY() };

        //Drawing triangle based on arrays.
        Path2D path = new Path2D.Double();
        path.moveTo(arrayX[0], arrayY[0]);
        for(int j = 1; j<arrayX.length; ++j)
            path.lineTo(arrayX[j], arrayY[j]);
        path.closePath();

        // Draw the triangle with black lines.
        g2d.setStroke(new BasicStroke(0.0f));
        g2d.setColor(Color.BLACK);
        g2d.draw(path);
        g2d.setColor(Color.BLACK);
        g2d.fill(path);
    }

}