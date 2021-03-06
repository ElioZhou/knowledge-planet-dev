package art.knowledgeplanet.article.service.impl;

import com.github.pagehelper.PageHelper;
import art.knowledgeplanet.api.service.BaseService;
import art.knowledgeplanet.article.mapper.ArticleMapper;
import art.knowledgeplanet.article.mapper.ArticleMapperCustom;
import art.knowledgeplanet.article.service.ArticlePortalService;
import art.knowledgeplanet.article.service.ArticleService;
import art.knowledgeplanet.enums.ArticleAppointType;
import art.knowledgeplanet.enums.ArticleReviewLevel;
import art.knowledgeplanet.enums.ArticleReviewStatus;
import art.knowledgeplanet.enums.YesOrNo;
import art.knowledgeplanet.exception.GraceException;
import art.knowledgeplanet.grace.result.ResponseStatusEnum;
import art.knowledgeplanet.pojo.Article;
import art.knowledgeplanet.pojo.Category;
import art.knowledgeplanet.pojo.bo.NewArticleBO;
import art.knowledgeplanet.pojo.vo.ArticleDetailVO;
import art.knowledgeplanet.utils.PagedGridResult;
import art.knowledgeplanet.utils.extend.AliTextReviewUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword,
                                                 Integer category,
                                                 Integer page,
                                                 Integer pageSize) {

        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();

        /**
         * ????????????????????????????????????????????????
         * isAppoint=????????????????????????????????????????????????????????????????????????????????????
         * isDelete=????????????????????????????????????????????????
         * articleStatus=?????????????????????????????????????????????/??????????????????????????????
         */
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);

        return setterPagedGrid(list, page);
    }

    @Override
    public List<Article> queryHotList() {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = setDefualArticleExample(articleExample);

        PageHelper.startPage(1, 5);
        List<Article> list  = articleMapper.selectByExample(articleExample);
        return list;
    }

    private Example.Criteria setDefualArticleExample(Example articleExample) {
        articleExample.orderBy("publishTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();

        /**
         * ????????????????????????????????????????????????
         * isAppoint=????????????????????????????????????????????????????????????????????????????????????
         * isDelete=????????????????????????????????????????????????
         * articleStatus=?????????????????????????????????????????????/??????????????????????????????
         */
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

        return criteria;
    }

    @Override
    public PagedGridResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);

        Example.Criteria criteria = setDefualArticleExample(articleExample);
        criteria.andEqualTo("publishUserId", writerId);

        /**
         * page: ?????????
         * pageSize: ??????????????????
         */
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryGoodArticleListOfWriter(String writerId) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();

        Example.Criteria criteria = setDefualArticleExample(articleExample);
        criteria.andEqualTo("publishUserId", writerId);

        /**
         * page: ?????????
         * pageSize: ??????????????????
         */
        PageHelper.startPage(1, 5);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, 1);
    }

    @Override
    public ArticleDetailVO queryDetail(String articleId) {

        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setIsDelete(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);

        Article result = articleMapper.selectOne(article);

        ArticleDetailVO detailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(result, detailVO);

        detailVO.setCover(result.getArticleCover());

        return detailVO;
    }
}
