package Model;

import java.util.List;

public class FestivalModel {
	
	private String name;
	private List<BandModel> bands;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BandModel> getBands() {
		return bands;
	}
	public void setBands(List<BandModel> bands) {
		this.bands = bands;
	}
	
	
}
