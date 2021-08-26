package com.crio.starter.repositoryServices;

import com.crio.starter.data.MemeEntity;
import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MemeRepositoryServiceImpl implements MemeRepositoryService {

  public static final int NO_OF_MEMES_LIMIT = 100;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ModelMapper modelmapper;

  @Override
  public List<Meme> getLatest100Memes() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.DESC, "$natural")).limit(NO_OF_MEMES_LIMIT);
    List<MemeEntity> memeEntities = mongoTemplate.find(query, MemeEntity.class);

    List<Meme> memes = new ArrayList<>();
    Meme meme;
    for (MemeEntity memeEntity : memeEntities) {
      meme = modelmapper.map(memeEntity, Meme.class);
      memes.add(meme);
    }

    return memes;
  }

  private Meme mapMemeEntityWithMeme(MemeEntity memeEntity) {
    if (memeEntity == null) {
      return null;
    } else {
      return modelmapper.map(memeEntity, Meme.class);
    }
  }

  @Override
  public Meme getMemeWithNameUrlCaption(String name, String url, String caption) {

    Query query = new Query();
    query.addCriteria(Criteria.where("name").is(name)
        .and("url").is(url).and("caption").is(caption));

    MemeEntity memeEntity = mongoTemplate.findOne(query, MemeEntity.class);
    return mapMemeEntityWithMeme(memeEntity);
  }

  @Override
  public Meme saveMeme(MemePostRequest memePostRequest) {

    MemeEntity entity = modelmapper.map(memePostRequest, MemeEntity.class);

    MemeEntity savedEntity = mongoTemplate.save(entity);
    return mapMemeEntityWithMeme(savedEntity);
  }

  @Override
  public Meme getMemeWithId(String id) {

    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));

    MemeEntity memeEntity = mongoTemplate.findOne(query, MemeEntity.class);
    return mapMemeEntityWithMeme(memeEntity);
  }

}