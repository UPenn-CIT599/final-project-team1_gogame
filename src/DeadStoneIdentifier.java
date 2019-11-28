/**
 * @author Chuan
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Color;

public class DeadStoneIdentifier {
	
	/**
	 * The dead stones will be identified, with modifications, according to an algorithm found online:
	 * https://www.uni-trier.de/fileadmin/fb4/prof/BWL/FIN/Veranstaltungen/A_static_method_for_computing_the_score_of_a_Go_game__Carta_.pdf
	 * 1. Group tangibly connected stones into chains
	 * 2. Group chains into logically connected chain groups
	 * 3. Calculate logical chain properties:
	 * eyes
	 * territory
	 * 4. Identify dead logical chains based on their properties:
	 * If a logical chain has two eyes, it is alive;
	 * If a logical chain does not have two eyes but possess a large territory
	 * (defined as 6 points), it is also alive;
	 * All other logical chains are identified as dead.
	 */
	
	private HashMap<Integer, HashSet<Point>> chainGroups = new HashMap<>();
	private HashMap<String, HashSet<Point>> preliminaryLogicalChainGroups = new HashMap<>();
	private HashMap<String, HashSet<Point>> finalLogicalChainGroups = new HashMap<>();
	private HashMap<String, HashSet<Point>> eyesOfLogicalChains = new HashMap<>();
	private HashMap<String, HashSet<Point>> territoryOfLogicalChains = new HashMap<>();
	private HashMap<String, String> statusOfLogicalChains = new HashMap<>();
	private HashSet<Integer[]> deadBlackStonePositions = new HashSet<>();
	private HashSet<Integer[]> deadWhiteStonePositions = new HashSet<>();
	private Point[][] boardPoint;
	private int size;
	
	public DeadStoneIdentifier(Score s){
		boardPoint = s.getFinalPositions();
		size = s.getSize();
	}
	
	/**
	 * This method groups tangibly connected stones into chains and 
	 * assigns a chain ID to each group. 
	 */ 
	public void groupStonesToChains() {
		int chainNo = 0;
		for (int x = 0; x < size; x++) {
			if (x == 0) {
				for (int y = 0; y < size; y++) {
					if (boardPoint[x][y].getStatus().contains("b") || boardPoint[x][y].getStatus().contains("w")) {
						if (y == 0) {
							HashSet<Point> chain = new HashSet<>();
	    					chainNo++;
	    					chain.add(boardPoint[x][y]);
	    					chainGroups.put(chainNo, chain);
	    					boardPoint[x][y].setChainGroup(chainNo);
						} else {
							if (boardPoint[x][y-1].getStatus().equals(boardPoint[x][y].getStatus())) {
								chainGroups.get(boardPoint[x][y-1].getChainGroup()).add(boardPoint[x][y]);
								boardPoint[x][y].setChainGroup(boardPoint[x][y-1].getChainGroup());
		    				} else {
		    					HashSet<Point> chain = new HashSet<>();
		    					chainNo++;
		    					chain.add(boardPoint[x][y]);
		    					chainGroups.put(chainNo, chain);
		    					boardPoint[x][y].setChainGroup(chainNo);
		    				}
						}
					} else {
						boardPoint[x][y].setChainGroup(-1);
						boardPoint[x][y].setLogicalChainGroup("");
					}
				}
			} else {
				for (int y = 0; y < size; y++) {
					if (boardPoint[x][y].getStatus().contains("b") || boardPoint[x][y].getStatus().contains("w")) {
						if (y == 0) {
							if (boardPoint[x-1][y].getStatus().equals(boardPoint[x][y].getStatus())) {
								chainGroups.get(boardPoint[x-1][y].getChainGroup()).add(boardPoint[x][y]);
								boardPoint[x][y].setChainGroup(boardPoint[x-1][y].getChainGroup());
		    				} else {
		    					HashSet<Point> chain = new HashSet<>();
		    					chainNo++;
		    					chain.add(boardPoint[x][y]);
		    					chainGroups.put(chainNo, chain);
		    					boardPoint[x][y].setChainGroup(chainNo);
		    				}
						} else {
							if (boardPoint[x-1][y].getStatus().equals(boardPoint[x][y].getStatus()) || 
									boardPoint[x][y-1].getStatus().equals(boardPoint[x][y].getStatus())) {
								if (boardPoint[x-1][y].getStatus().equals(boardPoint[x][y].getStatus())) {
									chainGroups.get(boardPoint[x-1][y].getChainGroup()).add(boardPoint[x][y]);
									boardPoint[x][y].setChainGroup(boardPoint[x-1][y].getChainGroup());
								} else if (boardPoint[x][y-1].getStatus().equals(boardPoint[x][y].getStatus())) {
									chainGroups.get(boardPoint[x][y-1].getChainGroup()).add(boardPoint[x][y]);
									boardPoint[x][y].setChainGroup(boardPoint[x][y-1].getChainGroup());
								}
		    				} else {
		    					HashSet<Point> chain = new HashSet<>();
		    					chainNo++;
		    					chain.add(boardPoint[x][y]);
		    					chainGroups.put(chainNo, chain);
		    					boardPoint[x][y].setChainGroup(chainNo);
		    				}
						}				
					} else {
						boardPoint[x][y].setChainGroup(-1);
						boardPoint[x][y].setLogicalChainGroup("");
					}
				}
			}
		}
	}
	
	/**
	 * This method groups chains into logically connected chain groups and
	 * assigns logical chain IDs.
	 */
	public void groupChainsByLogicalConnections() {
		HashMap<Integer, HashSet<Point>> reachGroups = new HashMap<>();
		for (int i = 1; i <= chainGroups.size(); i++) {
			ArrayList<HashSet<Point>> chainAndReach = ScoreHelper.getChainAndFindReach(chainGroups.get(i).iterator().next(), size, boardPoint);
			HashSet<Point> reach = chainAndReach.get(1);
			reachGroups.put(i, reach);
		}
		ArrayList<HashSet<Integer>> chainGroupRecord = new ArrayList<>();
		for (int i = 1; i <= chainGroups.size(); i++) {
			boolean findLogicalConnection = false;
			for (int j = 1; j <= chainGroups.size(); j++) {
				if (i != j) {
					int count = 0;
					for (Point adjacent : reachGroups.get(i)) {
						if (adjacent.getStatus().contains("e") && reachGroups.get(j).contains(adjacent)) {
							count++;
							if (count > 1) {
								findLogicalConnection = true;
								HashSet<Point> logicalChain = chainGroups.get(i);
								logicalChain.addAll(chainGroups.get(j));
								if (!preliminaryLogicalChainGroups.containsValue(logicalChain)) {
									String logicalChainId = "";
									logicalChainId += Integer.toString(i);
									logicalChainId += ",";
									logicalChainId += Integer.toString(j);
									preliminaryLogicalChainGroups.put(logicalChainId, logicalChain);
									HashSet<Integer> logicalConnections = new HashSet<>();
									logicalConnections.add(i);
									logicalConnections.add(j);
									chainGroupRecord.add(logicalConnections);
								} 
								break;
							}
						} 
					}
				}
			}
			if (!findLogicalConnection) {
				HashSet<Point> logicalChain = chainGroups.get(i);
				String logicalChainId = "";
				logicalChainId += Integer.toString(i);
				logicalChainId += ",";
				preliminaryLogicalChainGroups.put(logicalChainId, logicalChain);
				HashSet<Integer> singleChain = new HashSet<>();
				singleChain.add(i);
				chainGroupRecord.add(singleChain);
			}
		}
		
		ArrayList<HashSet<Integer>> chainGroupRecordFinal = Helper.findCommonElement(chainGroupRecord); 
		for (HashSet<Integer> hs : chainGroupRecordFinal) {
			HashSet<Point> logicalChain = new HashSet<>();
			String logicalChainId = "";
			for (Integer i : hs) {
				logicalChain.addAll(chainGroups.get(i));
				logicalChainId += Integer.toString(i);
				logicalChainId += ",";
			}
			finalLogicalChainGroups.put(logicalChainId, logicalChain);
		}
		//set logical chain ID
		for (String s : finalLogicalChainGroups.keySet()) {
			for (Point p : finalLogicalChainGroups.get(s)) {
				p.setLogicalChainGroup(s);
			}
		}		
		for (int y = 0; y < size; y++) {
			System.out.println("");
			for (int x = 0; x < size; x++) {
				System.out.print(boardPoint[x][y].getLogicalChainGroup() + "\t");
			}
		}
	}
	
	/**
	 * Given an empty point and a logical chain ID, 
	 * this method identifies if the empty point is an eye that belongs to the logical chain.
	 * @param emptyPoint
	 * @param logicalChainId
	 * @return emptyPointProperty
	 */
	public String emptyPointProperty(Point emptyPoint, String logicalChainId) {
		String emptyPointProperty = "";
		int eyeCount = 0;
		if (emptyPoint.getxPosition() > 0) {
			if (boardPoint[emptyPoint.getxPosition() - 1][emptyPoint.getyPosition()].getLogicalChainGroup().equals(logicalChainId)) {
				eyeCount++;
			}
		} else if (emptyPoint.getxPosition() == 0) {
			eyeCount++;
		}
		if (emptyPoint.getxPosition() < size - 1) {
			if (boardPoint[emptyPoint.getxPosition() + 1][emptyPoint.getyPosition()].getLogicalChainGroup().equals(logicalChainId)) {
				eyeCount++;
			}
		} else if (emptyPoint.getxPosition() == size - 1) {
			eyeCount++;
		}
		if (emptyPoint.getyPosition() > 0) {
			if (boardPoint[emptyPoint.getxPosition()][emptyPoint.getyPosition() - 1].getLogicalChainGroup().equals(logicalChainId)) {
				eyeCount++;
			}
		} else if (emptyPoint.getyPosition() == 0) {
			eyeCount++;
		}
		if (emptyPoint.getyPosition() < size - 1) {
			if (boardPoint[emptyPoint.getxPosition()][emptyPoint.getyPosition() + 1].getLogicalChainGroup().equals(logicalChainId)) {
				eyeCount++;
			}
		} else if (emptyPoint.getyPosition() == size - 1) {
			eyeCount++;
		}
		
		if (eyeCount == 4) {
			emptyPointProperty = "eye";
		} 
		return emptyPointProperty;
	}
	
	/**
	 * This method searches each logical chain for any eyes that it possesses. 
	 */
	public void searchEyes() {
		for (String s : finalLogicalChainGroups.keySet()) {
			HashSet<Point> eyes = new HashSet<>();
			ArrayList<HashSet<Point>> chainAndReach = new ArrayList<>();
			String[] chainNo = s.split(",");
			for (int i = 0; i < chainNo.length; i++) {
				if (!chainNo[i].equals("")) {
					chainAndReach = ScoreHelper.getChainAndFindReach(chainGroups.get(Integer.parseInt(chainNo[i])).iterator().next(), size, boardPoint);
					HashSet<Point> reach = chainAndReach.get(1);
					for (Point p : reach) {
						if (p.getStatus().equals("e")) {
							if (emptyPointProperty(p, s).equals("eye")) {
								eyes.add(p);
							}
						}
					}
				}
			}
			eyesOfLogicalChains.put(s, eyes);
		}
		
	}
	
	/**
	 * This method calculates the influence of every logical chain on the board
	 * according to the Bouzy algorithm.
	 * @param dilations, erosions - parameters of the Bouzy algorithm
	 * @return pointValue - positive (negative) value indicates white (black) territory 
	 * or white (black) stone
	 */
	public int[][] influenceOfLogicalChain(int dilations, int erosions) {
		int[][] pointValue = new int[size+2][size+2];
		int[][] pointValueWorking = new int[size+2][size+2];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (boardPoint[x][y].getStatus().equals("b")) {
					pointValue[x+1][y+1] = -128;
				} else if (boardPoint[x][y].getStatus().equals("w")) {
					pointValue[x+1][y+1] = 128;
				} else {
					pointValue[x+1][y+1] = 0;
				}
			}
		}
		for (int x = 0; x < size+2; x++) {
			pointValue[x][0] = 12345;
			pointValue[x][size+1] = 12345;
		}
		for (int y = 0; y < size+2; y++) {
			pointValue[0][y] = 12345;
			pointValue[size+1][y] = 12345;
		}
		pointValueWorking = Helper.setPointValue(pointValue, size+2);
		for (int i = 0; i < dilations; i++) {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (!Helper.checkOnBoard(pointValue[x][y])) {
						continue;
					}
					if (pointValue[x][y] >= 0 
							&& (!Helper.checkOnBoard(pointValue[x+1][y]) || pointValue[x+1][y] >= 0) 
							&& (!Helper.checkOnBoard(pointValue[x-1][y]) || pointValue[x-1][y] >= 0) 
							&& (!Helper.checkOnBoard(pointValue[x][y+1]) || pointValue[x][y+1] >= 0) 
							&& (!Helper.checkOnBoard(pointValue[x][y-1]) || pointValue[x][y-1] >= 0)) {
						if (Helper.checkOnBoard(pointValue[x+1][y]) && pointValue[x+1][y] > 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x-1][y]) && pointValue[x-1][y] > 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x][y+1]) && pointValue[x][y+1] > 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x][y-1]) && pointValue[x][y-1] > 0) {
							pointValueWorking[x][y]++;
						}
					}
					if (pointValue[x][y] <= 0 
							&& (!Helper.checkOnBoard(pointValue[x+1][y]) || pointValue[x+1][y] <= 0) 
							&& (!Helper.checkOnBoard(pointValue[x-1][y]) || pointValue[x-1][y] <= 0) 
							&& (!Helper.checkOnBoard(pointValue[x][y+1]) || pointValue[x][y+1] <= 0) 
							&& (!Helper.checkOnBoard(pointValue[x][y-1]) || pointValue[x][y-1] <= 0)) {
						if (Helper.checkOnBoard(pointValue[x+1][y]) && pointValue[x+1][y] < 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x-1][y]) && pointValue[x-1][y] < 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x][y+1]) && pointValue[x][y+1] < 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x][y-1]) && pointValue[x][y-1] < 0) {
							pointValueWorking[x][y]--;
						}
						
					}
				}
			}
			pointValue = Helper.setPointValue(pointValueWorking, size+2);
		}
		
		for (int i = 0; i < erosions; i++) {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (!Helper.checkOnBoard(pointValue[x][y])) {
						continue;
					}
					if (pointValueWorking[x][y] > 0) {
						if (Helper.checkOnBoard(pointValue[x+1][y]) && pointValue[x+1][y] <= 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x-1][y]) && pointValue[x-1][y] <= 0 && pointValueWorking[x][y] > 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x][y+1]) && pointValue[x][y+1] <= 0 && pointValueWorking[x][y] > 0) {
							pointValueWorking[x][y]--;
						}
						if (Helper.checkOnBoard(pointValue[x][y-1]) && pointValue[x][y-1] <= 0 && pointValueWorking[x][y] > 0) {
							pointValueWorking[x][y]--;
						}
					}
					if (pointValueWorking[x][y] < 0) {
						if (Helper.checkOnBoard(pointValue[x+1][y]) && pointValue[x+1][y] >= 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x-1][y]) && pointValue[x-1][y] >= 0 && pointValueWorking[x][y] < 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x][y+1]) && pointValue[x][y+1] >= 0 && pointValueWorking[x][y] < 0) {
							pointValueWorking[x][y]++;
						}
						if (Helper.checkOnBoard(pointValue[x][y-1]) && pointValue[x][y-1] >= 0 && pointValueWorking[x][y] < 0) {
							pointValueWorking[x][y]++;
						}
					}
				}
			}
			pointValue = Helper.setPointValue(pointValueWorking, size+2);
		}
		
		/*
		for (int y = 0; y < size+2; y++) {
			System.out.println("");
			for (int x = 0; x < size+2; x++) {
				System.out.print(pointValue[x][y] + "\t");
			}
		}
		*/
		
		return pointValue;
	}
	
	/**
	 * This method looks for the boundary of the influence of each logical chain.
	 * The number of empty points within the boundary is the territory of this logical chain.
	 * @param p - any stone of a logical chain 
	 * @param size - size of the board
	 * @param pointValue - positive (negative) value indicates white (black) territory or white (black) stone
	 * @return influence - all the empty points that belong to a logical chain and all the stones of this logical chain
	 */
	public HashSet<Point> getInfluence(Point p, int size, int[][] pointValue) {
		for (int x = 1; x <= size; x++) {
			for (int y = 1; y <= size; y++) {
				boardPoint[x-1][y-1].setValue(pointValue[x][y]);
			}
		}
    	ArrayList<Point> frontier = new ArrayList<>();
    	frontier.add(p);
    	HashSet<Point> influence = new HashSet<>(); 
    	influence.add(p);
    	while (frontier.size() > 0) {
    		Point currentPoint = frontier.get(frontier.size()-1);
    		frontier.remove(frontier.size()-1);
    		influence.add(currentPoint);
    		for (Point neighbor : Helper.getAdjacentPoints(currentPoint, size, boardPoint)) {
    			int neighborValue = boardPoint[neighbor.getxPosition()][neighbor.getyPosition()].getValue();
    			if ((((neighborValue > 0) && (currentPoint.getValue() > 0)) || ((neighborValue < 0) && (currentPoint.getValue() < 0)))
    					&& (!influence.contains(neighbor))) {
    				frontier.add(neighbor);
    			}
    		}
    	}
    	return influence;
    }
	
	/**
	 * This method searches each logical chain for the territory under its influence.
	 */
	public void computeTerritoryUnderInfluence() {
		for (String s : finalLogicalChainGroups.keySet()) {
			HashSet<Point> territory = new HashSet<>();
			int[][] pointValue = influenceOfLogicalChain(8, 21);
			territory = getInfluence(finalLogicalChainGroups.get(s).iterator().next(), size, pointValue);
			territory.removeAll(finalLogicalChainGroups.get(s));
			territoryOfLogicalChains.put(s, territory);
		}
	}
	
	
	/**
	 * This method identifies dead logical chains based on their properties.
	 * All the stones of a dead logical chain are considered dead.
	 */ 
	public void identifyDeadStones(){
		for (String s : finalLogicalChainGroups.keySet()) {
			if (eyesOfLogicalChains.get(s).size() >= 2) {
				statusOfLogicalChains.put(s, "alive");
			} else {
				if (territoryOfLogicalChains.get(s).size() >= 6) {
					statusOfLogicalChains.put(s, "alive");
				} else {
					statusOfLogicalChains.put(s, "dead");
				}
			}
		}
		for (String s : statusOfLogicalChains.keySet()) {
			if (statusOfLogicalChains.get(s).equals("dead")) {
				if (finalLogicalChainGroups.get(s).iterator().next().getStatus().contains("b")) {
					deadBlackStonePositions.addAll(Helper.getDeadStonePositions(finalLogicalChainGroups.get(s)));
					
				} else if (finalLogicalChainGroups.get(s).iterator().next().getStatus().contains("w")) {
					deadWhiteStonePositions.addAll(Helper.getDeadStonePositions(finalLogicalChainGroups.get(s)));
				}
				
			}
		}
	}
	
	/**
	 * This method groups all the dead black stones and dead white stones.
	 * @return deadStones
	 */
	public ArrayList<HashSet<DeadStone>> getDeadStones(){
		ArrayList<HashSet<DeadStone>> deadStones = new ArrayList<>();
		HashSet<DeadStone> deadBlackStones = new HashSet<>();
		HashSet<DeadStone> deadWhiteStones = new HashSet<>();
		for (Integer[] position : deadBlackStonePositions) {
			DeadStone ds = new DeadStone(Color.BLACK, position[0], position[1]);
			deadBlackStones.add(ds);
		}
		for (Integer[] position : deadWhiteStonePositions) {
			DeadStone ds = new DeadStone(Color.WHITE, position[0], position[1]);
			deadWhiteStones.add(ds);
		}
		deadStones.add(deadBlackStones);
		deadStones.add(deadWhiteStones);
		return deadStones;
	}
	
	//This method is for debugging.
	public void testPrint() {
		System.out.println("chain group No: " + chainGroups.keySet());
		System.out.println("");
		System.out.println("logical group ID: " + finalLogicalChainGroups.keySet());
		System.out.println("");
		System.out.println("eyes: ");
		for (String s : eyesOfLogicalChains.keySet()) {
			System.out.println(s + ": " + eyesOfLogicalChains.get(s).size());
		}
		System.out.println("");
		System.out.println("territory: ");
		for (String s : territoryOfLogicalChains.keySet()) {
			System.out.println(s + ": " + territoryOfLogicalChains.get(s).size());
		}
		System.out.println("");
		System.out.println("live dead status: ");
		for (String s : statusOfLogicalChains.keySet()) {
			System.out.println(s + ": " + statusOfLogicalChains.get(s));
		}
		System.out.println("");
		System.out.println("dead black: " + deadBlackStonePositions);
		System.out.println("");
		System.out.println("dead white: " + deadWhiteStonePositions);
	}

}
