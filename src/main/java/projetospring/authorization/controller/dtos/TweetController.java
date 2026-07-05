package projetospring.authorization.controller.dtos;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import projetospring.authorization.entity.Tweet;
import projetospring.authorization.repository.TweetRepository;
import projetospring.authorization.repository.UserRepository;


@RestController
public class TweetController {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    
    
    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/tweets")
    public ResponseEntity<Void> criarTweet(@RequestBody CreateTweetDto createTweetDto,
                                            JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.conteudo());
        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deletarTweet(@PathVariable("id") Long id,
                                                JwtAuthenticationToken token){
        var tweet = tweetRepository.findById(id)
                                .orElseThrow(()->(new ResponseStatusException(HttpStatus.NOT_FOUND)));
        var user = userRepository.findById(UUID.fromString(token.getName()));

        if(user.get().getUserId().equals(tweet.getUser().getUserId())){
            tweetRepository.deleteById(id);;
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        return ResponseEntity.ok().build();
    }
    
}
