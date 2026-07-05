package projetospring.authorization.controller;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import projetospring.authorization.controller.dtos.CreateTweetDto;
import projetospring.authorization.controller.dtos.FeedItemDto;
import projetospring.authorization.controller.dtos.FeedResponseDto;
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
        var user = userRepository.findById(UUID.fromString(token.getName()))
                                .orElseThrow(()->(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
        boolean isAdmin = user.getRoles().stream()
                            .anyMatch(role -> role.getNome().equalsIgnoreCase("admin"));

        if(user.getUserId().equals(tweet.getUser().getUserId()) || isAdmin){
            tweetRepository.deleteById(id);;
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        return ResponseEntity.ok().build();
    }
    

    @GetMapping("/feed")
    public ResponseEntity<FeedResponseDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "1") int pageSize){
        var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimeStamp"))
                                                    .map(tweet-> new FeedItemDto(tweet.getTweetId(), tweet.getUser().getUsername(), tweet.getContent()));
        return ResponseEntity.ok(new FeedResponseDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    }
    
}
