package devsinc.Instagram.clone.service.InterfaceImpls;

import devsinc.Instagram.clone.dto.FollowRequest;
import devsinc.Instagram.clone.enums.AccountType;
import devsinc.Instagram.clone.model.Follow;
import devsinc.Instagram.clone.model.FollowRequests;
import devsinc.Instagram.clone.model.User;
import devsinc.Instagram.clone.repository.FollowRepository;
import devsinc.Instagram.clone.repository.FollowRequestsRepository;
import devsinc.Instagram.clone.repository.UserRepository;
import devsinc.Instagram.clone.security.JwtService;
import devsinc.Instagram.clone.service.Interface.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtService jwtService;
    private final FollowRequestsRepository followRequestsRepository;

    @Override
    public ResponseEntity<?> followUserByFollowRequest(FollowRequest request) {
        if (!request.getFollowerId().equals(request.getFollowingId())) {
            Optional<User> follower = userRepository.findById(request.getFollowerId());
            Optional<User> following = userRepository.findById(request.getFollowingId());
            if (follower.isPresent() && following.isPresent()) {
                if (follower.get().getAccountType() == AccountType.PRIVATE) {
                    FollowRequests followRequests = followRequestsRepository.findByFollowerAndFollowing(follower.get(),following.get());
                    if(followRequests == null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, yyyy");
                        FollowRequests followRequest = FollowRequests
                                .builder()
                                .requestDate(dateFormat.format(new Date()))
                                .follower(follower.get())
                                .following(following.get())
                                .build();
                        followRequestsRepository.save(followRequest);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(follower.get().getUsername() +
                                " account is private!\nYour request has been added successfully!\n" +
                                follower.get().getUsername() + " will accept it!");
                    }
                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body("You have already requested to follow "+ follower.get().getUsername()+"!");

                } else if (follower.get().getAccountType() == AccountType.PUBLIC) {
                    Follow follow = followRepository.findByFollowerAndFollowing(follower.get(),
                            following.get());
                    if (follow == null) {
                        Follow saveFollower = Follow.builder()
                                .follower(follower.get())
                                .following(following.get())
                                .build();
                        followRepository.save(saveFollower);
                        return ResponseEntity.ok().body("You have followed " + follower.get().getUsername() + " successfully!");
                    }
                    return ResponseEntity.ok().body("You are already following to " + follower.get().getUsername());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request, user doesn't have consistent value in DB!");
            } else if (follower.isEmpty()) {
                return ResponseEntity.ok().body("Their is no entity exists against " + request.getFollowerId() + " ID!");
            } else {
                return ResponseEntity.ok().body("Their is no entity exists against " + request.getFollowingId() + " ID!");
            }
        }
        return ResponseEntity.ok().body("User cannot follow his own ID!");
    }

    @Override
    public ResponseEntity<?> getListOfMyFollowers(String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            List<String> list = user.get().getFollowers().stream().map(item -> item.getFollowing().getUsername()).toList();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get().getUsername()
                        + " you didn't have any follower yet!");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Here is your follower list: \n" +
                    String.join("\n", list));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    @Override
    public ResponseEntity<?> getListOfMyFollowings(String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            List<String> list = user.get().getFollowings().stream().map(item -> item.getFollower().getUsername()).toList();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get().getUsername()
                        + " you are not following anyone yet");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Here is your followings list: \n" +
                    String.join("\n", list));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    @Override
    public ResponseEntity<?> getListOfMyFollowRequests(String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            List<String> list = user.get().getRequestFollowers().stream().map(item -> item.getFollowing().getUsername()).toList();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get().getUsername()
                        + " you didn't have any follow request!");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Here is your follow requests list: \n" +
                    String.join("\n", list));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    @Override
    public ResponseEntity<?> getListOfMyFollowingRequests(String token) {
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        if (user.isPresent()) {
            List<String> list = user.get().getRequestFollowings().stream().map(item -> item.getFollower().getUsername()).toList();
            if (list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(user.get().getUsername()
                        + " you didn't have any pending following request!");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Here is your following requests list: \n" +
                    String.join("\n", list));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    @Override
    public ResponseEntity<?> acceptFollowRequest(String token, String username) {
        Optional<User> follower = userRepository.findByUsername(jwtService.extractUsername(token));
        Optional<User> following = userRepository.findByUsername(username);
        if (follower.isPresent() && following.isPresent()) {
            FollowRequests followRequests = followRequestsRepository.
                    findByFollowerAndFollowing(follower.get(),following.get());
            if(followRequests != null) {
                Follow saveFollower = Follow.builder()
                        .follower(followRequests.getFollower())
                        .following(followRequests.getFollowing())
                        .build();
                followRepository.save(saveFollower);
                followRequestsRepository.delete(followRequests);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(username+" your request has been accepted successfully by "+follower.get().getUsername());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("There is no follow request exists of "+username+"!");
        } else if (follower.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authorized!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't found by name " + username + "!");
    }
}
