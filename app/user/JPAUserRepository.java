package user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPAUserRepository implements UserRepository {

    private final JPAApi jpaApi;
    private final UserExecutionContext ec;
    private final CircuitBreaker<Optional<UserData>> circuitBreaker = new CircuitBreaker<Optional<UserData>>().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAUserRepository(JPAApi api, UserExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<UserData>> list() {
        return supplyAsync(() -> wrap(em -> select(em)), ec);
    }

    @Override
    public CompletionStage<UserData> create(UserData userData) {
        return supplyAsync(() -> wrap(em -> insert(em, userData)), ec);
    }

    @Override
    public CompletionStage<Optional<UserData>> get(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookup(em, id))), ec);
    }

    @Override
    public CompletionStage<Optional<UserData>> update(Long id, UserData userData) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> modify(em, id, userData))), ec);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Optional<UserData> lookup(EntityManager em, Long id) throws SQLException {
//        throw new SQLException("Call this to cause the circuit breaker to trip");
        return Optional.ofNullable(em.find(UserData.class, id));
    }

    private Stream<UserData> select(EntityManager em) {
        TypedQuery<UserData> query = em.createQuery("SELECT u FROM UserData u", UserData.class);
        return query.getResultList().stream();
    }

    private Optional<UserData> modify(EntityManager em, Long id, UserData userData) throws InterruptedException {
        final UserData data = em.find(UserData.class, id);
        if (data != null) {
            data.name = userData.name;
            data.email = userData.email;
        }
        Thread.sleep(10000L);
        return Optional.ofNullable(data);
    }

    private UserData insert(EntityManager em, UserData userData) {
        return em.merge(userData);
    }
}
