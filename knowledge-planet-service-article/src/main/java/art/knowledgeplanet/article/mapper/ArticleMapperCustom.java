package art.knowledgeplanet.article.mapper;

import art.knowledgeplanet.my.mapper.MyMapper;
import art.knowledgeplanet.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {

    public void updateAppointToPublish();

}