package io.evolvio;

import processing.core.PFont;

import java.util.ArrayList;

class Board extends GlobalScope {
	int boardWidth;
	int boardHeight;
	int creatureMinimum;
	Tile[][] tiles;
	double year = 0;
	float MIN_TEMPERATURE;
	float MAX_TEMPERATURE;
	final float THERMOMETER_MIN = -2;
	final float THERMOMETER_MAX = 2;
	final int ROCKS_TO_ADD;
	final float MIN_ROCK_ENERGY_BASE = 0.8f;
	final float MAX_ROCK_ENERGY_BASE = 1.6f;
	final float MIN_CREATURE_ENERGY = 1.2f;
	final float MAX_CREATURE_ENERGY = 2.0f;
	final float ROCK_DENSITY = 5;
	final float OBJECT_TIMESTEPS_PER_YEAR = 100;
	final int ROCK_COLOR = color(0,0,0.5f);
	final int BACKGROUND_COLOR = color(0,0,0.1f);
	final float MINIMUM_SURVIVABLE_SIZE = 0.2f;
	ArrayList[][] softBodiesInPositions;
	ArrayList<SoftBody> rocks;
	ArrayList<Creature> creatures;
	Creature selectedCreature = null;
	int creatureIDUpTo = 0;
	float[] letterFrequencies = {8.167f,1.492f,2.782f,4.253f,12.702f,2.228f,2.015f,6.094f,6.966f,0.153f,0.772f,4.025f,2.406f,6.749f,
		7.507f,1.929f,0.095f,5.987f,6.327f,9.056f,2.758f,0.978f,2.361f,0.150f,1.974f,10000.0f};//0.074};
	final int LIST_SLOTS = 6;
	int creatureRankMetric = 0;
	int buttonColor = color(0.82f,0.8f,0.7f);
	Creature[] list = new Creature[LIST_SLOTS];
	final int creatureMinimumIncrement = 5;
	String folder = "TEST";
	int[] fileSaveCounts;
	double[] fileSaveTimes;
	double imageSaveInterval = 1;
	double textSaveInterval = 1;
	final double FLASH_SPEED = 80;
	boolean userControl;
	float temperature;
	double MANUAL_BIRTH_SIZE = 1.2;
	boolean wasPressingB = false;
	double timeStep;
	int POPULATION_HISTORY_LENGTH = 200;
	int[] populationHistory;
	double recordPopulationEvery = 0.02;
	int playSpeed = 1;

	public Board(int w, int h, float stepSize, float min, float max, int rta, int cm, int SEED, String INITIAL_FILE_NAME, double ts){
		noiseSeed(SEED);
		randomSeed(SEED);
		boardWidth = w;
		boardHeight = h;
		tiles = new Tile[w][h];
		for(int x = 0; x < boardWidth; x++){
			for(int y = 0; y < boardHeight; y++){
				float bigForce = pow(((float)y)/boardHeight,0.5f);
				float fertility = noise(x*stepSize*3,y*stepSize*3)*(1-bigForce)*5.0f+noise(x*stepSize*0.5f,y*stepSize*0.5f)*bigForce*5.0f-1.5f;
				float climateType = noise(x*stepSize+10000,y*stepSize+10000)*1.63f-0.4f;
				climateType = min(max(climateType,0),0.8f);
				tiles[x][y] = new Tile(x,y,fertility,0,climateType);
			}
		}
		MIN_TEMPERATURE = min;
		MAX_TEMPERATURE = max;

		softBodiesInPositions = new ArrayList[boardWidth][boardHeight];
		for(int x = 0; x < boardWidth; x++){
			for(int y = 0; y < boardHeight; y++){
				softBodiesInPositions[x][y] = new ArrayList<SoftBody>(0);
			}
		}

		ROCKS_TO_ADD = rta;
		rocks = new ArrayList<SoftBody>(0);
		for(int i = 0; i < ROCKS_TO_ADD; i++){
			rocks.add(new SoftBody(random(0,boardWidth),random(0,boardHeight),0,0,
				getRandomSize(),ROCK_DENSITY,hue(ROCK_COLOR),saturation(ROCK_COLOR),brightness(ROCK_COLOR),this,year));
		}

		creatureMinimum = cm;
		creatures = new ArrayList<Creature>(0);
		maintainCreatureMinimum(false);
		for(int i = 0; i < LIST_SLOTS; i++){
			list[i] = null;
		}
		folder = INITIAL_FILE_NAME;
		fileSaveCounts = new int[4];
		fileSaveTimes = new double[4];
		for(int i = 0; i < 4; i++){
			fileSaveCounts[i] = 0;
			fileSaveTimes[i] = -999;
		}
		userControl = true;
		timeStep = ts;
		populationHistory = new int[POPULATION_HISTORY_LENGTH];
		for(int i = 0; i < POPULATION_HISTORY_LENGTH; i++){
			populationHistory[i] = 0;
		}
	}
	public void drawBoard(float scaleUp, float camZoom, int mX, int mY){
		for(int x = 0; x < boardWidth; x++){
			for(int y = 0; y < boardHeight; y++){
				tiles[x][y].drawTile(scaleUp, (mX == x && mY == y));
			}
		}
		for(int i = 0; i < rocks.size(); i++){
			rocks.get(i).drawSoftBody(scaleUp);
		}
		for(int i = 0; i < creatures.size(); i++){
			creatures.get(i).drawSoftBody(scaleUp, camZoom,true);
		}
	}
	public void drawBlankBoard(float scaleUp){
		fill(BACKGROUND_COLOR);
		rect(0,0,scaleUp*boardWidth,scaleUp*boardHeight);
	}
	public void drawUI(float scaleUp,double timeStep, int x1, int y1, int x2, int y2, PFont font){
		fill(0,0,0);
		noStroke();
		rect(x1,y1,x2-x1,y2-y1);

		pushMatrix();
		translate(x1,y1);

		fill(0,0,1);
		textAlign(LEFT);
		textFont(font,48);
		String yearText = "Year "+nf((float)year,0,2);
		text(yearText,10,48);
		float seasonTextXCoor = textWidth(yearText)+50;
		textFont(font,24);
		text("Population: "+creatures.size(),10,80);
		String[] seasons = {"Winter","Spring","Summer","Autumn"};
		text(seasons[(int)(getSeason()*4)],seasonTextXCoor,30);

		if(selectedCreature == null){
			for(int i = 0; i < LIST_SLOTS; i++){
				list[i] = null;
			}
			for(int i = 0; i < creatures.size(); i++){
				int lookingAt = 0;
				if(creatureRankMetric == 4){
					while(lookingAt < LIST_SLOTS && list[lookingAt] != null && list[lookingAt].name.compareTo(creatures.get(i).name) < 0){
						lookingAt++;
					}
				}else if(creatureRankMetric == 5){
					while(lookingAt < LIST_SLOTS && list[lookingAt] != null && list[lookingAt].name.compareTo(creatures.get(i).name) >= 0){
						lookingAt++;
					}
				}else{
					while(lookingAt < LIST_SLOTS && list[lookingAt] != null && list[lookingAt].measure(creatureRankMetric) > creatures.get(i).measure(creatureRankMetric)){
						lookingAt++;
					}
				}
				if(lookingAt < LIST_SLOTS){
					for(int j = LIST_SLOTS-1; j >= lookingAt+1; j--){
						list[j] = list[j-1];
					}
					list[lookingAt] = creatures.get(i);
				}
			}
			double maxEnergy = 0;
			for(int i = 0; i < LIST_SLOTS; i++){
				if(list[i] != null && list[i].energy > maxEnergy){
					maxEnergy = list[i].energy;
				}
			}
			for(int i = 0; i < LIST_SLOTS; i++){
				if(list[i] != null){
					list[i].preferredRank += (i-list[i].preferredRank)*0.4;
					float y = y1+175+70*list[i].preferredRank;
					drawCreature(list[i],45,y+5,0.7f,scaleUp);
					textFont(font, 24);
					textAlign(LEFT);
					noStroke();
					fill(0.333f,1,0.4f);
					float multi = (x2-x1-200);
					if(list[i].energy > 0){
						rect(85,y+5,(float)(multi*list[i].energy/maxEnergy),25);
					}
					if(list[i].energy > 1){
						fill(0.333f,1,0.8f);
						rect(85+(float)(multi/maxEnergy),y+5,(float)(multi*(list[i].energy-1)/maxEnergy),25);
					}
					fill(0,0,1);
					text(list[i].getCreatureName()+" ["+list[i].id+"] ("+toAge(list[i].birthTime)+")",90,y);
					text("Energy: "+nf(100*(float)(list[i].energy),0,2),90,y+25);
				}
			}
			noStroke();
			fill(buttonColor);
			rect(10,95,220,40);
			rect(240,95,220,40);
			fill(0,0,1);
			textAlign(CENTER);
			text("Reset zoom",120,123);
			String[] sorts = {"Biggest","Smallest","Youngest","Oldest","A to Z","Z to A","Highest Gen","Lowest Gen"};
			text("Sort by: "+sorts[creatureRankMetric],350,123);

			textFont(font,19);
			String[] buttonTexts = {"Brain Control","Maintain pop. at "+creatureMinimum,
				"Screenshot now","-   Image every "+nf((float)imageSaveInterval,0,2)+" years   +",
				"Text file now","-    Text every "+nf((float)textSaveInterval,0,2)+" years    +",
				"-    Play Speed ("+playSpeed+"x)    +","This button does nothing"};
			if(userControl){
				buttonTexts[0] = "Keyboard Control";
			}
			for(int i = 0; i < 8; i++){
				float x = (i%2)*230+10;
				float y = floor(i/2)*50+570;
				fill(buttonColor);
				rect(x,y,220,40);
				if(i >= 2 && i < 6){
					double flashAlpha = 1.0*Math.pow(0.5,(year-fileSaveTimes[i-2])*FLASH_SPEED);
					fill(0,0,1,(float)flashAlpha);
					rect(x,y,220,40);
				}
				fill(0,0,1,1);
				text(buttonTexts[i],x+110,y+17);
				if(i == 0){
				}else if(i == 1){
					text("-"+creatureMinimumIncrement+
						"                    +"+creatureMinimumIncrement,x+110,y+37);
				}else if(i <= 5){
					text(getNextFileName(i-2),x+110,y+37);
				}
			}
		}else{
			float energyUsage = (float)selectedCreature.getEnergyUsage(timeStep);
			noStroke();
			if(energyUsage <= 0){
				fill(0,1,0.5f);
			}else{
				fill(0.33f,1,0.4f);
			}
			float EUbar = 6*energyUsage;
			rect(110,280,min(max(EUbar,-110),110),25);
			if(EUbar < -110){
				rect(0,280,25,(-110-EUbar)*20+25);
			}else if(EUbar > 110){
				float h = (EUbar-110)*20+25;
				rect(185,280-h,25,h);
			}
			fill(0,0,1);
			text("Name: "+selectedCreature.getCreatureName(),10,225);
			text("Energy: "+nf(100*(float)selectedCreature.energy,0,2)+" yums",10,250);
			text("E Change: "+nf(100*energyUsage,0,2)+" yums/year",10,275);

			text("ID: "+selectedCreature.id,10,325);
			text("X: "+nf((float)selectedCreature.px,0,2),10,350);
			text("Y: "+nf((float)selectedCreature.py,0,2),10,375);
			text("Rotation: "+nf((float)selectedCreature.rotation,0,2),10,400);
			text("B-day: "+toDate(selectedCreature.birthTime),10,425);
			text("("+toAge(selectedCreature.birthTime)+")",10,450);
			text("Generation: "+selectedCreature.gen,10,475);
			text("Parents: "+selectedCreature.parents,10,500,210,255);
			text("Hue: "+nf((float)(selectedCreature.hue),0,2),10,550,210,255);
			text("Mouth hue: "+nf((float)(selectedCreature.mouthHue),0,2),10,575,210,255);

			if(userControl){
				text("Controls:\nUp/Down: Move\nLeft/Right: Rotate\nSpace: Eat\nF: Fight\nV: Vomit\nU,J: Change color"+
					"\nI,K: Change mouth color\nB: Give birth (Not possible if under "+Math.round((MANUAL_BIRTH_SIZE+1)*100)+" yums)",10,625,250,400);
			}
			pushMatrix();
			translate(400,80);
			float apX = round((mouseX()-264-x1)/26.0f);
			float apY = round((mouseY()-80-y1)/26.0f);
			selectedCreature.drawBrain(font,52,(int)apX,(int)apY);
			popMatrix();
		}
		drawPopulationGraph(x1,x2,y1,y2);
		fill(0,0,0);
		textAlign(RIGHT);
		textFont(font,24);
		text("Population: "+creatures.size(),x2-x1-10,y2-y1-10);
		popMatrix();

		pushMatrix();
		translate(x2,y1);
		textAlign(RIGHT);
		textFont(font,24);
		text("Temperature",-10,24);
		drawThermometer(-45,30,20,660,temperature,THERMOMETER_MIN,THERMOMETER_MAX,color(0,1,1));
		popMatrix();

		if(selectedCreature != null){
			drawCreature(selectedCreature,x1+65,y1+147,0.7f,scaleUp);
		}
	}
	void drawPopulationGraph(float x1, float x2, float y1, float y2){
		float barWidth = (x2-x1)/((float)(POPULATION_HISTORY_LENGTH));
		noStroke();
		fill(0.33333f,1,0.6f);
		int maxPopulation = 0;
		for(int i = 0; i < POPULATION_HISTORY_LENGTH; i++){
			if(populationHistory[i] > maxPopulation){
				maxPopulation = populationHistory[i];
			}
		}
		for(int i = 0; i < POPULATION_HISTORY_LENGTH; i++){
			float h = (((float)populationHistory[i])/maxPopulation)*(y2-770);
			rect((POPULATION_HISTORY_LENGTH-1-i)*barWidth,y2-h,barWidth,h);
		}
	}
	String getNextFileName(int type){
		String[] modes = {"manualImgs","autoImgs","manualTexts","autoTexts"};
		String ending = ".png";
		if(type >= 2){
			ending = ".txt";
		}
		return folder+"/"+modes[type]+"/"+nf(fileSaveCounts[type],5)+ending;
	}
	public void iterate(double timeStep){
		double prevYear = year;
		year += timeStep;
		if(Math.floor(year/recordPopulationEvery) != Math.floor(prevYear/recordPopulationEvery)){
			for(int i = POPULATION_HISTORY_LENGTH-1; i >= 1; i--){
				populationHistory[i] = populationHistory[i-1];
			}
			populationHistory[0] = creatures.size();
		}
		temperature = getGrowableTime();
		for(int x = 0; x < boardWidth; x++){
			for(int y = 0; y < boardHeight; y++){
				tiles[x][y].iterate(timeStep,getGrowableTime());
			}
		}
		for(int i = 0; i < creatures.size(); i++){
			creatures.get(i).setPreviousEnergy();
		}
		for(int i = 0; i < rocks.size(); i++){
			rocks.get(i).collide(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
		}
		maintainCreatureMinimum(false);
		for(int i = 0; i < creatures.size(); i++){
			Creature me = creatures.get(i);
			me.collide(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
			me.metabolize(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
			if(userControl){
				if(me == selectedCreature){
					if(keyPressed()){
						if (key() == CODED) {
							if (keyCode() == UP) me.accelerate(0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if (keyCode() == DOWN) me.accelerate(-0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if (keyCode() == LEFT) me.turn(-0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if (keyCode() == RIGHT) me.turn(0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
						}else{
							if(key() == ' ') me.eat(0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if(key() == 'v') me.eat(-0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if(key() == 'f')  me.fight(0.3,timeStep*OBJECT_TIMESTEPS_PER_YEAR);
							if(key() == 'u') me.setHue(me.hue+0.02);
							if(key() == 'j') me.setHue(me.hue-0.02);

							if(key() == 'i') me.setMouthHue(me.mouthHue+0.02);
							if(key() == 'k') me.setMouthHue(me.mouthHue-0.02);
              /*if(key == 'i') me.setSaturarion(me.saturation+0.05);
              if(key == 'k') me.setSaturarion(me.saturation-0.05);
              if(key == 'o') me.setBrightness(me.brightness+0.05);
              if(key == 'l') me.setBrightness(me.brightness-0.05);


              if(key == 'w') me.setVisionDistance(me.visionDistance+0.05);
              if(key == 's') me.setVisionDistance(me.visionDistance-0.05);
              if(key == 'a') me.setVisionAngle(me.visionAngle-0.05);
              if(key == 'd') me.setVisionAngle(me.visionAngle+0.05);*/
							if(key() == 'b'){
								if(!wasPressingB){
									me.reproduce(MANUAL_BIRTH_SIZE, timeStep);
								}
								wasPressingB = true;
							}else{
								wasPressingB = false;
							}
						}
					}
				}
			}
			me.useBrain(timeStep*OBJECT_TIMESTEPS_PER_YEAR, !userControl);
			if(me.getRadius() < MINIMUM_SURVIVABLE_SIZE){
				me.returnToEarth();
				creatures.remove(me);
				i--;
			}
		}
		for(int i = 0; i < rocks.size(); i++){
			rocks.get(i).applyMotions(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
		}
		for(int i = 0; i < creatures.size(); i++){
			creatures.get(i).applyMotions(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
			creatures.get(i).see(timeStep*OBJECT_TIMESTEPS_PER_YEAR);
		}
		if(Math.floor(fileSaveTimes[1]/imageSaveInterval) != Math.floor(year/imageSaveInterval)){
			prepareForFileSave(1);
		}
		if(Math.floor(fileSaveTimes[3]/textSaveInterval) != Math.floor(year/textSaveInterval)){
			prepareForFileSave(3);
		}
	}
	private float getGrowableTime(){
		float temperatureRange = MAX_TEMPERATURE-MIN_TEMPERATURE;
		return MIN_TEMPERATURE+temperatureRange*0.5f-temperatureRange*0.5f*cos((float)(getSeason()*2*PI));
	}
	private double getSeason(){
		return (year%1.0);
	}
	private void drawThermometer(float x1, float y1, float w, float h, float prog, float min, float max,
								 int fillColor){
		noStroke();
		fill(0,0,0.2f);
		rect(x1,y1,w,h);
		fill(fillColor);
		float proportionFilled = (prog-min)/(max-min);
		rect(x1,y1+h*(1-proportionFilled),w,proportionFilled*h);


		float zeroHeight = (0-min)/(max-min);
		float zeroLineY = y1+h*(1-zeroHeight);
		textAlign(RIGHT);
		stroke(0,0,1);
		strokeWeight(3);
		line(x1,zeroLineY,x1+w,zeroLineY);
		float minY = y1+h*(1-(MIN_TEMPERATURE-min)/(max-min));
		float maxY = y1+h*(1-(MAX_TEMPERATURE-min)/(max-min));
		fill(0,0,0.8f);
		line(x1,minY,x1+w*1.8f,minY);
		line(x1,maxY,x1+w*1.8f,maxY);
		line(x1+w*1.8f,minY,x1+w*1.8f,maxY);

		fill(0,0,1);
		text("Zero",x1-5,zeroLineY+8);
		text(nf(MIN_TEMPERATURE,0,2),x1-5,minY+8);
		text(nf(MAX_TEMPERATURE,0,2),x1-5,maxY+8);
	}
	private void drawVerticalSlider(float x1, float y1, float w, float h, float prog, int fillColor, int antiColor){
		noStroke();
		fill(0,0,0.2f);
		rect(x1,y1,w,h);
		if(prog >= 0){
			fill(fillColor);
		}else{
			fill(antiColor);
		}
		rect(x1,y1+h*(1-prog),w,prog*h);
	}
	boolean setMinTemperature(float temp){
		MIN_TEMPERATURE = tempBounds(THERMOMETER_MIN+temp*(THERMOMETER_MAX-THERMOMETER_MIN));
		if(MIN_TEMPERATURE > MAX_TEMPERATURE){
			float placeHolder = MAX_TEMPERATURE;
			MAX_TEMPERATURE = MIN_TEMPERATURE;
			MIN_TEMPERATURE = placeHolder;
			return true;
		}
		return false;
	}
	boolean setMaxTemperature(float temp){
		MAX_TEMPERATURE = tempBounds(THERMOMETER_MIN+temp*(THERMOMETER_MAX-THERMOMETER_MIN));
		if(MIN_TEMPERATURE > MAX_TEMPERATURE){
			float placeHolder = MAX_TEMPERATURE;
			MAX_TEMPERATURE = MIN_TEMPERATURE;
			MIN_TEMPERATURE = placeHolder;
			return true;
		}
		return false;
	}
	private float tempBounds(float temp){
		return min(max(temp,THERMOMETER_MIN),THERMOMETER_MAX);
	}
	float getHighTempProportion(){
		return (MAX_TEMPERATURE-THERMOMETER_MIN)/(THERMOMETER_MAX-THERMOMETER_MIN);
	}
	float getLowTempProportion(){
		return (MIN_TEMPERATURE-THERMOMETER_MIN)/(THERMOMETER_MAX-THERMOMETER_MIN);
	}
	private String toDate(double d){
		return "Year "+nf((float)(d),0,2);
	}
	private String toAge(double d){
		return nf((float)(year-d),0,2)+" yrs old";
	}
	private void maintainCreatureMinimum(boolean choosePreexisting){
		while(creatures.size() < creatureMinimum){
			if(choosePreexisting){
				Creature c = getRandomCreature();
				c.addEnergy(c.SAFE_SIZE);
				c.reproduce(c.SAFE_SIZE, timeStep);
			}else{
				creatures.add(new Creature(random(0,boardWidth),random(0,boardHeight),0,0,
					random(MIN_CREATURE_ENERGY,MAX_CREATURE_ENERGY),1,random(0,1),1,1,
					this,year,random(0,2*PI),0,"","[PRIMORDIAL]",true,null,null,1,random(0,1)));
			}
		}
	}
	private Creature getRandomCreature(){
		int index = (int)(random(0,creatures.size()));
		return creatures.get(index);
	}
	private double getRandomSize(){
		return pow(random(MIN_ROCK_ENERGY_BASE,MAX_ROCK_ENERGY_BASE),4);
	}
	private void drawCreature(Creature c, float x, float y, float scale, float scaleUp){
		pushMatrix();
		float scaleIconUp = scaleUp*scale;
		translate((float)(-c.px*scaleIconUp),(float)(-c.py*scaleIconUp));
		translate(x,y);
		c.drawSoftBody(scaleIconUp, 40.0f/scale,false);
		popMatrix();
	}
	void prepareForFileSave(int type){
		fileSaveTimes[type] = -999999;
	}
	void fileSave(){
		for(int i = 0; i < 4; i++){
			if(fileSaveTimes[i] < -99999){
				fileSaveTimes[i] = year;
				if(i < 2){
					saveFrame(getNextFileName(i));
				}else{
					String[] data = this.toBigString();
					saveStrings(getNextFileName(i),data);
				}
				fileSaveCounts[i]++;
			}
		}
	}
	public String[] toBigString(){
		String[] placeholder = {"Goo goo","Ga ga"};
		return placeholder;
	}
	public void unselect(){
		selectedCreature = null;
	}
}
