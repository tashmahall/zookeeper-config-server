package com.zookeeper_utils.configuration_server.properties;

import static com.zookeeper_utils.configuration_server.service.ZookeeperServiceScopeType.APPLICATION_SCOPED;
import static com.zookeeper_utils.configuration_server.service.ZookeeperServiceScopeType.REQUEST_SCOPED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperty;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesInterface;
@RunWith(MockitoJUnitRunner.class)
public class ConfigPropertiesFactoryTest {
	@Spy
	@InjectMocks
	private ConfigPropertiesFactory sbv;
	
	@Mock
	private InjectionPoint injectionPoint;
	
	@Mock
	private Annotated annotated;
	
	@Mock
	private ConfigProperty configProperties;
	
	@Mock
	private ZookeeperServicePropertiesInterface  zcInstanceServiceAppScoped;
	
	@Mock
	private ZookeeperServicePropertiesInterface  zcInstanceServiceReqScoped;
	
	@Mock
	private	Instance<ZookeeperServicePropertiesInterface> zkServiceAppScoped;

	@Mock
	private	Instance<ZookeeperServicePropertiesInterface> zkServiceReqScoped;

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	private String keyPath;
	private String value ;
	
	@Before
	public void loadData() {
		when(zkServiceAppScoped.get()).thenReturn(zcInstanceServiceAppScoped);
		when(zkServiceReqScoped.get()).thenReturn(zcInstanceServiceReqScoped);
		keyPath = "/first1";
		value = "test /zookeeper/first1";
	}
	

	@Test
	public void testProduceReqScopedNoWatcher() throws ConfigPropertiesException {
		when(injectionPoint.getAnnotated()).thenReturn(annotated);
		when(annotated.getAnnotation(ConfigProperty.class)).thenReturn(configProperties);
		when(configProperties.value()).thenReturn(keyPath);
		when(configProperties.scopeType()).thenReturn(REQUEST_SCOPED);
		when(configProperties.global()).thenReturn(false);
		when(zcInstanceServiceReqScoped.getPropertyValue(keyPath,false)).thenReturn(value);
		String test = sbv.produce(injectionPoint);
		assertEquals(value,test);
	}
	@Test
	public void testProduceAppScopedWithWatcher() throws ConfigPropertiesException {
		when(injectionPoint.getAnnotated()).thenReturn(annotated);
		when(annotated.getAnnotation(ConfigProperty.class)).thenReturn(configProperties);
		when(configProperties.value()).thenReturn(keyPath);
		when(configProperties.scopeType()).thenReturn(APPLICATION_SCOPED);
		when(configProperties.global()).thenReturn(false);
		when(zcInstanceServiceAppScoped.getPropertyValue(keyPath,false)).thenReturn(value);
		String test = sbv.produce(injectionPoint);
		assertEquals(value,test);
	}
}
