package com.financial.gavin.circleus.data.firebase;

/**
 * Created by gavin on 1/28/18.
 */

import com.financial.gavin.circleus.data.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates testing data set
 */
public class Mock {
	
	List<User> users = new ArrayList<>();
	
	public Mock() {
		User gavin = new User("Gavin", "7853070122", "https://scontent-sjc3-1.xx.fbcdn.net/v/t1.0-9/11896042_10153170089643590_3226484400942537577_n.jpg?oh=7d5a08312b400c95ca225989fc703043&oe=5B2501E1", 37.382253, -122.035677);
		User alice = new User("Alice", "7853070123", "https://scontent-sjc3-1.xx.fbcdn.net/v/t1.0-9/12301648_576830982472722_6815221090587201651_n.jpg?oh=11681dac35afa4a0831cceed5e21c7c4&oe=5B25BE96", 37.384119, -122.000698);
		User johnson = new User("Johnson", "7853070124", "https://scontent-sjc3-1.xx.fbcdn.net/v/t1.0-9/26169442_10215756392329624_7033488399869483264_n.jpg?oh=e08d945e6e0ce5141c0f3dc75c4e5a45&oe=5ADAE4DE", 37.397584, -122.031838);
		User courtney = new User("Courtney", "7853070125", "https://scontent-sjc3-1.xx.fbcdn.net/v/t1.0-9/24991312_1600742983297455_7705724121738812708_n.jpg?oh=ff1351da7badd5b0019e4c78b3098a1e&oe=5B228B05", 37.422283, -122.084052);
		users.add(gavin);
		users.add(alice);
		users.add(johnson);
		users.add(courtney);
	}
	
	public List<User> getUsers() {
		return users;
	}
}
