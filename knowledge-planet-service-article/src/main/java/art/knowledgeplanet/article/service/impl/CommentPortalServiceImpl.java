package art.knowledgeplanet.article.service.impl;

import com.github.pagehelper.PageHelper;
import art.knowledgeplanet.api.controller.article.CommentControllerApi;
import art.knowledgeplanet.api.service.BaseService;
import art.knowledgeplanet.article.mapper.ArticleMapper;
import art.knowledgeplanet.article.mapper.CommentsMapper;
import art.knowledgeplanet.article.mapper.CommentsMapperCustom;
import art.knowledgeplanet.article.service.ArticlePortalService;
import art.knowledgeplanet.article.service.CommentPortalService;
import art.knowledgeplanet.enums.ArticleReviewStatus;
import art.knowledgeplanet.enums.YesOrNo;
import art.knowledgeplanet.pojo.Article;
import art.knowledgeplanet.pojo.Comments;
import art.knowledgeplanet.pojo.vo.ArticleDetailVO;
import art.knowledgeplanet.pojo.vo.CommentsVO;
import art.knowledgeplanet.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentPortalServiceImpl extends BaseService implements CommentPortalService {

    @Autowired
    private Sid sid;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;

    @Transactional
    @Override
    public void createComment(String articleId,
                                                 String fatherCommentId,
                                                 String content,
                                                 String userId,
                                                 String nickname) {

        String commentId = sid.nextShort();

        ArticleDetailVO article
                 = articlePortalService.queryDetail(articleId);

        Comments comments = new Comments();
        comments.setId(commentId);

        comments.setWriterId(article.getPublishUserId());
        comments.setArticleTitle(article.getTitle());
        comments.setArticleCover(article.getCover());
        comments.setArticleId(articleId);

        comments.setFatherId(fatherCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);

        comments.setContent(content);
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);

        // ???????????????
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId, 1);
    }

    @Override
    public PagedGridResult queryArticleComments(String articleId,
                                                Integer page,
                                                Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);

        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsMapperCustom.queryArticleCommentList(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {

        Comments comment = new Comments();
        comment.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Comments> list = commentsMapper.select(comment);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        Comments comment = new Comments();
        comment.setId(commentId);
        comment.setWriterId(writerId);

        commentsMapper.delete(comment);
    }
}
