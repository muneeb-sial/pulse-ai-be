package app.Api.Auth.MagicLink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MagicLinkTokenRepository extends JpaRepository<MagicLinkTokenEntity, Long> {

    Optional<MagicLinkTokenEntity> findOneByToken(String token);

}
