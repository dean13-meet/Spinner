import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.math.BigInteger;
import java.util.ArrayList;






import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;


import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Players;
import org.powerbot.script.rt6.Bank;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Widgets;
import org.powerbot.script.Script.Manifest;



//import org.powerbot.script.methods.Widgets;



@Manifest(name = "Spinner", description = "short description of your script")

public class Spinner extends PollingScript<ClientContext> implements MessageListener, PaintListener{



ArrayList<Task> tasks = new ArrayList<Task>();

int spinned = 0;
int originalXp = ctx.skills.experience(ctx.skills.CRAFTING);
long originalTime = System.currentTimeMillis();






@Override

public void start(){

tasks.add(new Banker(ctx));

tasks.add(new walkThere(ctx));

tasks.add(new Spin(ctx));

tasks.add(new walkBack(ctx));

}


 
@Override

public void repaint(Graphics g) {
	spinned = (ctx.skills.experience(ctx.skills.CRAFTING) - originalXp)/15;

g.setColor(Color.WHITE);


    g.setFont(new Font("Tahoma", Font.BOLD, 16));

    g.drawString("String Spinned: " + spinned, 50, 50);
    g.drawString("Gained XP: " + (ctx.skills.experience(ctx.skills.CRAFTING) - originalXp),
    		50, 100);

}



@Override

public void messaged(MessageEvent msg) {

//fill below:

if(msg.getMessage().toLowerCase().contains("aa")){

//spinned++
}


}



@Override

public void poll() {

for(Task i : tasks){

if(i.activate())

i.execute();

try {

Thread.sleep(1554);

} catch (InterruptedException e) {

// TODO Auto-generated catch block

e.printStackTrace();

};

}
//spinned+=28;

}



}





abstract class Task extends ClientAccessor{

//verify:


public int[] flaxId = {1779};

public Task(ClientContext arg0) {

super(arg0);

// TODO Auto-generated constructor stub

}

public abstract boolean activate();

    public abstract void execute();

    public void sleep(int n){
    	System.out.println("Before " + n);
    	n += 0.1*n*Math.random();
    	System.out.println("After " + n);
    try {

Thread.sleep(n);

} catch (InterruptedException e) {

// TODO Auto-generated catch block

e.printStackTrace();

}

    }

}

class Banker extends Task{



Bank banking = new Bank(ctx);

boolean bankOpen = banking.open();
boolean deposited = (28-numberOfFlaxInInv()-numberOfEmptyItemsInInv())==0;
boolean withdrew = numberOfFlaxInInv()>0;
boolean closed = !bankOpen && deposited;


public int numberOfFlaxInInv(){
	int counter = 0;
	for(org.powerbot.script.rt6.Item item : ctx.backpack.items()) {
		if(item.id() == 1779){
			counter++;
		}
	}
	return counter;
}
public int numberOfEmptyItemsInInv(){
	int counter = 0;
	for(Item i : ctx.backpack.items()){
		if(i.id()==-1){
			counter ++;
		}
	}
	return counter;
}

public Banker(ClientContext context) {

super(context);

}



@Override

public boolean activate() {

	bankOpen = banking.open();
	deposited = false;//ctx.backpack.isEmpty();
	withdrew = numberOfFlaxInInv()>0;
	closed = !bankOpen && deposited;
	
if(true){//We always bank, if there is bank on screen


return true;

}
return true;



}



@Override

public void execute() {

	System.out.println("JREBEL!!");
	System.out.println("Open: " + bankOpen);
	System.out.println("Deposited number: " + (28-numberOfFlaxInInv()-numberOfEmptyItemsInInv()));
	System.out.println("Withdrew Number: " + numberOfFlaxInInv());

ctx.objects.select().id(banking.BANK_BOOTH_IDS);
GameObject bank = ctx.objects.nearest().poll();

if(bank.inViewport()){

	if(!bankOpen){
System.out.println("1");

bank.interact("Bank");

bankOpen = banking.opened();
System.out.println("Bank Open: " + bankOpen);


sleep(925);
	}
	
if(!deposited){
System.out.println("2");

banking.depositInventory();
System.out.println("Depositing");
deposited = ctx.backpack.isEmpty();
System.out.println("Diposited: " + deposited);
sleep(2222);
}


if(!withdrew){
System.out.println("3");

banking.withdraw(flaxId[0], 28);

withdrew = numberOfFlaxInInv()>0;
System.out.println("Withdrew: " + withdrew);
sleep(1723);
}
if(!closed){
System.out.println("4");

banking.close();

sleep(2222);
closed = !banking.opened();
System.out.println("Closed: " + closed);
System.out.println("5");
}

}else{
	ctx.camera.turnTo(bank);
	


sleep(250);
ctx.movement.newTilePath(bank.tile()).traverse();


sleep(511);


}

if(!(numberOfFlaxInInv()>0)||banking.opened()){
	System.out.println("Redo banking");
	if(banking.opened()){
	banking.close();}
	bankOpen = false;
	deposited = false;
	withdrew = false;
	closed = false;
	execute();
}
}


}

class walkThere extends Task{
boolean wentDown = false;
boolean clickedWheel = false;

GameObject stairs;
int[] stairsId = {36775};

int[] wheels = {36970};

public walkThere(ClientContext arg0) {

super(arg0);

// TODO Auto-generated constructor stub

}



@Override

public boolean activate() {

if(!ctx.backpack.id(flaxId).isEmpty()//Got flax

&&!ctx.objects.select().id(stairsId).isEmpty()//Can find stairs


){
	stairs = ctx.objects.nearest().poll();
	System.out.println("Stairs tile: " + stairs.tile());
	wentDown = false;
	clickedWheel = false;
return true;

}

return false;

}



@Override

public void execute() {


if(!wentDown){
if(stairs.inViewport()){

stairs.interact("Climb-down");

sleep(3568);


}


else{
	ctx.camera.turnTo(stairs);


//sleep(222);

	ctx.mouse.move(stairs.tile().matrix(ctx).mapPoint());
	ctx.mouse.click(true);
//ctx.movement.step(ctx.movement.closestOnMap(stairs.tile()));

sleep(322);



}


sleep(333);
Tile local = ctx.players.local().tile();
if(local.equals(new Tile(3205,3209,1))
		||local.equals(new Tile(3206,3209,1))
		||local.equals(new Tile(3204,3209,1))
		||local.equals(new Tile(3205,3210,1))
		||local.equals(new Tile(3205,3208,1))
		||local.equals(new Tile(3206,3210,1))
		||local.equals(new Tile(3206,3208,1))
		||local.equals(new Tile(3204,3210,1))
		||local.equals(new Tile(3204,3208,1))
		){
	System.out.println("Went Down");
	wentDown = true;
}
System.out.println("WD = " + wentDown);
}

ctx.objects.select().id(wheels);
GameObject wheel = ctx.objects.nearest().poll();

if(wentDown&&!clickedWheel){
if(wheel.inViewport()){
	sleep(724);
	if(!ctx.widgets.component(1370, 38).visible())
wheel.interact("Spin");

sleep(112);

}

else{
	ctx.camera.turnTo(wheel);


sleep(232);

ctx.movement.newTilePath(wheel.tile()).traverse();

sleep(432);



}
if(ctx.widgets.component(1370, 38).visible()){
	clickedWheel = true;
	System.out.println("Clicked Wheel" + clickedWheel);
}
}
if(!wentDown || !clickedWheel){
	System.out.println("Redo walk There");
	sleep(922);
	execute();
}
}


}

class Spin extends Task{



public Spin(ClientContext arg0) {

super(arg0);

// TODO Auto-generated constructor stub

}



@Override

public boolean activate() {

return true;


}



@Override

public void execute() {

	System.out.println("Started");
int crafting = 89849900;

int child = 4;


int otherCrafting = 1370;//89784358;

int child2 = 38;//-1;


//clickWidget(crafting, child);

//sleep(1700);
System.out.println("Clicking");
clickWidget(otherCrafting, child2);
System.out.println("Going to wait");
System.out.println("JREBEL");
sleep(30000);
if((int)(Math.random()*2)==0){
	System.out.println("Going to antiban");
	antiban();
}
sleep(23000);
System.out.println("JREBEL");
}

private void antiban() {
	switch((int)(Math.random()*2)){
	case 0:
		System.out.println("AB: Changing camera angel");
		ctx.camera.angle((int) (Math.random()*360));
		
	case 1:
		System.out.println("AB: Changing camera pitch");
		ctx.camera.pitch((int)(Math.random()*100));
	
	case 2:
		System.out.println("Hovering xp");
		ctx.widgets.component(1213, 13).hover();
	}
		
	
}



void clickWidget(int a, int b){
if(ctx.widgets.component(a, b).visible()){
	System.out.println("Visible: " + ctx.widgets.component(a, b).visible());
	ctx.widgets.component(a,b).click(true);
}



}


}

class walkBack extends Task{

boolean upStairs = false;
boolean inFrontOfBank = false;

int[] stairs = {36774};


public walkBack(ClientContext arg0) {

super(arg0);

// TODO Auto-generated constructor stub

}



@Override

public boolean activate() {
	upStairs = false;
	inFrontOfBank = false;

	
// TODO Auto-generated method stub

return true;

}



@Override

public void execute() {

// Do we need this: ctx.objects = null; ?
if(!upStairs){
ctx.objects.select().id(stairs);

GameObject stair = ctx.objects.nearest().poll();

if(stair.inViewport()){

stair.interact("Climb-up");

sleep(3330);

}

else{
	ctx.camera.turnTo(stair);


sleep(213);
ctx.movement.newTilePath(stair.tile()).traverse();


sleep(343);
}



}
sleep(432);
if(!inFrontOfBank){

    ctx.camera.turnTo(new Tile(3208, 3219,2));
	ctx.mouse.move(new Tile(3208, 3219, 2).matrix(ctx).mapPoint());
	ctx.mouse.click(true);
//ctx.movement.step(ctx.movement.closestOnMap(new Tile(3208, 3219,2)));
sleep(1030);
}
Bank banking = new Bank(ctx);
ctx.objects.select().id(banking.BANK_BOOTH_IDS);
GameObject bank = ctx.objects.nearest().poll();
if(ctx.players.local().tile().floor()==2){
	upStairs=true;
}
if(bank.inViewport()){
	inFrontOfBank = true;
}
if(!upStairs||!inFrontOfBank){
	System.out.println("Redo Get Back");
	execute();
}
}

}
