import greenfoot.*;
import java.util.List;
public class FreezeRay extends Ability {
    private int duration;
    private static GreenfootImage left;
    private static GreenfootImage right;
    private boolean movingRight;
    private Gru g;
    private FreezeRayBlast ammo;
    public FreezeRay() {
        super(1000, 10);
        duration = 0;
        left = getImage();
        left.scale(left.getWidth() / 2, left.getHeight() / 2);
        right = new GreenfootImage(getImage());
        right.mirrorHorizontally();
        ammo = new FreezeRayBlast(this);
    }
    public FreezeRay(Gru g) {
        this();
        this.g = g;
    }
    @Override
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        getWorld().addObject(ammo, this.getX() + 185, this.getY() - 20); 
    }
    @Override
    public void act() {
        if(g.facingRight()) {
            this.setImage(right);
            this.setLocation(g.getX() + 100, g.getY() - 30);
        }
        else {
            this.setImage(left);
            this.setLocation(g.getX() - 100, g.getY() - 30);
        }
        if(duration == 500) {
            removeFreezeBlocks();
            isFinished = true;
            getWorld().removeObject(ammo);
            getWorld().removeObject(this);
            duration = 0;
        }
        if(ammo == null) {
            ammo = new FreezeRayBlast(this);
            getWorld().addObject(ammo, this.getX() + 185, this.getY() - 20);
        }
        detectCollision("Gru");
        duration++;
    }
    @Override
    public void detectCollision(String name) {
        if(getWorld() != null) {
            Player p = (Player) ammo.getOneIntersectingObject(Player.class);
            if(p != null && !intersects && !(p instanceof Gru)) {
                p.decreaseHealth(getDamage());
                FreezeBlock freezedP = new FreezeBlock(p);
                getWorld().addObject(freezedP, p.getX(), p.getY());
                getWorld().setPaintOrder(Player.class, 
                    FreezeBlock.class);
                intersects = true;
                p.canMove = false;
                p.canCast = false;
                System.out.println(p.canMove);
            }
        }
    }
    private void removeFreezeBlocks() {
        List<FreezeBlock> freezeBlocks = 
            getWorld().getObjects(FreezeBlock.class);
        for(FreezeBlock fb : freezeBlocks) {
            getWorld().removeObject(fb);
        }
    }
}