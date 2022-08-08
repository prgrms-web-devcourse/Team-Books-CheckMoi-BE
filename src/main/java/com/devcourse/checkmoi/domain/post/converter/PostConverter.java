package com.devcourse.checkmoi.domain.post.converter;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public PostInfo postToInfo(Post post) {
        return PostInfo.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .category(post.getCategory())
            .studyId(post.getStudy().getId())
            .writer(post.getWriter().getName())
            .writerImage(post.getWriter().getProfileImgUrl())
            .commentCount(post.getCommentCount())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }

    public Post createToPost(Create request, Long writerId) {
        return Post.builder()
            .title(request.title())
            .content(request.content())
            .category(PostCategory.valueOf(request.category()))
            .writer(User.builder().id(writerId).build())
            .study(Study.builder().id(request.studyId()).build())
            .build();
    }

}
