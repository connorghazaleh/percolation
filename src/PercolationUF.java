
public class PercolationUF implements IPercolate {
	
	boolean[][] myGrid;
	IUnionFind myFinder;
	int numSites = 0;
	private final int VTOP;
	private final int VBOTTOM;
	private int myOpenCount = 0;
	
	
	
	PercolationUF(int size, IUnionFind finder){
		//creates new boolean n by n array
		myGrid = new boolean[size][size];
		for (int i = 0; i < myGrid.length; i++) {
			for (int j = 0; j < myGrid.length; j++) {
				myGrid[i][j] = false;
			}
		}
		
		//initializes IUnionFind object
		myFinder = finder;
		myFinder.initialize(size*size +2);
		
		//defines upper and lower boundaries for unions
		VTOP = (size)*(size);
		VBOTTOM = (size)*(size)+1;
	}
	
	@Override
	public void open(int row, int col) {
		//checks if coordinates are within grid
		if ((row > myGrid.length || row < 0) || (col > myGrid.length || col < 0)) {
			throw new IndexOutOfBoundsException();
		}
		
		//opens site by setting it equal to true in matrix and increments number of open sites by 1
		if (myGrid[row][col] == false) {
			myOpenCount += 1;
		}
		myGrid[row][col] = true;
		
		//calls an update method
		updateOnOpen(row,col);
	}

	@Override
	public boolean isOpen(int row, int col) {
		//checks if coordinates are within grid
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		//checks boolean value to determine if site is open (true = open)
		if (myGrid[row][col] == true) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFull(int row, int col) {
		//checks if coordinates are within grid
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		//checks if site is contained in a union that contains VTOP to determine if site is full or not
		if (myFinder.connected(VTOP,getIndex(row,col)) && isOpen(row,col)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean percolates() {
		//checks if VTOP and VBOTTOM are both contained in a union, in which case the grid percolates 
		if (myFinder.connected(VTOP,VBOTTOM)) {
			return true;
		}
		return false;
	}

	@Override
	public int numberOfOpenSites() {
		//returns the variable that counted how many open sites there are
		return myOpenCount;
	}
	
	protected int getIndex(int row,int col) {
		//checks if coordinates are within grid
		if ((row > myGrid.length || row < 0) || (col > myGrid.length || col < 0)) {
			throw new IndexOutOfBoundsException();
		}
		
		//returns an int that represents
		return (myGrid.length)*row + col;
	}
	
	

	protected void updateOnOpen(int row, int col) {
		//checks if coordinates are within grid
		if (!inBounds(row,col)) {
			throw new IndexOutOfBoundsException();
		}
		
		//unions anything in the top row with VTOP
		if (row == 0) {
			myFinder.union(getIndex(row,col), VTOP);
		}
		
		//unions anything in the bottom row with VBOTTOM
		if (row == myGrid.length-1) {
			myFinder.union(getIndex(row,col), VBOTTOM);
		}
		
		//opens the site
		if (!isOpen(row,col)) {
			myGrid[row][col] = true;
		}
		
		//next 4 if statements union the site with anything around it that is open
		if (row < myGrid.length-1 && isOpen(row+1,col)) {
			myFinder.union(getIndex(row+1,col), getIndex(row,col));
		}
		if (row > 0 && isOpen(row-1,col)) {
			myFinder.union(getIndex(row-1,col), getIndex(row,col));
		}
		if (col < myGrid.length-1 && isOpen(row,col+1)) {
			myFinder.union(getIndex(row,col+1), getIndex(row,col));
		}
		if (col > 0 && isOpen(row,col-1)) {
			myFinder.union(getIndex(row,col-1), getIndex(row,col));
		}

		
	}
	protected boolean inBounds(int row, int col) {
		//checks to see if the coordinates are within the grid
		if (row < 0 || row >= myGrid.length) return false;
		if (col < 0 || col >= myGrid[0].length) return false;
		return true;
	}

}
