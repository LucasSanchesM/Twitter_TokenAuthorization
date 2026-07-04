package projetospring.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projetospring.authorization.entity.Tweet;
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long>{

}
