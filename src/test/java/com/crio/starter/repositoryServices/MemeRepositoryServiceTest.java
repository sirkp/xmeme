package com.crio.starter.repositoryServices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.crio.starter.data.MemeEntity;
import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
public class MemeRepositoryServiceTest {
  private static final String ID = "1";
  private static final String NAME = "Pradeep";
  private static final String URL = "https://www.google.com";
  private static final String CAPTION = "Pawri";
  private static final Meme MEME = new Meme(ID, NAME, URL, CAPTION);
  private static final MemeEntity MEMEENTITY = getMemeEntity(ID, NAME, URL, CAPTION);
  
  @InjectMocks
  private MemeRepositoryServiceImpl memeRepositoryService;

  @Mock
  private MongoTemplate mongoTemplate;

  @Mock
  private ModelMapper modelmapper;


  @Test
  public void getLatest100MemesTest() throws Exception {
    when(mongoTemplate.find(any(Query.class), eq(MemeEntity.class)))
        .thenReturn(Arrays.asList(MEMEENTITY));
    when(modelmapper.map(eq(MEMEENTITY), eq(Meme.class))).thenReturn(MEME);

    List<Meme> memes = memeRepositoryService.getLatest100Memes();
    assertEquals(getMemesFromMemeEntities(Arrays.asList(MEMEENTITY)), memes);
  }

  @Test
  public void getMemeWithNameUrlCaptionTest() {
    Query query = new Query();
    query.addCriteria(Criteria.where("name").is(NAME)
        .and("url").is(URL).and("caption").is(CAPTION));

    when(mongoTemplate.findOne(eq(query), eq(MemeEntity.class))).thenReturn(MEMEENTITY);
    when(modelmapper.map(eq(MEMEENTITY), eq(Meme.class))).thenReturn(MEME);

    Meme meme = memeRepositoryService.getMemeWithNameUrlCaption(NAME, URL, CAPTION);
    assertEquals(MEME, meme);

    meme = memeRepositoryService.getMemeWithNameUrlCaption("tee", URL, CAPTION);
    assertEquals(null, meme);
  }

  @Test
  public void saveMemeTest() {
    MemePostRequest memePostRequest = getMemePostRequest(NAME, URL, CAPTION);
    MemeEntity entity = getMemeEntity(null, NAME, URL, CAPTION);

    when(modelmapper.map(eq(memePostRequest), eq(MemeEntity.class))).thenReturn(entity);
    when(mongoTemplate.save(eq(entity))).thenReturn(MEMEENTITY);
    when(modelmapper.map(eq(MEMEENTITY), eq(Meme.class))).thenReturn(MEME);

    Meme meme = memeRepositoryService.saveMeme(memePostRequest);
    assertEquals(MEME, meme);
  }

  @Test
  public void getMemeWithIdTest() {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(ID));
    
    when(mongoTemplate.findOne(eq(query), eq(MemeEntity.class))).thenReturn(MEMEENTITY);
    when(modelmapper.map(eq(MEMEENTITY), eq(Meme.class))).thenReturn(MEME);

    Meme meme = memeRepositoryService.getMemeWithId(ID);
    assertEquals(MEME, meme);

    meme = memeRepositoryService.getMemeWithId("-1");
    assertEquals(null, meme);
    
  }


  private static MemeEntity getMemeEntity(String id, String name, String url, String caption) {
    MemeEntity entity = new MemeEntity();
    entity.setId(id);
    entity.setName(name);
    entity.setUrl(url);
    entity.setCaption(caption);
    return entity;
  }

  private MemePostRequest getMemePostRequest(String name, String url, String caption) {
    MemePostRequest entity = new MemePostRequest();
    entity.setName(name);
    entity.setUrl(url);
    entity.setCaption(caption);
    return entity;
  }

  private List<Meme> getMemesFromMemeEntities(List<MemeEntity> memeEntities) {
    List<Meme> memes = new ArrayList<>();
    Meme meme;
    ModelMapper mapper = new ModelMapper();
    for (MemeEntity memeEntity : memeEntities) {
      meme = mapper.map(memeEntity, Meme.class);
      memes.add(meme);
    }
    return memes;
  }


}