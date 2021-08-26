package com.crio.starter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.crio.starter.dto.Meme;
import com.crio.starter.exchange.MemePostRequest;
import com.crio.starter.repositoryServices.MemeRepositoryService;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MemesServiceTest {
  
  private static final Meme MEME = new Meme("1", "pradeep",
      "https://www.google.com", "Party");

  @InjectMocks
  private MemesServiceImpl memeService;

  @MockBean
  private MemeRepositoryService memeRepositoryService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void getLatestMemesServiceTest() {
    when(memeRepositoryService.getLatest100Memes()).thenReturn(Arrays.asList(MEME));
  
    List<Meme> memes = memeService.getLatestMemesService();

    assertEquals(memes, Arrays.asList(MEME));
  }

  @Test
  public void getMemeWithIdServiceTest() {
    Answer<Meme> answer = new Answer<Meme>() {
      public Meme answer(InvocationOnMock invocation) throws Throwable {
        String id = invocation.getArgument(0);
        if (MEME.getId().equals(id)) {
          return MEME;
        } else {
          return null;
        }
      }
    };

    doAnswer(answer).when(memeRepositoryService).getMemeWithId(eq(MEME.getId()));
    Meme meme = memeService.getMemeWithIdService(MEME.getId());
    assertEquals(MEME, meme);

    doAnswer(answer).when(memeRepositoryService).getMemeWithId(eq("-1"));
    meme = memeService.getMemeWithIdService("-1");
    assertEquals(null, meme);




  } 

  @Test
  public void isMemeAlreadyPresentTest() throws Exception {
    when(memeRepositoryService.getMemeWithNameUrlCaption(eq(MEME.getName()),
        anyString(), anyString())).thenReturn(MEME);
    
    assertTrue(memeService.isMemeAlreadyPresent(
        getMemePostRequest(MEME.getName(), "", "")));
    
    when(memeRepositoryService.getMemeWithNameUrlCaption(eq("ram"),
    anyString(), anyString())).thenReturn(null);

    assertFalse(memeService.isMemeAlreadyPresent(
        getMemePostRequest("ram", "", "")));
  }

  @Test
  public void postMemeServiceTest() {
    when(memeRepositoryService.saveMeme(any(MemePostRequest.class)))
        .thenReturn(MEME);
    when(memeRepositoryService.getMemeWithNameUrlCaption(eq(MEME.getName()),
    anyString(), anyString())).thenReturn(null);

    Meme meme = memeService.postMemeService(
        getMemePostRequest(MEME.getName(), "", ""));
    
    assertEquals(MEME, meme);

    when(memeRepositoryService.getMemeWithNameUrlCaption(eq(MEME.getName()),
    anyString(), anyString())).thenReturn(MEME);

    meme = memeService.postMemeService(
        getMemePostRequest(MEME.getName(), "", ""));
    
    assertEquals(null, meme);
  }

  private MemePostRequest getMemePostRequest(String name, String url, String caption) {
    MemePostRequest entity = new MemePostRequest();
    entity.setName(name);
    entity.setUrl(url);
    entity.setCaption(caption);
    return entity;
  }
}