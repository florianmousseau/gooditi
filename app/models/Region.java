package models;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

import com.avaje.ebean.Expr;

@Entity
public class Region extends Model {


	private static final long serialVersionUID = 3090136267659563701L;
	
	@Id
	public Long id;
	
	public String longName;
	public String shortName;
	public String uri;
	public RegionType regionType;
	
	@ManyToOne
	public Region parent;
	
	public static Finder<Long, Region> find = new Finder<Long, Region>(Long.class, Region.class);
	
	public static List<Region> findAll() {
		return find.all();
	}
	public static Region findCountryByUri(String uri) {
		return find.where().and(Expr.eq("regionType", RegionType.COUNTRY), Expr.eq("uri", uri)).findUnique();
	}
	
	public static Region findRegionByUri(String uri) {
		return find.where(Expr.eq("uri", uri)).findUnique();
	}
	
	public static Region findRegionByUri(String countryUri, String regionUri) {
		Region country = findCountryByUri(countryUri);
		
		return find.where().and(Expr.eq("parent", country), Expr.eq("uri", regionUri)).findUnique();
	}
	
	public static List<Region> findChildren(Region region) {
		return find.where(Expr.eq("parent", region)).findList();
	}
	
	public static List<Region> findRegionAndChildren(Region region) {
		
		List<Region> regions = new LinkedList<Region>();
		regions.add(region);
		
		for(Region child : findChildren(region)){
			regions.addAll(findRegionAndChildren(child));
		}
		
		return regions;
	}
	
	public static void create(Region region) {
		region.save();
	}
	
	public static Region findById(Long id) {
		return find.byId(id);
	}
	
	
	public static String fullUri(Region region) {
		
		if(region.parent == null){
			return "/" + region.uri;
		}
		
		return Region.fullUri(region.parent) + "/" + region.uri;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
