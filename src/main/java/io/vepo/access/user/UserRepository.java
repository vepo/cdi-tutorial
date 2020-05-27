package io.vepo.access.user;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.mongodb.client.MongoCollection;

import io.vepo.access.infra.stats.Measured;

@Dependent
public class UserRepository {
	@Inject
	private MongoCollection<User> collection;

	@Measured
	public List<User> findAll() {
		return collection.find().into(new ArrayList<>());
	}

	@Measured
	public User create(User user) {
		collection.insertOne(user);
		return user;
	}

	@Measured
	public Optional<User> findByUsernameAndPassword(String username, String password) {
		return Optional
				.ofNullable(this.collection.find(and(eq("username", username), eq("password", password))).first());
	}

	@Measured
	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(this.collection.find(eq("username", username)).first());
	}

	@Measured
	public Optional<User> delete(String username) {
		return Optional.ofNullable(this.collection.findOneAndDelete(eq("username", username)));
	}
}
