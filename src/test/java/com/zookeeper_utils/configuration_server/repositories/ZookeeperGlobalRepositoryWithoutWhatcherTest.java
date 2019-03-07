package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;


@RunWith(MockitoJUnitRunner.class)
public class ZookeeperGlobalRepositoryWithoutWhatcherTest {
	@InjectMocks
	@Spy
	private ZookeeperGlobalRepositoryWithoutWatcher sbv;
	@Mock
	private CuratorFramework clientZookeeper;
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	@Mock
	private GetDataBuilder getDataBuilder;
	@Mock
	private ZookeeperKeyPathGenerator zri;
	
	private String globalRepository;
	
	private Map<String,String> configurationMap ;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() {
		globalRepository = sbv.getContextNameRepository();
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/"+globalRepository+"/first1", null);
		configurationMap.put("/"+globalRepository+"/first2","test /zookeeper/first2");
		configurationMap.put("/"+globalRepository+"/first1/second1","test /zookeeper/first1/second1");
		globalRepository = sbv.getContextNameRepository();
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testGetKeyPathTree() throws Exception {
		String $zookeeper = "/"+globalRepository;
		when(zri.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(configurationMap);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap, test);
	}

	@Test
	public void testGetValueFromKeyPath() throws Exception {
		String zookeeper = globalRepository;
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		String test =sbv.getValueFromKeyPath("/first1/second1");
		assertEquals(configurationMap.get($zookeeper$first1$second1), test);
	}
	@Test
	public void testGetValueFromKeyPathException() throws Exception {
		String zookeeper = globalRepository;
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while load the properties to the 'keyPath' [/first1/second1]");
        sbv.getValueFromKeyPath("/first1/second1");
	}
}
