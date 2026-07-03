package projetospring.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projetospring.authorization.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{

}
