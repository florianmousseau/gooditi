package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class LocationPoint extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	public String title;
	
	public Float latitude;
	
	public Float longitude;

	public String description;

	public static Finder<Long, LocationPoint> find = new Finder<Long, LocationPoint>(
			Long.class, LocationPoint.class);

	public static void create(LocationPoint location) {
		location.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static void update(LocationPoint location) {
		location.save();
	}

	public LocationPoint duplicate() {
		LocationPoint clone =  new LocationPoint();
		clone.title = this.title;
		clone.latitude = this.latitude;
		clone.longitude = this.longitude;
		clone.description = this.description;
		
		return clone;
	}

}
