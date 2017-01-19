public class NumberGame{
	
	private int scoreU, scoreC;
	
	public NumberGame(){
		scoreU = 0;
		scoreC = 0;
	}
	
	public void setUScore(int i){
		scoreU += i;
	}
	
	public void setCScore(int i){
		scoreC += i;
	}
	
	public int getUScore(){
		return scoreU;
	}
	
	public int getCScore(){
		return scoreC;
	}

}