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
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    });
    addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(gameOver){
                initGame();
            }
            else{
                int ex=e.getX()-margin;
                int ey=e.getY()-margin;

                //cliclking the grid
                if(ex<0 || ex>gridSize || ey<0 || ey>gridSize)
                    return;

                //get position on grid
                int c1=ex/tilesSize;
                int r1=ey/tilesSize;
                int c2=emptyPos%size;
                int r2=emptyPos%size;

                //ID cordination
                 int clickPos=r1*size+c1;
                 int dir=0;

                 //search of multiple direction to move tiles to at once
                if(c1==c2 && Math.abs(r1-r2)>0)
                    dir=(r1-r2)>0?size: -size;
                else if(r1==r2 && Math.abs(c1-c2)>0)
                    dir=(c1-c2)>0?size: -size;
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

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    });

    initGame();
    }

    private void reset(){
        for(int i=0; i<tiles.length; i++){
            tiles[i]=(i+1) % tiles.length;
        }
        emptyPos=tiles.length-1;
    }

    public void shuffle(){
        int n=numOfTiles;
        while(n>1){
            int r= RANDOM.nextInt(n--);
            int temp=tiles[r];
            tiles[r]=tiles[n];
            tiles[n]=temp;
        }
    }

    private boolean isSolved(){
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
                return true;
        }
        return true;
    }
    private void initGame(){

        do{
            reset();
            shuffle();
        }
        while(isSolved());
        gameOver=false;

    }
    private void drwaGrid(Graphics2D g){
        for(int i=0; i<tiles.length; i++){
            int row=i/size;
            int column=i%size;
            int x=margin+column*tilesSize;
            int y=margin+row*tilesSize;

            if (tiles[i]==0){
                if(gameOver){
                    g.setColor(FORECOLOR);
                    drawcentreString(g, "\u273", x, y);

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

    private void drawcentreString(Graphics2D g, String s, int x, int y) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Puzzle Game");
            frame.setResizable(false);

            frame.add(new Main(4, 550, 30), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}