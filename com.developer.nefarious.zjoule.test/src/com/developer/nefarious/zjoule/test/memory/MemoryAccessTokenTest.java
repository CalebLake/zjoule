package com.developer.nefarious.zjoule.test.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.developer.nefarious.zjoule.auth.AccessToken;
import com.developer.nefarious.zjoule.memory.IEclipseMemory;
import com.developer.nefarious.zjoule.memory.IMemoryAccessToken;
import com.developer.nefarious.zjoule.memory.MemoryAccessToken;
import com.developer.nefarious.zjoule.utils.IObjectSerializer;

public class MemoryAccessTokenTest {

	public static final String KEY = "access-token";
	
	private IMemoryAccessToken cut;
	
	@Mock
	IObjectSerializer mockObjectSerializer;
	
	@Mock
	IEclipseMemory mockEclipseMemory;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		
		MemoryAccessToken.resetInstance();
		MemoryAccessToken.initialize(mockObjectSerializer, mockEclipseMemory);
		cut = MemoryAccessToken.getInstance();
	}
	
	@Test
	public void shouldSaveAccessToken() {
		// Arrange
		AccessToken mockAccessToken = new AccessToken();
		String mockSerializedObject = "It doesn't matter";
		when(mockObjectSerializer.serialize(mockAccessToken)).thenReturn(mockSerializedObject);
		// Act
		cut.save(mockAccessToken);
		// Assert
		verify(mockEclipseMemory).saveOnEclipsePreferences(KEY, mockSerializedObject);
	}
	
	@Test
	public void shouldAccessToken() {
		// Arrange
		AccessToken expectedValue = new AccessToken();
		String mockSerializedObject = "It doesn't matter";
		when(mockEclipseMemory.loadFromEclipsePreferences(KEY)).thenReturn(mockSerializedObject);
		when(mockObjectSerializer.deserialize(mockSerializedObject, AccessToken.class)).thenReturn(expectedValue);
		// Act
		AccessToken returnValue = cut.load();
		// Assert
		assertEquals(returnValue, expectedValue);
	}
}
