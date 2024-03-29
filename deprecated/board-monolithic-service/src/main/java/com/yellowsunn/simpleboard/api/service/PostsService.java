package com.yellowsunn.simpleboard.api.service;

import com.yellowsunn.simpleboard.api.dto.posts.PostsEditDto;
import com.yellowsunn.simpleboard.api.dto.posts.PostsGetDto;
import com.yellowsunn.simpleboard.api.dto.posts.PostsUploadDto;
import com.yellowsunn.simpleboard.domain.file.FileRepository;
import com.yellowsunn.simpleboard.domain.posts.PostType;
import com.yellowsunn.simpleboard.domain.posts.Posts;
import com.yellowsunn.simpleboard.domain.posts.repository.PostsRepository;
import com.yellowsunn.simpleboard.domain.user.Role;
import com.yellowsunn.simpleboard.domain.user.User;
import com.yellowsunn.simpleboard.domain.user.repository.UserRepository;
import com.yellowsunn.simpleboard.exception.ForbiddenException;
import com.yellowsunn.simpleboard.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final CommentService commentService;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Posts save(User user, PostsUploadDto postsUploadDto) throws IOException {
        checkAuthorityForType(user, postsUploadDto.getType());
        Posts post = postsUploadDto.covertToPosts(user);
        return postsRepository.save(post);
    }

    @Transactional(readOnly = false)
    public PostsGetDto findPost(Long id) {
        return postsRepository.findPost(id)
                .map(PostsGetDto::new)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Posts edit(Long id, Long userId, PostsEditDto postsEditDto) {
        Posts post = postsRepository.findById(id).orElseThrow(NotFoundException::new);
        checkSameUser(post, userId);
        checkAuthorityForType(post.getUser(), postsEditDto.getType());
        postsEditDto.editPost(post);
        return post;
    }

    @Transactional
    public void delete(Long id, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));
        Posts post = postsRepository.findById(id).orElseThrow(NotFoundException::new);
        checkDeleteAuthority(post, loginUser);

        commentService.deleteByPost(post);
        fileRepository.deleteAllByPost(post);
        postsRepository.delete(post);
    }

    public boolean hasNotModified(Long id, String ifModifiedSince) {
        if (ifModifiedSince != null) {
            String lastModified = postsRepository.findLastModifiedById(id)
                    .map(lm -> lm.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.RFC_1123_DATE_TIME))
                    .orElse(null);

            return ifModifiedSince.equals(lastModified);
        }
        return false;
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
