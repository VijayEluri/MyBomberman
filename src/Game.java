import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

 
public class Game extends BasicGame {
	public static boolean blocked[][];
	private TiledMap map;
	private MyPlayer player;
	private Animation player_anim;
	private SpriteSheet sheet;
	private boolean set_bomb = false;
	private MyBomb bomb = null;
	private float bombX = 0;
	private float bombY = 0;
	private int cur_tile;
	private int l_tile;
	private int r_tile;
	private int u_tile;
	private int lo_tile;
	private Camera camera;
	private int posX;
	private int posY;
	
	public Game() {
		super("Slick bomberman");
	}
	 
	@Override
	public void init(GameContainer container) throws SlickException {
		container.setVSync(true);
		container.setTargetFrameRate(60); 
		player = new MyPlayer(32, 32, "res/figure.png");
		sheet = player.getImg();
		map = new TiledMap("res/map.tmx");	
		bomb = new MyBomb("res/bomb.png"); 
		camera = new Camera(container, map);
		
		player_anim = new Animation();
		player_anim.setAutoUpdate(false);
		for (int frame=0;frame<4;frame++) {
			player_anim.addFrame(sheet.getSprite(frame,0), 150);
		}
		
		blocked = new boolean[map.getWidth()][map.getHeight()];
		for (int xAxis = 0; xAxis < map.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < map.getHeight(); yAxis++) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if (value.equals("1")) {
					blocked[xAxis][yAxis] = true;
				}
			}
		}
	}
	@Override
	public void update(GameContainer container, int delta) {
		 
		cur_tile = (player.getX() + 16) / 32;
		cur_tile = map.getTileId(cur_tile, (player.getY() +16) /32, 0);
		l_tile = (player.getX() - 16) / 32;
		l_tile = map.getTileId(l_tile, (player.getY() + 16) / 32, 0);
		r_tile = (player.getX() + 32) / 32;
		r_tile = map.getTileId(r_tile, (player.getY() + 16) / 32, 0);
		u_tile = (player.getY() - 2) / 32;
		u_tile = map.getTileId(player.getX() / 32, u_tile, 0);
		lo_tile = (player.getY() + 34) / 32;
		lo_tile = map.getTileId(player.getX() / 32, lo_tile, 0);
		
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			player_anim.setCurrentFrame(2);
				
			boolean walkable = CollisionDetection.IsTileWalkable(player.getX(), player.getY(), 32, 32, 4);			
			if (walkable) {
				player.setX(player.getX()-2);
			}
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			player_anim.setCurrentFrame(3);
			boolean walkable = CollisionDetection.IsTileWalkable(player.getX(), player.getY(), 32, 32, 2);
			if (walkable) {
				player.setX(player.getX()+2);
			}
		} else if (container.getInput().isKeyDown(Input.KEY_UP)) {
			player_anim.setCurrentFrame(1);
			boolean walkable = CollisionDetection.IsTileWalkable(player.getX(), player.getY(), 32, 32, 1);
			if (walkable) {
				player.setY(player.getY()-2);
			}
		} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
				player_anim.setCurrentFrame(0);
				boolean walkable = CollisionDetection.IsTileWalkable(player.getX(), player.getY(), 32, 32, 3);
				if (walkable) {
					player.setY(player.getY()+2);
				}
		}
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			bombX = player.getX();
			bombY = player.getY();
			set_bomb = true;
		}
		camera.centerOn(player.getX(), player.getY());
	} 
	@Override
	public void render(GameContainer container, Graphics g)  {
		map.render(0, 0);
		g.drawString("cur TILE: "+cur_tile, 100, 10);
		g.drawString("left TILE: "+l_tile, 250, 10);
		g.drawString("right TILE: "+r_tile, 400, 10);
		g.drawString("upper TILE: "+u_tile, 550, 10);
		g.drawString("lower TILE: "+lo_tile, 700, 10);
		g.drawAnimation(player_anim, player.getX(), player.getY());
		
		if (set_bomb) {
			bomb.getImage().draw(bombX, bombY);
		}
	}
	public static void main(String[] argv) throws SlickException {
		AppGameContainer container = new AppGameContainer(new Game(), 955, 645, false);
		container.start();
	}
}