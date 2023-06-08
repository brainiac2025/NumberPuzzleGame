import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.Random;

public class Main  extends JPanel {


    //draw the grid in jpanel
    private int size;
    private int numOfTiles;
    private int dimension;
    private int [] tiles;
    private int tilesSize;
    private int emptyPos;
    private boolean gameOver;
    private int gridSize;
    private int margin;
    private static final Color FORECOLOR= new Color(80, 156, 239);
    private static final Random RANDOM= new Random();


    public Main(int size, int dim, int mar){
    this.size=size;
    this.dimension=dim;
    this.margin=mar;

    //initializing the tiles
    numOfTiles=size*size-1;  //i had to subtract -1 becuase of empty space
    tiles=new int[size*size];

    //calculate the grid size
        gridSize=(dim-2*margin);
        tilesSize=gridSize/size;

        setPreferredSize(new Dimension(dimension, dimension+margin));
        setBackground(Color.WHITE);
        setForeground(FORECOLOR);
        setFont(new Font("Ariel", Font.BOLD, 50));
        gameOver=true;

    addMouseListener(new MouseAdapter() {


        @Override
        public void mousePressed(MouseEvent e) {
            if(gameOver){
                initGame();
            }
            else{
                //get position of the click
                int ex=e.getX()-margin;
                int ey=e.getY()-margin;

                //cliclking the grid
                if(ex<0 || ex>gridSize || ey<0 || ey>gridSize)
                    return;

                //get position on grid
                int c1=ex/tilesSize;
                int r1=ey/tilesSize;

                //get position of empty cell
                int c2=emptyPos%size;
                int r2=emptyPos/size;

                //ID cordination
                 int clickPos=r1*size+c1;
                 int dir=0;

                 //search of multiple direction to move tiles to at once
                if(c1==c2 && Math.abs(r1-r2)>0)
                    dir=(r1-r2)>0?size: -size;
                else if(r1==r2 && Math.abs(c1-c2)>0)
                    dir=(c1-c2)>0? 1: -1;
                if(dir!=0){
                    //move tiles in the direction
                    do{
                        int newEmptyPos=emptyPos+dir;
                        tiles[emptyPos]=tiles[newEmptyPos];
                        emptyPos=newEmptyPos;
                    }
                    while(emptyPos!=clickPos);
                        tiles[emptyPos]=0;

                }

                gameOver=solved();

            }

            repaint();// repainting the panel

        }


    });

    initGame();
    }

    private void initGame(){

        do{
            reset();
            shuffle();
        }
        while(isSolved());
        gameOver=false;

    }


    //reset the game after completion
    protected void reset(){
        for(int i=0; i<tiles.length; i++){
            tiles[i]=(i+1) % tiles.length;
        }
        emptyPos=tiles.length-1;
    }

    //shuffle the number function
    protected void shuffle(){
        int n=numOfTiles;
        while(n>1){
            int r= RANDOM.nextInt(n--);
            int temp=tiles[r];
            tiles[r]=tiles[n];
            tiles[n]=temp;
        }
    }


    protected boolean isSolved(){
        int count=0;
        for(int i =0; i<numOfTiles; i++){
            for(int j=0; j<i; j++){
                if(tiles[j]>tiles[i]){
                    count++;
                }
            }
        }
        return count % 2==0;
    }


    private boolean solved(){
        if(tiles[tiles.length-1]!=0){
            return false;
        }

        for(int i=numOfTiles-1; i>=0; i--){

            if(tiles[i]!=i+1)
                return false;
        }
        return true;
    }



    private void drawGrid(Graphics2D g){
        for(int i=0; i<tiles.length; i++){
            int row=i/size;
            int column=i%size;
            //convert cordinate on frame
            int x=margin+column*tilesSize;
            int y=margin+row*tilesSize;
            //checking case for blank tiles
            if (tiles[i]==0){
                if(gameOver){
                    g.setColor(FORECOLOR);
                    drawcentreString(g, "\u2713", x, y);

                }
                continue;
            }

            // for other tiles
            g.setColor(getForeground());
            g.fillRoundRect(x, y, tilesSize, tilesSize,25, 25);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x,y,tilesSize,tilesSize,25,25);
            g.setColor(Color.WHITE);
            drawcentreString(g, String.valueOf(tiles[i]), x, y);

        }
    }

    private void drawStartMessage(Graphics2D g) {
        if(gameOver){
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FORECOLOR);
            String ss="Click Here TO Start New Game";
            g.drawString(ss, (getWidth() - g.getFontMetrics().stringWidth(ss)) / 2, getHeight()-margin);

        }
    }

    private void drawcentreString(Graphics2D g, String ss, int x, int y) {
        FontMetrics fm=g.getFontMetrics();
        int ascending=fm.getAscent();
        int descending=fm.getDescent();
        g.drawString(ss, x+(tilesSize-fm.stringWidth(ss))/ 2, y
                    +(ascending+(tilesSize-(ascending+descending))/2 ));

    }



    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2d);
        drawStartMessage(g2d);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Number Puzzle Game");
            frame.setResizable(false);

            frame.add(new Main(3, 700, 40), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}