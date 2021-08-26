package com.crio.starter.repositoryServices;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;
import java.util.List;


public interface MemeRepositoryService {

  public List<Meme> getLatest100Memes();

  public Meme getMemeWithNameUrlCaption(String name, String url, String caption);

  public Meme saveMeme(MemePostRequest memePostRequest);

  public Meme getMemeWithId(String id);

}