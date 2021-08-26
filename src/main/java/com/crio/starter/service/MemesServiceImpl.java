package com.crio.starter.service;

import com.crio.starter.dto.Meme;
import com.crio.starter.exceptions.DuplicateMemeException;
import com.crio.starter.exchange.MemePostRequest;
import com.crio.starter.repositoryServices.MemeRepositoryService;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemesServiceImpl implements MemesService {

  @Autowired
  private MemeRepositoryService memeRepositoryService;

  @Override
  public List<Meme> getLatestMemesService() {
    System.out.println("MemesService::getLatestMemesService");
    List<Meme> memes = memeRepositoryService.getLatest100Memes();
    return memes;
  }

  public boolean isMemeAlreadyPresent(MemePostRequest request) {
    System.out.println("MemesService::isMemeAlreadyPresent");
    Meme meme = memeRepositoryService.getMemeWithNameUrlCaption(request.getName(), request.getUrl(),
        request.getCaption());
    return (meme != null);
  }

  @Override
  public Meme postMemeService(MemePostRequest request) {
    System.out.println("MemesService::postMemeService");
    Meme meme = null;
    if (!isMemeAlreadyPresent(request)) {
      meme = memeRepositoryService.saveMeme(request);
    }
    return meme;
  }

  @Override
  public Meme getMemeWithIdService(String id) {
    System.out.println("MemesService::getMemeWithIdService");
    Meme meme = memeRepositoryService.getMemeWithId(id);
    return meme;
  }
}