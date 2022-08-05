package com.devcourse.checkmoi.domain.post.converter;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public PostInfo postToInfo(Post post) {
        return PostInfo.builder()
            .id(post.getId())
            .build();
    }

    public Post createToPost(Create request) {
        return Post.builder()
            .title(request.title())
            .content(request.content())
            .build();
    }

}
