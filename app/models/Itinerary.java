package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@Entity
public class Itinerary extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	
	public String title;
	
	public String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<LocationPoint> locations = new ArrayList<LocationPoint>();

	public Itinerary duplicate() {
		Itinerary clone = new Itinerary();
		clone.title = this.title;
		clone.locations = new ArrayList<LocationPoint>();
		
		for(LocationPoint location : this.locations){
			clone.locations.add(location.duplicate());
		}
		
		return clone;
	}



}
