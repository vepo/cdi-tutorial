package io.vepo.access.infra;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

@ApplicationScoped
public class MongoClientFactory {
	private static final Logger logger = LoggerFactory.getLogger(MongoClientFactory.class);

	private static final String DATABASE = "cdi-tutorial";

	@Inject
	@ConfigProperty(name = "mongo.user")
	String mongoUser;

	@Inject
	@ConfigProperty(name = "mongo.password")
	String mongoPassword;

	@Inject
	@ConfigProperty(name = "mongo.host")
	String mongoHost;

	@Inject
	@ConfigProperty(name = "mongo.port", defaultValue = "27017")
	int mongoPort;

	@Inject
	@ConfigProperty(name = "mongo.admin-db", defaultValue = "admin")
	String mongoAdminDb;

	private MongoClient mongoClient;
	private MongoCredential mongoCredential;

	@PostConstruct
	void buildMongoClient() {
		logger.info("Building MongoClientFactory");
		mongoCredential = MongoCredential.createCredential(mongoUser, mongoAdminDb, mongoPassword.toCharArray());
	}

	@Produces
	public MongoClient produceMongoClient() {
		if (isNull(mongoClient)) {
			mongoClient = MongoClients.create(MongoClientSettings.builder()
					.applyToClusterSettings(
							builder -> builder.hosts(Arrays.asList(new ServerAddress(mongoHost, mongoPort))))
					.credential(mongoCredential)
					.codecRegistry(fromRegistries(getDefaultCodecRegistry(), fromProviders(new EnumCodecProvider(),
							PojoCodecProvider.builder().automatic(true).build())))
					.build());
			logger.info("Connected to MongoDB server on {}:{}", mongoHost, mongoPort);
		}
		return mongoClient;
	}

	@Produces
	@SuppressWarnings({ "unchecked" })
	public <T> MongoCollection<T> getCollection(InjectionPoint injectionPoint) {
		final ParameterizedType parameterizedType = (ParameterizedType) injectionPoint.getType();
		System.out.println(parameterizedType);
		final Class<T> genericTypeClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		return produceMongoClient().getDatabase(DATABASE).getCollection(uncapitalize(genericTypeClass.getSimpleName()),
				genericTypeClass);
	}

	@PreDestroy
	void cleanup() {
		if (nonNull(mongoClient)) {
			mongoClient.close();
		}
	}
}
