package com.katafoni.kindlevocabulary.core.transformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katafoni.kindlevocabulary.common.exception.FileUploadException;
import com.katafoni.kindlevocabulary.common.exception.InternalFailureException;
import com.katafoni.kindlevocabulary.common.exception.RequestParameterException;
import com.katafoni.kindlevocabulary.common.exception.api.ApiError;
import com.katafoni.kindlevocabulary.domain.dto.PhraseDto;
import com.katafoni.util.TestJsonUtils;
import com.katafoni.util.creators.PhraseDtoCreator;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VocabularyTransformationController.class)
class VocabularyTransformationControllerTest {

    private static final String TESTED_ERROR_CODE = "KVC001";
    private static final String TESTED_EXCEPTION_MESSAGE = "Failure message";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VocabularyTransformationService vocabularyTransformationService;

    @Captor
    ArgumentCaptor<TransformationRequest> transformationRequestCaptor;

    private MockMultipartFile file = new MockMultipartFile("file", "mockFilename.db", "text/plain", "MockMultipartFile".getBytes());

    private RequestPostProcessor getRequestPostProcessor = request -> {
        request.setMethod("GET");
        return request;
    };

    @Test
    void whenInputHasAllParameters_thenReturns200() throws Exception {

        //given
        Set<PhraseDto> phraseDtos = PhraseDtoCreator.createPhraseDtosCollection(2);
        when(this.vocabularyTransformationService.transformVocabulary(any(TransformationRequest.class))).thenReturn(phraseDtos);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        //then
        verify(this.vocabularyTransformationService, times(1)).transformVocabulary(transformationRequestCaptor.capture());
        TransformationRequest actualTransformationRequest = transformationRequestCaptor.getValue();

        assertAll(() -> assertEquals("polish", actualTransformationRequest.getTranslationLanguage()),
                () -> assertEquals("english", actualTransformationRequest.getFilteringLanguage()),
                () -> assertEquals("file", actualTransformationRequest.getMultipartFile().getName()),
                () -> assertEquals("mockFilename.db", actualTransformationRequest.getMultipartFile().getOriginalFilename())
        );

        assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(phraseDtos));
    }

    @Test
    void whenInputHasFileAndFilterLang_thenReturns200() throws Exception {

        //given
        Set<PhraseDto> phraseDtos = Sets.newHashSet();
        when(this.vocabularyTransformationService.transformVocabulary(any(TransformationRequest.class))).thenReturn(phraseDtos);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        //then
        verify(this.vocabularyTransformationService, times(1)).transformVocabulary(transformationRequestCaptor.capture());

        TransformationRequest actualTransformationRequest = transformationRequestCaptor.getValue();

        assertAll(() -> assertEquals(null, actualTransformationRequest.getTranslationLanguage()),
                () -> assertEquals("english", actualTransformationRequest.getFilteringLanguage()),
                () -> assertEquals("file", actualTransformationRequest.getMultipartFile().getName()),
                () -> assertEquals("mockFilename.db", actualTransformationRequest.getMultipartFile().getOriginalFilename())
        );

        assertThat(responseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(phraseDtos));
    }

    @Test
    void whenInputHasNotFile_thenReturns400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isBadRequest());

        verify(this.vocabularyTransformationService, never()).transformVocabulary(any());
    }

    @Test
    void whenInputHasNotFilterLang_thenReturns400() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .with(getRequestPostProcessor))
                .andExpect(status().isBadRequest());

        verify(this.vocabularyTransformationService, never()).transformVocabulary(any());
    }

    @Test
    void whenInternalFailureException_thenReturns500() throws Exception {

        //given
        String expectedBody = objectMapper.writeValueAsString(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE));
        doThrow(new InternalFailureException(TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE)).when(this.vocabularyTransformationService).transformVocabulary(any());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        //then
        verify(this.vocabularyTransformationService, times(1)).transformVocabulary(any(TransformationRequest.class));
        JSONAssert.assertEquals(expectedBody, responseBody, TestJsonUtils.getTimestampJsonCustomComparator());
    }

    @Test
    void whenFileUploadException_thenReturns400() throws Exception {

        //given
        String expectedBody = objectMapper.writeValueAsString(new ApiError(HttpStatus.BAD_REQUEST, TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE));
        doThrow(new FileUploadException(TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE)).when(this.vocabularyTransformationService).transformVocabulary(any());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        //then
        verify(this.vocabularyTransformationService, times(1)).transformVocabulary(any(TransformationRequest.class));
        JSONAssert.assertEquals(expectedBody, responseBody, TestJsonUtils.getTimestampJsonCustomComparator());
    }

    @Test
    void whenRequestParameterException_thenReturns400() throws Exception {

        //given
        String expectedBody = objectMapper.writeValueAsString(new ApiError(HttpStatus.BAD_REQUEST, TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE));
        doThrow(new RequestParameterException(TESTED_ERROR_CODE, TESTED_EXCEPTION_MESSAGE)).when(this.vocabularyTransformationService).transformVocabulary(any());

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/files/vocabulary")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("trans_lang", "polish")
                        .param("filter_lang", "english")
                        .with(getRequestPostProcessor))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        //then
        verify(this.vocabularyTransformationService, times(1)).transformVocabulary(any(TransformationRequest.class));
        JSONAssert.assertEquals(expectedBody, responseBody, TestJsonUtils.getTimestampJsonCustomComparator());
    }
}