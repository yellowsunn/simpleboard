package com.yellowsunn.simpleforum.api.service;

import com.yellowsunn.simpleforum.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleforum.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleforum.domain.posts.PostType;
import com.yellowsunn.simpleforum.domain.posts.Posts;
import com.yellowsunn.simpleforum.domain.posts.PostsRepository;
import com.yellowsunn.simpleforum.domain.user.Role;
import com.yellowsunn.simpleforum.domain.user.User;
import com.yellowsunn.simpleforum.domain.user.UserRepository;
import com.yellowsunn.simpleforum.exception.ForbiddenException;
import com.yellowsunn.simpleforum.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Posts save(User user, PostsUploadDto postsUploadDto) throws IOException {
        checkAuthorityForType(user, postsUploadDto.getType());
        Posts post = postsUploadDto.covertToPosts(user);
        return postsRepository.save(post);
    }

    @Transactional
    public PostsGetDto findPost(Long id) {
        return postsRepository.findPostAndUpdateHit(id)
                .map(PostsGetDto::new)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(Long id, Long userId, PostsEditDto postsEditDto) {
        Posts post = postsRepository.findById(id).orElseThrow(NotFoundException::new);
        checkSameUser(post, userId);
        checkAuthorityForType(post.getUser(), postsEditDto.getType());
        postsEditDto.editPost(post);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
        Posts post = postsRepository.findById(id).orElseThrow(NotFoundException::new);
        checkDeleteAuthority(post, loginUser);
        postsRepository.delete(post);
    }

    private void checkAuthorityForType(User user, PostType type) {
        if (user.getRole() != Role.ADMIN && type == PostType.NOTICE) {
            throw new ForbiddenException();
        }
    }

    private void checkSameUser(Posts post, Long userId) {
        if (!isSameUser(post, userId)) {
            throw new ForbiddenException();
        }
    }

    private void checkDeleteAuthority(Posts post, User loginUser) {
        if (!(isAdmin(loginUser) || isSameUser(post, loginUser.getId()))) {
            throw new ForbiddenException();
        }
    }

    private boolean isSameUser(Posts post, Long userId) {
        return userId != null && userId.equals(post.getUser().getId());
    }

    private boolean isAdmin(User user) {
        return Role.ADMIN.equals(user.getRole());
    }
}
