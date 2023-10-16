package com.katafoni.kindlevocabulary.core.binarydata;

import com.katafoni.kindlevocabulary.common.exception.FileUploadException;
import com.katafoni.kindlevocabulary.util.MessageSourceFacade;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinaryDataServiceImplTest {

    private static final String EXPECTED_FILE_NAME = "tmp12122021_1212111.db";

    @InjectMocks
    private BinaryDataServiceImpl binaryDataServiceImpl;

    @Spy
    private MockMultipartFile file = new MockMultipartFile("file", "mockFilename.db", "text/plain", "MockMultipartFile".getBytes());

    @Mock
    private BinaryDataProviderFactory binaryDataProviderFactory;

    @Mock
    private LocalStorageProvider localStorageProvider;

    @Mock
    private MessageSourceFacade messageSourceFacade;

    @Captor
    private ArgumentCaptor<InputStream> inputStreamCaptor;

    @Test
    public void whenSaveFileSuccess_thenReturnFileName() throws IOException {

        //given
        when(this.binaryDataProviderFactory.findBinaryDataProvider(eq(BinaryDataProviderName.LOCAL_STORAGE))).thenReturn(this.localStorageProvider);
        when(this.localStorageProvider.saveFile(any())).thenReturn(EXPECTED_FILE_NAME);

        //when
        String fileName = this.binaryDataServiceImpl.saveFileAsDatabaseStorage(file);

        //then
        verify(this.binaryDataProviderFactory, times(1)).findBinaryDataProvider(eq(BinaryDataProviderName.LOCAL_STORAGE));
        verify(this.localStorageProvider, times(1)).saveFile(this.inputStreamCaptor.capture());
        assertThat(this.file.getInputStream()).hasSameContentAs(this.inputStreamCaptor.getValue());
        assertEquals(EXPECTED_FILE_NAME, fileName);
    }

    @Test
    public void whenSaveFileSuccess_IOException_thenThrowFileUploadException() throws IOException {

        //given
        when(this.binaryDataProviderFactory.findBinaryDataProvider(eq(BinaryDataProviderName.LOCAL_STORAGE))).thenReturn(this.localStorageProvider);
        doThrow(new IOException()).when(this.file).getInputStream();
        when(this.messageSourceFacade.getMessage(any(), any())).thenReturn("MULTIPART_FILE_FAILURE");
        when(this.messageSourceFacade.getMessage(any())).thenReturn("FILE_UPLOAD_EXCEPTION_CODE");

        //when
        FileUploadException exception = assertThrows(FileUploadException.class, () -> this.binaryDataServiceImpl.saveFileAsDatabaseStorage(file));

        //then
        assertEquals("FILE_UPLOAD_EXCEPTION_CODE", exception.getMessage());
        assertEquals(BinaryDataErrorCodes.MULTIPART_FILE_FAILURE.getErrorCode(), exception.getErrorCode());
    }

    @Test
    public void whenSaveFileSuccess_MultipartFileNull_thenThrowIllegalArgumentException() {

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.binaryDataServiceImpl.saveFileAsDatabaseStorage(null));

        //then
        assertEquals("Argument multipartFile cannot be null", exception.getMessage());
    }

    @Test
    public void whenDeleteFileSavedAsDatabaseStorage_thenDeleteFile() {

        //given
        when(this.binaryDataProviderFactory.findBinaryDataProvider(eq(BinaryDataProviderName.LOCAL_STORAGE))).thenReturn(this.localStorageProvider);

        //when
        this.binaryDataServiceImpl.deleteFileSavedAsDatabaseStorage(EXPECTED_FILE_NAME);

        //then
        verify(this.localStorageProvider, times(1)).deleteFile(eq(EXPECTED_FILE_NAME));
    }

    @Test
    public void whenDeleteFile_FileNameNull_thenThrowIllegalArgumentException() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.binaryDataServiceImpl.deleteFileSavedAsDatabaseStorage(null));

        //then
        assertEquals("Argument fileName cannot be empty", exception.getMessage());
    }

    @Test
    public void whenDeleteFile_FileNameEmpty_thenThrowIllegalArgumentException() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> this.binaryDataServiceImpl.deleteFileSavedAsDatabaseStorage(StringUtils.EMPTY));

        //then
        assertEquals("Argument fileName cannot be empty", exception.getMessage());
    }
}