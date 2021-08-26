package com.crio.starter.service;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;
import java.util.List;

public interface MemesService {
  public List<Meme> getLatestMemesService();

  public Meme postMemeService(MemePostRequest request);

  public Meme getMemeWithIdService(String id);
}