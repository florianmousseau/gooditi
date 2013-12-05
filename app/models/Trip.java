package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

@Entity
public class Trip extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	public User author;
	
	public String uri;

	@Required
	public String title;
	
	public String description;
	
	public Date requestPublishDate;
	
	public Date publishedDate;
	
	@ManyToOne
	public Region region;
	
	@OneToMany(cascade = CascadeType.ALL)
	public List<Itinerary> itineries = new ArrayList<Itinerary>();

	public static Finder<Long, Trip> find = new Finder<Long, Trip>(Long.class,
			Trip.class);
	
	public static Trip findPublishedTripByUri(String uri) {
		return find.where().and(Expr.eq("uri", uri), Expr.isNotNull("publishedDate")).findUnique();
	}

	public static List<Trip> findCustomTrip(final User author) {
		return find.where().and(Expr.eq("author", author), Expr.isNull("requestPublishDate")).findList();
	}
	
	public static boolean isEditable(Long id, final User author) {
		Trip trip = find.ref(id);
		
		return trip.author.equals(author) && trip.requestPublishDate == null;
	}
	
	public static List<Trip> findRequestPublishedTrip(final User author) {
		return find.where().and(Expr.eq("author", author), Expr.and(Expr.isNotNull("requestPublishDate"),Expr.isNull("publishedDate"))).findList();
	}

	public static void create(Trip trip) {
		trip.save();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}
	
	public static void update(Trip trip) {
		Ebean.saveAssociation(trip, "itineries");
		Ebean.update(trip);
	}
	
	public static boolean editable(final User author) {

		return true;
	}
	
	public Trip duplicate()  {
		
		Trip clone = new Trip();
		clone.author = this.author;
		clone.title = this.title;
		clone.uri = this.uri;
		clone.description = this.description;
		clone.region = this.region;
		clone.itineries = new ArrayList<Itinerary>();
		for(Itinerary itineray : this.itineries){
			clone.itineries.add(itineray.duplicate());
		}
		
		return clone;
		
	
	}

	public static List<Trip> findRequestPublishedTrip() {
		return find.where().and(Expr.isNull("publishedDate"), Expr.isNotNull("requestPublishDate")).findList();
	}

	public static List<Trip> findPublishedTrip(User user) {
		return find.where().and(Expr.eq("author", user), Expr.isNotNull("publishedDate")).findList();

	}
	
	public static List<Trip> allPublishedTrip() {
		return find.where(Expr.isNotNull("publishedDate")).findList();
	}
	
	public static List<Trip> findPublishedTripByRegion(Region region) {
		
		List<Trip> trips = new LinkedList<Trip>();
		List<Region> regions = Region.findRegionAndChildren(region);
		
		for(Trip trip : allPublishedTrip()) {
			if(regions.contains(trip.region)){
				trips.add(trip);
			}
		}
		
		return trips;
	}
}
