package backend.ssr.ddd.ssrblog.friends.controller;

import backend.ssr.ddd.ssrblog.friends.domain.entity.Friends;
import backend.ssr.ddd.ssrblog.friends.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendsController {

    private final FriendService friendService;

    @GetMapping("/api/friends/{accepterIdx}/required")
    public List<Friends> getRequiredFriendsList(@PathVariable Long accepterIdx) {
        return friendService.getRequiredFriendsList(accepterIdx);
    }

}
