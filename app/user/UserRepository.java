package user;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface UserRepository {

    CompletionStage<Stream<UserData>> list();

    CompletionStage<UserData> create(UserData postData);

    CompletionStage<Optional<UserData>> get(Long id);

    CompletionStage<Optional<UserData>> update(Long id, UserData postData);
}

