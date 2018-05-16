//package me.planetguy.hex;
//
//import android.graphics.Color;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Container;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Polygon;
//import java.awt.RenderingHints;
//import java.awt.Stroke;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.List;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import me.planetguy.hex.math.HexMath;
//import me.planetguy.hex.math.HexPoint;
//
//
//public class HexTest {
//
//	public static final int xSize=7;
//	public static final int lengthening=5;
//
//	public static Color[][] renderWorld;
//	private static Color[] walkerColors;
//
//	public static Scroll walkerC;
//	public static List<Walker> walkers;
//
//	public static HexRenderer theRenderer=new HexRenderer();
//	public static ScheduledThreadPoolExecutor timer=new ScheduledThreadPoolExecutor(1);
//
//
//	public static void main(String[] args){
//		HexPoint a=HexMath.getArraySizedFor(xSize, xSize+lengthening);
//		renderWorld=new Color[a.x()][a.y()];
//		for(int i=0; i<renderWorld.length; i++){
//			for(int j=0; j<renderWorld[i].length; j++) {
//				renderWorld[i][j]=Color.RED;
//			}
//		}
//
//		walkerColors=new Color[]{
//				Color.GRAY,
//				Color.BLUE
//		};
//		walkerC=new Scroll(xSize, xSize+lengthening);
//		walkers=walkerC.walkers;
//		walkerC.init();
//		new Updater().scheduleSelf();
//
//		JFrame frame = new JFrame("Hex Testing 4");
//		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//		Container content = frame.getContentPane();
//		content.add(theRenderer);
//		frame.setResizable(true);
//		frame.setSize(900, 600);
//		frame.setLocationRelativeTo( null );
//		frame.setVisible(true);
//	}
//
//	static class HexRenderer extends JPanel {
//
//		private static final long serialVersionUID = -2930808359815342593L;
//
//		public HexRenderer() {
//			setBackground(Color.BLACK);
//			addMouseListener(new MouseListener(){
//
//				@Override
//				public void mouseClicked(MouseEvent ev) {
//					int x=ev.getX();
//					int y=ev.getY();
//					System.out.println(x+","+y);
//				}
//
//				public void mouseEntered(MouseEvent arg0) {}
//				public void mouseExited(MouseEvent arg0) {}
//				public void mousePressed(MouseEvent arg0) {}
//				public void mouseReleased(MouseEvent arg0) {}
//			});
//		}
//
//		final int dims=30;
//
//		double skewFactor=0.5;
//		boolean debugMode=false;
//
//		Color darkRed=new Color(128,0,0);
//
//		Stroke thin=new BasicStroke(2);
//		Stroke thick=new BasicStroke(3);
//
//		public void paintComponent(Graphics graphics) {
//			Graphics2D g = (Graphics2D)graphics;
//			g.clearRect(0, 0, 1000, 1000);
//			RenderingHints rh = new RenderingHints(
//					RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setRenderingHints(rh);
//			g.setStroke(thick);
//
//			synchronized(renderWorld){
//				//draws hexes
//				for(int i=0; i<renderWorld.length; i++){
//					for(int j=0; j<renderWorld[i].length; j++) {
//						Color theNewColor=null;
//						boolean fillHex=false;
//						for(int wn=0; wn<walkers.size(); wn++){
//							Walker w=walkers.get(wn);
//							if(i==w.x() && j==w.y()) {
//								theNewColor=walkerColors[wn];
//								fillHex=true;
//							}
//						}
//						if(theNewColor==null){
//							theNewColor=HexMath.permitSpace(i, j, xSize, xSize+lengthening) ? null :
//								debugMode ? Color.green : Color.white;
//						}
//						if( theNewColor == null) {
//							if(j<6 && (i+j<12 || i>11)
//									|| j>6 && (i<6 || i+j>17)){
//								theNewColor=darkRed;
//							} else {
//								theNewColor=Color.red;
//							}
//						}
//						g.setColor(theNewColor);
//
//						Polygon hex=drawHex(i, j, g);
//						if(fillHex)
//							g.fillPolygon(hex);
//						else
//							g.drawPolygon(hex);
//					}
//				}
//				g.setColor(Color.green);
//				for(HexPoint p:walkerC.activePatterns.keySet()){
//					int i=p.x();
//					int j=p.y();
//					g.fillOval( i*dims+(int)(j*dims*skewFactor)+dims, j*dims+dims, dims/4, dims/4);
//				}
//
//				//draw symbols
//				g.setFont(new Font("default", Font.BOLD, 16));
//				for(int i=0; i<renderWorld.length; i++){
//					for(int j=0; j<renderWorld[i].length; j++) {
//						if(HexMath.permitSpace(i, j, xSize, xSize+lengthening)) {
//							for(int w=0; w<walkers.size(); w++){
//								Walker walker=walkers.get(w);
//								if(i==walker.x() && j==walker.y()) {
//									g.setColor(Color.white);
//								}else{
//									g.setColor(walkerColors[w]);
//								}
//								char c=walker.code[i][j].mnemonic;
//								g.drawChars(new char[]{c}, 0, 1, i*dims+(int)(j*dims*skewFactor)-dims/3+w*dims/2+dims, j*dims+dims);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		private Polygon drawHex(int i, int j, Graphics2D g){
//			Polygon sprite=new Polygon();
//			for(int a=0; a<6; a++) {
//				//Go around a circle by sixths (pi/3), taking 10% off to leave space between things
//				double dx=Math.sin(a*Math.PI/3.0)/2.0+1;
//				double dy=Math.cos(a*Math.PI/3.0)/2.0+1;
//				sprite.addPoint((int)(dims*i+dims*dx), (int)(dims*j+dims*dy));
//			}
//			sprite.translate((int)(j*dims*skewFactor), 0); //make the hexagon more regular - parallelogram with corners cut off, not rectangle
//			return sprite;
//		}
//	}
//}
