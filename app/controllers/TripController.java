package controllers;

import java.util.Date;

import models.Trip;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;

public class TripController extends Controller {

	@Restrict(@Group(Application.USER_ROLE))
	public static Result myTrips() {
		final User user = Application.getLocalUser(session());
		return ok(views.html.trip.mytrips.render(Trip.findCustomTrip(user), Trip.findRequestPublishedTrip(user), Trip.findPublishedTrip(user)));
	}

	static Form<Trip> createTripForm = Form.form(Trip.class);

	@Restrict(@Group(Application.USER_ROLE))
	public static Result createTrip() {
		return ok(views.html.trip.createTrip.render(createTripForm));
	}

	@Restrict(@Group(Application.USER_ROLE))
	public static Result doCreateTrip() {
		Form<Trip> filledForm = createTripForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.trip.createTrip.render(filledForm));
		} else {
			Trip tripToCreate = filledForm.get();
			tripToCreate.author = Application.getLocalUser(session());
			
			Trip.create(tripToCreate);
			return redirect(routes.TripController.myTrips());
		}
	}

	@Restrict(@Group(Application.USER_ROLE))
	public static Result doDeleteTrip(Long id) {
		Trip.delete(id);
		return redirect(routes.TripController.myTrips());
	}


	@Restrict(@Group(Application.USER_ROLE))
	public static Result editTrip(Long id) {
		Form<Trip> editTripForm = Form.form(Trip.class);
		return ok(views.html.trip.editTrip.render(id, editTripForm.fill(Trip.find.byId(id))));
	}
	
	public static Result viewTrip(Long id) {
		
		Trip trip = Trip.find.byId(id);
		
		return ok(views.html.trip.viewTrip.render(trip, Trip.isEditable(id, Application.getLocalUser(session()))));
	}
	

	@Restrict(@Group(Application.USER_ROLE))
	public static Result doEditTrip(Long id) {
		Form<Trip> tripForm = Form.form(Trip.class).bindFromRequest();
		
        if(tripForm.hasErrors()) {
            return badRequest(views.html.trip.editTrip.render(id, tripForm.fill(Trip.find.byId(id))));
        }
        
        Trip updatedTrip = tripForm.get();
        updatedTrip.id = id;
        
        Trip.update(updatedTrip);
        
		return redirect(routes.TripController.myTrips());
	}
	
	public static Result doRequestPublishedTrip(Long id){
		
		Trip trip = Trip.find.byId(id);
		
		Trip tripToPublish = trip.duplicate();
		
		tripToPublish.requestPublishDate = new Date();
		tripToPublish.save();
		
		return redirect(routes.TripController.myTrips());
	}
	
	public static Result doPublishTrip(Long id){
		
		Trip trip = Trip.find.byId(id);
		trip.publishedDate = new Date();
		
		trip.save();
		
		return redirect(routes.TripController.admin());
	}
	
	@SubjectPresent
	public static Result admin() {
		return ok(views.html.admin.render(Trip.findRequestPublishedTrip()));
	}

}
